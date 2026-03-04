package com.dumpster.calculator.domain.service;

import com.dumpster.calculator.domain.model.CostComparisonOption;
import com.dumpster.calculator.domain.model.RangeValue;
import com.dumpster.calculator.domain.reference.JunkQuoteBenchmark;
import com.dumpster.calculator.domain.reference.JunkPricingProfile;
import com.dumpster.calculator.domain.reference.PricingAssumption;
import com.dumpster.calculator.infra.persistence.JunkQuoteBenchmarkRepository;
import com.dumpster.calculator.infra.persistence.JunkPricingProfileRepository;
import com.dumpster.calculator.infra.persistence.JunkPricingProfileRuleRepository;
import com.dumpster.calculator.infra.persistence.MarketTierZipRuleRepository;
import com.dumpster.calculator.infra.persistence.PricingAssumptionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CostComparisonService {

    private final PricingAssumptionRepository pricingAssumptionRepository;
    private final JunkPricingProfileRepository junkPricingProfileRepository;
    private final JunkPricingProfileRuleRepository junkPricingProfileRuleRepository;
    private final JunkQuoteBenchmarkRepository junkQuoteBenchmarkRepository;
    private final MarketTierZipRuleRepository marketTierZipRuleRepository;
    private final String defaultMarketTier;

    public CostComparisonService(
            PricingAssumptionRepository pricingAssumptionRepository,
            JunkPricingProfileRepository junkPricingProfileRepository,
            JunkPricingProfileRuleRepository junkPricingProfileRuleRepository,
            JunkQuoteBenchmarkRepository junkQuoteBenchmarkRepository,
            MarketTierZipRuleRepository marketTierZipRuleRepository,
            @Value("${app.pricing.market-tier:national}") String marketTier
    ) {
        this.pricingAssumptionRepository = pricingAssumptionRepository;
        this.junkPricingProfileRepository = junkPricingProfileRepository;
        this.junkPricingProfileRuleRepository = junkPricingProfileRuleRepository;
        this.junkQuoteBenchmarkRepository = junkQuoteBenchmarkRepository;
        this.marketTierZipRuleRepository = marketTierZipRuleRepository;
        this.defaultMarketTier = normalizeMarketTier(marketTier);
    }

    public List<CostComparisonOption> compare(
            int safeSizeYd,
            int multiHaulSizeYd,
            int multiHaulCount,
            RangeValue totalWeightTons,
            RangeValue totalVolumeYd3,
            double includedTons,
            String needTiming,
            String zipCode
    ) {
        ResolvedMarketTier resolvedMarketTier = resolveMarketTier(zipCode);
        List<CostComparisonOption> options = new ArrayList<>();
        options.add(singleDumpsterOption(safeSizeYd, totalWeightTons, includedTons));
        options.add(multiHaulOption(multiHaulSizeYd, multiHaulCount, totalWeightTons, includedTons));
        options.add(junkRemovalOption(totalVolumeYd3, totalWeightTons, needTiming, resolvedMarketTier));
        return options;
    }

    private CostComparisonOption singleDumpsterOption(int sizeYd, RangeValue weightTons, double includedTons) {
        Optional<PricingAssumption> pricing = pricingAssumptionRepository.findBySize(sizeYd);
        if (pricing.isEmpty()) {
            return unavailable("single_dumpster", sizeYd + "yd single haul");
        }
        RangeValue cost = calculateDumpsterCost(pricing.get(), weightTons, includedTons, 1);
        return new CostComparisonOption(
                "single_dumpster",
                sizeYd + "yd single haul",
                cost.round2(),
                true,
                "likely lower when overage stays controlled",
                List.of("includes rental + haul + overage estimate")
        );
    }

    private CostComparisonOption multiHaulOption(int sizeYd, int haulCount, RangeValue weightTons, double includedTons) {
        Optional<PricingAssumption> pricing = pricingAssumptionRepository.findBySize(sizeYd);
        if (pricing.isEmpty()) {
            return unavailable("multi_haul", sizeYd + "yd multi-haul");
        }
        RangeValue perHaulWeight = RangeValue.of(
                weightTons.low() / Math.max(1, haulCount),
                weightTons.typ() / Math.max(1, haulCount),
                weightTons.high() / Math.max(1, haulCount)
        );
        RangeValue cost = calculateDumpsterCost(pricing.get(), perHaulWeight, includedTons, haulCount);
        return new CostComparisonOption(
                "multi_haul",
                sizeYd + "yd x " + haulCount + " hauls",
                cost.round2(),
                true,
                "can reduce overweight risk on heavy debris",
                List.of("more trips can raise labor/scheduling overhead")
        );
    }

    private CostComparisonOption junkRemovalOption(
            RangeValue volumeYd3,
            RangeValue weightTons,
            String needTiming,
            ResolvedMarketTier resolvedMarketTier
    ) {
        Optional<JunkPricingProfile> junkProfile = resolveProfile(resolvedMarketTier.marketTier(), needTiming);
        if (junkProfile.isEmpty()) {
            return unavailable("junk_removal", "Junk removal");
        }
        JunkPricingProfile profile = junkProfile.get();
        boolean denseLoad = isDenseLoad(volumeYd3, weightTons, profile);
        double billedVolumeLow = billedVolume(volumeYd3.low(), profile);
        double billedVolumeTyp = billedVolume(volumeYd3.typ(), profile);
        double billedVolumeHigh = billedVolume(volumeYd3.high(), profile);
        RangeValue cost = RangeValue.of(
                (profile.minServiceFeeLow() + (billedVolumeLow * profile.perCyFeeLow()))
                        * (denseLoad ? profile.denseMaterialMultiplierLow() : 1.0d),
                (profile.minServiceFeeTyp() + (billedVolumeTyp * profile.perCyFeeTyp()))
                        * (denseLoad ? profile.denseMaterialMultiplierTyp() : 1.0d),
                (profile.minServiceFeeHigh() + (billedVolumeHigh * profile.perCyFeeHigh()))
                        * (denseLoad ? profile.denseMaterialMultiplierHigh() : 1.0d)
        );
        Optional<JunkQuoteBenchmark> benchmark = selectBenchmark(
                resolvedMarketTier.marketTier(),
                needTiming,
                volumeYd3.typ()
        );
        RangeValue blendedCost = benchmark
                .map(data -> blendWithBenchmark(cost, data))
                .orElse(cost);

        List<String> notes = new ArrayList<>();
        notes.add("model: " + profile.displayName());
        notes.add("profile: " + profile.profileId());
        notes.add("market tier: " + resolvedMarketTier.marketTier()
                + " (" + resolvedMarketTier.resolutionSource() + ")");
        notes.add("source pack: " + profile.source());
        notes.add("billed in " + billingIncrementLabel(profile) + " truck increments");
        benchmark.ifPresent(data -> notes.add("benchmark (" + data.sampleCount() + " samples): "
                + data.scenarioTag() + ", $" + round2(data.quotedTotalLow())
                + "-$" + round2(data.quotedTotalHigh()) + " for "
                + round2(data.volumeCyLow()) + "-" + round2(data.volumeCyHigh()) + " yd3"));
        if (denseLoad) {
            notes.add("dense-load surcharge likely due to tons-per-yard profile");
        } else {
            notes.add("dense-load surcharge not applied under current assumptions");
        }

        return new CostComparisonOption(
                "junk_removal",
                "Junk removal service",
                blendedCost.round2(),
                true,
                "often faster for mixed/bulky loads or access-heavy cleanup",
                notes
        );
    }

    private static RangeValue calculateDumpsterCost(
            PricingAssumption pricing,
            RangeValue weightTons,
            double includedTons,
            int haulCount
    ) {
        RangeValue oneHaulCost = RangeValue.of(
                pricing.rentalFeeLow() + pricing.haulFeeLow()
                        + Math.max(0.0d, weightTons.low() - includedTons) * pricing.overageFeePerTonLow(),
                pricing.rentalFeeTyp() + pricing.haulFeeTyp()
                        + Math.max(0.0d, weightTons.typ() - includedTons) * pricing.overageFeePerTonTyp(),
                pricing.rentalFeeHigh() + pricing.haulFeeHigh()
                        + Math.max(0.0d, weightTons.high() - includedTons) * pricing.overageFeePerTonHigh()
        );
        return oneHaulCost.multiply(haulCount);
    }

    private static CostComparisonOption unavailable(String optionId, String title) {
        return new CostComparisonOption(
                optionId,
                title,
                RangeValue.single(0.0d),
                false,
                "pricing unavailable",
                List.of("missing pricing assumptions for this option")
        );
    }

    private static boolean isDenseLoad(
            RangeValue volumeYd3,
            RangeValue weightTons,
            JunkPricingProfile profile
    ) {
        double denominator = Math.max(volumeYd3.typ(), 0.1d);
        double tonsPerYd = weightTons.typ() / denominator;
        return tonsPerYd >= profile.denseMaterialThresholdTonPerCy();
    }

    private static double billedVolume(double volumeYd3, JunkPricingProfile profile) {
        double safeVolume = Math.max(volumeYd3, profile.minimumBillableVolumeCy());
        double increment = Math.max(0.01d, profile.truckCapacityCy() * profile.billingIncrementFraction());
        return Math.ceil(safeVolume / increment) * increment;
    }

    private static String billingIncrementLabel(JunkPricingProfile profile) {
        int denominator = (int) Math.round(1.0d / Math.max(0.01d, profile.billingIncrementFraction()));
        return "1/" + denominator;
    }

    private Optional<JunkPricingProfile> resolveProfile(String marketTier, String needTiming) {
        String normalizedTiming = normalizeNeedTiming(needTiming);
        return junkPricingProfileRuleRepository.resolveProfileId(normalizeMarketTier(marketTier), normalizedTiming)
                .flatMap(junkPricingProfileRepository::findById)
                .or(junkPricingProfileRepository::findDefaultProfile);
    }

    private Optional<JunkQuoteBenchmark> selectBenchmark(String marketTier, String needTiming, double volumeTyp) {
        List<JunkQuoteBenchmark> candidates = junkQuoteBenchmarkRepository.findCandidates(
                normalizeMarketTier(marketTier),
                normalizeNeedTiming(needTiming)
        );
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        return candidates.stream()
                .filter(candidate -> volumeTyp >= candidate.volumeCyLow() && volumeTyp <= candidate.volumeCyHigh())
                .findFirst()
                .or(() -> candidates.stream()
                        .min((a, b) -> Double.compare(
                                distanceToBand(volumeTyp, a.volumeCyLow(), a.volumeCyHigh()),
                                distanceToBand(volumeTyp, b.volumeCyLow(), b.volumeCyHigh())
                        )));
    }

    private static double distanceToBand(double value, double low, double high) {
        if (value < low) {
            return low - value;
        }
        if (value > high) {
            return value - high;
        }
        return 0.0d;
    }

    private static RangeValue blendWithBenchmark(RangeValue modeledCost, JunkQuoteBenchmark benchmark) {
        double modeledWeight = 0.75d;
        double benchmarkWeight = 0.25d;
        return RangeValue.of(
                (modeledCost.low() * modeledWeight) + (benchmark.quotedTotalLow() * benchmarkWeight),
                (modeledCost.typ() * modeledWeight) + (benchmark.quotedTotalTyp() * benchmarkWeight),
                (modeledCost.high() * modeledWeight) + (benchmark.quotedTotalHigh() * benchmarkWeight)
        );
    }

    private static String round2(double value) {
        return String.format(java.util.Locale.US, "%.2f", value);
    }

    private ResolvedMarketTier resolveMarketTier(String zipCode) {
        String normalizedZip = normalizeZip(zipCode);
        if (normalizedZip != null) {
            return marketTierZipRuleRepository.resolveByZip(normalizedZip)
                    .map(rule -> new ResolvedMarketTier(
                            normalizeMarketTier(rule.marketTier()),
                            "zip rule " + rule.ruleId() + " (" + rule.zipStart() + "-" + rule.zipEnd() + ")"
                    ))
                    .orElseGet(() -> new ResolvedMarketTier(
                            defaultMarketTier,
                            "default tier (zip unmapped)"
                    ));
        }
        return new ResolvedMarketTier(defaultMarketTier, "default tier (zip missing)");
    }

    private static String normalizeMarketTier(String tier) {
        if (tier == null || tier.isBlank()) {
            return "national";
        }
        String normalized = tier.trim().toLowerCase();
        if ("urban".equals(normalized)
                || "value".equals(normalized)
                || "national".equals(normalized)
                || "coastal".equals(normalized)
                || "mountain".equals(normalized)
                || "heartland".equals(normalized)) {
            return normalized;
        }
        return "national";
    }

    private static String normalizeNeedTiming(String needTiming) {
        if (needTiming == null || needTiming.isBlank()) {
            return "any";
        }
        String normalized = needTiming.trim().toLowerCase();
        if ("48h".equals(normalized) || "research".equals(normalized) || "this_week".equals(normalized)) {
            return normalized;
        }
        return "any";
    }

    private static String normalizeZip(String zipCode) {
        if (zipCode == null) {
            return null;
        }
        String digits = zipCode.replaceAll("[^0-9]", "");
        if (digits.length() != 5) {
            return null;
        }
        return digits;
    }

    private record ResolvedMarketTier(String marketTier, String resolutionSource) {
    }
}


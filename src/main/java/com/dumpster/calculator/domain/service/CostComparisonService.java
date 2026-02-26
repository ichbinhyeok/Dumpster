package com.dumpster.calculator.domain.service;

import com.dumpster.calculator.domain.model.CostComparisonOption;
import com.dumpster.calculator.domain.model.RangeValue;
import com.dumpster.calculator.domain.reference.PricingAssumption;
import com.dumpster.calculator.infra.persistence.PricingAssumptionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CostComparisonService {

    private final PricingAssumptionRepository pricingAssumptionRepository;

    public CostComparisonService(PricingAssumptionRepository pricingAssumptionRepository) {
        this.pricingAssumptionRepository = pricingAssumptionRepository;
    }

    public List<CostComparisonOption> compare(
            int safeSizeYd,
            int multiHaulSizeYd,
            int multiHaulCount,
            RangeValue totalWeightTons,
            RangeValue totalVolumeYd3,
            double includedTons
    ) {
        List<CostComparisonOption> options = new ArrayList<>();
        options.add(singleDumpsterOption(safeSizeYd, totalWeightTons, includedTons));
        options.add(multiHaulOption(multiHaulSizeYd, multiHaulCount, totalWeightTons, includedTons));
        options.add(junkRemovalOption(totalVolumeYd3));
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

    private CostComparisonOption junkRemovalOption(RangeValue volumeYd3) {
        Optional<PricingAssumption> junkPricing = pricingAssumptionRepository.findBySize(0);
        if (junkPricing.isEmpty()) {
            return unavailable("junk_removal", "Junk removal");
        }
        PricingAssumption pricing = junkPricing.get();
        // For size_yd=0 row, rental fields represent per-yd rates and haul_typ is service minimum.
        double minServiceLow = pricing.haulFeeLow();
        double minServiceTyp = pricing.haulFeeTyp();
        double minServiceHigh = pricing.haulFeeHigh();
        RangeValue cost = RangeValue.of(
                minServiceLow + (volumeYd3.low() * pricing.rentalFeeLow()),
                minServiceTyp + (volumeYd3.typ() * pricing.rentalFeeTyp()),
                minServiceHigh + (volumeYd3.high() * pricing.rentalFeeHigh())
        );
        return new CostComparisonOption(
                "junk_removal",
                "Junk removal service",
                cost.round2(),
                true,
                "often competitive when sorting and hauling are difficult",
                List.of("price can vary by local labor and access constraints")
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
}


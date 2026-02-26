package com.dumpster.calculator.domain.service;

import com.dumpster.calculator.domain.model.CostComparisonOption;
import com.dumpster.calculator.domain.model.CtaRouting;
import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateResult;
import com.dumpster.calculator.domain.model.Feasibility;
import com.dumpster.calculator.domain.model.LineItemEstimate;
import com.dumpster.calculator.domain.model.PriceRisk;
import com.dumpster.calculator.domain.model.RangeValue;
import com.dumpster.calculator.domain.model.RecommendationOption;
import com.dumpster.calculator.domain.reference.DumpsterSizePolicy;
import com.dumpster.calculator.domain.reference.MaterialCategory;
import com.dumpster.calculator.infra.persistence.DumpsterSizeRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class EstimationFacade {

    private static final String CALC_ENGINE_VERSION = "v1.2.0";

    private final NormalizationService normalizationService;
    private final DumpsterSizeRepository dumpsterSizeRepository;
    private final CostComparisonService costComparisonService;
    private final CtaRoutingService ctaRoutingService;
    private final Clock clock;

    public EstimationFacade(
            NormalizationService normalizationService,
            DumpsterSizeRepository dumpsterSizeRepository,
            CostComparisonService costComparisonService,
            CtaRoutingService ctaRoutingService,
            Clock clock
    ) {
        this.normalizationService = normalizationService;
        this.dumpsterSizeRepository = dumpsterSizeRepository;
        this.costComparisonService = costComparisonService;
        this.ctaRoutingService = ctaRoutingService;
        this.clock = clock;
    }

    public EstimateResult estimate(EstimateCommand command) {
        List<LineItemEstimate> lineItems = normalizationService.normalize(command);
        RangeValue totalVolume = sumVolume(lineItems);
        RangeValue totalWeight = sumWeight(lineItems);
        double bulkingFactor = command.safeOptions().resolvedBulkingFactor();
        RangeValue safeVolume = totalVolume.multiply(bulkingFactor);

        List<DumpsterSizePolicy> policies = dumpsterSizeRepository.findAllOrdered();
        if (policies.isEmpty()) {
            throw new IllegalStateException("No dumpster size policy data available.");
        }

        boolean heavyMode = isHeavyMode(lineItems, totalWeight);
        List<CandidateEvaluation> evaluations = policies.stream()
                .map(policy -> evaluateCandidate(policy, safeVolume, totalWeight, command.safeOptions().allowanceTons(), heavyMode))
                .sorted(Comparator.comparingInt(CandidateEvaluation::score))
                .toList();

        RecommendationOption safeRecommendation = selectSafeRecommendation(evaluations);
        RecommendationOption budgetRecommendation = selectBudgetRecommendation(evaluations, safeRecommendation);
        List<RecommendationOption> recommendations = new ArrayList<>();
        if (safeRecommendation != null) {
            recommendations.add(safeRecommendation);
        }
        if (budgetRecommendation != null) {
            recommendations.add(budgetRecommendation);
        }

        PriceRisk overallRisk = safeRecommendation == null ? PriceRisk.HIGH : safeRecommendation.risk();
        Feasibility overallFeasibility = safeRecommendation == null ? Feasibility.NOT_RECOMMENDED : safeRecommendation.feasibility();
        boolean usedAssumedAllowance = command.safeOptions().allowanceTons() == null;
        double includedTons = resolveIncludedTons(command.safeOptions().allowanceTons(), safeRecommendation, policies);

        int safeSize = safeRecommendation == null ? 10 : safeRecommendation.sizeYd();
        int multiHaulCount = deriveMultiHaulCount(safeRecommendation, safeVolume, totalWeight, policies);
        List<CostComparisonOption> costComparison = costComparisonService.compare(
                safeSize,
                10,
                multiHaulCount,
                totalWeight,
                safeVolume,
                includedTons
        );

        List<String> hardStops = collectHardStops(evaluations);
        List<String> assumptions = buildAssumptions(usedAssumedAllowance, bulkingFactor);
        List<String> impacts = buildInputImpactSummary(lineItems, usedAssumedAllowance);
        CtaRouting ctaRouting = ctaRoutingService.decide(command, overallRisk, overallFeasibility, heavyMode);

        return new EstimateResult(
                safeVolume.round2(),
                totalWeight.round2(),
                overallRisk,
                overallFeasibility,
                usedAssumedAllowance,
                heavyMode,
                ctaRouting,
                recommendations,
                costComparison,
                hardStops,
                assumptions,
                impacts,
                LocalDate.now(clock).toString(),
                CALC_ENGINE_VERSION
        );
    }

    private static RangeValue sumVolume(List<LineItemEstimate> lineItems) {
        RangeValue total = RangeValue.single(0.0d);
        for (LineItemEstimate lineItem : lineItems) {
            total = total.add(lineItem.volumeYd3());
        }
        return total;
    }

    private static RangeValue sumWeight(List<LineItemEstimate> lineItems) {
        RangeValue total = RangeValue.single(0.0d);
        for (LineItemEstimate lineItem : lineItems) {
            total = total.add(lineItem.weightTons());
        }
        return total;
    }

    private static boolean isHeavyMode(List<LineItemEstimate> lineItems, RangeValue totalWeight) {
        double heavyWeight = 0.0d;
        for (LineItemEstimate lineItem : lineItems) {
            if (lineItem.category() == MaterialCategory.HEAVY) {
                heavyWeight += lineItem.weightTons().typ();
            }
        }
        if (totalWeight.typ() <= 0.0d) {
            return false;
        }
        return (heavyWeight / totalWeight.typ()) >= 0.35d || heavyWeight >= 1.5d;
    }

    private static CandidateEvaluation evaluateCandidate(
            DumpsterSizePolicy policy,
            RangeValue safeVolume,
            RangeValue totalWeight,
            Double allowanceInput,
            boolean heavyMode
    ) {
        boolean volumeFits = policy.sizeYd() >= safeVolume.typ();
        double allowance = allowanceInput == null ? policy.includedTonsTyp() : allowanceInput;
        PriceRisk risk = classifyPriceRisk(totalWeight, allowance);

        Feasibility feasibility = classifyFeasibility(totalWeight, policy, safeVolume, heavyMode);

        int score = 0;
        if (!volumeFits) {
            score += 150;
        }
        score += switch (risk) {
            case LOW -> 5;
            case MEDIUM -> 20;
            case HIGH -> 45;
        };
        score += switch (feasibility) {
            case OK -> 0;
            case MULTI_HAUL_REQUIRED -> 70;
            case NOT_RECOMMENDED -> 200;
        };
        score += policy.sizeYd(); // mild preference for smaller bins when safety is equal.

        List<String> reasons = new ArrayList<>();
        reasons.add(volumeFits ? "volume fits with safety buffer" : "volume may exceed capacity");
        reasons.add("included tons baseline: " + allowance);
        if (heavyMode) {
            reasons.add("heavy debris mode active");
        }

        int haulCount = Math.max(1, (int) Math.ceil(Math.max(
                safeVolume.typ() / policy.sizeYd(),
                totalWeight.typ() / Math.max(0.1d, policy.maxHaulTonsTyp())
        )));

        return new CandidateEvaluation(policy, risk, feasibility, volumeFits, score, haulCount, reasons);
    }

    private static PriceRisk classifyPriceRisk(RangeValue totalWeight, double includedTons) {
        if (totalWeight.low() > includedTons) {
            return PriceRisk.HIGH;
        }
        if (totalWeight.high() < includedTons) {
            return PriceRisk.LOW;
        }
        return PriceRisk.MEDIUM;
    }

    private static Feasibility classifyFeasibility(
            RangeValue totalWeight,
            DumpsterSizePolicy policy,
            RangeValue safeVolume,
            boolean heavyMode
    ) {
        if (totalWeight.low() > policy.maxHaulTonsHigh()) {
            return Feasibility.NOT_RECOMMENDED;
        }
        if (totalWeight.typ() > policy.maxHaulTonsTyp()) {
            return Feasibility.MULTI_HAUL_REQUIRED;
        }
        if (heavyMode && safeVolume.typ() > (policy.sizeYd() * policy.heavyDebrisMaxFillRatio())) {
            return Feasibility.MULTI_HAUL_REQUIRED;
        }
        return Feasibility.OK;
    }

    private static RecommendationOption selectSafeRecommendation(List<CandidateEvaluation> evaluations) {
        for (CandidateEvaluation candidate : evaluations) {
            if (candidate.volumeFits() && candidate.feasibility() == Feasibility.OK) {
                return toRecommendation(candidate, "Safe");
            }
        }
        for (CandidateEvaluation candidate : evaluations) {
            if (candidate.feasibility() != Feasibility.NOT_RECOMMENDED) {
                return toRecommendation(candidate, "Safe");
            }
        }
        return evaluations.isEmpty() ? null : toRecommendation(evaluations.get(0), "Safe");
    }

    private static RecommendationOption selectBudgetRecommendation(
            List<CandidateEvaluation> evaluations,
            RecommendationOption safeRecommendation
    ) {
        if (safeRecommendation == null) {
            return null;
        }
        for (CandidateEvaluation candidate : evaluations) {
            if (candidate.policy().sizeYd() < safeRecommendation.sizeYd()) {
                return toRecommendation(candidate, "Budget");
            }
        }
        for (CandidateEvaluation candidate : evaluations) {
            if (candidate.policy().sizeYd() != safeRecommendation.sizeYd()) {
                return toRecommendation(candidate, "Budget");
            }
        }
        return null;
    }

    private static RecommendationOption toRecommendation(CandidateEvaluation candidate, String label) {
        return new RecommendationOption(
                candidate.policy().sizeYd(),
                label,
                candidate.risk(),
                candidate.feasibility(),
                candidate.haulCount() > 1 || candidate.feasibility() == Feasibility.MULTI_HAUL_REQUIRED,
                candidate.haulCount(),
                candidate.reasons()
        );
    }

    private static int deriveMultiHaulCount(
            RecommendationOption safeRecommendation,
            RangeValue safeVolume,
            RangeValue totalWeight,
            List<DumpsterSizePolicy> policies
    ) {
        if (safeRecommendation != null && safeRecommendation.multiHaul()) {
            return Math.max(1, safeRecommendation.haulCount());
        }
        DumpsterSizePolicy policy10 = policies.stream()
                .filter(p -> p.sizeYd() == 10)
                .findFirst()
                .orElse(policies.get(0));
        return Math.max(1, (int) Math.ceil(Math.max(
                safeVolume.typ() / policy10.sizeYd(),
                totalWeight.typ() / Math.max(0.1d, policy10.maxHaulTonsTyp())
        )));
    }

    private static double resolveIncludedTons(
            Double allowanceInput,
            RecommendationOption safeRecommendation,
            List<DumpsterSizePolicy> policies
    ) {
        if (allowanceInput != null) {
            return allowanceInput;
        }
        int size = safeRecommendation == null ? 10 : safeRecommendation.sizeYd();
        return policies.stream()
                .filter(p -> p.sizeYd() == size)
                .map(DumpsterSizePolicy::includedTonsTyp)
                .findFirst()
                .orElse(2.0d);
    }

    private static List<String> collectHardStops(List<CandidateEvaluation> evaluations) {
        Set<String> hardStops = new HashSet<>();
        for (CandidateEvaluation evaluation : evaluations) {
            if (evaluation.feasibility() == Feasibility.NOT_RECOMMENDED) {
                hardStops.add("Size " + evaluation.policy().sizeYd()
                        + "yd can exceed operational haul limits for this load.");
            }
        }
        return new ArrayList<>(hardStops);
    }

    private static List<String> buildAssumptions(boolean usedAssumedAllowance, double bulkingFactor) {
        List<String> assumptions = new ArrayList<>();
        assumptions.add("density model: effective loaded bulk density");
        assumptions.add("bulking factor applied: " + bulkingFactor);
        assumptions.add(usedAssumedAllowance
                ? "allowance: estimated from typical included tons"
                : "allowance: user-provided value");
        assumptions.add("pricing and policy values are estimates and vary by hauler");
        return assumptions;
    }

    private static List<String> buildInputImpactSummary(
            List<LineItemEstimate> lineItems,
            boolean usedAssumedAllowance
    ) {
        Set<String> impacts = new HashSet<>();
        for (LineItemEstimate lineItem : lineItems) {
            impacts.addAll(lineItem.impacts());
        }
        if (usedAssumedAllowance) {
            impacts.add("allowance not provided -> typical included tons assumption used");
        }
        if (impacts.isEmpty()) {
            impacts.add("default dry/non-mixed assumptions used");
        }
        return new ArrayList<>(impacts);
    }

    private record CandidateEvaluation(
            DumpsterSizePolicy policy,
            PriceRisk risk,
            Feasibility feasibility,
            boolean volumeFits,
            int score,
            int haulCount,
            List<String> reasons
    ) {
    }
}

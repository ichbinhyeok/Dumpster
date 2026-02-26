package com.dumpster.calculator.domain.model;

import java.util.List;

public record EstimateResult(
        RangeValue volumeYd3,
        RangeValue weightTons,
        PriceRisk priceRisk,
        Feasibility feasibility,
        boolean usedAssumedAllowance,
        boolean heavyDebrisWarning,
        List<RecommendationOption> recommendations,
        List<CostComparisonOption> costComparison,
        List<String> hardStopReasons,
        List<String> assumptions,
        List<String> inputImpactSummary,
        String dataVersion,
        String calcEngineVersion
) {
}


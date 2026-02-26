package com.dumpster.calculator.domain.model;

import java.util.List;

public record RecommendationOption(
        int sizeYd,
        String label,
        PriceRisk risk,
        Feasibility feasibility,
        boolean multiHaul,
        int haulCount,
        List<String> why
) {
}


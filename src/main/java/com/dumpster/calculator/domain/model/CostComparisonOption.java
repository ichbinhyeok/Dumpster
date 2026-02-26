package com.dumpster.calculator.domain.model;

import java.util.List;

public record CostComparisonOption(
        String optionId,
        String title,
        RangeValue estimatedTotalCostUsd,
        boolean available,
        String summary,
        List<String> notes
) {
}


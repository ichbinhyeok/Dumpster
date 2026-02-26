package com.dumpster.calculator.domain.model;

import com.dumpster.calculator.domain.reference.MaterialCategory;
import java.util.List;

public record LineItemEstimate(
        String materialId,
        MaterialCategory category,
        RangeValue volumeYd3,
        RangeValue weightTons,
        List<String> impacts
) {
}


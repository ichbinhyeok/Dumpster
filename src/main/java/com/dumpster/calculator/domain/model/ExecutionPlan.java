package com.dumpster.calculator.domain.model;

import java.util.List;

public record ExecutionPlan(
        String focus,
        String dominantMaterialId,
        String dominantMaterialLabel,
        String headline,
        String summary,
        List<String> checkpoints
) {
}

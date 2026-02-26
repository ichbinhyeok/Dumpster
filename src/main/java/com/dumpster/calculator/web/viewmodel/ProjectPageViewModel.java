package com.dumpster.calculator.web.viewmodel;

public record ProjectPageViewModel(
        String projectId,
        String title,
        String recommendedUnit,
        String defaultMaterialId,
        String commonMistake,
        String canonicalPath
) {
}


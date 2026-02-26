package com.dumpster.calculator.web.viewmodel;

public record MaterialPageViewModel(
        String materialId,
        String title,
        String categoryLabel,
        double densityLow,
        double densityTyp,
        double densityHigh,
        double exampleVolumeYd3,
        double exampleWeightTypTons,
        String source,
        String cautionNote,
        String operatorQuestion
) {
}

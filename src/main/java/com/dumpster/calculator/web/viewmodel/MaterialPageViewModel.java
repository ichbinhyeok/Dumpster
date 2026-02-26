package com.dumpster.calculator.web.viewmodel;

public record MaterialPageViewModel(
        String materialId,
        String title,
        double densityLow,
        double densityTyp,
        double densityHigh,
        double exampleVolumeYd3,
        double exampleWeightTypTons,
        String source
) {
}


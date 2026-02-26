package com.dumpster.calculator.domain.reference;

public record MaterialFactor(
        String materialId,
        String name,
        MaterialCategory category,
        double densityLow,
        double densityTyp,
        double densityHigh,
        double wetMultiplierLow,
        double wetMultiplierHigh,
        DataQuality dataQuality,
        String source
) {
}


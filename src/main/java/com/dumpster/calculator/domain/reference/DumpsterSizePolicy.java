package com.dumpster.calculator.domain.reference;

public record DumpsterSizePolicy(
        int sizeYd,
        String dimensionsApprox,
        double includedTonsLow,
        double includedTonsTyp,
        double includedTonsHigh,
        double maxHaulTonsLow,
        double maxHaulTonsTyp,
        double maxHaulTonsHigh,
        double heavyDebrisMaxFillRatio,
        boolean cleanLoadRequiredForHeavy
) {
}


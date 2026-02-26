package com.dumpster.calculator.domain.model;

public record EstimateOptions(
        Boolean mixedLoad,
        Double allowanceTons,
        Double bulkingFactor
) {

    public static EstimateOptions defaults() {
        return new EstimateOptions(false, null, 1.2d);
    }

    public boolean globalMixedLoad() {
        return Boolean.TRUE.equals(mixedLoad);
    }

    public double resolvedBulkingFactor() {
        double defaultValue = bulkingFactor == null ? 1.2d : bulkingFactor;
        return Math.max(1.0d, Math.min(1.5d, defaultValue));
    }
}


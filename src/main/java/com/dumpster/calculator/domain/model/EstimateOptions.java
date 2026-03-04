package com.dumpster.calculator.domain.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;

public record EstimateOptions(
        Boolean mixedLoad,
        @DecimalMin("0.0") Double allowanceTons,
        @DecimalMin("1.0") @DecimalMax("1.5") Double bulkingFactor,
        @Pattern(regexp = "\\d{5}") String zipCode
) {

    public static EstimateOptions defaults() {
        return new EstimateOptions(false, null, 1.2d, null);
    }

    public EstimateOptions(Boolean mixedLoad, Double allowanceTons, Double bulkingFactor) {
        this(mixedLoad, allowanceTons, bulkingFactor, null);
    }

    public boolean globalMixedLoad() {
        return Boolean.TRUE.equals(mixedLoad);
    }

    public double resolvedBulkingFactor() {
        double defaultValue = bulkingFactor == null ? 1.2d : bulkingFactor;
        return Math.max(1.0d, Math.min(1.5d, defaultValue));
    }
}

package com.dumpster.calculator.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EstimateItemInput(
        @NotBlank String materialId,
        @Positive double quantity,
        @NotBlank String unitId,
        @Valid ItemConditions conditions
) {

    public ItemConditions safeConditions() {
        return conditions == null ? ItemConditions.defaults() : conditions;
    }
}

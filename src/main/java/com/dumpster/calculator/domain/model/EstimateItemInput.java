package com.dumpster.calculator.domain.model;

public record EstimateItemInput(
        String materialId,
        double quantity,
        String unitId,
        ItemConditions conditions
) {

    public ItemConditions safeConditions() {
        return conditions == null ? ItemConditions.defaults() : conditions;
    }
}


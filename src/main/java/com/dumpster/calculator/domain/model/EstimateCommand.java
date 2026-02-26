package com.dumpster.calculator.domain.model;

import java.util.List;

public record EstimateCommand(
        String projectId,
        String persona,
        List<EstimateItemInput> items,
        EstimateOptions options,
        String needTiming
) {

    public EstimateOptions safeOptions() {
        return options == null ? EstimateOptions.defaults() : options;
    }
}


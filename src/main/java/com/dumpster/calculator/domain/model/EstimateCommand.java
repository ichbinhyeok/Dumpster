package com.dumpster.calculator.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record EstimateCommand(
        @NotBlank String projectId,
        @NotBlank String persona,
        @NotNull @Size(min = 1, max = 3) List<@Valid EstimateItemInput> items,
        @Valid EstimateOptions options,
        @Pattern(regexp = "48h|this_week|research") String needTiming
) {

    public EstimateOptions safeOptions() {
        return options == null ? EstimateOptions.defaults() : options;
    }
}

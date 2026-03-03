package com.dumpster.calculator.api.dto;

import com.dumpster.calculator.domain.model.EstimateResult;

public record EstimatePreviewResponse(
        EstimateResult result
) {
}

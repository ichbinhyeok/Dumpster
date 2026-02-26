package com.dumpster.calculator.api.dto;

import com.dumpster.calculator.domain.model.EstimateResult;
import java.time.Instant;

public record EstimateApiResponse(
        String estimateId,
        Instant createdAt,
        Instant expiresAt,
        EstimateResult result
) {
}


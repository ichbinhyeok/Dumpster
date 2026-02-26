package com.dumpster.calculator.infra.persistence;

import java.time.Instant;

public record StoredEstimate(
        String estimateId,
        Instant createdAt,
        Instant expiresAt,
        StoredEstimatePayload payload
) {
}


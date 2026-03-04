package com.dumpster.calculator.infra.persistence;

import java.time.Instant;
import java.util.List;

public record QuoteMatchIntake(
        String intakeId,
        String estimateId,
        String status,
        String zipCode,
        String contactMethod,
        String contactValue,
        String persona,
        String needTiming,
        String decisionMode,
        String recommendedRoute,
        String projectId,
        List<String> materialIds,
        Instant createdAt,
        Instant updatedAt
) {
}

package com.dumpster.calculator.api.dto;

public record QuoteMatchIntakeResponse(
        String intakeId,
        String status,
        String statusLabel,
        String expectedResponseWindow,
        String submittedAtIso,
        String message
) {
}

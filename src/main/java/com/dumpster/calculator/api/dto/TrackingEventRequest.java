package com.dumpster.calculator.api.dto;

import java.util.Map;

public record TrackingEventRequest(
        String eventName,
        String estimateId,
        Map<String, Object> payload
) {
}


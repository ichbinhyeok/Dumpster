package com.dumpster.calculator.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Map;

public record TrackingEventRequest(
        @NotBlank String eventName,
        @Pattern(regexp = "^[a-zA-Z0-9-]{0,64}$") String estimateId,
        @Size(max = 25) Map<String, Object> payload
) {
}

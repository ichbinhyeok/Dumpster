package com.dumpster.calculator.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record QuoteMatchIntakeRequest(
        @Pattern(regexp = "^[a-zA-Z0-9-]{0,64}$") String estimateId,
        @NotBlank @Pattern(regexp = "\\d{5}") String zipCode,
        @NotBlank @Pattern(regexp = "email|phone") String contactMethod,
        @NotBlank @Size(max = 255) String contactValue,
        @Pattern(regexp = "homeowner|contractor|property_manager|investor|business|") String persona,
        @Pattern(regexp = "48h|this_week|research|") String needTiming,
        @Pattern(regexp = "dumpster|junk|multi_haul|unsure|") String decisionMode,
        @Pattern(regexp = "dumpster_call|dumpster_form|junk_call|") String recommendedRoute,
        @Size(max = 120) String projectId,
        @Size(max = 3) List<@NotBlank @Size(max = 80) String> materialIds
) {
}

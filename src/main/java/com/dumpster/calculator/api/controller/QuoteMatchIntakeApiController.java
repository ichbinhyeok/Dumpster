package com.dumpster.calculator.api.controller;

import com.dumpster.calculator.api.dto.QuoteMatchIntakeRequest;
import com.dumpster.calculator.api.dto.QuoteMatchIntakeResponse;
import com.dumpster.calculator.infra.persistence.QuoteMatchIntake;
import com.dumpster.calculator.infra.persistence.QuoteMatchIntakeRepository;
import com.dumpster.calculator.infra.tracking.TrackingService;
import jakarta.validation.Valid;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/quote-match/intakes")
public class QuoteMatchIntakeApiController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9()\\-\\s]{7,25}$");
    private static final DateTimeFormatter ISO_TIME = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC);

    private final QuoteMatchIntakeRepository quoteMatchIntakeRepository;
    private final TrackingService trackingService;

    public QuoteMatchIntakeApiController(
            QuoteMatchIntakeRepository quoteMatchIntakeRepository,
            TrackingService trackingService
    ) {
        this.quoteMatchIntakeRepository = quoteMatchIntakeRepository;
        this.trackingService = trackingService;
    }

    @PostMapping
    public ResponseEntity<QuoteMatchIntakeResponse> create(@Valid @RequestBody QuoteMatchIntakeRequest request) {
        validateContact(request.contactMethod(), request.contactValue());

        QuoteMatchIntake intake = quoteMatchIntakeRepository.create(
                request.estimateId(),
                request.zipCode(),
                request.contactMethod(),
                request.contactValue(),
                request.persona(),
                request.needTiming(),
                request.decisionMode(),
                request.recommendedRoute(),
                request.projectId(),
                request.materialIds()
        );

        trackingService.track("quote_match_intake_submitted", intake.estimateId(), Map.of(
                "intakeId", intake.intakeId(),
                "zipPrefix3", intake.zipCode().substring(0, 3),
                "contactMethod", intake.contactMethod(),
                "status", intake.status()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(intake));
    }

    @GetMapping("/{intakeId}")
    public ResponseEntity<?> get(@PathVariable("intakeId") String intakeId) {
        Optional<QuoteMatchIntake> intake = quoteMatchIntakeRepository.findById(intakeId);
        if (intake.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "intake_not_found"));
        }
        QuoteMatchIntake found = intake.get();
        trackingService.track("quote_match_intake_status_viewed", found.estimateId(), Map.of(
                "intakeId", found.intakeId(),
                "status", found.status()
        ));
        return ResponseEntity.ok(toResponse(found));
    }

    private static QuoteMatchIntakeResponse toResponse(QuoteMatchIntake intake) {
        return new QuoteMatchIntakeResponse(
                intake.intakeId(),
                intake.status(),
                statusLabel(intake.status()),
                expectedWindow(intake.needTiming()),
                ISO_TIME.format(intake.createdAt()),
                "Queued for partner-coverage review. This is not instant booking."
        );
    }

    private static String statusLabel(String status) {
        return switch (String.valueOf(status)) {
            case "queued_for_coverage" -> "Queued for coverage";
            case "partner_review" -> "Partner review";
            case "contacted" -> "Partner contacted";
            case "closed" -> "Closed";
            default -> "In progress";
        };
    }

    private static String expectedWindow(String needTiming) {
        return switch (String.valueOf(needTiming)) {
            case "48h" -> "Target response: 1-2 business days in covered ZIPs";
            case "this_week" -> "Target response: 2-4 business days in covered ZIPs";
            default -> "Target response: 3-7 business days in covered ZIPs";
        };
    }

    private static void validateContact(String method, String value) {
        String trimmed = value == null ? "" : value.trim();
        if ("email".equals(method) && !EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("contactValue is not a valid email");
        }
        if ("phone".equals(method) && !PHONE_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("contactValue is not a valid phone number");
        }
    }
}

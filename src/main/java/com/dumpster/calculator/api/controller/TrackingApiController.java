package com.dumpster.calculator.api.controller;

import com.dumpster.calculator.api.dto.TrackingEventRequest;
import com.dumpster.calculator.infra.tracking.TrackingService;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/events")
public class TrackingApiController {

    private static final Set<String> ALLOWED_EVENTS = Set.of(
            "calc_started",
            "calc_completed",
            "result_viewed",
            "cta_click_dumpster_call",
            "cta_click_dumpster_form",
            "cta_click_junk_call",
            "persona_selected",
            "heavy_debris_flagged",
            "allowance_entered",
            "used_assumed_allowance",
            "feasibility_not_ok",
            "share_estimate_created"
    );

    private final TrackingService trackingService;

    public TrackingApiController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> track(@Valid @RequestBody TrackingEventRequest request) {
        if (!ALLOWED_EVENTS.contains(request.eventName())) {
            throw new IllegalArgumentException("eventName is not allowed");
        }
        if (request.payload() != null && request.payload().size() > 25) {
            throw new IllegalArgumentException("payload has too many keys");
        }
        trackingService.track(request.eventName(), request.estimateId(), request.payload());
        return ResponseEntity.ok(Map.of("status", "tracked"));
    }
}

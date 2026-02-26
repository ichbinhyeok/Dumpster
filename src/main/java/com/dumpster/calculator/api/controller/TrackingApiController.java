package com.dumpster.calculator.api.controller;

import com.dumpster.calculator.api.dto.TrackingEventRequest;
import com.dumpster.calculator.infra.tracking.TrackingService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class TrackingApiController {

    private final TrackingService trackingService;

    public TrackingApiController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> track(@RequestBody TrackingEventRequest request) {
        if (request.eventName() == null || request.eventName().isBlank()) {
            throw new IllegalArgumentException("eventName is required");
        }
        trackingService.track(request.eventName(), request.estimateId(), request.payload());
        return ResponseEntity.ok(Map.of("status", "tracked"));
    }
}


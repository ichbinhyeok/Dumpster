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
            "allowance_entered",
            "answer_page_group",
            "calc_completed",
            "calc_completed_client",
            "calc_completed_server",
            "calc_started",
            "call_qualified",
            "comparison_hub_entry_click",
            "comparison_page_cta_click",
            "comparison_page_exit_to_calculator",
            "comparison_page_view",
            "comparison_priority_selected",
            "content_gate_fail",
            "content_gate_pass",
            "cta_click_dumpster_call",
            "cta_click_dumpster_form",
            "cta_click_heavy_rules",
            "cta_click_junk_call",
            "decision_mode_selected",
            "decision_priority_applied",
            "decision_scorecard_rendered",
            "decision_stage_link_click",
            "feasibility_not_ok",
            "heavy_debris_flagged",
            "lead_submitted",
            "market_zip_entered",
            "material_page_to_calculator_click",
            "multi_material_line_added",
            "multi_material_line_removed",
            "persona_selected",
            "pickup_converter_used",
            "project_page_to_calculator_click",
            "quote_match_intake_status_viewed",
            "quote_match_intake_submitted",
            "result_viewed",
            "rules_page_to_calculator_click",
            "share_estimate_created",
            "used_assumed_allowance",
            "vendor_questions_expand"
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

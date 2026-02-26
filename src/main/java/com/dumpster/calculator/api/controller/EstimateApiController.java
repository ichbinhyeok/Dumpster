package com.dumpster.calculator.api.controller;

import com.dumpster.calculator.api.dto.EstimateApiResponse;
import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateResult;
import com.dumpster.calculator.domain.service.EstimationFacade;
import com.dumpster.calculator.infra.persistence.EstimateStorageService;
import com.dumpster.calculator.infra.persistence.StoredEstimate;
import com.dumpster.calculator.infra.tracking.TrackingService;
import jakarta.validation.Valid;
import java.util.Map;
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
@RequestMapping("/api/estimates")
public class EstimateApiController {

    private final EstimationFacade estimationFacade;
    private final EstimateStorageService estimateStorageService;
    private final TrackingService trackingService;

    public EstimateApiController(
            EstimationFacade estimationFacade,
            EstimateStorageService estimateStorageService,
            TrackingService trackingService
    ) {
        this.estimationFacade = estimationFacade;
        this.estimateStorageService = estimateStorageService;
        this.trackingService = trackingService;
    }

    @PostMapping
    public ResponseEntity<EstimateApiResponse> createEstimate(@Valid @RequestBody EstimateCommand command) {
        EstimateResult result = estimationFacade.estimate(command);
        StoredEstimate storedEstimate = estimateStorageService.save(command, result);
        trackingService.track("calc_completed", storedEstimate.estimateId(), Map.of(
                "projectId", command.projectId(),
                "persona", command.persona(),
                "priceRisk", result.priceRisk().name(),
                "feasibility", result.feasibility().name()
        ));
        EstimateApiResponse response = new EstimateApiResponse(
                storedEstimate.estimateId(),
                storedEstimate.createdAt(),
                storedEstimate.expiresAt(),
                storedEstimate.payload().result()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{estimateId}")
    public ResponseEntity<?> getEstimate(@PathVariable String estimateId) {
        return estimateStorageService.findValidById(estimateId)
                .<ResponseEntity<?>>map(stored -> {
                    trackingService.track("result_viewed", estimateId, Map.of("source", "api_get"));
                    return ResponseEntity.ok(new EstimateApiResponse(
                            stored.estimateId(),
                            stored.createdAt(),
                            stored.expiresAt(),
                            stored.payload().result()
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "estimate_not_found_or_expired")));
    }
}

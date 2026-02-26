package com.dumpster.calculator.api.controller;

import com.dumpster.calculator.api.dto.EstimateApiResponse;
import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateResult;
import com.dumpster.calculator.domain.service.EstimationFacade;
import com.dumpster.calculator.infra.persistence.EstimateStorageService;
import com.dumpster.calculator.infra.persistence.StoredEstimate;
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

    public EstimateApiController(EstimationFacade estimationFacade, EstimateStorageService estimateStorageService) {
        this.estimationFacade = estimationFacade;
        this.estimateStorageService = estimateStorageService;
    }

    @PostMapping
    public ResponseEntity<EstimateApiResponse> createEstimate(@RequestBody EstimateCommand command) {
        EstimateResult result = estimationFacade.estimate(command);
        StoredEstimate storedEstimate = estimateStorageService.save(command, result);
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
                .<ResponseEntity<?>>map(stored -> ResponseEntity.ok(new EstimateApiResponse(
                        stored.estimateId(),
                        stored.createdAt(),
                        stored.expiresAt(),
                        stored.payload().result()
                )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "estimate_not_found_or_expired")));
    }
}


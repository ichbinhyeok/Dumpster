package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateResult;

public record StoredEstimatePayload(
        EstimateCommand request,
        EstimateResult result
) {
}


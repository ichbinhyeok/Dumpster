package com.dumpster.calculator.domain.service;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EstimationFacade {

    public Map<String, Object> placeholderResult() {
        return Map.of("message", "estimation engine scaffold ready");
    }
}

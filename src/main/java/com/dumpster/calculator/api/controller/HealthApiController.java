package com.dumpster.calculator.api.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthApiController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "ok", "service", "dumpster-calculator");
    }
}

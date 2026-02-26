package com.dumpster.calculator.infra.tracking;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class TrackingService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public TrackingService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper, Clock clock) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    public void track(String eventName, String estimateId, Map<String, Object> payload) {
        String eventPayload = "{}";
        try {
            eventPayload = objectMapper.writeValueAsString(payload == null ? Map.of() : payload);
        } catch (Exception ignored) {
            eventPayload = "{\"serialization\":\"failed\"}";
        }
        jdbcTemplate.update(
                "insert into tracking_events (estimate_id, event_name, event_payload, created_at) values (?, ?, ?, ?)",
                estimateId,
                eventName,
                eventPayload,
                Instant.now(clock)
        );
    }
}


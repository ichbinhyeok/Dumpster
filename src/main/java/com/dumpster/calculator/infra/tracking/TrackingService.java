package com.dumpster.calculator.infra.tracking;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class TrackingService {

    private static final int MAX_EVENT_NAME_LEN = 120;
    private static final int MAX_ESTIMATE_ID_LEN = 64;
    private static final int MAX_EVENT_PAYLOAD_LEN = 4096;

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
        if (eventPayload.length() > MAX_EVENT_PAYLOAD_LEN) {
            eventPayload = eventPayload.substring(0, MAX_EVENT_PAYLOAD_LEN);
        }
        String safeEventName = truncate(eventName, MAX_EVENT_NAME_LEN);
        String safeEstimateId = truncate(estimateId, MAX_ESTIMATE_ID_LEN);
        jdbcTemplate.update(
                "insert into tracking_events (estimate_id, event_name, event_payload, created_at) values (?, ?, ?, ?)",
                safeEstimateId,
                safeEventName,
                eventPayload,
                Instant.now(clock)
        );
    }

    private static String truncate(String value, int maxLen) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLen ? value : value.substring(0, maxLen);
    }
}

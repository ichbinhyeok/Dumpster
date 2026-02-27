package com.dumpster.calculator.infra.tracking;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
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
    private final double nullEstimateSampleRate;

    public TrackingService(
            JdbcTemplate jdbcTemplate,
            ObjectMapper objectMapper,
            Clock clock,
            @Value("${app.tracking.null-estimate-sample-rate:0.2}") double nullEstimateSampleRate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.clock = clock;
        this.nullEstimateSampleRate = normalizeSampleRate(nullEstimateSampleRate);
    }

    public void track(String eventName, String estimateId, Map<String, Object> payload) {
        if (isBlank(estimateId) && ThreadLocalRandom.current().nextDouble() > nullEstimateSampleRate) {
            return;
        }
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
        String safeEstimateId = truncate(isBlank(estimateId) ? null : estimateId, MAX_ESTIMATE_ID_LEN);
        jdbcTemplate.update(
                "insert into tracking_events (estimate_id, event_name, event_payload, created_at) values (?, ?, ?, ?)",
                safeEstimateId,
                safeEventName,
                eventPayload,
                Instant.now(clock)
        );
    }

    public int deleteOlderThanDays(int retentionDays) {
        int safeRetentionDays = Math.max(1, retentionDays);
        Instant cutoff = Instant.now(clock).minusSeconds((long) safeRetentionDays * 24L * 60L * 60L);
        return jdbcTemplate.update(
                "delete from tracking_events where created_at < ?",
                cutoff
        );
    }

    private static String truncate(String value, int maxLen) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLen ? value : value.substring(0, maxLen);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static double normalizeSampleRate(double sampleRate) {
        if (sampleRate < 0.0d) {
            return 0.0d;
        }
        if (sampleRate > 1.0d) {
            return 1.0d;
        }
        return sampleRate;
    }
}

package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateResult;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
public class EstimateStorageService {

    private static final Duration ESTIMATE_TTL = Duration.ofDays(30);

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public EstimateStorageService(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper, Clock clock) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @Transactional
    public StoredEstimate save(EstimateCommand request, EstimateResult result) {
        Instant now = Instant.now(clock);
        Instant expiresAt = now.plus(ESTIMATE_TTL);
        String estimateId = UUID.randomUUID().toString();
        StoredEstimatePayload payload = new StoredEstimatePayload(request, result);
        String payloadJson = writePayload(payload);

        jdbcTemplate.update(
                "insert into estimates (estimate_id, created_at, expires_at, payload_json) values (?, ?, ?, ?)",
                estimateId,
                now,
                expiresAt,
                payloadJson
        );
        return new StoredEstimate(estimateId, now, expiresAt, payload);
    }

    @Transactional(readOnly = true)
    public Optional<StoredEstimate> findValidById(String estimateId) {
        return jdbcTemplate.query(
                        "select estimate_id, created_at, expires_at, payload_json from estimates where estimate_id = ?",
                        (rs, rowNum) -> new StoredEstimate(
                                rs.getString("estimate_id"),
                                rs.getTimestamp("created_at").toInstant(),
                                rs.getTimestamp("expires_at").toInstant(),
                                readPayload(rs.getString("payload_json"))
                        ),
                        estimateId
                ).stream()
                .findFirst()
                .filter(estimate -> estimate.expiresAt().isAfter(Instant.now(clock)));
    }

    @Transactional
    public int deleteExpired() {
        return jdbcTemplate.update(
                "delete from estimates where expires_at < ?",
                Instant.now(clock)
        );
    }

    private String writePayload(StoredEstimatePayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize estimate payload.", e);
        }
    }

    private StoredEstimatePayload readPayload(String payloadJson) {
        try {
            return objectMapper.readValue(payloadJson, StoredEstimatePayload.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize estimate payload.", e);
        }
    }
}

package com.dumpster.calculator.infra.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteMatchIntakeRepository {

    private static final String STATUS_QUEUED = "queued_for_coverage";
    private final JdbcTemplate jdbcTemplate;

    public QuoteMatchIntakeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public QuoteMatchIntake create(
            String estimateId,
            String zipCode,
            String contactMethod,
            String contactValue,
            String persona,
            String needTiming,
            String decisionMode,
            String recommendedRoute,
            String projectId,
            List<String> materialIds
    ) {
        Instant now = Instant.now();
        String intakeId = "qmb_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        String safeMaterialIds = joinMaterialIds(materialIds);

        jdbcTemplate.update(
                """
                        insert into quote_match_intakes (
                            intake_id,
                            estimate_id,
                            status,
                            zip_code,
                            contact_method,
                            contact_value,
                            persona,
                            need_timing,
                            decision_mode,
                            recommended_route,
                            project_id,
                            material_ids,
                            created_at,
                            updated_at
                        ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                intakeId,
                blankToNull(estimateId),
                STATUS_QUEUED,
                zipCode,
                contactMethod,
                truncate(contactValue, 255),
                blankToNull(persona),
                blankToNull(needTiming),
                blankToNull(decisionMode),
                blankToNull(recommendedRoute),
                blankToNull(projectId),
                safeMaterialIds,
                Timestamp.from(now),
                Timestamp.from(now)
        );

        return new QuoteMatchIntake(
                intakeId,
                blankToNull(estimateId),
                STATUS_QUEUED,
                zipCode,
                contactMethod,
                truncate(contactValue, 255),
                blankToNull(persona),
                blankToNull(needTiming),
                blankToNull(decisionMode),
                blankToNull(recommendedRoute),
                blankToNull(projectId),
                materialIds == null ? List.of() : List.copyOf(materialIds),
                now,
                now
        );
    }

    public Optional<QuoteMatchIntake> findById(String intakeId) {
        return jdbcTemplate.query(
                        """
                                select intake_id, estimate_id, status, zip_code, contact_method, contact_value,
                                       persona, need_timing, decision_mode, recommended_route, project_id,
                                       material_ids, created_at, updated_at
                                from quote_match_intakes
                                where intake_id = ?
                                """,
                        (rs, rowNum) -> new QuoteMatchIntake(
                                rs.getString("intake_id"),
                                rs.getString("estimate_id"),
                                rs.getString("status"),
                                rs.getString("zip_code"),
                                rs.getString("contact_method"),
                                rs.getString("contact_value"),
                                rs.getString("persona"),
                                rs.getString("need_timing"),
                                rs.getString("decision_mode"),
                                rs.getString("recommended_route"),
                                rs.getString("project_id"),
                                splitMaterialIds(rs.getString("material_ids")),
                                rs.getTimestamp("created_at").toInstant(),
                                rs.getTimestamp("updated_at").toInstant()
                        ),
                        intakeId
                ).stream()
                .findFirst();
    }

    private static String joinMaterialIds(List<String> materialIds) {
        if (materialIds == null || materialIds.isEmpty()) {
            return null;
        }
        return materialIds.stream()
                .filter(id -> id != null && !id.isBlank())
                .map(id -> truncate(id.trim(), 80))
                .distinct()
                .limit(3)
                .reduce((a, b) -> a + "," + b)
                .orElse(null);
    }

    private static List<String> splitMaterialIds(String materialIds) {
        if (materialIds == null || materialIds.isBlank()) {
            return List.of();
        }
        return Arrays.stream(materialIds.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();
    }

    private static String truncate(String value, int maxLen) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLen ? value : value.substring(0, maxLen);
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }
}

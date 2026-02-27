package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.infra.tracking.TrackingService;
import java.sql.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class TrackingCleanupJobTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TrackingService trackingService;

    @Test
    void deleteOlderThanDaysRemovesExpiredEvents() {
        jdbcTemplate.update("delete from tracking_events where event_name like 'cleanup_test_%'");

        Instant now = Instant.now();
        jdbcTemplate.update(
                "insert into tracking_events (estimate_id, event_name, event_payload, created_at) values (?, ?, ?, ?)",
                "old-id",
                "cleanup_test_old",
                "{}",
                Timestamp.from(now.minusSeconds(95L * 24L * 60L * 60L))
        );
        jdbcTemplate.update(
                "insert into tracking_events (estimate_id, event_name, event_payload, created_at) values (?, ?, ?, ?)",
                "new-id",
                "cleanup_test_new",
                "{}",
                Timestamp.from(now.minusSeconds(10L * 24L * 60L * 60L))
        );

        int deleted = trackingService.deleteOlderThanDays(90);
        assertThat(deleted).isGreaterThanOrEqualTo(1);

        Integer oldCount = jdbcTemplate.queryForObject(
                "select count(*) from tracking_events where event_name = 'cleanup_test_old'",
                Integer.class
        );
        Integer newCount = jdbcTemplate.queryForObject(
                "select count(*) from tracking_events where event_name = 'cleanup_test_new'",
                Integer.class
        );
        assertThat(oldCount).isZero();
        assertThat(newCount).isEqualTo(1);
    }
}

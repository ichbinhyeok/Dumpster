package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.infra.tracking.TrackingService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "app.tracking.null-estimate-sample-rate=0.0"
})
class TrackingServiceSamplingTests {

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanMarkers() {
        jdbcTemplate.update("delete from tracking_events where event_payload like '%sampling_test_%'");
    }

    @Test
    void nullEstimateTopFunnelEventBypassesSampling() {
        trackingService.track("calc_started", null, Map.of("source", "sampling_test_keep"));

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from tracking_events where event_name = 'calc_started' and event_payload like '%sampling_test_keep%'",
                Integer.class
        );
        assertThat(count).isEqualTo(1);
    }

    @Test
    void nullEstimateNonTopFunnelEventIsDroppedAtZeroSamplingRate() {
        trackingService.track("result_viewed", null, Map.of("source", "sampling_test_drop"));

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from tracking_events where event_name = 'result_viewed' and event_payload like '%sampling_test_drop%'",
                Integer.class
        );
        assertThat(count).isZero();
    }

    @Test
    void eventWithEstimateIdIsAlwaysStored() {
        trackingService.track("result_viewed", "sampling-estimate-id", Map.of("source", "sampling_test_with_id"));

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from tracking_events where estimate_id = 'sampling-estimate-id' and event_payload like '%sampling_test_with_id%'",
                Integer.class
        );
        assertThat(count).isEqualTo(1);
    }
}

package com.dumpster.calculator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dumpster.calculator.infra.tracking.TrackingRequestGuardFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@TestPropertySource(properties = {
        "app.tracking.rate-limit-per-minute=3",
        "app.tracking.max-body-bytes=200"
})
class TrackingApiGuardTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TrackingRequestGuardFilter trackingRequestGuardFilter;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(trackingRequestGuardFilter)
                .build();
    }

    @Test
    void trackingRequestBodyTooLargeReturns413() throws Exception {
        String payload = """
                {
                  "eventName":"calc_started",
                  "estimateId":"test-id",
                  "payload":{"blob":"%s"}
                }
                """.formatted("x".repeat(500));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Forwarded-For", "198.51.100.40")
                        .content(payload))
                .andExpect(status().isPayloadTooLarge());
    }

    @Test
    void trackingRateLimitReturns429() throws Exception {
        String payload = """
                {"eventName":"calc_started","estimateId":"test-id","payload":{"source":"test"}}
                """;
        String ip = "198.51.100.41";

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Forwarded-For", ip)
                            .content(payload))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Forwarded-For", ip)
                        .content(payload))
                .andExpect(status().isTooManyRequests());
    }
}

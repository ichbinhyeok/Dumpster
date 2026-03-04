package com.dumpster.calculator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class TrackingApiControllerEventAllowlistTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void acceptsDecisionAndComparisonEvents() throws Exception {
        List<String> allowedEvents = List.of(
                "decision_mode_selected",
                "decision_scorecard_rendered",
                "decision_priority_applied",
                "content_gate_pass",
                "content_gate_fail",
                "comparison_page_view",
                "comparison_page_exit_to_calculator",
                "comparison_priority_selected",
                "cta_click_heavy_rules",
                "market_zip_entered",
                "pickup_converter_used",
                "answer_page_group",
                "vendor_questions_expand",
                "multi_material_line_added",
                "multi_material_line_removed",
                "quote_match_intake_submitted",
                "quote_match_intake_status_viewed"
        );

        for (String eventName : allowedEvents) {
            String body = """
                    {"eventName":"%s","estimateId":"test-estimate","payload":{"source":"test"}}
                    """.formatted(eventName);

            mockMvc.perform(post("/api/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void rejectsUnknownEventName() throws Exception {
        String body = """
                {"eventName":"totally_unknown_event","estimateId":"test-estimate","payload":{"source":"test"}}
                """;

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}

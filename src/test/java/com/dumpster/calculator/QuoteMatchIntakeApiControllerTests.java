package com.dumpster.calculator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
class QuoteMatchIntakeApiControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createAndFetchQuoteMatchIntake() throws Exception {
        String createBody = """
                {
                  "estimateId":"test-estimate-id",
                  "zipCode":"30339",
                  "contactMethod":"email",
                  "contactValue":"owner@example.com",
                  "persona":"homeowner",
                  "needTiming":"this_week",
                  "decisionMode":"dumpster",
                  "recommendedRoute":"dumpster_call",
                  "projectId":"garage_cleanout",
                  "materialIds":["household_junk","furniture"]
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/quote-match/intakes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.intakeId").value(org.hamcrest.Matchers.startsWith("qmb_")))
                .andExpect(jsonPath("$.status").value("queued_for_coverage"))
                .andExpect(jsonPath("$.statusLabel").value("Queued for coverage"))
                .andReturn();

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = objectMapper.readValue(createResult.getResponse().getContentAsString(), Map.class);
        String intakeId = String.valueOf(payload.get("intakeId"));

        mockMvc.perform(get("/api/quote-match/intakes/{intakeId}", intakeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intakeId").value(intakeId))
                .andExpect(jsonPath("$.status").value("queued_for_coverage"));
    }

    @Test
    void rejectsInvalidContactValue() throws Exception {
        String createBody = """
                {
                  "estimateId":"test-estimate-id",
                  "zipCode":"30339",
                  "contactMethod":"email",
                  "contactValue":"invalid-email",
                  "persona":"homeowner",
                  "needTiming":"research",
                  "decisionMode":"unsure",
                  "recommendedRoute":"dumpster_form",
                  "projectId":"garage_cleanout",
                  "materialIds":["household_junk"]
                }
                """;

        mockMvc.perform(post("/api/quote-match/intakes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isBadRequest());
    }
}

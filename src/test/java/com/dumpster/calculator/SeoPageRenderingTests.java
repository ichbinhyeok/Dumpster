package com.dumpster.calculator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class SeoPageRenderingTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void materialPageRendersAnswerQuickRulesFaqAndSchema() throws Exception {
        mockMvc.perform(get("/dumpster/weight/asphalt_shingles"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-header")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quick rules")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Asphalt shingles are weight-first debris.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"@type\": \"FAQPage\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/dumpster/size-weight-calculator?material=asphalt_shingles")));
    }

    @Test
    void projectPageRendersAnswerQuickRulesFaqAndSchema() throws Exception {
        mockMvc.perform(get("/dumpster/size/roof_tearoff"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-header")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quick rules")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Roof tear-off decisions should be weight-first.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"@type\": \"FAQPage\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/dumpster/size-weight-calculator?project=roof_tearoff&material=asphalt_shingles")));
    }
}

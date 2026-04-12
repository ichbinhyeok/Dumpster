package com.dumpster.calculator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class CalculatorPageRenderingTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void calculatorPageRendersHeaderFooterAndKeepsSchema() throws Exception {
        mockMvc.perform(get("/dumpster/size-weight-calculator"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-header")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Heavy Debris Dumpster Calculator")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Answer-led heavy debris sizing")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("decision-stage debris questions")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Winner decision guides")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/about/quote-match-beta")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("value=\"concrete_removal\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("value=\"concrete\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Other cleanup cases")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Check local heavy-load options")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"@type\": \"WebApplication\"")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Data updated:"))));
    }

    @Test
    void rootPathRedirectsToCalculatorCanonicalUrl() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/dumpster/size-weight-calculator"));
    }
}

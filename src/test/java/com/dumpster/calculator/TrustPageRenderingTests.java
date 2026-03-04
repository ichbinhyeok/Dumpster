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
class TrustPageRenderingTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void methodologyPageRenders() throws Exception {
        mockMvc.perform(get("/about/methodology"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Calculation Methodology")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("How the estimate is built")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")));
    }

    @Test
    void editorialPolicyPageRenders() throws Exception {
        mockMvc.perform(get("/about/editorial-policy"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Editorial Policy")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Content standards")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")));
    }

    @Test
    void contactPageRenders() throws Exception {
        mockMvc.perform(get("/about/contact"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("General inquiries")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Correction requests")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")));
    }

    @Test
    void quoteMatchBetaPageRenders() throws Exception {
        mockMvc.perform(get("/about/quote-match-beta"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quote Match Beta")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("not instant booking")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")));
    }
}

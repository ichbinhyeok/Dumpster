package com.dumpster.calculator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
        mockMvc.perform(get("/dumpster/weight/shingles"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-header")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quick rules")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Next decision steps")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Asphalt shingles are weight-first debris.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"@type\": \"FAQPage\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/dumpster/size-weight-calculator?material=asphalt_shingles")));
    }

    @Test
    void projectPageRendersAnswerQuickRulesFaqAndSchema() throws Exception {
        mockMvc.perform(get("/dumpster/size/roof-tear-off"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-header")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("site-footer")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quick rules")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Next decision steps")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Roof tear-off decisions should be weight-first.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"@type\": \"FAQPage\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/dumpster/size-weight-calculator?project=roof_tearoff&material=asphalt_shingles")));
    }

    @Test
    void materialPageRendersWeightTable() throws Exception {
        mockMvc.perform(get("/dumpster/weight/shingles"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("data-table")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Estimated weight by dumpster size")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Overage risk")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("tons")));
    }

    @Test
    void materialGuidesHubRendersComparisonTable() throws Exception {
        mockMvc.perform(get("/dumpster/material-guides"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Heavy Debris")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("data-table")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/dumpster/dumpster-vs-junk-removal-which-is-cheaper")));
    }

    @Test
    void projectGuidesHubIncludesComparisonHubLink() throws Exception {
        mockMvc.perform(get("/dumpster/project-guides"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("All project guides")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/dumpster/dumpster-vs-junk-removal-which-is-cheaper")));
    }

    @Test
    void guideHubsAreServedAsNoindexByPolicy() throws Exception {
        mockMvc.perform(get("/dumpster/material-guides"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")));

        mockMvc.perform(get("/dumpster/project-guides"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")));
    }

    @Test
    void heavyRulesPageRendersLimitTable() throws Exception {
        mockMvc.perform(get("/dumpster/heavy-debris-rules"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("data-table")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Max haul")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/dumpster/dumpster-vs-junk-removal-which-is-cheaper")));
    }

    @Test
    void intentPageRendersDirectAnswerAndComparisonTable() throws Exception {
        mockMvc.perform(get("/dumpster/answers/roof-tear-off/shingles/size-guide"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Direct answer:")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Size-by-size load comparison")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Homeowner decision blocks")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Next decision steps")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"@type\": \"BreadcrumbList\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Related intent guides")));
    }

    @Test
    void intentPageRendersForRoofSecondaryMaterials() throws Exception {
        mockMvc.perform(get("/dumpster/answers/roof-tear-off/tile-ceramic/size-guide"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Direct answer:")));

        mockMvc.perform(get("/dumpster/answers/roof-tear-off/metal-scrap-light/size-guide"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Direct answer:")));
    }

    @Test
    void intentAliasRouteRedirectsToCanonicalPath() throws Exception {
        mockMvc.perform(get("/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/dumpster/answers/roof-tear-off/shingles/size-guide"));
    }

    @Test
    void whitelistedIntentPageIsIndexable() throws Exception {
        mockMvc.perform(get("/dumpster/answers/roof-tear-off/shingles/overage-risk"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("index")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("content=\"max-snippet:-1,max-image-preview:large,max-video-preview:-1\"")));
    }

    @Test
    void nonWhitelistedIntentPageRemainsNoindex() throws Exception {
        mockMvc.perform(get("/dumpster/answers/roof-tear-off/tile-ceramic/size-guide"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("content=\"noindex,follow,max-snippet:-1,max-image-preview:large,max-video-preview:-1\"")));
    }

    @Test
    void nonPriorityProjectPageRemainsAccessibleButNoindex() throws Exception {
        mockMvc.perform(get("/dumpster/size/concrete-removal"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")));
    }

    @Test
    void nonPrioritySpecialPageRemainsAccessibleButNoindex() throws Exception {
        mockMvc.perform(get("/dumpster/what-size-dumpster-do-i-need"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("noindex")));
    }

    @Test
    void specialDecisionPageRendersDirectAnswerMatrixAndSchema() throws Exception {
        mockMvc.perform(get("/dumpster/dumpster-vs-junk-removal-which-is-cheaper"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Direct answer:")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Decision matrix")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"@type\": \"FAQPage\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"@type\": \"BreadcrumbList\"")));
    }

    @Test
    void legacySpecialSlugsRedirectToCanonicalSlug() throws Exception {
        mockMvc.perform(get("/dumpster/how-many-tons-can-a-10-yard-dumpster-hold"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/dumpster/10-yard-dumpster-weight-limit-overage"));

        mockMvc.perform(get("/dumpster/dumpster-vs-junk-removal"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/dumpster/dumpster-vs-junk-removal-which-is-cheaper"));

        mockMvc.perform(get("/dumpster/roofing-squares-to-dumpster-size"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/dumpster/roof-shingles-dumpster-size-calculator"));
    }

    @Test
    void waveThreePagesRenderWithDefaultWaveThree() throws Exception {
        mockMvc.perform(get("/dumpster/weight/brick-block"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("index")));

        mockMvc.perform(get("/dumpster/size/garage-cleanout"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("index")));

        mockMvc.perform(get("/dumpster/fill-line-rules-for-heavy-debris"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Robots-Tag", org.hamcrest.Matchers.containsString("index")));
    }
}

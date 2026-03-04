package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.web.content.SeoContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "app.seo.max-wave=3",
        "app.seo.intent-index-mode=curated"
})
class SeoIntentIndexModeTests {

    @Autowired
    private SeoContentService seoContentService;

    @Test
    void curatedModeKeepsExplicitIntentSeedSet() {
        var paths = seoContentService.indexableIntentPaths();

        assertThat(paths).hasSize(19);
        assertThat(paths).contains("/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide");
        assertThat(paths).contains("/dumpster/answers/concrete_removal/concrete/overage-risk");
        assertThat(paths).contains("/dumpster/answers/garage_cleanout/household_junk/size-guide");
        assertThat(paths).doesNotContain("/dumpster/answers/roof_tearoff/tile_ceramic/size-guide");
    }
}


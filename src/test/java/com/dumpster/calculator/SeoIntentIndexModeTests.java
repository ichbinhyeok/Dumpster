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
    void curatedModeKeepsExplicitPrimaryAndExperimentIntentSets() {
        var paths = seoContentService.indexableIntentPaths();

        assertThat(paths).hasSize(34);
        assertThat(paths).contains("/dumpster/answers/roof-tear-off/shingles/size-guide");
        assertThat(paths).contains("/dumpster/answers/concrete-removal/concrete/overage-risk");
        assertThat(paths).contains("/dumpster/answers/garage-cleanout/household-junk/size-guide");
        assertThat(paths).contains("/dumpster/answers/bathroom-remodel/tile-ceramic/size-guide");
        assertThat(paths).contains("/dumpster/answers/concrete-removal/brick-block/overage-risk");
        assertThat(paths).contains("/dumpster/answers/estate-cleanout/furniture/weight-estimate");
        assertThat(paths).contains("/dumpster/answers/light-commercial-fitout/drywall/size-guide");
        assertThat(paths).doesNotContain("/dumpster/answers/roof-tear-off/tile-ceramic/size-guide");
    }
}

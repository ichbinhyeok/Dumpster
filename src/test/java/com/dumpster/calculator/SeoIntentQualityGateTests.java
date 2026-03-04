package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.web.content.SeoContentService;
import com.dumpster.calculator.web.viewmodel.IntentPageViewModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeoIntentQualityGateTests {

    @Autowired
    private SeoContentService seoContentService;

    @Test
    void indexableIntentPagesMeetHumanizedTitleAndBlockCompletenessRules() {
        List<String> paths = seoContentService.indexableIntentPaths();
        assertThat(paths).isNotEmpty();

        Set<String> uniquePageTitles = new HashSet<>();
        Set<String> uniqueSeoTitles = new HashSet<>();

        for (String path : paths) {
            String[] parts = path.split("/");
            assertThat(parts).hasSizeGreaterThanOrEqualTo(6);
            String projectId = parts[3];
            String materialId = parts[4];
            String intentSlug = parts[5];
            IntentPageViewModel page = seoContentService.intentPage(
                    projectId,
                    materialId,
                    intentSlug,
                    "https://debrisdecision.com"
            ).orElseThrow();

            String pageTitle = page.pageTitle();
            String seoTitle = page.seoTitle();

            assertThat(pageTitle).isNotBlank();
            assertThat(seoTitle).isNotBlank();
            assertThat(pageTitle).doesNotContain("_");
            assertThat(seoTitle).doesNotContain("_");
            assertThat(pageTitle.toLowerCase()).doesNotContain("routing signal");
            assertThat(seoTitle.toLowerCase()).doesNotContain("routing signal");
            assertThat(pageTitle.toLowerCase()).doesNotContain("dumpster size for dumpster size");
            assertThat(seoTitle.toLowerCase()).doesNotContain("dumpster size for dumpster size");

            assertThat(page.directAnswer()).isNotBlank();
            assertThat(page.decisionChecklist()).hasSizeGreaterThanOrEqualTo(4);
            assertThat(page.homeownerDecisionBlocks()).hasSizeGreaterThanOrEqualTo(7);
            assertThat(page.faqItems()).hasSizeGreaterThanOrEqualTo(3);
            assertThat(page.decisionStageLinks()).isNotEmpty();

            uniquePageTitles.add(pageTitle);
            uniqueSeoTitles.add(seoTitle);
        }

        assertThat(uniquePageTitles.size()).isGreaterThanOrEqualTo((int) Math.floor(paths.size() * 0.95));
        assertThat(uniqueSeoTitles.size()).isGreaterThanOrEqualTo((int) Math.floor(paths.size() * 0.95));
    }
}

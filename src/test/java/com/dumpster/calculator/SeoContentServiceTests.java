package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.web.content.SeoContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeoContentServiceTests {

    @Autowired
    private SeoContentService seoContentService;

    @Test
    void materialPageIncludesAnswerFirstQuickRulesAndFaq() {
        var topMaterial = seoContentService.materialPage("asphalt_shingles", "http://localhost:8080").orElseThrow();
        var fallbackMaterial = seoContentService.materialPage("plaster", "http://localhost:8080").orElseThrow();

        assertThat(topMaterial.answerFirst()).isNotBlank();
        assertThat(topMaterial.quickRules()).hasSize(3);
        assertThat(topMaterial.faqItems()).hasSize(3);

        assertThat(fallbackMaterial.answerFirst()).isNotBlank();
        assertThat(fallbackMaterial.quickRules()).hasSize(3);
        assertThat(fallbackMaterial.faqItems()).hasSize(3);
    }

    @Test
    void projectPageIncludesAnswerFirstQuickRulesAndFaq() {
        var project = seoContentService.projectPage("roof_tearoff", "http://localhost:8080").orElseThrow();

        assertThat(project.answerFirst()).isNotBlank();
        assertThat(project.quickRules()).hasSize(3);
        assertThat(project.faqItems()).hasSize(3);
    }
}

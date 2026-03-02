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

    @Test
    void materialPageIncludesSizeWeightTableAndSeoFields() {
        var material = seoContentService.materialPage("asphalt_shingles", "http://localhost:8080").orElseThrow();

        assertThat(material.sizeWeightTable()).hasSize(5);
        assertThat(material.sizeWeightTable().getFirst().sizeYd()).isEqualTo(10);
        assertThat(material.sizeWeightTable().getFirst().weightTypTons()).isGreaterThan(0.0d);
        assertThat(material.seoTitle()).contains("lbs/yd3");
        assertThat(material.metaDescription()).contains("tons");
        assertThat(material.calculatorAbsoluteUrl()).startsWith("http");
    }

    @Test
    void intentPageBuildsWithChecklistAndFaq() {
        var intent = seoContentService.intentPage(
                "roof_tearoff",
                "asphalt_shingles",
                "size-guide",
                "http://localhost:8080"
        ).orElseThrow();

        assertThat(intent.intentLabel()).isEqualTo("Size Guide");
        assertThat(intent.directAnswer()).contains("yard");
        assertThat(intent.sizeWeightTable()).hasSize(5);
        assertThat(intent.decisionChecklist()).hasSize(5);
        assertThat(intent.faqItems()).hasSize(3);
        assertThat(intent.relatedIntentLinks()).isNotEmpty();
    }
}

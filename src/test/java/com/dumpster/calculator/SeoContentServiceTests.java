package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import com.dumpster.calculator.web.content.SeoContentService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeoContentServiceTests {

    @Autowired
    private SeoContentService seoContentService;

    @Autowired
    private MaterialFactorRepository materialFactorRepository;

    @Test
    void materialPageIncludesAnswerFirstQuickRulesAndFaq() {
        var topMaterial = seoContentService.materialPage("asphalt_shingles", "http://localhost:8080").orElseThrow();

        assertThat(topMaterial.answerFirst()).isNotBlank();
        assertThat(topMaterial.quickRules()).hasSize(3);
        assertThat(topMaterial.faqItems()).hasSize(3);
        assertThat(topMaterial.decisionStageLinks()).hasSizeGreaterThanOrEqualTo(5);
        assertThat(topMaterial.decisionStageLinks())
                .extracting(link -> link.href())
                .contains("/dumpster/dumpster-vs-junk-removal-which-is-cheaper");
        assertThat(seoContentService.materialPage("plaster", "http://localhost:8080")).isPresent();
        assertThat(seoContentService.materialPage("does_not_exist", "http://localhost:8080")).isEmpty();
    }

    @Test
    void projectPageIncludesAnswerFirstQuickRulesAndFaq() {
        var project = seoContentService.projectPage("roof_tearoff", "http://localhost:8080").orElseThrow();

        assertThat(project.answerFirst()).isNotBlank();
        assertThat(project.quickRules()).hasSize(3);
        assertThat(project.faqItems()).hasSize(3);
        assertThat(project.decisionStageLinks()).hasSizeGreaterThanOrEqualTo(5);
        assertThat(project.decisionStageLinks())
                .extracting(link -> link.href())
                .contains("/dumpster/dumpster-vs-junk-removal-which-is-cheaper");
    }

    @Test
    void materialPageIncludesSizeWeightTableAndSeoFields() {
        var material = seoContentService.materialPage("asphalt_shingles", "http://localhost:8080").orElseThrow();

        assertThat(material.sizeWeightTable()).hasSize(5);
        assertThat(material.sizeWeightTable().getFirst().sizeYd()).isEqualTo(10);
        assertThat(material.sizeWeightTable().getFirst().weightTypTons()).isGreaterThan(0.0d);
        assertThat(material.title()).isEqualTo("Shingles Dumpster Size Calculator");
        assertThat(material.seoTitle()).contains("Shingles Dumpster Size Calculator");
        assertThat(material.metaDescription()).contains("overage risk");
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
        assertThat(intent.homeownerDecisionBlocks()).hasSize(8);
        assertThat(intent.faqItems()).hasSize(3);
        assertThat(intent.decisionStageLinks()).hasSizeGreaterThanOrEqualTo(5);
        assertThat(intent.decisionStageLinks())
                .extracting(link -> link.href())
                .contains("/dumpster/dumpster-vs-junk-removal-which-is-cheaper");
        assertThat(intent.relatedIntentLinks()).isNotEmpty();
    }

    @Test
    void concreteClusterPagesExposeConcreteFocusedInternalLinks() {
        var material = seoContentService.materialPage("concrete", "http://localhost:8080").orElseThrow();
        var project = seoContentService.projectPage("concrete_removal", "http://localhost:8080").orElseThrow();
        var intent = seoContentService.intentPage(
                "concrete_removal",
                "concrete",
                "size-guide",
                "http://localhost:8080"
        ).orElseThrow();

        assertThat(material.decisionStageLinks())
                .extracting(link -> link.href())
                .contains(
                        "/about/quote-match-beta",
                        "/dumpster/can-you-put-concrete-in-a-dumpster",
                        "/dumpster/size/concrete-removal",
                        "/dumpster/weight/concrete"
                );
        assertThat(project.decisionStageLinks())
                .extracting(link -> link.href())
                .contains(
                        "/about/quote-match-beta",
                        "/dumpster/can-you-put-concrete-in-a-dumpster",
                        "/dumpster/size/concrete-removal",
                        "/dumpster/weight/concrete"
                );
        assertThat(intent.decisionStageLinks())
                .extracting(link -> link.href())
                .contains(
                        "/about/quote-match-beta",
                        "/dumpster/can-you-put-concrete-in-a-dumpster",
                        "/dumpster/size/concrete-removal",
                        "/dumpster/weight/concrete"
                );
    }

    @Test
    void defaultConfigUsesPrimaryAndExperimentIntentIndexPaths() {
        var paths = seoContentService.indexableIntentPaths();

        assertThat(paths).hasSize(34);
        assertThat(paths).contains("/dumpster/answers/roof-tear-off/shingles/overage-risk");
        assertThat(paths).contains("/dumpster/answers/roof-tear-off/shingles/size-guide");
        assertThat(paths).contains("/dumpster/answers/concrete-removal/concrete/size-guide");
        assertThat(paths).contains("/dumpster/answers/garage-cleanout/household-junk/size-guide");
        assertThat(paths).contains("/dumpster/answers/yard-cleanup/yard-waste/size-guide");
        assertThat(paths).contains("/dumpster/answers/concrete-removal/brick-block/overage-risk");
        assertThat(paths).contains("/dumpster/answers/bathroom-remodel/tile-ceramic/size-guide");
        assertThat(paths).contains("/dumpster/answers/dirt-grading/dirt/weight-estimate");
        assertThat(paths).contains("/dumpster/answers/estate-cleanout/furniture/size-guide");
        assertThat(paths).contains("/dumpster/answers/light-commercial-fitout/drywall/size-guide");
        assertThat(paths).doesNotContain("/dumpster/answers/roof-tear-off/tile-ceramic/size-guide");
        assertThat(paths).doesNotContain("/dumpster/answers/roof-tear-off/metal-scrap-light/size-guide");
    }

    @Test
    void defaultLastModifiedDateTracksLatestMaterialSourceDate() {
        LocalDate latestSourceDate = materialFactorRepository.findAll().stream()
                .map(material -> material.sourceVersionDate())
                .filter(date -> date != null)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.of(2026, 3, 4));
        LocalDate expected = latestSourceDate.isAfter(LocalDate.of(2026, 3, 4))
                ? latestSourceDate
                : LocalDate.of(2026, 3, 4);

        assertThat(seoContentService.defaultLastModifiedDate()).isEqualTo(expected);
    }
}

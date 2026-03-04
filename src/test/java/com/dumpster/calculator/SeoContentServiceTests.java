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
        assertThat(intent.homeownerDecisionBlocks()).hasSize(8);
        assertThat(intent.faqItems()).hasSize(3);
        assertThat(intent.decisionStageLinks()).hasSizeGreaterThanOrEqualTo(5);
        assertThat(intent.decisionStageLinks())
                .extracting(link -> link.href())
                .contains("/dumpster/dumpster-vs-junk-removal-which-is-cheaper");
        assertThat(intent.relatedIntentLinks()).isNotEmpty();
    }

    @Test
    void indexableIntentPathsContainOnlyWhitelistedMoneyRoutes() {
        var paths = seoContentService.indexableIntentPaths();

        assertThat(paths).hasSize(89);
        assertThat(paths).contains("/dumpster/answers/roof_tearoff/asphalt_shingles/overage-risk");
        assertThat(paths).contains("/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide");
        assertThat(paths).contains("/dumpster/answers/roof_tearoff/metal_scrap_light/size-guide");
        assertThat(paths).contains("/dumpster/answers/concrete_removal/concrete/size-guide");
        assertThat(paths).contains("/dumpster/answers/concrete_removal/concrete/weight-estimate");
        assertThat(paths).contains("/dumpster/answers/dirt_grading/dirt_soil/size-guide");
        assertThat(paths).contains("/dumpster/answers/light_commercial_fitout/drywall/size-guide");
        assertThat(paths).contains("/dumpster/answers/garage_cleanout/household_junk/size-guide");
        assertThat(paths).contains("/dumpster/answers/yard_cleanup/yard_waste/size-guide");
        assertThat(paths).doesNotContain("/dumpster/answers/roof_tearoff/tile_ceramic/size-guide");
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

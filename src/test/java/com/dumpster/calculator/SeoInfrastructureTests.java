package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.web.content.SeoContentService;
import com.dumpster.calculator.web.controller.SeoInfrastructureController;
import java.util.LinkedHashSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class SeoInfrastructureTests {

    @Autowired
    private SeoInfrastructureController seoInfrastructureController;

    @Autowired
    private SeoContentService seoContentService;

    @Test
    void sitemapIndexReferencesSplitSitemaps() {
        ResponseEntity<String> response = seoInfrastructureController.sitemap();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotBlank();

        String body = response.getBody();
        assertThat(body).contains("<sitemapindex");
        assertThat(body).contains("/sitemap-core.xml");
        assertThat(body).contains("/sitemap-money.xml");
        assertThat(body).contains("/sitemap-experiments.xml");
    }

    @Test
    void sitemapCoreUsesStableLastmodForDefaultPages() {
        ResponseEntity<String> response = seoInfrastructureController.sitemapCore();
        String body = response.getBody();
        String stableLastMod = seoContentService.defaultLastModifiedDate().toString();
        assertThat(body).contains("/dumpster/size-weight-calculator</loc><lastmod>" + stableLastMod + "</lastmod>");
        assertThat(body).contains("/dumpster/material-guides</loc><lastmod>" + stableLastMod + "</lastmod>");
        assertThat(body).contains("/dumpster/project-guides</loc><lastmod>" + stableLastMod + "</lastmod>");
        assertThat(body).contains("/dumpster/heavy-debris-rules</loc><lastmod>" + stableLastMod + "</lastmod>");
    }

    @Test
    void sitemapMoneyContainsCuratedMoneyUrlsAndWhitelistedAnswers() {
        ResponseEntity<String> response = seoInfrastructureController.sitemapMoney();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotBlank();

        String body = response.getBody();
        int urlCount = body.split("<url>").length - 1;
        LinkedHashSet<String> expectedPaths = new LinkedHashSet<>();
        expectedPaths.addAll(seoContentService.specialPageIndexPaths(3));
        expectedPaths.addAll(seoContentService.projectIndexPaths(3));
        seoContentService.indexableMaterialIds(3).stream()
                .map(seoContentService::materialCanonicalPath)
                .forEach(expectedPaths::add);
        expectedPaths.addAll(seoContentService.priorityIntentPaths());

        assertThat(urlCount).isEqualTo(expectedPaths.size());
        assertThat(body).contains("/dumpster/weight/shingles");
        assertThat(body).contains("/dumpster/roof-shingles-dumpster-size-calculator");
        assertThat(body).contains("/dumpster/drywall-disposal-dumpster-rules");
        assertThat(body).contains("/dumpster/size/roof-tear-off");
        assertThat(body).contains("/dumpster/size/garage-cleanout");
        assertThat(body).contains("/dumpster/size/concrete-removal");
        assertThat(body).contains("/dumpster/size/estate-cleanout");
        assertThat(body).contains("/dumpster/answers/roof-tear-off/shingles/overage-risk");
        assertThat(body).contains("/dumpster/answers/concrete-removal/concrete/size-guide");
        assertThat(body).contains("/dumpster/answers/garage-cleanout/household-junk/size-guide");
        assertThat(body).contains("/dumpster/answers/estate-cleanout/household-junk/overage-risk");
        assertThat(body).doesNotContain("/dumpster/answers/roof-tear-off/tile-ceramic/size-guide");
        assertThat(body).doesNotContain("/dumpster/answers/roof-tear-off/metal-scrap-light/size-guide");
        assertThat(body).doesNotContain("/dumpster/what-size-dumpster-do-i-need");
        assertThat(body).doesNotContain("/dumpster/size/light-commercial-fitout");
        assertThat(body).doesNotContain("/dumpster/answers/kitchen-remodel/mixed-cd/size-guide");
        assertThat(body).doesNotContain("/dumpster/material-guides");
        assertThat(body).doesNotContain("/dumpster/project-guides");
    }

    @Test
    void sitemapExperimentsContainsControlledExpansionUrls() {
        ResponseEntity<String> response = seoInfrastructureController.sitemapExperiments();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotBlank();
        String body = response.getBody();
        int urlCount = body.split("<url>").length - 1;
        LinkedHashSet<String> expectedPaths = new LinkedHashSet<>();
        expectedPaths.addAll(seoContentService.experimentSpecialPageIndexPaths(3));
        expectedPaths.addAll(seoContentService.experimentProjectIndexPaths(3));
        expectedPaths.addAll(seoContentService.experimentIntentPaths());

        assertThat(urlCount).isEqualTo(expectedPaths.size());
        assertThat(body).contains("/dumpster/what-size-dumpster-do-i-need");
        assertThat(body).contains("/dumpster/size/light-commercial-fitout");
        assertThat(body).contains("/dumpster/size/yard-cleanup");
        assertThat(body).contains("/dumpster/answers/bathroom-remodel/drywall/weight-estimate");
        assertThat(body).contains("/dumpster/answers/kitchen-remodel/mixed-cd/size-guide");
        assertThat(body).doesNotContain("/dumpster/drywall-disposal-dumpster-rules");
        assertThat(body).doesNotContain("/dumpster/size/concrete-removal");
        assertThat(body).doesNotContain("/dumpster/size/estate-cleanout");
        assertThat(body).doesNotContain("/dumpster/answers/estate-cleanout/furniture/size-guide");
        assertThat(body).doesNotContain("/dumpster/material-guides");
        assertThat(body).doesNotContain("/dumpster/project-guides");
    }

    @Test
    void robotsAllowsAnswersAndDeclaresSplitSitemaps() {
        ResponseEntity<String> response = seoInfrastructureController.robotsTxt();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotBlank();

        String body = response.getBody();
        assertThat(body).contains("Allow: /dumpster/answers/");
        assertThat(body).doesNotContain("Disallow: /dumpster/answers/");
        assertThat(body).doesNotContain("Disallow: /dumpster/estimate/");
        assertThat(body).contains("Sitemap:").contains("/sitemap.xml");
        assertThat(body).contains("/sitemap-core.xml");
        assertThat(body).contains("/sitemap-money.xml");
        assertThat(body).contains("/sitemap-experiments.xml");
    }
}

package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.web.controller.SeoInfrastructureController;
import java.time.Clock;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class SeoInfrastructureTests {

    @Autowired
    private SeoInfrastructureController seoInfrastructureController;

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
        String today = LocalDate.now(Clock.systemUTC()).toString();
        assertThat(body).contains("/dumpster/size-weight-calculator</loc><lastmod>" + today + "</lastmod>");
        assertThat(body).contains("/dumpster/heavy-debris-rules</loc><lastmod>" + today + "</lastmod>");
    }

    @Test
    void sitemapMoneyContainsExpandedMoneyUrlsAndWhitelistedAnswers() {
        ResponseEntity<String> response = seoInfrastructureController.sitemapMoney();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotBlank();

        String body = response.getBody();
        int urlCount = body.split("<url>").length - 1;

        assertThat(urlCount).isGreaterThanOrEqualTo(35);
        assertThat(body).contains("/dumpster/what-size-dumpster-do-i-need");
        assertThat(body).contains("/dumpster/weight/shingles");
        assertThat(body).contains("/dumpster/size/roof-tear-off");
        assertThat(body).contains("/dumpster/size/concrete-removal");
        assertThat(body).contains("/dumpster/size/light-commercial-fitout");
        assertThat(body).contains("/dumpster/answers/roof_tearoff/asphalt_shingles/overage-risk");
        assertThat(body).contains("/dumpster/answers/concrete_removal/concrete/size-guide");
        assertThat(body).doesNotContain("/dumpster/answers/roof_tearoff/tile_ceramic/size-guide");
        assertThat(body).doesNotContain("/dumpster/material-guides");
        assertThat(body).doesNotContain("/dumpster/project-guides");
    }

    @Test
    void sitemapExperimentsContainsGuideHubs() {
        ResponseEntity<String> response = seoInfrastructureController.sitemapExperiments();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotBlank();
        String body = response.getBody();
        assertThat(body).contains("/dumpster/material-guides");
        assertThat(body).contains("/dumpster/project-guides");
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

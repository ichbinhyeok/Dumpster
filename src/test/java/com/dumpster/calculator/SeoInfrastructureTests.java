package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.web.controller.SeoInfrastructureController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class SeoInfrastructureTests {

    @Autowired
    private SeoInfrastructureController seoInfrastructureController;

    @Test
    void sitemapContainsDefaultWaveTwoIndexableUrls() {
        ResponseEntity<String> response = seoInfrastructureController.sitemap();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotBlank();

        String body = response.getBody();
        int urlCount = body.split("<url>").length - 1;

        assertThat(urlCount).isGreaterThanOrEqualTo(18);
        assertThat(body).contains("/dumpster/size-weight-calculator");
        assertThat(body).contains("/dumpster/heavy-debris-rules");
        assertThat(body).contains("/about/methodology");
        assertThat(body).contains("/about/editorial-policy");
        assertThat(body).contains("/about/contact");
        assertThat(body).contains("/dumpster/how-many-tons-can-a-10-yard-dumpster-hold");
        assertThat(body).contains("/dumpster/can-you-put-concrete-in-a-dumpster");
        assertThat(body).contains("/dumpster/can-you-mix-concrete-and-wood-in-a-dumpster");
        assertThat(body).contains("/dumpster/dumpster-vs-junk-removal");
        assertThat(body).contains("/dumpster/pickup-truck-loads-to-dumpster-size");
        assertThat(body).contains("/dumpster/roofing-squares-to-dumpster-size");
        assertThat(body).contains("/dumpster/bagster-vs-dumpster");
        assertThat(body).contains("/dumpster/weight/shingles");
        assertThat(body).contains("/dumpster/weight/concrete");
        assertThat(body).contains("/dumpster/weight/drywall");
        assertThat(body).contains("/dumpster/weight/dirt");
        assertThat(body).contains("/dumpster/size/roof-tear-off");
        assertThat(body).contains("/dumpster/size/bathroom-remodel");
        assertThat(body).contains("/dumpster/size/deck-removal");
        assertThat(body).doesNotContain("/dumpster/weight/brick-block");
        assertThat(body).doesNotContain("/dumpster/size/garage-cleanout");
        assertThat(body).doesNotContain("/dumpster/size/kitchen-remodel");
        assertThat(body).doesNotContain("/dumpster/fill-line-rules-for-heavy-debris");
        assertThat(body).doesNotContain("/dumpster/one-20-yard-vs-two-10-yard");
        assertThat(body).doesNotContain("/dumpster/drywall-sheets-to-dumpster-size");
        assertThat(body).doesNotContain("/dumpster/material-guides");
        assertThat(body).doesNotContain("/dumpster/project-guides");
        assertThat(body).doesNotContain("/dumpster/answers/");
    }

    @Test
    void sitemapUsesStableLastmodForDefaultPages() {
        ResponseEntity<String> response = seoInfrastructureController.sitemap();
        String body = response.getBody();
        assertThat(body).contains("/dumpster/size-weight-calculator</loc><lastmod>2026-03-01</lastmod>");
        assertThat(body).contains("/dumpster/heavy-debris-rules</loc><lastmod>2026-03-01</lastmod>");
    }
}

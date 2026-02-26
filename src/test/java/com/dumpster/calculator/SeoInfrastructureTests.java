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
    void sitemapContainsExpandedIndexableUrls() {
        ResponseEntity<String> response = seoInfrastructureController.sitemap();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotBlank();

        String body = response.getBody();
        int urlCount = body.split("<url>").length - 1;

        assertThat(urlCount).isGreaterThanOrEqualTo(34);
        assertThat(body).contains("/dumpster/size-weight-calculator");
        assertThat(body).contains("/dumpster/heavy-debris-rules");
        assertThat(body).contains("/dumpster/material-guides");
        assertThat(body).contains("/dumpster/project-guides");
        assertThat(body).contains("/dumpster/weight/asphalt_shingles");
        assertThat(body).contains("/dumpster/weight/metal_scrap_light");
        assertThat(body).contains("/dumpster/size/light_commercial_fitout");
    }
}

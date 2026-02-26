package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.content.SeoContentService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SeoInfrastructureController {

    private final SeoContentService seoContentService;

    public SeoInfrastructureController(SeoContentService seoContentService) {
        this.seoContentService = seoContentService;
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> robotsTxt() {
        String body = """
                User-agent: *
                Allow: /dumpster/size-weight-calculator
                Allow: /dumpster/heavy-debris-rules
                Allow: /dumpster/weight/
                Allow: /dumpster/size/
                Disallow: /dumpster/estimate/
                Disallow: /api/
                Sitemap: /sitemap.xml
                """;
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() {
        List<String> urls = new ArrayList<>();
        urls.add("/dumpster/size-weight-calculator");
        urls.add("/dumpster/heavy-debris-rules");
        seoContentService.projectPages().values().forEach(project -> urls.add(project.canonicalPath()));
        urls.add("/dumpster/weight/asphalt_shingles");
        urls.add("/dumpster/weight/concrete");
        urls.add("/dumpster/weight/dirt_soil");
        urls.add("/dumpster/weight/drywall");
        urls.add("/dumpster/weight/mixed_cd");
        urls.add("/dumpster/weight/yard_waste");

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (String url : urls) {
            xml.append("<url><loc>").append(url).append("</loc></url>");
        }
        xml.append("</urlset>");
        return ResponseEntity.ok(xml.toString());
    }
}


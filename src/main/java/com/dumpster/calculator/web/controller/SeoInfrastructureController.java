package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.content.SeoContentService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SeoInfrastructureController {

    private final SeoContentService seoContentService;
    private final String baseUrl;

    public SeoInfrastructureController(
            SeoContentService seoContentService,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl
    ) {
        this.seoContentService = seoContentService;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> robotsTxt() {
        String body = """
                User-agent: *
                Allow: /dumpster/size-weight-calculator
                Allow: /dumpster/heavy-debris-rules
                Allow: /dumpster/weight/
                Allow: /dumpster/size/
                Allow: /dumpster/material-guides
                Allow: /dumpster/project-guides
                Disallow: /dumpster/estimate/
                Disallow: /api/
                Sitemap: %s/sitemap.xml
                """;
        return ResponseEntity.ok(body.formatted(baseUrl));
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() {
        List<SitemapEntry> urls = new ArrayList<>();
        String defaultLastMod = seoContentService.defaultLastModifiedDate().toString();
        urls.add(new SitemapEntry("/dumpster/size-weight-calculator", defaultLastMod));
        urls.add(new SitemapEntry("/dumpster/heavy-debris-rules", defaultLastMod));
        urls.add(new SitemapEntry("/dumpster/material-guides", defaultLastMod));
        urls.add(new SitemapEntry("/dumpster/project-guides", defaultLastMod));
        seoContentService.projectIndexPaths().forEach(path -> urls.add(new SitemapEntry(path, defaultLastMod)));
        seoContentService.indexableMaterialIds().forEach(materialId -> urls.add(
                new SitemapEntry(
                        "/dumpster/weight/" + materialId,
                        seoContentService.materialLastModifiedDate(materialId).toString()
                )
        ));

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (SitemapEntry url : urls) {
            xml.append("<url><loc>")
                    .append(baseUrl)
                    .append(url.path())
                    .append("</loc><lastmod>")
                    .append(url.lastMod())
                    .append("</lastmod></url>");
        }
        xml.append("</urlset>");
        return ResponseEntity.ok(xml.toString());
    }

    private record SitemapEntry(
            String path,
            String lastMod
    ) {
    }
}

package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.content.SeoContentService;
import java.util.LinkedHashSet;
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
    private final int seoMaxWave;

    public SeoInfrastructureController(
            SeoContentService seoContentService,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl,
            @Value("${app.seo.max-wave:3}") int seoMaxWave
    ) {
        this.seoContentService = seoContentService;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.seoMaxWave = Math.max(1, Math.min(seoMaxWave, 3));
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> robotsTxt() {
        String body = """
                User-agent: *
                Allow: /dumpster/size-weight-calculator
                Allow: /dumpster/heavy-debris-rules
                Allow: /dumpster/weight/
                Allow: /dumpster/size/
                Allow: /dumpster/answers/
                Allow: /about/
                Disallow: /api/
                Sitemap: %s/sitemap.xml
                Sitemap: %s/sitemap-core.xml
                Sitemap: %s/sitemap-money.xml
                Sitemap: %s/sitemap-experiments.xml
                """;
        return ResponseEntity.ok(body.formatted(baseUrl, baseUrl, baseUrl, baseUrl));
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() {
        String defaultLastMod = seoContentService.defaultLastModifiedDate().toString();
        List<SitemapEntry> sitemaps = List.of(
                new SitemapEntry("/sitemap-core.xml", defaultLastMod),
                new SitemapEntry("/sitemap-money.xml", defaultLastMod),
                new SitemapEntry("/sitemap-experiments.xml", defaultLastMod)
        );

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (SitemapEntry sitemap : sitemaps) {
            xml.append("<sitemap><loc>")
                    .append(baseUrl)
                    .append(sitemap.path())
                    .append("</loc><lastmod>")
                    .append(sitemap.lastMod())
                    .append("</lastmod></sitemap>");
        }
        xml.append("</sitemapindex>");
        return ResponseEntity.ok(xml.toString());
    }

    @GetMapping(value = "/sitemap-core.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemapCore() {
        String defaultLastMod = seoContentService.defaultLastModifiedDate().toString();
        List<SitemapEntry> urls = List.of(
                new SitemapEntry("/dumpster/size-weight-calculator", defaultLastMod),
                new SitemapEntry("/dumpster/material-guides", defaultLastMod),
                new SitemapEntry("/dumpster/project-guides", defaultLastMod),
                new SitemapEntry("/dumpster/heavy-debris-rules", defaultLastMod),
                new SitemapEntry("/about/methodology", defaultLastMod),
                new SitemapEntry("/about/editorial-policy", defaultLastMod),
                new SitemapEntry("/about/contact", defaultLastMod)
        );
        return toUrlSet(urls);
    }

    @GetMapping(value = "/sitemap-money.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemapMoney() {
        String defaultLastMod = seoContentService.defaultLastModifiedDate().toString();
        LinkedHashSet<String> uniquePaths = new LinkedHashSet<>();
        seoContentService.specialPageIndexPaths(seoMaxWave).forEach(uniquePaths::add);
        seoContentService.projectIndexPaths(seoMaxWave).forEach(uniquePaths::add);
        seoContentService.indexableMaterialIds(seoMaxWave).stream()
                .map(seoContentService::materialCanonicalPath)
                .forEach(uniquePaths::add);
        seoContentService.priorityIntentPaths().forEach(uniquePaths::add);
        seoContentService.experimentSpecialPageIndexPaths(seoMaxWave).forEach(uniquePaths::add);
        seoContentService.experimentProjectIndexPaths(seoMaxWave).forEach(uniquePaths::add);
        seoContentService.experimentIntentPaths().forEach(uniquePaths::add);

        List<SitemapEntry> urls = uniquePaths.stream()
                .map(path -> new SitemapEntry(path, defaultLastMod))
                .toList();
        return toUrlSet(urls);
    }

    @GetMapping(value = "/sitemap-experiments.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemapExperiments() {
        String defaultLastMod = seoContentService.defaultLastModifiedDate().toString();
        LinkedHashSet<String> uniquePaths = new LinkedHashSet<>();
        seoContentService.experimentSpecialPageIndexPaths(seoMaxWave).forEach(uniquePaths::add);
        seoContentService.experimentProjectIndexPaths(seoMaxWave).forEach(uniquePaths::add);
        seoContentService.experimentIntentPaths().forEach(uniquePaths::add);

        List<SitemapEntry> urls = uniquePaths.stream()
                .map(path -> new SitemapEntry(path, defaultLastMod))
                .toList();
        return toUrlSet(urls);
    }

    private ResponseEntity<String> toUrlSet(List<SitemapEntry> urls) {
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

package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.content.SeoContentService;
import com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel;
import com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel;
import com.dumpster.calculator.web.viewmodel.IntentPageViewModel;
import com.dumpster.calculator.web.viewmodel.MaterialPageViewModel;
import com.dumpster.calculator.web.viewmodel.ProjectPageViewModel;
import com.dumpster.calculator.web.viewmodel.SpecialSeoPageViewModel;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SeoPageController {

    private final SeoContentService seoContentService;
    private final String baseUrl;

    public SeoPageController(
            SeoContentService seoContentService,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl
    ) {
        this.seoContentService = seoContentService;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @GetMapping("/dumpster/heavy-debris-rules")
    public ModelAndView heavyRulesPage() {
        HeavyRulesViewModel model = new HeavyRulesViewModel(
                seoContentService.heavyRulesUrl(baseUrl),
                seoContentService.calculatorUrl(baseUrl),
                seoContentService.ogImageUrl(baseUrl),
                seoContentService.materialGuidesUrl(baseUrl),
                seoContentService.projectGuidesUrl(baseUrl),
                seoContentService.defaultLastModifiedDate().toString(),
                List.of(
                        "Heavy materials often require smaller bins with multiple hauls.",
                        "Included tons and max haul tons are not the same constraint.",
                        "Fill-ratio caps for heavy debris can force multi-haul strategy."
                ),
                List.of(
                        "What is the max haul limit per container in my area?",
                        "Do you require clean-load separation for heavy debris?",
                        "How do overage fees apply when weight crosses included tons?"
                ),
                seoContentService.heavyLimitRows(),
                seoContentService.heavyRulesIncludedVsMaxExplanation()
        );
        ModelAndView modelAndView = new ModelAndView("seo/heavy-rules");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/material-guides")
    public ModelAndView materialGuidesHub() {
        GuideHubPageViewModel model = new GuideHubPageViewModel(
                "Dumpster Material Weight Guides: Density Chart + Live Calculator",
                seoContentService.materialGuidesUrl(baseUrl),
                seoContentService.calculatorUrl(baseUrl),
                seoContentService.ogImageUrl(baseUrl),
                "Compare material density ranges, sample tonnage, and overage risk before choosing a dumpster size.",
                "Material Guides",
                seoContentService.materialGuideLinks(),
                seoContentService.materialGroupsByCategory(),
                seoContentService.materialComparisonTable(),
                seoContentService.materialHubFaq(),
                seoContentService.intentClusterLinksForMaterialHub()
        );
        ModelAndView modelAndView = new ModelAndView("seo/material-guides");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/project-guides")
    public ModelAndView projectGuidesHub() {
        GuideHubPageViewModel model = new GuideHubPageViewModel(
                "Dumpster Project Guides: Size Strategy by Job Type",
                seoContentService.projectGuidesUrl(baseUrl),
                seoContentService.calculatorUrl(baseUrl),
                seoContentService.ogImageUrl(baseUrl),
                "Use project-based presets to pick safer dumpster strategies and avoid overage surprises by timeline.",
                "Project Guides",
                seoContentService.projectGuideLinks(),
                null,
                null,
                seoContentService.projectHubFaq(),
                seoContentService.intentClusterLinksForProjectHub()
        );
        ModelAndView modelAndView = new ModelAndView("seo/project-guides");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/weight/{materialId}")
    public ModelAndView materialPage(@PathVariable("materialId") String materialId) {
        String resolvedMaterialId = seoContentService.resolveMaterialId(materialId);
        if (!seoContentService.isMaterialEnabled(resolvedMaterialId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String requestedPath = "/dumpster/weight/" + materialId;
        String canonicalPath = seoContentService.materialCanonicalPath(resolvedMaterialId);
        if (!requestedPath.equals(canonicalPath)) {
            ModelAndView redirect = new ModelAndView("redirect:" + canonicalPath);
            redirect.setStatus(HttpStatus.MOVED_PERMANENTLY);
            return redirect;
        }

        MaterialPageViewModel model = seoContentService.materialPage(resolvedMaterialId, baseUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("seo/material-page");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/size/{projectId}")
    public ModelAndView projectPage(@PathVariable("projectId") String projectId) {
        String resolvedProjectId = seoContentService.resolveProjectId(projectId);
        if (!seoContentService.isProjectEnabled(resolvedProjectId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String requestedPath = "/dumpster/size/" + projectId;
        String canonicalPath = seoContentService.projectCanonicalPath(resolvedProjectId);
        if (!requestedPath.equals(canonicalPath)) {
            ModelAndView redirect = new ModelAndView("redirect:" + canonicalPath);
            redirect.setStatus(HttpStatus.MOVED_PERMANENTLY);
            return redirect;
        }

        ProjectPageViewModel model = seoContentService.projectPage(resolvedProjectId, baseUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("seo/project-page");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/answers/{projectId}/{materialId}/{intent}")
    public ModelAndView intentPage(
            @PathVariable("projectId") String projectId,
            @PathVariable("materialId") String materialId,
            @PathVariable("intent") String intent,
            HttpServletResponse response
    ) {
        String resolvedProjectId = seoContentService.resolveProjectId(projectId);
        String resolvedMaterialId = seoContentService.resolveMaterialId(materialId);
        if (seoContentService.isIntentSlugSupported(intent)) {
            String requestedPath = "/dumpster/answers/" + projectId + "/" + materialId + "/" + intent;
            String canonicalPath = seoContentService.intentCanonicalPath(resolvedProjectId, resolvedMaterialId, intent);
            if (!requestedPath.equals(canonicalPath)) {
                ModelAndView redirect = new ModelAndView("redirect:" + canonicalPath);
                redirect.setStatus(HttpStatus.MOVED_PERMANENTLY);
                return redirect;
            }
        }
        boolean indexableIntent = seoContentService.isIndexableIntentPath(resolvedProjectId, resolvedMaterialId, intent);
        response.setHeader("X-Robots-Tag", indexableIntent ? "index, follow" : "noindex, follow");
        IntentPageViewModel model = seoContentService.intentPage(resolvedProjectId, resolvedMaterialId, intent, baseUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("seo/intent-page");
        modelAndView.addObject("model", model);
        modelAndView.addObject("indexableIntent", indexableIntent);
        return modelAndView;
    }

    @GetMapping({"/dumpster/{slug}", "/dumpster/{slug}/"})
    public ModelAndView specialPage(@PathVariable("slug") String slug) {
        String resolvedSlug = seoContentService.resolveSpecialSlug(slug);
        if (!seoContentService.isSpecialPageEnabled(resolvedSlug)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!resolvedSlug.equals(slug)) {
            ModelAndView redirect = new ModelAndView("redirect:/dumpster/" + resolvedSlug);
            redirect.setStatus(HttpStatus.MOVED_PERMANENTLY);
            return redirect;
        }
        SpecialSeoPageViewModel model = seoContentService.specialPage(resolvedSlug, baseUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("seo/special-page");
        modelAndView.addObject("model", model);
        return modelAndView;
    }
}

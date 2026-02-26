package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.content.SeoContentService;
import com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel;
import com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel;
import com.dumpster.calculator.web.viewmodel.MaterialPageViewModel;
import com.dumpster.calculator.web.viewmodel.ProjectPageViewModel;
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
                seoContentService.materialGuidesUrl(baseUrl),
                seoContentService.projectGuidesUrl(baseUrl),
                List.of(
                        "Heavy materials often require smaller bins with multiple hauls.",
                        "Included tons and max haul tons are not the same constraint.",
                        "Fill-ratio caps for heavy debris can force multi-haul strategy."
                ),
                List.of(
                        "What is the max haul limit per container in my area?",
                        "Do you require clean-load separation for heavy debris?",
                        "How do overage fees apply when weight crosses included tons?"
                )
        );
        ModelAndView modelAndView = new ModelAndView("seo/heavy-rules");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/material-guides")
    public ModelAndView materialGuidesHub() {
        GuideHubPageViewModel model = new GuideHubPageViewModel(
                "Dumpster Material Weight Guides",
                seoContentService.materialGuidesUrl(baseUrl),
                "Browse material-specific weight ranges and decision notes before choosing dumpster size.",
                "Material Guides",
                seoContentService.materialGuideLinks()
        );
        ModelAndView modelAndView = new ModelAndView("seo/material-guides");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/project-guides")
    public ModelAndView projectGuidesHub() {
        GuideHubPageViewModel model = new GuideHubPageViewModel(
                "Dumpster Project Guides",
                seoContentService.projectGuidesUrl(baseUrl),
                "Use project presets and scenario-based advice to choose safe vs budget strategies.",
                "Project Guides",
                seoContentService.projectGuideLinks()
        );
        ModelAndView modelAndView = new ModelAndView("seo/project-guides");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/weight/{materialId}")
    public ModelAndView materialPage(@PathVariable String materialId) {
        MaterialPageViewModel model = seoContentService.materialPage(materialId, baseUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("seo/material-page");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/size/{projectId}")
    public ModelAndView projectPage(@PathVariable String projectId) {
        ProjectPageViewModel model = seoContentService.projectPage(projectId, baseUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("seo/project-page");
        modelAndView.addObject("model", model);
        return modelAndView;
    }
}

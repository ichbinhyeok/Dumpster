package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.content.SeoContentService;
import com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel;
import com.dumpster.calculator.web.viewmodel.MaterialPageViewModel;
import com.dumpster.calculator.web.viewmodel.ProjectPageViewModel;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SeoPageController {

    private final SeoContentService seoContentService;

    public SeoPageController(SeoContentService seoContentService) {
        this.seoContentService = seoContentService;
    }

    @GetMapping("/dumpster/heavy-debris-rules")
    public ModelAndView heavyRulesPage() {
        HeavyRulesViewModel model = new HeavyRulesViewModel(
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

    @GetMapping("/dumpster/weight/{materialId}")
    public ModelAndView materialPage(@PathVariable String materialId) {
        MaterialPageViewModel model = seoContentService.materialPage(materialId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("seo/material-page");
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @GetMapping("/dumpster/size/{projectId}")
    public ModelAndView projectPage(@PathVariable String projectId) {
        ProjectPageViewModel model = seoContentService.projectPage(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ModelAndView modelAndView = new ModelAndView("seo/project-page");
        modelAndView.addObject("model", model);
        return modelAndView;
    }
}


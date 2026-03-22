package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.viewmodel.CalculatorPageViewModel;
import com.dumpster.calculator.web.viewmodel.ShareEstimateViewModel;
import com.dumpster.calculator.infra.persistence.EstimateStorageService;
import com.dumpster.calculator.web.content.SeoContentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalculatorPageController {

    private final EstimateStorageService estimateStorageService;
    private final SeoContentService seoContentService;
    private final String baseUrl;

    public CalculatorPageController(
            EstimateStorageService estimateStorageService,
            SeoContentService seoContentService,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl
    ) {
        this.estimateStorageService = estimateStorageService;
        this.seoContentService = seoContentService;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @GetMapping("/")
    public ModelAndView rootToCalculatorRedirect() {
        ModelAndView redirect = new ModelAndView("redirect:/dumpster/size-weight-calculator");
        redirect.setStatus(HttpStatus.MOVED_PERMANENTLY);
        return redirect;
    }

    @GetMapping("/dumpster/size-weight-calculator")
    public ModelAndView calculatorPage() {
        ModelAndView modelAndView = new ModelAndView("calculator/index");
        modelAndView.addObject("model", new CalculatorPageViewModel(
                "Dumpster Size Calculator by Job and Material",
                "Dumpster Size Calculator by Job + Material: Weight, Risk, Route",
                "Estimate dumpster size by project scope and debris type. See weight range, overage risk, and when junk removal or staged hauls are the safer choice.",
                seoContentService.calculatorConfidenceTier(),
                seoContentService.calculatorVarianceNote(),
                seoContentService.calculatorVendorChecklist(),
                seoContentService.defaultLastModifiedDate().toString(),
                baseUrl,
                baseUrl + "/dumpster/size-weight-calculator",
                seoContentService.ogImageUrl(baseUrl),
                seoContentService.featuredMaterialLinks(6),
                seoContentService.featuredProjectLinks(6)
        ));
        return modelAndView;
    }

    @GetMapping("/dumpster/estimate/{estimateId}")
    public ModelAndView shareEstimate(
            @PathVariable("estimateId") String estimateId,
            HttpServletResponse response
    ) {
        response.setHeader("X-Robots-Tag", "noindex, noarchive");
        return estimateStorageService.findValidById(estimateId)
                .map(storedEstimate -> {
                    ModelAndView modelAndView = new ModelAndView("calculator/share-estimate");
                    modelAndView.addObject("model", ShareEstimateViewModel.from(storedEstimate));
                    return modelAndView;
                })
                .orElseGet(() -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ModelAndView modelAndView = new ModelAndView("calculator/estimate-not-found");
                    modelAndView.addObject("estimateId", estimateId);
                    return modelAndView;
                });
    }
}

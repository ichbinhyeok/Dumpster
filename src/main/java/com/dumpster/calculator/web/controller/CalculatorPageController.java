package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.viewmodel.CalculatorPageViewModel;
import com.dumpster.calculator.web.viewmodel.ShareEstimateViewModel;
import com.dumpster.calculator.infra.persistence.EstimateStorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalculatorPageController {

    private final EstimateStorageService estimateStorageService;

    public CalculatorPageController(EstimateStorageService estimateStorageService) {
        this.estimateStorageService = estimateStorageService;
    }

    @GetMapping({"/", "/dumpster/size-weight-calculator"})
    public ModelAndView calculatorPage() {
        ModelAndView modelAndView = new ModelAndView("calculator/index");
        modelAndView.addObject("model", new CalculatorPageViewModel(
                "Dumpster Size & Weight Calculator",
                "/dumpster/size-weight-calculator"
        ));
        return modelAndView;
    }

    @GetMapping("/dumpster/estimate/{estimateId}")
    public ModelAndView shareEstimate(
            @PathVariable String estimateId,
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

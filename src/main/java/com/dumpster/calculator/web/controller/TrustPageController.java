package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.viewmodel.TrustPageViewModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrustPageController {

    private final String baseUrl;

    public TrustPageController(@Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @GetMapping("/about/methodology")
    public ModelAndView methodology() {
        ModelAndView modelAndView = new ModelAndView("trust/methodology");
        modelAndView.addObject("model", new TrustPageViewModel(
                "Calculation Methodology",
                "Dumpster Calculation Methodology: Range, Risk, and Feasibility Model",
                "How dumpster recommendations are calculated from volume, weight, included tons, and haul limits with uncertainty ranges.",
                baseUrl,
                baseUrl + "/about/methodology",
                baseUrl + "/og-image.png"
        ));
        return modelAndView;
    }

    @GetMapping("/about/editorial-policy")
    public ModelAndView editorialPolicy() {
        ModelAndView modelAndView = new ModelAndView("trust/editorial-policy");
        modelAndView.addObject("model", new TrustPageViewModel(
                "Editorial Policy",
                "Editorial Policy: Data Quality, Updates, and Accuracy Standards",
                "Editorial standards for source review, update cadence, and content quality across material and project guides.",
                baseUrl,
                baseUrl + "/about/editorial-policy",
                baseUrl + "/og-image.png"
        ));
        return modelAndView;
    }

    @GetMapping("/about/contact")
    public ModelAndView contact() {
        ModelAndView modelAndView = new ModelAndView("trust/contact");
        modelAndView.addObject("model", new TrustPageViewModel(
                "Contact",
                "Contact: Data Feedback, Corrections, and Partnership Requests",
                "Contact page for data correction requests, source updates, and partnership inquiries related to dumpster planning content.",
                baseUrl,
                baseUrl + "/about/contact",
                baseUrl + "/og-image.png"
        ));
        return modelAndView;
    }

    @GetMapping("/about/quote-match-beta")
    public ModelAndView quoteMatchBeta() {
        ModelAndView modelAndView = new ModelAndView("trust/quote-match-beta");
        modelAndView.addObject("model", new TrustPageViewModel(
                "Local Heavy-Debris Match",
                "Local Heavy-Debris Match: Dumpster and Crew Pickup Intake",
                "Request local heavy-load dumpster or crew-pickup follow-up for concrete, dirt, shingles, and other dense debris scenarios.",
                baseUrl,
                baseUrl + "/about/quote-match-beta",
                baseUrl + "/og-image.png"
        ));
        return modelAndView;
    }
}

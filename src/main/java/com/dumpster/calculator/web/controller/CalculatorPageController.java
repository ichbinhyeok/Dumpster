package com.dumpster.calculator.web.controller;

import com.dumpster.calculator.web.viewmodel.CalculatorPageViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalculatorPageController {

    @GetMapping({"/", "/dumpster/size-weight-calculator"})
    public ModelAndView calculatorPage() {
        ModelAndView modelAndView = new ModelAndView("calculator/index");
        modelAndView.addObject("model", new CalculatorPageViewModel("Dumpster Size & Weight Calculator"));
        return modelAndView;
    }
}

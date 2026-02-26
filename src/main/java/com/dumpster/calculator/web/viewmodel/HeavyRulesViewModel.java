package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record HeavyRulesViewModel(
        List<String> rules,
        List<String> operatorQuestions
) {
}


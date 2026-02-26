package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record HeavyRulesViewModel(
        String canonicalUrl,
        String materialGuidesUrl,
        String projectGuidesUrl,
        List<String> rules,
        List<String> operatorQuestions
) {
}

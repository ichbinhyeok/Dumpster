package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record HeavyRulesViewModel(
        String canonicalUrl,
        String calculatorUrl,
        String ogImageUrl,
        String materialGuidesUrl,
        String projectGuidesUrl,
        List<String> rules,
        List<String> operatorQuestions,
        List<HeavyLimitRow> heavyLimits,
        String includedVsMaxExplanation
) {
    public record HeavyLimitRow(
            int sizeYd,
            String dimensions,
            double maxHaulTonsTyp,
            double fillRatioPercent,
            double effectiveYd3,
            boolean cleanLoadRequired
    ) {
    }
}

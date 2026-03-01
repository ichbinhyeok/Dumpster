package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record ProjectPageViewModel(
        String projectId,
        String title,
        String seoTitle,
        String metaDescription,
        String canonicalUrl,
        String calculatorAbsoluteUrl,
        String recommendedUnit,
        String defaultMaterialId,
        String defaultMaterialName,
        String commonMistake,
        String recommendedStrategy,
        String operatorQuestion,
        String canonicalPath,
        String sampleInput,
        String sampleDecision,
        String answerFirst,
        List<String> quickRules,
        List<FaqItemViewModel> faqItems,
        String projectGuidesUrl,
        List<LinkItemViewModel> relatedMaterialLinks
) {
}

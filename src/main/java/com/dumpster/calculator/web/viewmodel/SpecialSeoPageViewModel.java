package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record SpecialSeoPageViewModel(
        String slug,
        String pageTitle,
        String seoTitle,
        String metaDescription,
        String canonicalUrl,
        String ogImageUrl,
        String calculatorUrl,
        String modifiedDateIso,
        String eyebrow,
        String directAnswer,
        String summary,
        List<String> quickChecks,
        List<DecisionRow> decisionRows,
        List<FaqItemViewModel> faqItems,
        String primaryCtaLabel,
        String primaryCtaHref,
        String secondaryCtaLabel,
        String secondaryCtaHref,
        List<LinkItemViewModel> relatedLinks
) {
    public record DecisionRow(
            String factor,
            String baseline,
            String implication
    ) {
    }
}

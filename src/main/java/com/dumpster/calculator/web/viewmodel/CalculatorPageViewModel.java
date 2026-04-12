package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record CalculatorPageViewModel(
        String pageTitle,
        String seoTitle,
        String seoDescription,
        String confidenceTier,
        String varianceNote,
        List<String> vendorChecklist,
        String lastUpdatedIso,
        String siteBaseUrl,
        String canonicalUrl,
        String ogImageUrl,
        List<LinkItemViewModel> featuredAnswerLinks,
        List<LinkItemViewModel> featuredMaterialLinks,
        List<LinkItemViewModel> featuredProjectLinks
) {
}

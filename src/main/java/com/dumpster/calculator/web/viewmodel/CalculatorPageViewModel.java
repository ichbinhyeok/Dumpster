package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record CalculatorPageViewModel(
        String pageTitle,
        String seoTitle,
        String seoDescription,
        String siteBaseUrl,
        String canonicalUrl,
        String ogImageUrl,
        List<LinkItemViewModel> featuredMaterialLinks,
        List<LinkItemViewModel> featuredProjectLinks
) {
}

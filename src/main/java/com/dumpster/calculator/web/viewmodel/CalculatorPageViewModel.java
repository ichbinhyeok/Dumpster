package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record CalculatorPageViewModel(
        String pageTitle,
        String seoTitle,
        String seoDescription,
        String canonicalUrl,
        List<LinkItemViewModel> featuredMaterialLinks,
        List<LinkItemViewModel> featuredProjectLinks
) {
}

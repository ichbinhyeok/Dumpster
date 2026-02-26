package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record CalculatorPageViewModel(
        String pageTitle,
        String canonicalUrl,
        List<LinkItemViewModel> featuredMaterialLinks,
        List<LinkItemViewModel> featuredProjectLinks
) {
}

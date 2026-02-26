package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record GuideHubPageViewModel(
        String pageTitle,
        String canonicalUrl,
        String description,
        String eyebrow,
        List<LinkItemViewModel> links
) {
}

package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record GuideHubPageViewModel(
        String pageTitle,
        String canonicalUrl,
        String description,
        String eyebrow,
        List<LinkItemViewModel> links,
        List<MaterialGroupViewModel> materialGroups,
        List<MaterialSummaryRow> comparisonTable,
        List<FaqItemViewModel> hubFaqItems
) {
    public record MaterialGroupViewModel(
            String categoryLabel,
            String categoryIntro,
            List<LinkItemViewModel> materials
    ) {
    }

    public record MaterialSummaryRow(
            String materialName,
            String materialHref,
            String category,
            double densityTyp,
            double exampleWeightTons
    ) {
    }
}

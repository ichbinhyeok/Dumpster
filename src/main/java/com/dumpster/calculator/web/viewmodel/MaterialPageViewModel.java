package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record MaterialPageViewModel(
        String materialId,
        String materialName,
        String title,
        String seoTitle,
        String metaDescription,
        String canonicalUrl,
        String calculatorAbsoluteUrl,
        String categoryLabel,
        double densityLow,
        double densityTyp,
        double densityHigh,
        double wetMultiplierLow,
        double wetMultiplierHigh,
        double exampleVolumeYd3,
        double exampleWeightLowTons,
        double exampleWeightTypTons,
        double exampleWeightHighTons,
        String sourceDateDisplay,
        String source,
        String cautionNote,
        String operatorQuestion,
        String scenarioInput,
        String scenarioDecision,
        String answerFirst,
        List<SizeWeightRow> sizeWeightTable,
        List<String> quickRules,
        List<FaqItemViewModel> faqItems,
        String materialGuidesUrl,
        List<LinkItemViewModel> relatedMaterialLinks,
        List<LinkItemViewModel> relatedProjectLinks
) {
    public record SizeWeightRow(
            int sizeYd,
            String dimensions,
            double effectiveVolumeYd3,
            double weightLowTons,
            double weightTypTons,
            double weightHighTons,
            double includedTonsTyp,
            String overageRisk
    ) {
    }
}

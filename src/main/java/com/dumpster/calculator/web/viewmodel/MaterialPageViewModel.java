package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record MaterialPageViewModel(
        String materialId,
        String title,
        String canonicalUrl,
        String categoryLabel,
        double densityLow,
        double densityTyp,
        double densityHigh,
        double exampleVolumeYd3,
        double exampleWeightLowTons,
        double exampleWeightTypTons,
        double exampleWeightHighTons,
        String source,
        String cautionNote,
        String operatorQuestion,
        String scenarioInput,
        String scenarioDecision,
        String materialGuidesUrl,
        List<LinkItemViewModel> relatedProjectLinks
) {
}

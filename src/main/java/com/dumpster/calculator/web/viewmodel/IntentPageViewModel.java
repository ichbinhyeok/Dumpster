package com.dumpster.calculator.web.viewmodel;

import java.util.List;

public record IntentPageViewModel(
        String pageTitle,
        String seoTitle,
        String metaDescription,
        String canonicalUrl,
        String ogImageUrl,
        String calculatorUrl,
        String materialGuidesUrl,
        String projectGuidesUrl,
        String materialName,
        String materialId,
        String projectTitle,
        String projectId,
        String intentLabel,
        String intentQuestion,
        String directAnswer,
        String intentSummary,
        String evidenceNote,
        String modifiedDateIso,
        List<MaterialPageViewModel.SizeWeightRow> sizeWeightTable,
        List<String> decisionChecklist,
        List<FaqItemViewModel> faqItems,
        List<LinkItemViewModel> relatedIntentLinks,
        List<LinkItemViewModel> relatedMaterialLinks,
        List<LinkItemViewModel> relatedProjectLinks
) {
}

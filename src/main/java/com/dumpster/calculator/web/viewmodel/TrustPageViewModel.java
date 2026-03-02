package com.dumpster.calculator.web.viewmodel;

public record TrustPageViewModel(
        String pageTitle,
        String seoTitle,
        String seoDescription,
        String siteBaseUrl,
        String canonicalUrl,
        String ogImageUrl
) {
}

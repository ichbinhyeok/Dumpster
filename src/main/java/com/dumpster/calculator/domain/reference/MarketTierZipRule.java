package com.dumpster.calculator.domain.reference;

public record MarketTierZipRule(
        String ruleId,
        String zipStart,
        String zipEnd,
        String marketTier,
        int priority,
        String source,
        String sourceUrl,
        String notes
) {
}

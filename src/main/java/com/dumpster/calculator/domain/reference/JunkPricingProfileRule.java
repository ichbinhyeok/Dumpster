package com.dumpster.calculator.domain.reference;

public record JunkPricingProfileRule(
        String ruleId,
        String marketTier,
        String needTiming,
        String profileId,
        int priority
) {
}

package com.dumpster.calculator.domain.reference;

public record JunkQuoteBenchmark(
        String benchmarkId,
        String marketTier,
        String needTiming,
        String scenarioTag,
        int sampleCount,
        double volumeCyLow,
        double volumeCyHigh,
        double quotedTotalLow,
        double quotedTotalTyp,
        double quotedTotalHigh,
        double minFeeTyp,
        String source,
        String sourceUrl,
        String sourceVersionDate,
        String notes
) {
}

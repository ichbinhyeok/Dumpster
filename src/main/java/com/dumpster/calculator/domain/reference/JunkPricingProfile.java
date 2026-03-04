package com.dumpster.calculator.domain.reference;

public record JunkPricingProfile(
        String profileId,
        String displayName,
        double minServiceFeeLow,
        double minServiceFeeTyp,
        double minServiceFeeHigh,
        double perCyFeeLow,
        double perCyFeeTyp,
        double perCyFeeHigh,
        double minimumBillableVolumeCy,
        double truckCapacityCy,
        double billingIncrementFraction,
        double denseMaterialThresholdTonPerCy,
        double denseMaterialMultiplierLow,
        double denseMaterialMultiplierTyp,
        double denseMaterialMultiplierHigh,
        String dataQuality,
        String source,
        String sourceUrl,
        String sourceVersionDate,
        String notes
) {
}

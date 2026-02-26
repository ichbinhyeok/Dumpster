package com.dumpster.calculator.domain.reference;

public record PricingAssumption(
        int sizeYd,
        double rentalFeeLow,
        double rentalFeeTyp,
        double rentalFeeHigh,
        double overageFeePerTonLow,
        double overageFeePerTonTyp,
        double overageFeePerTonHigh,
        double haulFeeLow,
        double haulFeeTyp,
        double haulFeeHigh,
        String junkRateBasis
) {
}


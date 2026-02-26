package com.dumpster.calculator.domain.model;

public record RangeValue(double low, double typ, double high) {

    public RangeValue {
        if (low < 0 || typ < 0 || high < 0) {
            throw new IllegalArgumentException("Range values must be non-negative");
        }
        if (!(low <= typ && typ <= high)) {
            throw new IllegalArgumentException("Range must satisfy low <= typ <= high");
        }
    }

    public static RangeValue of(double low, double typ, double high) {
        return new RangeValue(low, typ, high);
    }

    public static RangeValue single(double value) {
        return new RangeValue(value, value, value);
    }

    public RangeValue add(RangeValue other) {
        return new RangeValue(this.low + other.low, this.typ + other.typ, this.high + other.high);
    }

    public RangeValue multiply(double factor) {
        return new RangeValue(this.low * factor, this.typ * factor, this.high * factor);
    }

    public RangeValue inflate(double uncertainty) {
        double safeUncertainty = Math.max(0.0d, uncertainty);
        return new RangeValue(this.typ * (1.0d - safeUncertainty), this.typ, this.typ * (1.0d + safeUncertainty));
    }

    public RangeValue round2() {
        return new RangeValue(round2(low), round2(typ), round2(high));
    }

    private static double round2(double value) {
        return Math.round(value * 100.0d) / 100.0d;
    }
}


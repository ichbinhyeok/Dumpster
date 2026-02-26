package com.dumpster.calculator.domain.reference;

public enum DataQuality {
    LOW,
    MEDIUM,
    HIGH;

    public double uncertaintyBonus() {
        return switch (this) {
            case LOW -> 0.12d;
            case MEDIUM -> 0.06d;
            case HIGH -> 0.0d;
        };
    }
}


package com.dumpster.calculator.domain.model;

public enum CompactionLevel {
    LOW,
    MEDIUM,
    HIGH;

    public static CompactionLevel fromNullable(String value) {
        if (value == null || value.isBlank()) {
            return MEDIUM;
        }
        return CompactionLevel.valueOf(value.trim().toUpperCase());
    }
}


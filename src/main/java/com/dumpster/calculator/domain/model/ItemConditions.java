package com.dumpster.calculator.domain.model;

public record ItemConditions(Boolean wet, Boolean mixedLoad, String compaction) {

    public static ItemConditions defaults() {
        return new ItemConditions(false, false, CompactionLevel.MEDIUM.name());
    }

    public boolean wetEnabled() {
        return Boolean.TRUE.equals(wet);
    }

    public boolean mixedLoadEnabled() {
        return Boolean.TRUE.equals(mixedLoad);
    }

    public CompactionLevel compactionLevel() {
        return CompactionLevel.fromNullable(compaction);
    }
}


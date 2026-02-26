package com.dumpster.calculator.domain.model;

import java.util.List;

public record CtaRouting(
        String primaryCta,
        String secondaryCta,
        List<String> reasons
) {
}


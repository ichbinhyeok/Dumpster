package com.dumpster.calculator.domain.reference;

import java.util.HashMap;
import java.util.Map;

public record UnitConversion(
        String unitId,
        FormulaType formulaType,
        boolean materialRequired,
        double uncertaintyPct,
        String formulaExpression
) {

    public Map<String, Double> parsedExpression() {
        Map<String, Double> parsed = new HashMap<>();
        if (formulaExpression == null || formulaExpression.isBlank()) {
            return parsed;
        }
        String[] pairs = formulaExpression.split(";");
        for (String pair : pairs) {
            String[] tokens = pair.split("=");
            if (tokens.length != 2) {
                continue;
            }
            parsed.put(tokens[0].trim(), Double.parseDouble(tokens[1].trim()));
        }
        return parsed;
    }
}


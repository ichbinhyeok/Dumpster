package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class CsvDataIntegrityTests {

    @Test
    void materialDensityRangesAreOrdered() throws Exception {
        try (CSVParser parser = parse("data/material_factors.csv")) {
            for (CSVRecord record : parser) {
                double low = Double.parseDouble(record.get("density_low"));
                double typ = Double.parseDouble(record.get("density_typ"));
                double high = Double.parseDouble(record.get("density_high"));
                assertThat(low).isLessThanOrEqualTo(typ);
                assertThat(typ).isLessThanOrEqualTo(high);
            }
        }
    }

    @Test
    void dumpsterPoliciesHaveValidTonHierarchy() throws Exception {
        Set<Integer> sizeSet = new HashSet<>();
        try (CSVParser parser = parse("data/dumpster_sizes.csv")) {
            for (CSVRecord record : parser) {
                int size = Integer.parseInt(record.get("size_yd"));
                sizeSet.add(size);
                double includedLow = Double.parseDouble(record.get("included_tons_low"));
                double includedTyp = Double.parseDouble(record.get("included_tons_typ"));
                double includedHigh = Double.parseDouble(record.get("included_tons_high"));
                double maxLow = Double.parseDouble(record.get("max_haul_tons_low"));
                double maxTyp = Double.parseDouble(record.get("max_haul_tons_typ"));
                double maxHigh = Double.parseDouble(record.get("max_haul_tons_high"));
                double fillRatio = Double.parseDouble(record.get("heavy_debris_max_fill_ratio"));

                assertThat(includedLow).isLessThanOrEqualTo(includedTyp);
                assertThat(includedTyp).isLessThanOrEqualTo(includedHigh);
                assertThat(maxLow).isLessThanOrEqualTo(maxTyp);
                assertThat(maxTyp).isLessThanOrEqualTo(maxHigh);
                assertThat(includedLow).isLessThanOrEqualTo(maxLow);
                assertThat(includedTyp).isLessThanOrEqualTo(maxTyp);
                assertThat(includedHigh).isLessThanOrEqualTo(maxHigh);
                assertThat(fillRatio).isBetween(0.1d, 1.0d);
            }
        }

        Set<Integer> pricedSizes = new HashSet<>();
        boolean hasJunkPricing = false;
        try (CSVParser parser = parse("data/pricing_assumptions.csv")) {
            for (CSVRecord record : parser) {
                int size = Integer.parseInt(record.get("size_yd"));
                if (size == 0) {
                    hasJunkPricing = true;
                } else {
                    pricedSizes.add(size);
                }
            }
        }
        assertThat(hasJunkPricing).isTrue();
        assertThat(pricedSizes).containsAll(sizeSet);
    }

    private static CSVParser parse(String classpathFile) throws Exception {
        return CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreSurroundingSpaces(true)
                .build()
                .parse(new InputStreamReader(
                        new ClassPathResource(classpathFile).getInputStream(),
                        StandardCharsets.UTF_8
                ));
    }
}


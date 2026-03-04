package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class CsvDataIntegrityTests {

    private static final long MAX_SOURCE_AGE_DAYS = 365;

    @Test
    void materialDensityRangesAreOrdered() throws Exception {
        try (CSVParser parser = parse("data/material_factors.csv")) {
            for (CSVRecord record : parser) {
                double low = Double.parseDouble(record.get("density_low"));
                double typ = Double.parseDouble(record.get("density_typ"));
                double high = Double.parseDouble(record.get("density_high"));
                assertThat(low).isLessThanOrEqualTo(typ);
                assertThat(typ).isLessThanOrEqualTo(high);
                assertSourceMetadata(record, true);
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
        try (CSVParser parser = parse("data/pricing_assumptions.csv")) {
            for (CSVRecord record : parser) {
                int size = Integer.parseInt(record.get("size_yd"));
                pricedSizes.add(size);
            }
        }
        assertThat(pricedSizes).containsAll(sizeSet);
    }

    @Test
    void junkPricingProfilesHaveOrderedRangesAndValidBillingRules() throws Exception {
        Set<String> profileIds = new HashSet<>();
        try (CSVParser parser = parse("data/junk_pricing_profiles.csv")) {
            for (CSVRecord record : parser) {
                String profileId = record.get("profile_id");
                double minLow = Double.parseDouble(record.get("min_service_fee_low"));
                double minTyp = Double.parseDouble(record.get("min_service_fee_typ"));
                double minHigh = Double.parseDouble(record.get("min_service_fee_high"));
                double perLow = Double.parseDouble(record.get("per_cy_fee_low"));
                double perTyp = Double.parseDouble(record.get("per_cy_fee_typ"));
                double perHigh = Double.parseDouble(record.get("per_cy_fee_high"));
                double minBillable = Double.parseDouble(record.get("minimum_billable_volume_cy"));
                double truckCapacity = Double.parseDouble(record.get("truck_capacity_cy"));
                double incrementFraction = Double.parseDouble(record.get("billing_increment_fraction"));
                double denseThreshold = Double.parseDouble(record.get("dense_material_threshold_ton_per_cy"));
                double denseLow = Double.parseDouble(record.get("dense_material_multiplier_low"));
                double denseTyp = Double.parseDouble(record.get("dense_material_multiplier_typ"));
                double denseHigh = Double.parseDouble(record.get("dense_material_multiplier_high"));
                String source = record.get("source");
                String sourceUrl = record.get("source_url");

                profileIds.add(profileId);
                assertThat(minLow).isLessThanOrEqualTo(minTyp);
                assertThat(minTyp).isLessThanOrEqualTo(minHigh);
                assertThat(perLow).isLessThanOrEqualTo(perTyp);
                assertThat(perTyp).isLessThanOrEqualTo(perHigh);
                assertThat(minBillable).isGreaterThan(0.0d);
                assertThat(truckCapacity).isGreaterThan(0.0d);
                assertThat(minBillable).isLessThanOrEqualTo(truckCapacity);
                assertThat(incrementFraction).isBetween(0.01d, 1.0d);
                assertThat(denseThreshold).isGreaterThan(0.0d);
                assertThat(denseLow).isGreaterThanOrEqualTo(1.0d);
                assertThat(denseLow).isLessThanOrEqualTo(denseTyp);
                assertThat(denseTyp).isLessThanOrEqualTo(denseHigh);
                assertThat(source).isNotBlank();
                assertThat(sourceUrl).contains("http");
                assertSourceMetadata(record, true);
            }
        }

        try (CSVParser parser = parse("data/junk_pricing_profile_rules.csv")) {
            for (CSVRecord record : parser) {
                String marketTier = record.get("market_tier");
                String needTiming = record.get("need_timing");
                String profileId = record.get("profile_id");
                int priority = Integer.parseInt(record.get("priority"));

                assertThat(marketTier).isIn(
                        "urban",
                        "value",
                        "national",
                        "coastal",
                        "mountain",
                        "heartland",
                        "any"
                );
                assertThat(needTiming).isNotBlank();
                assertThat(priority).isGreaterThan(0);
                assertThat(profileIds).contains(profileId);
                assertSourceMetadata(record, true);
            }
        }
    }

    @Test
    void marketTierZipOverridesHaveValidRangesAndTierValues() throws Exception {
        Set<String> ruleIds = new HashSet<>();
        List<String> files = new ArrayList<>();
        files.add("data/market_tier_zip_overrides.csv");
        if (new ClassPathResource("data/market_tier_zip_overrides_regional.csv").exists()) {
            files.add("data/market_tier_zip_overrides_regional.csv");
        }

        for (String file : files) {
            try (CSVParser parser = parse(file)) {
                for (CSVRecord record : parser) {
                    String ruleId = record.get("rule_id");
                    String zipStart = record.get("zip_start");
                    String zipEnd = record.get("zip_end");
                    String marketTier = record.get("market_tier");
                    int priority = Integer.parseInt(record.get("priority"));
                    String source = record.get("source");
                    String sourceUrl = record.get("source_url");

                    ruleIds.add(ruleId);
                    assertThat(zipStart).matches("\\d{5}");
                    assertThat(zipEnd).matches("\\d{5}");
                    assertThat(Integer.parseInt(zipStart)).isLessThanOrEqualTo(Integer.parseInt(zipEnd));
                    assertThat(marketTier).isIn("urban", "value", "national", "coastal", "mountain", "heartland");
                    assertThat(priority).isGreaterThan(0);
                    assertThat(source).isNotBlank();
                    assertThat(sourceUrl).contains("http");
                    assertSourceMetadata(record, true);
                }
            }
        }
        assertThat(ruleIds).isNotEmpty();
    }

    @Test
    void junkQuoteBenchmarksHaveOrderedRangesAndSampleDepth() throws Exception {
        int rowCount = 0;
        try (CSVParser parser = parse("data/junk_quote_benchmarks.csv")) {
            for (CSVRecord record : parser) {
                rowCount++;
                String marketTier = record.get("market_tier");
                String needTiming = record.get("need_timing");
                int sampleCount = Integer.parseInt(record.get("sample_count"));
                double volumeLow = Double.parseDouble(record.get("volume_cy_low"));
                double volumeHigh = Double.parseDouble(record.get("volume_cy_high"));
                double quoteLow = Double.parseDouble(record.get("quoted_total_low"));
                double quoteTyp = Double.parseDouble(record.get("quoted_total_typ"));
                double quoteHigh = Double.parseDouble(record.get("quoted_total_high"));
                double minFeeTyp = Double.parseDouble(record.get("min_fee_typ"));

                assertThat(marketTier).isIn("urban", "value", "national", "coastal", "mountain", "heartland");
                assertThat(needTiming).isIn("research", "this_week", "48h");
                assertThat(sampleCount).isGreaterThanOrEqualTo(20);
                assertThat(volumeLow).isGreaterThan(0.0d);
                assertThat(volumeLow).isLessThanOrEqualTo(volumeHigh);
                assertThat(quoteLow).isLessThanOrEqualTo(quoteTyp);
                assertThat(quoteTyp).isLessThanOrEqualTo(quoteHigh);
                assertThat(minFeeTyp).isGreaterThan(0.0d);
                assertSourceMetadata(record, true);
            }
        }
        assertThat(rowCount).isGreaterThanOrEqualTo(18);
    }

    private static void assertSourceMetadata(CSVRecord record, boolean requireSourceUrl) {
        String source = record.get("source");
        String sourceVersionDateRaw = record.get("source_version_date");

        assertThat(source).isNotBlank();
        assertThat(sourceVersionDateRaw).isNotBlank();
        if (requireSourceUrl) {
            String sourceUrl = record.get("source_url");
            assertThat(sourceUrl).contains("http");
        }

        LocalDate sourceVersionDate = LocalDate.parse(sourceVersionDateRaw);
        LocalDate today = LocalDate.now();
        assertThat(sourceVersionDate).isBeforeOrEqualTo(today);
        assertThat(ChronoUnit.DAYS.between(sourceVersionDate, today)).isLessThanOrEqualTo(MAX_SOURCE_AGE_DAYS);
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

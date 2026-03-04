package com.dumpster.calculator.infra.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CsvBootstrapService {

    private static final Logger log = LoggerFactory.getLogger(CsvBootstrapService.class);

    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;

    public CsvBootstrapService(JdbcTemplate jdbcTemplate, ResourceLoader resourceLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.resourceLoader = resourceLoader;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void bootstrapReferenceData() {
        seedMaterialFactors();
        seedUnitConversions();
        seedDumpsterSizes();
        seedPricingAssumptions();
        seedJunkPricingProfiles();
        seedJunkPricingProfileRules();
        seedMarketTierZipOverrides();
    }

    private void seedMaterialFactors() {
        List<Map<String, String>> rows = readCsv("classpath:data/material_factors.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    merge into material_factors (
                        material_id, name, category, density_low, density_typ, density_high,
                        wet_multiplier_low, wet_multiplier_high, data_quality, source, source_url, source_version_date
                    ) key(material_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    row.get("material_id"),
                    row.get("name"),
                    row.get("category"),
                    parseDouble(row.get("density_low")),
                    parseDouble(row.get("density_typ")),
                    parseDouble(row.get("density_high")),
                    parseDouble(row.get("wet_multiplier_low")),
                    parseDouble(row.get("wet_multiplier_high")),
                    row.get("data_quality"),
                    row.getOrDefault("source", ""),
                    row.getOrDefault("source_url", ""),
                    row.getOrDefault("source_version_date", null));
        }
        log.info("Upserted material_factors: {}", rows.size());
    }

    private void seedUnitConversions() {
        List<Map<String, String>> rows = readCsv("classpath:data/unit_conversions.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    merge into unit_conversions (
                        unit_id, formula_type, material_required, uncertainty_pct, formula_expression
                    ) key(unit_id) values (?, ?, ?, ?, ?)
                    """,
                    row.get("unit_id"),
                    row.get("formula_type"),
                    parseBoolean(row.get("material_required")),
                    parseDouble(row.get("uncertainty_pct")),
                    row.get("formula_expression"));
        }
        log.info("Upserted unit_conversions: {}", rows.size());
    }

    private void seedDumpsterSizes() {
        List<Map<String, String>> rows = readCsv("classpath:data/dumpster_sizes.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    merge into dumpster_sizes (
                        size_yd, dimensions_approx,
                        included_tons_low, included_tons_typ, included_tons_high,
                        max_haul_tons_low, max_haul_tons_typ, max_haul_tons_high,
                        heavy_debris_max_fill_ratio, clean_load_required_for_heavy
                    ) key(size_yd) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    parseInteger(row.get("size_yd")),
                    row.get("dimensions_approx"),
                    parseDouble(row.get("included_tons_low")),
                    parseDouble(row.get("included_tons_typ")),
                    parseDouble(row.get("included_tons_high")),
                    parseDouble(row.get("max_haul_tons_low")),
                    parseDouble(row.get("max_haul_tons_typ")),
                    parseDouble(row.get("max_haul_tons_high")),
                    parseDouble(row.get("heavy_debris_max_fill_ratio")),
                    parseBoolean(row.get("clean_load_required_for_heavy")));
        }
        log.info("Upserted dumpster_sizes: {}", rows.size());
    }

    private void seedPricingAssumptions() {
        List<Map<String, String>> rows = readCsv("classpath:data/pricing_assumptions.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    merge into pricing_assumptions (
                        size_yd,
                        rental_fee_low, rental_fee_typ, rental_fee_high,
                        overage_fee_per_ton_low, overage_fee_per_ton_typ, overage_fee_per_ton_high,
                        haul_fee_low, haul_fee_typ, haul_fee_high, junk_rate_basis
                    ) key(size_yd) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    parseInteger(row.get("size_yd")),
                    parseDouble(row.get("rental_fee_low")),
                    parseDouble(row.get("rental_fee_typ")),
                    parseDouble(row.get("rental_fee_high")),
                    parseDouble(row.get("overage_fee_per_ton_low")),
                    parseDouble(row.get("overage_fee_per_ton_typ")),
                    parseDouble(row.get("overage_fee_per_ton_high")),
                    parseDouble(row.get("haul_fee_low")),
                    parseDouble(row.get("haul_fee_typ")),
                    parseDouble(row.get("haul_fee_high")),
                    row.get("junk_rate_basis"));
        }
        log.info("Upserted pricing_assumptions: {}", rows.size());
    }

    private void seedJunkPricingProfiles() {
        List<Map<String, String>> rows = readCsv("classpath:data/junk_pricing_profiles.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    merge into junk_pricing_profiles (
                        profile_id,
                        display_name,
                        min_service_fee_low, min_service_fee_typ, min_service_fee_high,
                        per_cy_fee_low, per_cy_fee_typ, per_cy_fee_high,
                        minimum_billable_volume_cy,
                        truck_capacity_cy,
                        billing_increment_fraction,
                        dense_material_threshold_ton_per_cy,
                        dense_material_multiplier_low,
                        dense_material_multiplier_typ,
                        dense_material_multiplier_high,
                        data_quality,
                        source,
                        source_url,
                        source_version_date,
                        notes
                    ) key(profile_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    row.get("profile_id"),
                    row.get("display_name"),
                    parseDouble(row.get("min_service_fee_low")),
                    parseDouble(row.get("min_service_fee_typ")),
                    parseDouble(row.get("min_service_fee_high")),
                    parseDouble(row.get("per_cy_fee_low")),
                    parseDouble(row.get("per_cy_fee_typ")),
                    parseDouble(row.get("per_cy_fee_high")),
                    parseDouble(row.get("minimum_billable_volume_cy")),
                    parseDouble(row.get("truck_capacity_cy")),
                    parseDouble(row.get("billing_increment_fraction")),
                    parseDouble(row.get("dense_material_threshold_ton_per_cy")),
                    parseDouble(row.get("dense_material_multiplier_low")),
                    parseDouble(row.get("dense_material_multiplier_typ")),
                    parseDouble(row.get("dense_material_multiplier_high")),
                    row.get("data_quality"),
                    row.get("source"),
                    row.get("source_url"),
                    row.getOrDefault("source_version_date", null),
                    row.getOrDefault("notes", ""));
        }
        log.info("Upserted junk_pricing_profiles: {}", rows.size());
    }

    private void seedJunkPricingProfileRules() {
        List<Map<String, String>> rows = readCsv("classpath:data/junk_pricing_profile_rules.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    merge into junk_pricing_profile_rules (
                        rule_id,
                        market_tier,
                        need_timing,
                        profile_id,
                        priority,
                        source,
                        source_url,
                        source_version_date,
                        notes
                    ) key(rule_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    row.get("rule_id"),
                    row.get("market_tier"),
                    row.get("need_timing"),
                    row.get("profile_id"),
                    parseInteger(row.get("priority")),
                    row.get("source"),
                    row.get("source_url"),
                    row.getOrDefault("source_version_date", null),
                    row.getOrDefault("notes", ""));
        }
        log.info("Upserted junk_pricing_profile_rules: {}", rows.size());
    }

    private void seedMarketTierZipOverrides() {
        List<Map<String, String>> rows = new ArrayList<>(readCsv("classpath:data/market_tier_zip_overrides.csv"));
        Resource regionalOverrides = resourceLoader.getResource("classpath:data/market_tier_zip_overrides_regional.csv");
        if (regionalOverrides.exists()) {
            rows.addAll(readCsv("classpath:data/market_tier_zip_overrides_regional.csv"));
        }
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    merge into market_tier_zip_overrides (
                        rule_id,
                        zip_start,
                        zip_end,
                        market_tier,
                        priority,
                        source,
                        source_url,
                        source_version_date,
                        notes
                    ) key(rule_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    row.get("rule_id"),
                    row.get("zip_start"),
                    row.get("zip_end"),
                    row.get("market_tier"),
                    parseInteger(row.get("priority")),
                    row.get("source"),
                    row.get("source_url"),
                    row.getOrDefault("source_version_date", null),
                    row.getOrDefault("notes", ""));
        }
        log.info("Upserted market_tier_zip_overrides: {}", rows.size());
    }

    private List<Map<String, String>> readCsv(String location) {
        Resource resource = resourceLoader.getResource(location);
        if (!resource.exists()) {
            throw new IllegalStateException("CSV resource not found: " + location);
        }
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreSurroundingSpaces(true)
                     .build()
                     .parse(reader)) {

            if (parser.getHeaderMap() == null || parser.getHeaderMap().isEmpty()) {
                return List.of();
            }
            List<Map<String, String>> rows = new ArrayList<>();
            for (CSVRecord record : parser) {
                Map<String, String> row = new HashMap<>();
                boolean hasData = false;
                for (String header : parser.getHeaderNames()) {
                    String key = header.trim();
                    String value = record.isMapped(header) ? record.get(header).trim() : "";
                    row.put(key, value);
                    if (!value.isBlank()) {
                        hasData = true;
                    }
                }
                if (!hasData) {
                    continue;
                }
                rows.add(row);
            }
            return rows;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read CSV: " + location, e);
        }
    }

    private static double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return 0.0d;
        }
        return Double.parseDouble(value);
    }

    private static int parseInteger(String value) {
        return Integer.parseInt(value);
    }

    private static boolean parseBoolean(String value) {
        return Boolean.parseBoolean(value);
    }
}

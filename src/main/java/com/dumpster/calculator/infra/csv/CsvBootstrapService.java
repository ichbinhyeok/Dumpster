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
    }

    private void seedMaterialFactors() {
        if (countRows("material_factors") > 0) {
            return;
        }
        List<Map<String, String>> rows = readCsv("classpath:data/material_factors.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    insert into material_factors (
                        material_id, name, category, density_low, density_typ, density_high,
                        wet_multiplier_low, wet_multiplier_high, data_quality, source, source_version_date
                    ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
                    row.getOrDefault("source_version_date", null));
        }
        log.info("Seeded material_factors: {}", rows.size());
    }

    private void seedUnitConversions() {
        if (countRows("unit_conversions") > 0) {
            return;
        }
        List<Map<String, String>> rows = readCsv("classpath:data/unit_conversions.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    insert into unit_conversions (
                        unit_id, formula_type, material_required, uncertainty_pct, formula_expression
                    ) values (?, ?, ?, ?, ?)
                    """,
                    row.get("unit_id"),
                    row.get("formula_type"),
                    parseBoolean(row.get("material_required")),
                    parseDouble(row.get("uncertainty_pct")),
                    row.get("formula_expression"));
        }
        log.info("Seeded unit_conversions: {}", rows.size());
    }

    private void seedDumpsterSizes() {
        if (countRows("dumpster_sizes") > 0) {
            return;
        }
        List<Map<String, String>> rows = readCsv("classpath:data/dumpster_sizes.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    insert into dumpster_sizes (
                        size_yd, dimensions_approx,
                        included_tons_low, included_tons_typ, included_tons_high,
                        max_haul_tons_low, max_haul_tons_typ, max_haul_tons_high,
                        heavy_debris_max_fill_ratio, clean_load_required_for_heavy
                    ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
        log.info("Seeded dumpster_sizes: {}", rows.size());
    }

    private void seedPricingAssumptions() {
        if (countRows("pricing_assumptions") > 0) {
            return;
        }
        List<Map<String, String>> rows = readCsv("classpath:data/pricing_assumptions.csv");
        for (Map<String, String> row : rows) {
            jdbcTemplate.update("""
                    insert into pricing_assumptions (
                        size_yd,
                        rental_fee_low, rental_fee_typ, rental_fee_high,
                        overage_fee_per_ton_low, overage_fee_per_ton_typ, overage_fee_per_ton_high,
                        haul_fee_low, haul_fee_typ, haul_fee_high, junk_rate_basis
                    ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
        log.info("Seeded pricing_assumptions: {}", rows.size());
    }

    private int countRows(String tableName) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from " + tableName, Integer.class);
        return count == null ? 0 : count;
    }

    private List<Map<String, String>> readCsv(String location) {
        Resource resource = resourceLoader.getResource(location);
        if (!resource.exists()) {
            throw new IllegalStateException("CSV resource not found: " + location);
        }
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.isBlank()) {
                return List.of();
            }
            String[] headers = headerLine.split(",");
            List<Map<String, String>> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = line.split(",", -1);
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String key = headers[i].trim();
                    String value = i < values.length ? values[i].trim() : "";
                    row.put(key, value);
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


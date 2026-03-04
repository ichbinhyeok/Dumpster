package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.reference.MarketTierZipRule;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MarketTierZipRuleRepository {

    private static final RowMapper<MarketTierZipRule> ROW_MAPPER = (rs, rowNum) -> new MarketTierZipRule(
            rs.getString("rule_id"),
            rs.getString("zip_start"),
            rs.getString("zip_end"),
            rs.getString("market_tier"),
            rs.getInt("priority"),
            rs.getString("source"),
            rs.getString("source_url"),
            rs.getString("notes")
    );

    private final JdbcTemplate jdbcTemplate;

    public MarketTierZipRuleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MarketTierZipRule> resolveByZip(String zipCode) {
        String normalizedZip = normalizeZip(zipCode);
        if (normalizedZip == null) {
            return Optional.empty();
        }
        List<MarketTierZipRule> rows = jdbcTemplate.query(
                """
                select rule_id, zip_start, zip_end, market_tier, priority, source, source_url, notes
                from market_tier_zip_overrides
                where ? between zip_start and zip_end
                order by priority asc
                fetch first 1 rows only
                """,
                ROW_MAPPER,
                normalizedZip
        );
        return rows.stream().findFirst();
    }

    private static String normalizeZip(String zipCode) {
        if (zipCode == null) {
            return null;
        }
        String digits = zipCode.replaceAll("[^0-9]", "");
        if (digits.length() != 5) {
            return null;
        }
        return digits;
    }
}

package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.reference.JunkPricingProfileRule;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JunkPricingProfileRuleRepository {

    private static final RowMapper<JunkPricingProfileRule> ROW_MAPPER = (rs, rowNum) -> new JunkPricingProfileRule(
            rs.getString("rule_id"),
            rs.getString("market_tier"),
            rs.getString("need_timing"),
            rs.getString("profile_id"),
            rs.getInt("priority")
    );

    private final JdbcTemplate jdbcTemplate;

    public JunkPricingProfileRuleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<String> resolveProfileId(String marketTier, String needTiming) {
        List<JunkPricingProfileRule> rules = jdbcTemplate.query(
                """
                select *
                from junk_pricing_profile_rules
                where (market_tier = ? or market_tier = 'any')
                  and (need_timing = ? or need_timing = 'any')
                order by priority asc
                """,
                ROW_MAPPER,
                normalize(marketTier),
                normalize(needTiming)
        );
        return rules.stream()
                .map(JunkPricingProfileRule::profileId)
                .findFirst();
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "any";
        }
        return value.trim().toLowerCase();
    }
}

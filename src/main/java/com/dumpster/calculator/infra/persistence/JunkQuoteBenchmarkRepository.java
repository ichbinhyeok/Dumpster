package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.reference.JunkQuoteBenchmark;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JunkQuoteBenchmarkRepository {

    private static final RowMapper<JunkQuoteBenchmark> ROW_MAPPER = (rs, rowNum) -> new JunkQuoteBenchmark(
            rs.getString("benchmark_id"),
            rs.getString("market_tier"),
            rs.getString("need_timing"),
            rs.getString("scenario_tag"),
            rs.getInt("sample_count"),
            rs.getDouble("volume_cy_low"),
            rs.getDouble("volume_cy_high"),
            rs.getDouble("quoted_total_low"),
            rs.getDouble("quoted_total_typ"),
            rs.getDouble("quoted_total_high"),
            rs.getDouble("min_fee_typ"),
            rs.getString("source"),
            rs.getString("source_url"),
            rs.getString("source_version_date"),
            rs.getString("notes")
    );

    private final JdbcTemplate jdbcTemplate;

    public JunkQuoteBenchmarkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<JunkQuoteBenchmark> findCandidates(String marketTier, String needTiming) {
        return jdbcTemplate.query(
                """
                        select *
                        from junk_quote_benchmarks
                        where (market_tier = ? or market_tier = 'national')
                          and (need_timing = ? or need_timing = 'research')
                        order by sample_count desc, benchmark_id asc
                        """,
                ROW_MAPPER,
                normalize(marketTier),
                normalize(needTiming)
        );
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return "research";
        }
        return value.trim().toLowerCase();
    }
}

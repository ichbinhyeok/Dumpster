package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.reference.DumpsterSizePolicy;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DumpsterSizeRepository {

    private static final RowMapper<DumpsterSizePolicy> ROW_MAPPER = (rs, rowNum) -> new DumpsterSizePolicy(
            rs.getInt("size_yd"),
            rs.getString("dimensions_approx"),
            rs.getDouble("included_tons_low"),
            rs.getDouble("included_tons_typ"),
            rs.getDouble("included_tons_high"),
            rs.getDouble("max_haul_tons_low"),
            rs.getDouble("max_haul_tons_typ"),
            rs.getDouble("max_haul_tons_high"),
            rs.getDouble("heavy_debris_max_fill_ratio"),
            rs.getBoolean("clean_load_required_for_heavy")
    );

    private final JdbcTemplate jdbcTemplate;

    public DumpsterSizeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DumpsterSizePolicy> findAllOrdered() {
        return jdbcTemplate.query("select * from dumpster_sizes order by size_yd asc", ROW_MAPPER);
    }

    public Optional<DumpsterSizePolicy> findBySize(int sizeYd) {
        List<DumpsterSizePolicy> results = jdbcTemplate.query(
                "select * from dumpster_sizes where size_yd = ?",
                ROW_MAPPER,
                sizeYd
        );
        return results.stream().findFirst();
    }
}


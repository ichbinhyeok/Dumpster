package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.reference.PricingAssumption;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PricingAssumptionRepository {

    private static final RowMapper<PricingAssumption> ROW_MAPPER = (rs, rowNum) -> new PricingAssumption(
            rs.getInt("size_yd"),
            rs.getDouble("rental_fee_low"),
            rs.getDouble("rental_fee_typ"),
            rs.getDouble("rental_fee_high"),
            rs.getDouble("overage_fee_per_ton_low"),
            rs.getDouble("overage_fee_per_ton_typ"),
            rs.getDouble("overage_fee_per_ton_high"),
            rs.getDouble("haul_fee_low"),
            rs.getDouble("haul_fee_typ"),
            rs.getDouble("haul_fee_high"),
            rs.getString("junk_rate_basis")
    );

    private final JdbcTemplate jdbcTemplate;

    public PricingAssumptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<PricingAssumption> findBySize(int sizeYd) {
        List<PricingAssumption> results = jdbcTemplate.query(
                "select * from pricing_assumptions where size_yd = ?",
                ROW_MAPPER,
                sizeYd
        );
        return results.stream().findFirst();
    }
}


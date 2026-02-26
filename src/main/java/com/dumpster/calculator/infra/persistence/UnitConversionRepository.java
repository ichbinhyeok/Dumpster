package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.reference.FormulaType;
import com.dumpster.calculator.domain.reference.UnitConversion;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UnitConversionRepository {

    private static final RowMapper<UnitConversion> ROW_MAPPER = (rs, rowNum) -> new UnitConversion(
            rs.getString("unit_id"),
            FormulaType.valueOf(rs.getString("formula_type").toUpperCase()),
            rs.getBoolean("material_required"),
            rs.getDouble("uncertainty_pct"),
            rs.getString("formula_expression")
    );

    private final JdbcTemplate jdbcTemplate;

    public UnitConversionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<UnitConversion> findById(String unitId) {
        List<UnitConversion> results = jdbcTemplate.query(
                "select * from unit_conversions where unit_id = ?",
                ROW_MAPPER,
                unitId
        );
        return results.stream().findFirst();
    }

    public List<UnitConversion> findAll() {
        return jdbcTemplate.query("select * from unit_conversions", ROW_MAPPER);
    }
}


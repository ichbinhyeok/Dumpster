package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.reference.DataQuality;
import com.dumpster.calculator.domain.reference.MaterialCategory;
import com.dumpster.calculator.domain.reference.MaterialFactor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MaterialFactorRepository {

    private static final RowMapper<MaterialFactor> ROW_MAPPER = (rs, rowNum) -> new MaterialFactor(
            rs.getString("material_id"),
            rs.getString("name"),
            MaterialCategory.valueOf(rs.getString("category").toUpperCase()),
            rs.getDouble("density_low"),
            rs.getDouble("density_typ"),
            rs.getDouble("density_high"),
            rs.getDouble("wet_multiplier_low"),
            rs.getDouble("wet_multiplier_high"),
            DataQuality.valueOf(rs.getString("data_quality").toUpperCase()),
            rs.getString("source"),
            readOptionalSourceVersionDate(rs)
    );

    private final JdbcTemplate jdbcTemplate;

    public MaterialFactorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MaterialFactor> findById(String materialId) {
        List<MaterialFactor> results = jdbcTemplate.query(
                "select * from material_factors where material_id = ?",
                ROW_MAPPER,
                materialId
        );
        return results.stream().findFirst();
    }

    public List<MaterialFactor> findAll() {
        return jdbcTemplate.query("select * from material_factors", ROW_MAPPER);
    }

    private static java.time.LocalDate readOptionalSourceVersionDate(ResultSet rs) {
        try {
            var date = rs.getDate("source_version_date");
            return date == null ? null : date.toLocalDate();
        } catch (SQLException ignored) {
            // Backward compatibility for local DB files created before this column existed.
            return null;
        }
    }
}

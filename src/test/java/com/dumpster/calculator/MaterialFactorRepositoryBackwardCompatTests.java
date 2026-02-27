package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class MaterialFactorRepositoryBackwardCompatTests {

    @Test
    void rowMapperHandlesMissingSourceVersionDateColumn() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(
                new org.springframework.jdbc.datasource.DriverManagerDataSource(
                        "jdbc:h2:mem:mf_repo_compat;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1",
                        "sa",
                        ""
                )
        );
        jdbcTemplate.execute("""
                create table material_factors (
                    material_id varchar(120) primary key,
                    name varchar(200) not null,
                    category varchar(20) not null,
                    density_low double precision not null,
                    density_typ double precision not null,
                    density_high double precision not null,
                    wet_multiplier_low double precision not null,
                    wet_multiplier_high double precision not null,
                    data_quality varchar(10) not null,
                    "source" varchar(255)
                )
                """);
        jdbcTemplate.update("""
                insert into material_factors (
                    material_id, name, category, density_low, density_typ, density_high,
                    wet_multiplier_low, wet_multiplier_high, data_quality, "source"
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                "asphalt_shingles",
                "Asphalt shingles",
                "heavy",
                700.0d,
                950.0d,
                1300.0d,
                1.08d,
                1.25d,
                "medium",
                "EPA_SMM"
        );

        MaterialFactorRepository repository = new MaterialFactorRepository(jdbcTemplate);
        var factor = repository.findById("asphalt_shingles").orElseThrow();
        assertThat(factor.sourceVersionDate()).isNull();
    }
}

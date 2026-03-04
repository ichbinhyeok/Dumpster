package com.dumpster.calculator.infra.persistence;

import com.dumpster.calculator.domain.reference.JunkPricingProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JunkPricingProfileRepository {

    private static final String DEFAULT_PROFILE_ID = "national_baseline_2026";

    private static final RowMapper<JunkPricingProfile> ROW_MAPPER = (rs, rowNum) -> new JunkPricingProfile(
            rs.getString("profile_id"),
            rs.getString("display_name"),
            rs.getDouble("min_service_fee_low"),
            rs.getDouble("min_service_fee_typ"),
            rs.getDouble("min_service_fee_high"),
            rs.getDouble("per_cy_fee_low"),
            rs.getDouble("per_cy_fee_typ"),
            rs.getDouble("per_cy_fee_high"),
            rs.getDouble("minimum_billable_volume_cy"),
            rs.getDouble("truck_capacity_cy"),
            rs.getDouble("billing_increment_fraction"),
            rs.getDouble("dense_material_threshold_ton_per_cy"),
            rs.getDouble("dense_material_multiplier_low"),
            rs.getDouble("dense_material_multiplier_typ"),
            rs.getDouble("dense_material_multiplier_high"),
            rs.getString("data_quality"),
            rs.getString("source"),
            rs.getString("source_url"),
            rs.getString("source_version_date"),
            rs.getString("notes")
    );

    private final JdbcTemplate jdbcTemplate;

    public JunkPricingProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<JunkPricingProfile> findDefaultProfile() {
        List<JunkPricingProfile> preferred = jdbcTemplate.query(
                "select * from junk_pricing_profiles where profile_id = ?",
                ROW_MAPPER,
                DEFAULT_PROFILE_ID
        );
        if (!preferred.isEmpty()) {
            return Optional.of(preferred.getFirst());
        }
        List<JunkPricingProfile> fallback = jdbcTemplate.query(
                "select * from junk_pricing_profiles order by profile_id limit 1",
                ROW_MAPPER
        );
        return fallback.stream().findFirst();
    }

    public Optional<JunkPricingProfile> findById(String profileId) {
        if (profileId == null || profileId.isBlank()) {
            return Optional.empty();
        }
        List<JunkPricingProfile> results = jdbcTemplate.query(
                "select * from junk_pricing_profiles where profile_id = ?",
                ROW_MAPPER,
                profileId
        );
        return results.stream().findFirst();
    }
}

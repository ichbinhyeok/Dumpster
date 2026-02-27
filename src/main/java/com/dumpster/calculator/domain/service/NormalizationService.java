package com.dumpster.calculator.domain.service;

import com.dumpster.calculator.domain.model.CompactionLevel;
import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateItemInput;
import com.dumpster.calculator.domain.model.ItemConditions;
import com.dumpster.calculator.domain.model.LineItemEstimate;
import com.dumpster.calculator.domain.model.RangeValue;
import com.dumpster.calculator.domain.reference.FormulaType;
import com.dumpster.calculator.domain.reference.MaterialCategory;
import com.dumpster.calculator.domain.reference.MaterialFactor;
import com.dumpster.calculator.domain.reference.UnitConversion;
import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import com.dumpster.calculator.infra.persistence.UnitConversionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class NormalizationService {

    private final MaterialFactorRepository materialFactorRepository;
    private final UnitConversionRepository unitConversionRepository;

    public NormalizationService(
            MaterialFactorRepository materialFactorRepository,
            UnitConversionRepository unitConversionRepository
    ) {
        this.materialFactorRepository = materialFactorRepository;
        this.unitConversionRepository = unitConversionRepository;
    }

    public List<LineItemEstimate> normalize(EstimateCommand command) {
        if (command.items() == null || command.items().isEmpty()) {
            throw new IllegalArgumentException("At least one line item is required.");
        }
        List<LineItemEstimate> normalizedItems = new ArrayList<>();
        boolean globalMixed = command.safeOptions().globalMixedLoad();

        for (EstimateItemInput item : command.items()) {
            if (item.quantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
            MaterialFactor material = materialFactorRepository.findById(item.materialId())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown material_id: " + item.materialId()));
            UnitConversion conversion = unitConversionRepository.findById(item.unitId())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown unit_id: " + item.unitId()));
            validateUnitMaterialCompatibility(conversion.unitId(), material.materialId());

            ItemConditions conditions = item.safeConditions();
            Map<String, Double> expression = conversion.parsedExpression();
            double volumeTyp = computeVolumeTyp(item.quantity(), conversion.formulaType(), expression, material);
            double uncertainty = (conversion.uncertaintyPct() / 100.0d) + material.dataQuality().uncertaintyBonus();
            RangeValue volume = RangeValue.single(volumeTyp).inflate(uncertainty);

            List<String> impacts = new ArrayList<>();
            if (conditions.wetEnabled()) {
                impacts.add(material.name() + ": wet on -> weight range increased.");
            }
            if (conditions.mixedLoadEnabled() || globalMixed) {
                volume = volume.multiply(1.12d);
                impacts.add(material.name() + ": mixed load on -> volume inflated by 12%.");
            }
            double compactionFactor = compactionFactor(conditions.compactionLevel());
            if (compactionFactor != 1.0d) {
                volume = volume.multiply(compactionFactor);
                impacts.add(material.name() + ": compaction " + conditions.compactionLevel().name().toLowerCase()
                        + " adjusted volume.");
            }

            RangeValue weight = computeWeightRange(volume, material, conditions.wetEnabled());
            normalizedItems.add(new LineItemEstimate(material.materialId(), material.category(), volume, weight, impacts));
        }

        return normalizedItems;
    }

    private static double computeVolumeTyp(
            double quantity,
            FormulaType formulaType,
            Map<String, Double> expression,
            MaterialFactor material
    ) {
        return switch (formulaType) {
            case VOLUME_MULTIPLIER -> quantity * expression.getOrDefault("multiplier", 1.0d);
            case AREA_THICKNESS -> {
                double thicknessIn = expression.getOrDefault("thicknessIn", 1.0d);
                yield (quantity * (thicknessIn / 12.0d)) / 27.0d;
            }
            case WEIGHT_LBS_PER_UNIT -> {
                double lbsPerUnit = expression.getOrDefault("lbsPerUnit", 1.0d);
                double weightLbs = quantity * lbsPerUnit;
                yield weightLbs / material.densityTyp();
            }
        };
    }

    private static RangeValue computeWeightRange(RangeValue volume, MaterialFactor material, boolean wet) {
        double low = volume.low() * material.densityLow() / 2000.0d;
        double typ = volume.typ() * material.densityTyp() / 2000.0d;
        double high = volume.high() * material.densityHigh() / 2000.0d;

        if (wet) {
            low *= material.wetMultiplierLow();
            typ *= (material.wetMultiplierLow() + material.wetMultiplierHigh()) / 2.0d;
            high *= material.wetMultiplierHigh();
        }
        return RangeValue.of(low, typ, high);
    }

    private static double compactionFactor(CompactionLevel level) {
        return switch (level) {
            case LOW -> 1.05d;
            case MEDIUM -> 1.0d;
            case HIGH -> 0.90d;
        };
    }

    private static void validateUnitMaterialCompatibility(String unitId, String materialId) {
        if ("roof_square".equals(unitId) && !"asphalt_shingles".equals(materialId)) {
            throw new IllegalArgumentException("unit roof_square is only supported for asphalt_shingles");
        }
    }
}

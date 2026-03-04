package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateItemInput;
import com.dumpster.calculator.domain.model.EstimateOptions;
import com.dumpster.calculator.domain.model.EstimateResult;
import com.dumpster.calculator.domain.model.ItemConditions;
import com.dumpster.calculator.domain.model.PriceRisk;
import com.dumpster.calculator.domain.model.CostComparisonOption;
import com.dumpster.calculator.domain.service.EstimationFacade;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EstimationFacadeTests {

    @Autowired
    private EstimationFacade estimationFacade;

    @Test
    void heavyConcreteLargeLoadRequiresMultiHaulOrHardStop() {
        EstimateCommand command = command(
                "concrete_removal",
                "homeowner",
                "concrete",
                600,
                "sqft_4in",
                false,
                false,
                2.0d,
                "research"
        );

        EstimateResult result = estimationFacade.estimate(command);

        assertThat(result.feasibility().name()).isNotEqualTo("OK");
        assertThat(result.recommendations()).isNotEmpty();
        assertThat(result.recommendations().getFirst().multiHaul()
                || !result.hardStopReasons().isEmpty()).isTrue();
    }

    @Test
    void wetYardWasteIncreasesWeightRange() {
        EstimateCommand dry = command(
                "yard_cleanup",
                "homeowner",
                "yard_waste",
                8,
                "pickup_load",
                false,
                false,
                null,
                "research"
        );
        EstimateCommand wet = command(
                "yard_cleanup",
                "homeowner",
                "yard_waste",
                8,
                "pickup_load",
                true,
                false,
                null,
                "research"
        );

        EstimateResult dryResult = estimationFacade.estimate(dry);
        EstimateResult wetResult = estimationFacade.estimate(wet);

        assertThat(wetResult.weightTons().high()).isGreaterThan(dryResult.weightTons().high());
    }

    @Test
    void missingAllowanceFlagsAssumedAllowance() {
        EstimateCommand command = command(
                "kitchen_remodel",
                "homeowner",
                "mixed_cd",
                7,
                "pickup_load",
                false,
                true,
                null,
                "research"
        );

        EstimateResult result = estimationFacade.estimate(command);
        assertThat(result.usedAssumedAllowance()).isTrue();
    }

    @Test
    void lowAllowanceLeadsToHighPriceRisk() {
        EstimateCommand command = command(
                "roof_tearoff",
                "contractor",
                "asphalt_shingles",
                18,
                "roof_square",
                false,
                false,
                1.0d,
                "research"
        );

        EstimateResult result = estimationFacade.estimate(command);
        assertThat(result.priceRisk()).isEqualTo(PriceRisk.HIGH);
    }

    @Test
    void urgentTimingPromotesCallCta() {
        EstimateCommand command = command(
                "kitchen_remodel",
                "homeowner",
                "mixed_cd",
                5,
                "pickup_load",
                false,
                false,
                3.0d,
                "48h"
        );

        EstimateResult result = estimationFacade.estimate(command);
        assertThat(result.ctaRouting().primaryCta()).isEqualTo("dumpster_call");
    }

    @Test
    void zipTierOverridesAdjustJunkRemovalCostProfile() {
        EstimateCommand urban = command(
                "garage_cleanout",
                "homeowner",
                "household_junk",
                6,
                "pickup_load",
                false,
                true,
                null,
                "48h",
                "94105"
        );
        EstimateCommand value = command(
                "garage_cleanout",
                "homeowner",
                "household_junk",
                6,
                "pickup_load",
                false,
                true,
                null,
                "48h",
                "58012"
        );

        EstimateResult urbanResult = estimationFacade.estimate(urban);
        EstimateResult valueResult = estimationFacade.estimate(value);

        CostComparisonOption urbanJunk = junkOption(urbanResult);
        CostComparisonOption valueJunk = junkOption(valueResult);

        assertThat(urbanJunk.estimatedTotalCostUsd().typ()).isGreaterThan(valueJunk.estimatedTotalCostUsd().typ());
        assertThat(String.join(" ", urbanJunk.notes())).contains("market tier: urban");
        assertThat(String.join(" ", valueJunk.notes())).contains("market tier: value");
    }

    @Test
    void unmappedZipFallsBackToDefaultNationalTier() {
        EstimateCommand fallback = command(
                "garage_cleanout",
                "homeowner",
                "household_junk",
                4,
                "pickup_load",
                false,
                false,
                null,
                "research",
                "12345"
        );

        EstimateResult result = estimationFacade.estimate(fallback);
        CostComparisonOption junk = junkOption(result);
        assertThat(String.join(" ", junk.notes())).contains("market tier: national");
        assertThat(String.join(" ", junk.notes())).contains("default tier");
    }

    @Test
    void regionalTierProfilesApplyWithDeterministicCostOrdering() {
        EstimateCommand coastal = command(
                "garage_cleanout",
                "homeowner",
                "household_junk",
                6,
                "pickup_load",
                false,
                true,
                null,
                "this_week",
                "07005"
        );
        EstimateCommand mountain = command(
                "garage_cleanout",
                "homeowner",
                "household_junk",
                6,
                "pickup_load",
                false,
                true,
                null,
                "this_week",
                "83702"
        );
        EstimateCommand heartland = command(
                "garage_cleanout",
                "homeowner",
                "household_junk",
                6,
                "pickup_load",
                false,
                true,
                null,
                "this_week",
                "63101"
        );
        EstimateCommand value = command(
                "garage_cleanout",
                "homeowner",
                "household_junk",
                6,
                "pickup_load",
                false,
                true,
                null,
                "this_week",
                "58012"
        );
        EstimateCommand nationalFallback = command(
                "garage_cleanout",
                "homeowner",
                "household_junk",
                6,
                "pickup_load",
                false,
                true,
                null,
                "this_week",
                "12345"
        );

        CostComparisonOption coastalJunk = junkOption(estimationFacade.estimate(coastal));
        CostComparisonOption mountainJunk = junkOption(estimationFacade.estimate(mountain));
        CostComparisonOption heartlandJunk = junkOption(estimationFacade.estimate(heartland));
        CostComparisonOption valueJunk = junkOption(estimationFacade.estimate(value));
        CostComparisonOption nationalJunk = junkOption(estimationFacade.estimate(nationalFallback));

        assertThat(String.join(" ", coastalJunk.notes())).contains("market tier: coastal");
        assertThat(String.join(" ", mountainJunk.notes())).contains("market tier: mountain");
        assertThat(String.join(" ", heartlandJunk.notes())).contains("market tier: heartland");
        assertThat(String.join(" ", valueJunk.notes())).contains("market tier: value");
        assertThat(String.join(" ", nationalJunk.notes())).contains("market tier: national");

        assertThat(coastalJunk.estimatedTotalCostUsd().typ()).isGreaterThan(nationalJunk.estimatedTotalCostUsd().typ());
        assertThat(nationalJunk.estimatedTotalCostUsd().typ()).isGreaterThan(heartlandJunk.estimatedTotalCostUsd().typ());
        assertThat(heartlandJunk.estimatedTotalCostUsd().typ()).isGreaterThan(valueJunk.estimatedTotalCostUsd().typ());
        assertThat(mountainJunk.estimatedTotalCostUsd().typ())
                .isBetween(heartlandJunk.estimatedTotalCostUsd().typ(), nationalJunk.estimatedTotalCostUsd().typ());
    }

    private static EstimateCommand command(
            String projectId,
            String persona,
            String materialId,
            double quantity,
            String unitId,
            boolean wet,
            boolean mixed,
            Double allowanceTons,
            String needTiming
    ) {
        return command(projectId, persona, materialId, quantity, unitId, wet, mixed, allowanceTons, needTiming, null);
    }

    private static EstimateCommand command(
            String projectId,
            String persona,
            String materialId,
            double quantity,
            String unitId,
            boolean wet,
            boolean mixed,
            Double allowanceTons,
            String needTiming,
            String zipCode
    ) {
        EstimateItemInput item = new EstimateItemInput(
                materialId,
                quantity,
                unitId,
                new ItemConditions(wet, mixed, "MEDIUM")
        );
        EstimateOptions options = new EstimateOptions(mixed, allowanceTons, 1.2d, zipCode);
        return new EstimateCommand(projectId, persona, List.of(item), options, needTiming);
    }

    private static CostComparisonOption junkOption(EstimateResult result) {
        return result.costComparison().stream()
                .filter(option -> "junk_removal".equals(option.optionId()))
                .findFirst()
                .orElseThrow();
    }
}

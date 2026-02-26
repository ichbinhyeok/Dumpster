package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateItemInput;
import com.dumpster.calculator.domain.model.EstimateOptions;
import com.dumpster.calculator.domain.model.EstimateResult;
import com.dumpster.calculator.domain.model.ItemConditions;
import com.dumpster.calculator.domain.service.EstimationFacade;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EstimationRegressionScenarioTests {

    @Autowired
    private EstimationFacade estimationFacade;

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("scenarios")
    void runScenarioRegression(
            String scenarioName,
            String projectId,
            String persona,
            String materialId,
            String unitId,
            double quantity,
            boolean wet,
            boolean mixed,
            Double allowance,
            String needTiming,
            boolean mustNotBeOk,
            boolean mustUseAssumedAllowance,
            boolean shouldBeHighRisk
    ) {
        EstimateCommand command = new EstimateCommand(
                projectId,
                persona,
                List.of(new EstimateItemInput(
                        materialId,
                        quantity,
                        unitId,
                        new ItemConditions(wet, mixed, "MEDIUM")
                )),
                new EstimateOptions(mixed, allowance, 1.2d),
                needTiming
        );

        EstimateResult result = estimationFacade.estimate(command);

        assertThat(result.volumeYd3().high()).isGreaterThan(0.0d);
        assertThat(result.weightTons().high()).isGreaterThan(0.0d);
        assertThat(result.recommendations()).isNotEmpty();
        assertThat(result.costComparison()).isNotEmpty();

        if (mustNotBeOk) {
            assertThat(result.feasibility().name()).isNotEqualTo("OK");
        }
        if (mustUseAssumedAllowance) {
            assertThat(result.usedAssumedAllowance()).isTrue();
        } else {
            assertThat(result.usedAssumedAllowance()).isFalse();
        }
        if (shouldBeHighRisk) {
            assertThat(result.priceRisk().name()).isEqualTo("HIGH");
        }
    }

    private static Stream<Arguments> scenarios() {
        return Stream.of(
                // Heavy / must-fail style cases
                row("concrete-large-1", "concrete_removal", "homeowner", "concrete", "sqft_4in", 600, false, false, 2.0, "research", true, false, true),
                row("concrete-large-2", "concrete_removal", "contractor", "concrete", "sqft_4in", 450, false, false, 2.5, "this_week", false, false, true),
                row("dirt-heavy", "yard_cleanup", "contractor", "dirt_soil", "pickup_load", 12, false, true, 2.0, "research", true, false, true),
                row("brick-heavy", "kitchen_remodel", "contractor", "brick", "pickup_load", 10, false, true, 2.0, "research", true, false, true),
                row("tile-heavy", "kitchen_remodel", "homeowner", "tile_ceramic", "pickup_load", 9, false, true, 2.0, "research", true, false, true),
                row("shingle-heavy", "roof_tearoff", "contractor", "asphalt_shingles", "roof_square", 24, false, false, 1.5, "research", true, false, true),

                // allowance assumed cases
                row("mixed-assumed-1", "kitchen_remodel", "homeowner", "mixed_cd", "pickup_load", 5, false, true, null, "research", false, true, false),
                row("mixed-assumed-2", "kitchen_remodel", "business", "household_junk", "pickup_load", 4, false, true, null, "this_week", false, true, false),
                row("yard-assumed-wet", "yard_cleanup", "homeowner", "yard_waste", "pickup_load", 8, true, true, null, "research", false, true, false),
                row("drywall-assumed", "kitchen_remodel", "contractor", "drywall", "drywall_sheet", 40, false, true, null, "research", false, true, false),

                // medium expected cases
                row("mixed-1", "kitchen_remodel", "homeowner", "mixed_cd", "pickup_load", 3, false, true, 3.0, "research", false, false, false),
                row("mixed-2", "kitchen_remodel", "homeowner", "mixed_cd", "pickup_load", 4, false, true, 3.0, "research", false, false, false),
                row("mixed-3", "kitchen_remodel", "contractor", "mixed_cd", "pickup_load", 6, false, true, 3.0, "this_week", false, false, false),
                row("mixed-4", "kitchen_remodel", "business", "mixed_cd", "pickup_load", 7, false, true, 3.0, "research", false, false, false),
                row("junk-1", "yard_cleanup", "homeowner", "household_junk", "pickup_load", 2, false, false, 2.0, "research", false, false, false),
                row("junk-2", "yard_cleanup", "homeowner", "household_junk", "pickup_load", 3, false, true, 2.0, "research", false, false, false),
                row("junk-3", "yard_cleanup", "business", "household_junk", "pickup_load", 5, false, true, 2.5, "this_week", false, false, false),
                row("yard-1", "yard_cleanup", "homeowner", "yard_waste", "pickup_load", 4, false, false, 2.0, "research", false, false, false),
                row("yard-2", "yard_cleanup", "homeowner", "yard_waste", "pickup_load", 6, true, false, 2.0, "research", false, false, false),
                row("yard-3", "yard_cleanup", "contractor", "yard_waste", "pickup_load", 9, true, true, 2.0, "this_week", false, false, false),
                row("roof-1", "roof_tearoff", "contractor", "asphalt_shingles", "roof_square", 10, false, false, 3.0, "this_week", false, false, false),
                row("roof-2", "roof_tearoff", "contractor", "asphalt_shingles", "roof_square", 14, false, false, 3.0, "research", false, false, false),
                row("roof-3", "roof_tearoff", "homeowner", "asphalt_shingles", "roof_square", 16, false, false, 2.0, "research", false, false, true),
                row("drywall-1", "kitchen_remodel", "homeowner", "drywall", "drywall_sheet", 20, false, false, 2.0, "research", false, false, false),
                row("drywall-2", "kitchen_remodel", "contractor", "drywall", "drywall_sheet", 30, false, true, 2.0, "this_week", false, false, false),
                row("lumber-1", "kitchen_remodel", "homeowner", "lumber", "pickup_load", 4, false, false, 2.0, "research", false, false, false),
                row("lumber-2", "kitchen_remodel", "business", "lumber", "pickup_load", 7, false, true, 2.5, "research", false, false, false),
                row("soil-mid", "yard_cleanup", "contractor", "dirt_soil", "pickup_load", 5, false, true, 3.0, "this_week", false, false, false),
                row("concrete-mid", "concrete_removal", "contractor", "concrete", "sqft_4in", 180, false, false, 3.0, "research", false, false, false),
                row("tile-mid", "kitchen_remodel", "homeowner", "tile_ceramic", "pickup_load", 4, false, true, 2.5, "research", false, false, false),
                row("brick-mid", "kitchen_remodel", "homeowner", "brick", "pickup_load", 4, false, true, 2.5, "this_week", false, false, false),
                row("rush-safe", "kitchen_remodel", "homeowner", "mixed_cd", "pickup_load", 2, false, false, 4.0, "48h", false, false, false),
                row("rush-heavy", "concrete_removal", "contractor", "concrete", "sqft_4in", 320, false, false, 2.0, "48h", false, false, true)
        );
    }

    private static Arguments row(
            String scenarioName,
            String projectId,
            String persona,
            String materialId,
            String unitId,
            double quantity,
            boolean wet,
            boolean mixed,
            Double allowance,
            String needTiming,
            boolean mustNotBeOk,
            boolean mustUseAssumedAllowance,
            boolean shouldBeHighRisk
    ) {
        return Arguments.of(
                scenarioName,
                projectId,
                persona,
                materialId,
                unitId,
                quantity,
                wet,
                mixed,
                allowance,
                needTiming,
                mustNotBeOk,
                mustUseAssumedAllowance,
                shouldBeHighRisk
        );
    }
}

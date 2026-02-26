package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateItemInput;
import com.dumpster.calculator.domain.model.EstimateOptions;
import com.dumpster.calculator.domain.model.EstimateResult;
import com.dumpster.calculator.domain.model.ItemConditions;
import com.dumpster.calculator.domain.model.PriceRisk;
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
        EstimateItemInput item = new EstimateItemInput(
                materialId,
                quantity,
                unitId,
                new ItemConditions(wet, mixed, "MEDIUM")
        );
        EstimateOptions options = new EstimateOptions(mixed, allowanceTons, 1.2d);
        return new EstimateCommand(projectId, persona, List.of(item), options, needTiming);
    }
}


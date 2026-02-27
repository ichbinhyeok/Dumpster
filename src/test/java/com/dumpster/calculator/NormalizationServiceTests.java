package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateItemInput;
import com.dumpster.calculator.domain.model.EstimateOptions;
import com.dumpster.calculator.domain.model.ItemConditions;
import com.dumpster.calculator.domain.service.NormalizationService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NormalizationServiceTests {

    @Autowired
    private NormalizationService normalizationService;

    @Test
    void roofSquareWithAsphaltShinglesIsAllowed() {
        EstimateCommand command = new EstimateCommand(
                "roof_tearoff",
                "contractor",
                List.of(new EstimateItemInput(
                        "asphalt_shingles",
                        20,
                        "roof_square",
                        new ItemConditions(false, false, "MEDIUM")
                )),
                new EstimateOptions(false, 2.0d, 1.2d),
                "research"
        );

        assertThat(normalizationService.normalize(command)).isNotEmpty();
    }

    @Test
    void roofSquareWithNonShingleMaterialIsRejected() {
        EstimateCommand command = new EstimateCommand(
                "concrete_removal",
                "homeowner",
                List.of(new EstimateItemInput(
                        "concrete",
                        20,
                        "roof_square",
                        new ItemConditions(false, false, "MEDIUM")
                )),
                new EstimateOptions(false, 2.0d, 1.2d),
                "research"
        );

        assertThatThrownBy(() -> normalizationService.normalize(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("roof_square");
    }
}

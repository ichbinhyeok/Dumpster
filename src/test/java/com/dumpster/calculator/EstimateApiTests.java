package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.api.controller.EstimateApiController;
import com.dumpster.calculator.api.controller.TrackingApiController;
import com.dumpster.calculator.api.dto.EstimateApiResponse;
import com.dumpster.calculator.api.dto.TrackingEventRequest;
import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.EstimateItemInput;
import com.dumpster.calculator.domain.model.EstimateOptions;
import com.dumpster.calculator.domain.model.ItemConditions;
import com.dumpster.calculator.web.controller.CalculatorPageController;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

@SpringBootTest
class EstimateApiTests {

    @Autowired
    private EstimateApiController estimateApiController;

    @Autowired
    private TrackingApiController trackingApiController;

    @Autowired
    private CalculatorPageController calculatorPageController;

    @Test
    void createAndFetchEstimateById() {
        EstimateCommand command = new EstimateCommand(
                "kitchen_remodel",
                "homeowner",
                List.of(new EstimateItemInput(
                        "mixed_cd",
                        6,
                        "pickup_load",
                        new ItemConditions(false, true, "MEDIUM")
                )),
                new EstimateOptions(true, 2.0d, 1.2d),
                "research"
        );

        ResponseEntity<EstimateApiResponse> created = estimateApiController.createEstimate(command);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody()).isNotNull();
        assertThat(created.getBody().estimateId()).isNotBlank();

        ResponseEntity<?> fetched = estimateApiController.getEstimate(created.getBody().estimateId());
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void trackingEndpointAcceptsEvents() {
        ResponseEntity<Map<String, Object>> tracked = trackingApiController.track(new TrackingEventRequest(
                "cta_click_dumpster_call",
                "test-id",
                Map.of("source", "test")
        ));
        assertThat(tracked.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(tracked.getBody()).containsEntry("status", "tracked");
    }

    @Test
    void trackingEndpointAcceptsLeadSubmittedEvent() {
        ResponseEntity<Map<String, Object>> tracked = trackingApiController.track(new TrackingEventRequest(
                "lead_submitted",
                "lead-id",
                Map.of("zipCode", "30339", "contactMethod", "email")
        ));
        assertThat(tracked.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void sharePageSetsNoindexHeader() {
        EstimateCommand command = new EstimateCommand(
                "yard_cleanup",
                "homeowner",
                List.of(new EstimateItemInput(
                        "yard_waste",
                        4,
                        "pickup_load",
                        new ItemConditions(false, false, "MEDIUM")
                )),
                new EstimateOptions(false, null, 1.2d),
                "research"
        );
        ResponseEntity<EstimateApiResponse> created = estimateApiController.createEstimate(command);
        String estimateId = created.getBody().estimateId();

        MockHttpServletResponse response = new MockHttpServletResponse();
        ModelAndView modelAndView = calculatorPageController.shareEstimate(estimateId, response);

        assertThat(response.getHeader("X-Robots-Tag")).contains("noindex");
        assertThat(modelAndView.getViewName()).isEqualTo("calculator/share-estimate");
    }
}

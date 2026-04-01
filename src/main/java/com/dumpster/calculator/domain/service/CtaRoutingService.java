package com.dumpster.calculator.domain.service;

import com.dumpster.calculator.domain.model.CtaRouting;
import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.Feasibility;
import com.dumpster.calculator.domain.model.PriceRisk;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CtaRoutingService {

    public CtaRouting decide(
            EstimateCommand command,
            PriceRisk priceRisk,
            Feasibility feasibility,
            boolean heavyWarning,
            boolean usedAssumedAllowance
    ) {
        List<String> reasons = new ArrayList<>();
        String primary = "dumpster_call";
        String secondary = "junk_call";

        if (feasibility != Feasibility.OK) {
            primary = "junk_call";
            secondary = "dumpster_call";
            reasons.add("operational feasibility is not OK");
        } else if (priceRisk == PriceRisk.HIGH) {
            primary = "junk_call";
            secondary = "dumpster_form";
            reasons.add("high overweight price risk");
        } else if (heavyWarning && usedAssumedAllowance) {
            primary = "dumpster_form";
            secondary = "dumpster_call";
            reasons.add("heavy debris route depends on assumed allowance");
        }

        if (heavyWarning) {
            reasons.add("heavy debris flow active");
        }
        if ("48h".equalsIgnoreCase(command.needTiming())
                && feasibility == Feasibility.OK
                && priceRisk != PriceRisk.HIGH) {
            primary = "dumpster_call";
            secondary = "junk_call";
            reasons.add("urgent timing selected");
        }
        if ("contractor".equalsIgnoreCase(command.persona())) {
            reasons.add("contractor persona selected: checklist/share emphasized");
        }

        if (reasons.isEmpty()) {
            reasons.add("standard routing applied");
        }
        return new CtaRouting(primary, secondary, reasons);
    }
}

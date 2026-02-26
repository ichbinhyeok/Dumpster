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

    public CtaRouting decide(EstimateCommand command, PriceRisk priceRisk, Feasibility feasibility, boolean heavyWarning) {
        List<String> reasons = new ArrayList<>();
        String primary = "dumpster_quote";
        String secondary = "junk_removal";

        if (feasibility != Feasibility.OK) {
            primary = "junk_removal";
            secondary = "dumpster_quote";
            reasons.add("operational feasibility is not OK");
        } else if (priceRisk == PriceRisk.HIGH) {
            primary = "junk_removal";
            secondary = "dumpster_quote";
            reasons.add("high overweight price risk");
        }

        if (heavyWarning) {
            reasons.add("heavy debris flow active");
        }
        if ("48h".equalsIgnoreCase(command.needTiming())
                && feasibility == Feasibility.OK
                && priceRisk != PriceRisk.HIGH) {
            primary = "dumpster_call";
            secondary = "junk_removal";
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

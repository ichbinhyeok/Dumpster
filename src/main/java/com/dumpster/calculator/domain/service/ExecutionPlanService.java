package com.dumpster.calculator.domain.service;

import com.dumpster.calculator.domain.model.EstimateCommand;
import com.dumpster.calculator.domain.model.ExecutionPlan;
import com.dumpster.calculator.domain.model.Feasibility;
import com.dumpster.calculator.domain.model.LineItemEstimate;
import com.dumpster.calculator.domain.model.PriceRisk;
import com.dumpster.calculator.domain.model.RecommendationOption;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExecutionPlanService {

    public ExecutionPlan build(
            EstimateCommand command,
            List<LineItemEstimate> lineItems,
            RecommendationOption safeRecommendation,
            PriceRisk priceRisk,
            Feasibility feasibility,
            boolean usedAssumedAllowance,
            boolean heavyMode
    ) {
        String dominantMaterialId = lineItems.stream()
                .max(Comparator.comparingDouble(item -> item.weightTons().typ()))
                .map(LineItemEstimate::materialId)
                .orElse("mixed_cd");
        String dominantMaterialLabel = humanizeMaterial(dominantMaterialId);

        if (feasibility == Feasibility.NOT_RECOMMENDED || priceRisk == PriceRisk.HIGH) {
            return new ExecutionPlan(
                    "crew_pickup_fallback",
                    dominantMaterialId,
                    dominantMaterialLabel,
                    "Single-load dumpster plan looks weak for " + dominantMaterialLabel.toLowerCase() + ".",
                    "Current haul-limit or overage signals make a one-container plan fragile. Compare crew pickup or split "
                            + dominantMaterialLabel.toLowerCase()
                            + " into smaller staged pulls before you book anything locally.",
                    List.of(
                            "Ask whether this material can be rejected at pickup when the tonnage trends high.",
                            "Compare crew pickup versus staged 10-yard pulls before chasing headline price.",
                            "Do not assume a larger bin fixes dense-load haul limits."
                    )
            );
        }

        if ((safeRecommendation != null && safeRecommendation.multiHaul())
                || feasibility == Feasibility.MULTI_HAUL_REQUIRED) {
            int size = safeRecommendation == null ? 10 : safeRecommendation.sizeYd();
            int haulCount = safeRecommendation == null ? 2 : Math.max(2, safeRecommendation.haulCount());
            return new ExecutionPlan(
                    "staged_multi_haul",
                    dominantMaterialId,
                    dominantMaterialLabel,
                    size + "yd staged-haul plan is safer for " + dominantMaterialLabel.toLowerCase() + ".",
                    "This load should be treated as a controlled heavy-debris workflow, not a generic single-bin rental. "
                            + "Plan " + haulCount + " pulls, controlled fill, and local swap timing before pickup day.",
                    List.of(
                            "Confirm swap or turnaround timing before the first haul is loaded.",
                            "Ask whether heavy fill-line limits or clean-load rules apply to " + dominantMaterialLabel.toLowerCase() + ".",
                            "Keep the load split strategy explicit so dispatch does not assume one full oversized pull."
                    )
            );
        }

        if (heavyMode && usedAssumedAllowance) {
            return new ExecutionPlan(
                    "heavy_assumption_check",
                    dominantMaterialId,
                    dominantMaterialLabel,
                    dominantMaterialLabel + " route looks feasible, but local ton limits still need confirmation.",
                    "The calculator used a typical included-ton assumption. Before local handoff, tighten allowance and "
                            + "heavy-load policy details so a dense " + dominantMaterialLabel.toLowerCase()
                            + " plan does not drift into overage or pickup failure.",
                    List.of(
                            "Replace assumed included tons with the local hauler's real allowance if you have it.",
                            "Confirm whether heavy fill-line or separation rules apply to this material.",
                            "If the operator sounds unsure, treat the load as a staged-haul candidate."
                    )
            );
        }

        if (heavyMode) {
            int size = safeRecommendation == null ? 10 : safeRecommendation.sizeYd();
            return new ExecutionPlan(
                    "heavy_dumpster_route",
                    dominantMaterialId,
                    dominantMaterialLabel,
                    "Local dumpster route looks viable for " + dominantMaterialLabel.toLowerCase() + ".",
                    "Weight, haul-limit, and feasibility signals support a controlled dumpster plan. The next question is "
                            + "which local operator can actually handle a " + size + "-yard "
                            + dominantMaterialLabel.toLowerCase() + " load without surprise policy changes.",
                    List.of(
                            "Confirm included tons and overage fee per ton in the local quote.",
                            "Ask whether fill-line limits are enforced below the rim for this material.",
                            "Check if same-day swap or staged pulls are available if the load trends heavier on site."
                    )
            );
        }

        return new ExecutionPlan(
                "standard_dumpster_route",
                dominantMaterialId,
                dominantMaterialLabel,
                "Dumpster route looks workable under current assumptions.",
                "Volume and weight fit a normal route, so the next step is choosing the best local option for timing, labor, "
                        + "and price instead of re-solving the debris physics.",
                List.of(
                        "Confirm included tons before comparing price alone.",
                        "Check access, labor, and loading pace before picking dumpster versus crew pickup.",
                        "If the material mix changes on site, rerun the estimate before booking."
                )
        );
    }

    private static String humanizeMaterial(String materialId) {
        return switch (materialId) {
            case "dirt_soil" -> "Dirt";
            case "asphalt_shingles" -> "Shingles";
            case "gravel_rock" -> "Gravel";
            case "mixed_cd" -> "Mixed C&D";
            case "tile_ceramic" -> "Tile";
            case "asphalt_pavement" -> "Asphalt";
            case "decking_wood" -> "Decking";
            case "yard_waste" -> "Yard waste";
            default -> {
                String normalized = materialId == null ? "Material" : materialId.replace('_', ' ').trim();
                if (normalized.isEmpty()) {
                    yield "Material";
                }
                yield Character.toUpperCase(normalized.charAt(0)) + normalized.substring(1);
            }
        };
    }
}

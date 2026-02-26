package com.dumpster.calculator.web.content;

import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import com.dumpster.calculator.web.viewmodel.MaterialPageViewModel;
import com.dumpster.calculator.web.viewmodel.ProjectPageViewModel;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SeoContentService {

    private final MaterialFactorRepository materialFactorRepository;
    private final Map<String, ProjectPageViewModel> projectPages = new LinkedHashMap<>();
    private static final List<String> MATERIAL_PRIORITY = List.of(
            "asphalt_shingles",
            "concrete",
            "dirt_soil",
            "brick",
            "tile_ceramic",
            "gravel_rock",
            "asphalt_pavement",
            "drywall",
            "lumber",
            "mixed_cd",
            "household_junk",
            "furniture",
            "carpet_pad",
            "decking_wood",
            "plaster",
            "insulation_wet",
            "yard_waste",
            "green_waste_brush",
            "cardboard_packaging",
            "metal_scrap_light"
    );

    public SeoContentService(MaterialFactorRepository materialFactorRepository) {
        this.materialFactorRepository = materialFactorRepository;
        addProject(
                "roof_tearoff",
                "Dumpster Size for Roof Tear-off",
                "roof_square",
                "asphalt_shingles",
                "Choosing 20yd by volume alone can trigger overweight fees.",
                "Use weight-first sizing and check clean-load requirements.",
                "What max haul tons apply for shingle loads in my market?"
        );
        addProject(
                "kitchen_remodel",
                "Dumpster Size for Kitchen Remodel",
                "pickup_load",
                "mixed_cd",
                "Underestimating cabinet and countertop weight is common.",
                "Pick safe size first, then compare budget option if risk is low.",
                "What included tons and overage rates apply to mixed C&D?"
        );
        addProject(
                "bathroom_remodel",
                "Dumpster Size for Bathroom Remodel",
                "pickup_load",
                "tile_ceramic",
                "Tile and mortar can turn a small cleanup into a heavy load.",
                "If tile share is high, use smaller bins with staged hauling.",
                "Do you allow mixed tile and drywall in one container?"
        );
        addProject(
                "deck_demolition",
                "Dumpster Size for Deck Demolition",
                "pickup_load",
                "decking_wood",
                "Ignoring nails, railings, and wet lumber inflates risk.",
                "Estimate with mixed-load bulking and weather adjustment.",
                "Is treated lumber accepted and charged at standard rates?"
        );
        addProject(
                "garage_cleanout",
                "Dumpster Size for Garage Cleanout",
                "pickup_load",
                "household_junk",
                "Volume looks small until furniture and bulky items stack poorly.",
                "Choose risk-aware sizing with mixed-load inefficiency enabled.",
                "Are appliances or e-waste surcharges applied separately?"
        );
        addProject(
                "estate_cleanout",
                "Dumpster Size for Estate Cleanout",
                "pickup_load",
                "household_junk",
                "One-trip assumptions fail when bulky items and recyclables mix.",
                "Plan for two-stage loading or compare junk removal routing.",
                "Can partial pulls be scheduled within the same rental window?"
        );
        addProject(
                "yard_cleanup",
                "Dumpster Size for Yard Cleanup",
                "pickup_load",
                "yard_waste",
                "Wet yard waste can spike weight quickly after rain.",
                "Toggle wet-load assumptions before selecting budget option.",
                "Do wet green waste loads have weight caps or special rules?"
        );
        addProject(
                "dirt_grading",
                "Dumpster Strategy for Dirt and Grading Debris",
                "sqft_4in",
                "dirt_soil",
                "Large bins can become operationally infeasible for dense soil.",
                "Use low fill ratio and multi-haul planning from the start.",
                "What is the per-container haul cap for soil and rock?"
        );
        addProject(
                "concrete_removal",
                "Dumpster Strategy for Concrete Removal",
                "sqft_4in",
                "concrete",
                "Large bins are often not feasible for heavy concrete loads.",
                "Default to small container strategy with explicit haul count.",
                "Do you require clean concrete-only loads for pickup?"
        );
        addProject(
                "light_commercial_fitout",
                "Dumpster Plan for Light Commercial Fit-out",
                "pickup_load",
                "mixed_cd",
                "Mixed materials hide risk until final day rush loading.",
                "Use safe recommendation for timeline-critical jobs.",
                "Can you confirm included tons and same-day swap availability?"
        );
    }

    public Optional<MaterialPageViewModel> materialPage(String materialId) {
        return materialFactorRepository.findById(materialId)
                .map(material -> {
                    double exampleVolume = material.category().name().equals("HEAVY") ? 4.0d : 8.0d;
                    double typWeight = (exampleVolume * material.densityTyp()) / 2000.0d;
                    String categoryLabel = switch (material.category().name()) {
                        case "HEAVY" -> "Heavy debris";
                        case "MIXED" -> "Mixed debris";
                        default -> "Light debris";
                    };
                    String cautionNote = switch (material.category().name()) {
                        case "HEAVY" -> "High density can hit haul limits before the container is visually full.";
                        case "MIXED" -> "Packing inefficiency can increase required volume by 10% to 20%.";
                        default -> "Moisture swings can widen the expected weight range.";
                    };
                    String operatorQuestion = switch (material.category().name()) {
                        case "HEAVY" -> "What is the operational haul cap for this heavy material?";
                        case "MIXED" -> "Are mixed loads billed with additional sorting or contamination fees?";
                        default -> "Do wet loads have seasonal restrictions or surcharge rules?";
                    };
                    return new MaterialPageViewModel(
                            material.materialId(),
                            material.name() + " Dumpster Weight Guide",
                            categoryLabel,
                            material.densityLow(),
                            material.densityTyp(),
                            material.densityHigh(),
                            exampleVolume,
                            Math.round(typWeight * 100.0d) / 100.0d,
                            material.source(),
                            cautionNote,
                            operatorQuestion
                    );
                });
    }

    public Optional<ProjectPageViewModel> projectPage(String projectId) {
        return Optional.ofNullable(projectPages.get(projectId));
    }

    public Map<String, ProjectPageViewModel> projectPages() {
        return projectPages;
    }

    public List<String> indexableMaterialIds() {
        Map<String, Integer> priorityRank = new LinkedHashMap<>();
        for (int i = 0; i < MATERIAL_PRIORITY.size(); i++) {
            priorityRank.put(MATERIAL_PRIORITY.get(i), i);
        }
        return materialFactorRepository.findAll().stream()
                .map(material -> material.materialId())
                .sorted(Comparator
                        .comparingInt((String id) -> priorityRank.getOrDefault(id, Integer.MAX_VALUE))
                        .thenComparing(id -> id))
                .limit(20)
                .toList();
    }

    private void addProject(
            String projectId,
            String title,
            String recommendedUnit,
            String defaultMaterialId,
            String commonMistake,
            String recommendedStrategy,
            String operatorQuestion
    ) {
        projectPages.put(projectId, new ProjectPageViewModel(
                projectId,
                title,
                recommendedUnit,
                defaultMaterialId,
                commonMistake,
                recommendedStrategy,
                operatorQuestion,
                "/dumpster/size/" + projectId
        ));
    }
}

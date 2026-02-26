package com.dumpster.calculator.web.content;

import com.dumpster.calculator.domain.reference.MaterialCategory;
import com.dumpster.calculator.domain.reference.MaterialFactor;
import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import com.dumpster.calculator.web.viewmodel.LinkItemViewModel;
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
    private final Map<String, ProjectSeed> projectSeeds = new LinkedHashMap<>();
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
    private static final String CALCULATOR_PATH = "/dumpster/size-weight-calculator";
    private static final String MATERIAL_GUIDES_PATH = "/dumpster/material-guides";
    private static final String PROJECT_GUIDES_PATH = "/dumpster/project-guides";
    private static final String HEAVY_RULES_PATH = "/dumpster/heavy-debris-rules";

    public SeoContentService(MaterialFactorRepository materialFactorRepository) {
        this.materialFactorRepository = materialFactorRepository;
        addProject(
                "roof_tearoff",
                "Dumpster Size for Roof Tear-off",
                "roof_square",
                "asphalt_shingles",
                "Choosing 20yd by volume alone can trigger overweight fees.",
                "Use weight-first sizing and check clean-load requirements.",
                "What max haul tons apply for shingle loads in my market?",
                "Input 22 roof squares with asphalt shingles and compare 15yd vs 20yd risk.",
                "Even when volume looks close, weight can force a safer or split-haul strategy."
        );
        addProject(
                "kitchen_remodel",
                "Dumpster Size for Kitchen Remodel",
                "pickup_load",
                "mixed_cd",
                "Underestimating cabinet and countertop weight is common.",
                "Pick safe size first, then compare budget option if risk is low.",
                "What included tons and overage rates apply to mixed C&D?",
                "Input 5 pickup loads with mixed C&D and enable mixed-load bulking.",
                "Budget option is viable only when overage exposure stays acceptable."
        );
        addProject(
                "bathroom_remodel",
                "Dumpster Size for Bathroom Remodel",
                "pickup_load",
                "tile_ceramic",
                "Tile and mortar can turn a small cleanup into a heavy load.",
                "If tile share is high, use smaller bins with staged hauling.",
                "Do you allow mixed tile and drywall in one container?",
                "Input 4 pickup loads where tile is dominant and compare haul feasibility.",
                "Tile-dominant jobs often need weight-first planning over pure cubic volume."
        );
        addProject(
                "deck_demolition",
                "Dumpster Size for Deck Demolition",
                "pickup_load",
                "decking_wood",
                "Ignoring nails, railings, and wet lumber inflates risk.",
                "Estimate with mixed-load bulking and weather adjustment.",
                "Is treated lumber accepted and charged at standard rates?",
                "Input 7 pickup loads for decking wood with wet toggle after rain.",
                "Moisture and mixed hardware can move the job from budget to safe recommendation."
        );
        addProject(
                "garage_cleanout",
                "Dumpster Size for Garage Cleanout",
                "pickup_load",
                "household_junk",
                "Volume looks small until furniture and bulky items stack poorly.",
                "Choose risk-aware sizing with mixed-load inefficiency enabled.",
                "Are appliances or e-waste surcharges applied separately?",
                "Input 6 pickup loads with furniture and household junk mixed together.",
                "Stack inefficiency can require more volume than visual estimates suggest."
        );
        addProject(
                "estate_cleanout",
                "Dumpster Size for Estate Cleanout",
                "pickup_load",
                "household_junk",
                "One-trip assumptions fail when bulky items and recyclables mix.",
                "Plan for two-stage loading or compare junk removal routing.",
                "Can partial pulls be scheduled within the same rental window?",
                "Input 10 pickup loads and evaluate dumpster vs junk-removal handoff.",
                "Large mixed cleanouts often convert better with a split strategy."
        );
        addProject(
                "yard_cleanup",
                "Dumpster Size for Yard Cleanup",
                "pickup_load",
                "yard_waste",
                "Wet yard waste can spike weight quickly after rain.",
                "Toggle wet-load assumptions before selecting budget option.",
                "Do wet green waste loads have weight caps or special rules?",
                "Input 8 pickup loads of yard waste and compare dry vs wet assumptions.",
                "Moisture variance is the main reason risk bands widen for green waste."
        );
        addProject(
                "dirt_grading",
                "Dumpster Strategy for Dirt and Grading Debris",
                "sqft_4in",
                "dirt_soil",
                "Large bins can become operationally infeasible for dense soil.",
                "Use low fill ratio and multi-haul planning from the start.",
                "What is the per-container haul cap for soil and rock?",
                "Input 240 sqft at 4in for dirt/soil and check feasibility before booking.",
                "For dense soil, transport constraints dominate container size decisions."
        );
        addProject(
                "concrete_removal",
                "Dumpster Strategy for Concrete Removal",
                "sqft_4in",
                "concrete",
                "Large bins are often not feasible for heavy concrete loads.",
                "Default to small container strategy with explicit haul count.",
                "Do you require clean concrete-only loads for pickup?",
                "Input 180 sqft at 4in for concrete and validate multi-haul requirement.",
                "Concrete usually reaches haul limits long before volume capacity."
        );
        addProject(
                "light_commercial_fitout",
                "Dumpster Plan for Light Commercial Fit-out",
                "pickup_load",
                "mixed_cd",
                "Mixed materials hide risk until final day rush loading.",
                "Use safe recommendation for timeline-critical jobs.",
                "Can you confirm included tons and same-day swap availability?",
                "Input 9 pickup loads for mixed fit-out debris under tight schedule.",
                "Schedule risk plus mixed debris usually favors safer allowance margin."
        );
    }

    public Optional<MaterialPageViewModel> materialPage(String materialId, String baseUrl) {
        return materialFactorRepository.findById(materialId)
                .map(material -> {
                    double exampleVolume = material.category() == MaterialCategory.HEAVY ? 4.0d : 8.0d;
                    double lowWeight = (exampleVolume * material.densityLow()) / 2000.0d;
                    double typWeight = (exampleVolume * material.densityTyp()) / 2000.0d;
                    double highWeight = (exampleVolume * material.densityHigh()) / 2000.0d;
                    String categoryLabel = switch (material.category()) {
                        case HEAVY -> "Heavy debris";
                        case MIXED -> "Mixed debris";
                        default -> "Light debris";
                    };
                    String cautionNote = switch (material.category()) {
                        case HEAVY -> "High density can hit haul limits before the container is visually full.";
                        case MIXED -> "Packing inefficiency can increase required volume by 10% to 20%.";
                        default -> "Moisture swings can widen the expected weight range.";
                    };
                    String operatorQuestion = switch (material.category()) {
                        case HEAVY -> "What is the operational haul cap for this heavy material?";
                        case MIXED -> "Are mixed loads billed with additional sorting or contamination fees?";
                        default -> "Do wet loads have seasonal restrictions or surcharge rules?";
                    };
                    MaterialScenario scenario = materialScenario(material);
                    return new MaterialPageViewModel(
                            material.materialId(),
                            material.name() + " Dumpster Weight Guide",
                            absoluteUrl(baseUrl, "/dumpster/weight/" + material.materialId()),
                            categoryLabel,
                            material.densityLow(),
                            material.densityTyp(),
                            material.densityHigh(),
                            exampleVolume,
                            round2(lowWeight),
                            Math.round(typWeight * 100.0d) / 100.0d,
                            round2(highWeight),
                            material.source(),
                            cautionNote,
                            operatorQuestion,
                            scenario.input(),
                            scenario.decision(),
                            absoluteUrl(baseUrl, MATERIAL_GUIDES_PATH),
                            relatedProjectsForMaterial(material.materialId())
                    );
                });
    }

    public Optional<ProjectPageViewModel> projectPage(String projectId, String baseUrl) {
        ProjectSeed seed = projectSeeds.get(projectId);
        if (seed == null) {
            return Optional.empty();
        }
        String canonicalPath = "/dumpster/size/" + seed.projectId();
        return Optional.of(new ProjectPageViewModel(
                seed.projectId(),
                seed.title(),
                absoluteUrl(baseUrl, canonicalPath),
                seed.recommendedUnit(),
                seed.defaultMaterialId(),
                seed.commonMistake(),
                seed.recommendedStrategy(),
                seed.operatorQuestion(),
                canonicalPath,
                seed.sampleInput(),
                seed.sampleDecision(),
                absoluteUrl(baseUrl, PROJECT_GUIDES_PATH),
                relatedMaterialsForProject(seed)
        ));
    }

    public List<String> projectIndexPaths() {
        return projectSeeds.values().stream()
                .map(seed -> "/dumpster/size/" + seed.projectId())
                .toList();
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

    public List<LinkItemViewModel> featuredMaterialLinks(int limit) {
        return sortedIndexableMaterials().stream()
                .limit(limit)
                .map(material -> new LinkItemViewModel(
                        "/dumpster/weight/" + material.materialId(),
                        material.name() + " weight guide",
                        material.category().name().toLowerCase() + " debris profile with tonnage range"
                ))
                .toList();
    }

    public List<LinkItemViewModel> featuredProjectLinks(int limit) {
        return projectSeeds.values().stream()
                .limit(limit)
                .map(seed -> new LinkItemViewModel(
                        "/dumpster/size/" + seed.projectId(),
                        seed.title(),
                        seed.sampleInput()
                ))
                .toList();
    }

    public List<LinkItemViewModel> materialGuideLinks() {
        return sortedIndexableMaterials().stream()
                .map(material -> new LinkItemViewModel(
                        "/dumpster/weight/" + material.materialId(),
                        material.name() + " weight guide",
                        material.category().name().toLowerCase() + " load behavior and decision notes"
                ))
                .toList();
    }

    public List<LinkItemViewModel> projectGuideLinks() {
        return projectSeeds.values().stream()
                .map(seed -> new LinkItemViewModel(
                        "/dumpster/size/" + seed.projectId(),
                        seed.title(),
                        seed.sampleDecision()
                ))
                .toList();
    }

    public String calculatorUrl(String baseUrl) {
        return absoluteUrl(baseUrl, CALCULATOR_PATH);
    }

    public String materialGuidesUrl(String baseUrl) {
        return absoluteUrl(baseUrl, MATERIAL_GUIDES_PATH);
    }

    public String projectGuidesUrl(String baseUrl) {
        return absoluteUrl(baseUrl, PROJECT_GUIDES_PATH);
    }

    public String heavyRulesUrl(String baseUrl) {
        return absoluteUrl(baseUrl, HEAVY_RULES_PATH);
    }

    private void addProject(
            String projectId,
            String title,
            String recommendedUnit,
            String defaultMaterialId,
            String commonMistake,
            String recommendedStrategy,
            String operatorQuestion,
            String sampleInput,
            String sampleDecision
    ) {
        projectSeeds.put(projectId, new ProjectSeed(
                projectId,
                title,
                recommendedUnit,
                defaultMaterialId,
                commonMistake,
                recommendedStrategy,
                operatorQuestion,
                sampleInput,
                sampleDecision
        ));
    }

    private List<MaterialFactor> sortedIndexableMaterials() {
        Map<String, Integer> priorityRank = new LinkedHashMap<>();
        for (int i = 0; i < MATERIAL_PRIORITY.size(); i++) {
            priorityRank.put(MATERIAL_PRIORITY.get(i), i);
        }
        return materialFactorRepository.findAll().stream()
                .sorted(Comparator
                        .comparingInt((MaterialFactor material) -> priorityRank.getOrDefault(material.materialId(), Integer.MAX_VALUE))
                        .thenComparing(material -> material.materialId()))
                .limit(20)
                .toList();
    }

    private List<LinkItemViewModel> relatedProjectsForMaterial(String materialId) {
        List<LinkItemViewModel> directMatches = projectSeeds.values().stream()
                .filter(seed -> seed.defaultMaterialId().equals(materialId))
                .map(seed -> new LinkItemViewModel(
                        "/dumpster/size/" + seed.projectId(),
                        seed.title(),
                        seed.sampleDecision()
                ))
                .toList();
        if (!directMatches.isEmpty()) {
            return directMatches;
        }
        List<String> fallback = switch (materialId) {
            case "concrete", "brick", "tile_ceramic", "dirt_soil", "gravel_rock", "asphalt_pavement", "metal_scrap_light" ->
                    List.of("concrete_removal", "dirt_grading", "roof_tearoff");
            case "yard_waste", "green_waste_brush", "cardboard_packaging" ->
                    List.of("yard_cleanup", "estate_cleanout", "garage_cleanout");
            default -> List.of("kitchen_remodel", "bathroom_remodel", "light_commercial_fitout");
        };
        return fallback.stream()
                .map(projectSeeds::get)
                .filter(seed -> seed != null)
                .map(seed -> new LinkItemViewModel(
                        "/dumpster/size/" + seed.projectId(),
                        seed.title(),
                        seed.sampleDecision()
                ))
                .toList();
    }

    private List<LinkItemViewModel> relatedMaterialsForProject(ProjectSeed seed) {
        List<MaterialFactor> materials = sortedIndexableMaterials();
        List<LinkItemViewModel> links = materials.stream()
                .filter(material -> material.materialId().equals(seed.defaultMaterialId()))
                .map(material -> new LinkItemViewModel(
                        "/dumpster/weight/" + material.materialId(),
                        material.name() + " weight guide",
                        "Primary material profile for this project"
                ))
                .toList();
        if (!links.isEmpty()) {
            return links;
        }
        return materials.stream()
                .limit(3)
                .map(material -> new LinkItemViewModel(
                        "/dumpster/weight/" + material.materialId(),
                        material.name() + " weight guide",
                        "Compare against this material profile"
                ))
                .toList();
    }

    private MaterialScenario materialScenario(MaterialFactor material) {
        return switch (material.materialId()) {
            case "asphalt_shingles" -> new MaterialScenario(
                    "Scenario: 20 to 24 roof squares, dry shingle tear-off.",
                    "Treat shingles as weight-first. Confirm haul cap before choosing larger bins."
            );
            case "concrete" -> new MaterialScenario(
                    "Scenario: 150 to 220 sqft at 4in slab removal.",
                    "Concrete typically requires small bins with explicit multi-haul planning."
            );
            case "dirt_soil" -> new MaterialScenario(
                    "Scenario: grading cleanup with dense soil and occasional rock.",
                    "Use low fill ratio and check operator max-haul limits first."
            );
            case "yard_waste", "green_waste_brush" -> new MaterialScenario(
                    "Scenario: post-rain yard cleanup with mixed green waste.",
                    "Run dry and wet assumptions. Moisture can shift risk tier quickly."
            );
            case "insulation_wet" -> new MaterialScenario(
                    "Scenario: attic tear-out with damp insulation bags.",
                    "Density variance is wide. Keep a conservative allowance margin."
            );
            default -> new MaterialScenario(
                    "Scenario: mixed load where this material is the dominant component.",
                    "Use safe recommendation when timeline or overage tolerance is tight."
            );
        };
    }

    private static String absoluteUrl(String baseUrl, String path) {
        String safeBase = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        return safeBase + path;
    }

    private static double round2(double value) {
        return Math.round(value * 100.0d) / 100.0d;
    }

    private record ProjectSeed(
            String projectId,
            String title,
            String recommendedUnit,
            String defaultMaterialId,
            String commonMistake,
            String recommendedStrategy,
            String operatorQuestion,
            String sampleInput,
            String sampleDecision
    ) {
    }

    private record MaterialScenario(
            String input,
            String decision
    ) {
    }
}

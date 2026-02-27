package com.dumpster.calculator.web.content;

import com.dumpster.calculator.domain.reference.MaterialCategory;
import com.dumpster.calculator.domain.reference.MaterialFactor;
import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import com.dumpster.calculator.web.viewmodel.FaqItemViewModel;
import com.dumpster.calculator.web.viewmodel.LinkItemViewModel;
import com.dumpster.calculator.web.viewmodel.MaterialPageViewModel;
import com.dumpster.calculator.web.viewmodel.ProjectPageViewModel;
import java.time.LocalDate;
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
    private static final Map<String, CopyBlock> MATERIAL_COPY = buildMaterialCopy();
    private static final Map<String, CopyBlock> PROJECT_COPY = buildProjectCopy();

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
                    CopyBlock copy = materialCopyFor(material);
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
                            copy.answerFirst(),
                            copy.quickRules(),
                            copy.faqItems(),
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
        CopyBlock copy = projectCopyFor(seed);
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
                copy.answerFirst(),
                copy.quickRules(),
                copy.faqItems(),
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

    public String materialAnswerFirst(String materialId) {
        return materialFactorRepository.findById(materialId)
                .map(this::materialCopyFor)
                .map(CopyBlock::answerFirst)
                .orElse(defaultMaterialCopy().answerFirst());
    }

    public List<String> materialQuickRules(String materialId) {
        return materialFactorRepository.findById(materialId)
                .map(this::materialCopyFor)
                .map(CopyBlock::quickRules)
                .orElse(defaultMaterialCopy().quickRules());
    }

    public List<FaqItemViewModel> materialFaq(String materialId) {
        return materialFactorRepository.findById(materialId)
                .map(this::materialCopyFor)
                .map(CopyBlock::faqItems)
                .orElse(defaultMaterialCopy().faqItems());
    }

    public String projectAnswerFirst(String projectId) {
        ProjectSeed seed = projectSeeds.get(projectId);
        return projectCopyFor(seed).answerFirst();
    }

    public List<String> projectQuickRules(String projectId) {
        ProjectSeed seed = projectSeeds.get(projectId);
        return projectCopyFor(seed).quickRules();
    }

    public List<FaqItemViewModel> projectFaq(String projectId) {
        ProjectSeed seed = projectSeeds.get(projectId);
        return projectCopyFor(seed).faqItems();
    }

    public LocalDate materialLastModifiedDate(String materialId) {
        return materialFactorRepository.findById(materialId)
                .map(material -> material.sourceVersionDate() == null ? LocalDate.now() : material.sourceVersionDate())
                .orElse(LocalDate.now());
    }

    public LocalDate defaultLastModifiedDate() {
        return LocalDate.now();
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

    private CopyBlock materialCopyFor(MaterialFactor material) {
        CopyBlock exact = MATERIAL_COPY.get(material.materialId());
        if (exact != null) {
            return exact;
        }
        return switch (material.category()) {
            case HEAVY -> new CopyBlock(
                    "This material behaves as heavy debris, so haul limits are usually the first constraint. Use a conservative fill plan and verify max haul tons before booking.",
                    List.of(
                            "Size by haul constraints first, then container volume.",
                            "Confirm if clean-load separation is required.",
                            "Plan multi-haul if high-range tons approach haul caps."
                    ),
                    List.of(
                            faq("Why can heavy loads fail even in larger dumpsters?", "Haul limits can be reached before visual volume is full."),
                            faq("Do I need clean-load separation?", "Many operators require separation for dense heavy debris."),
                            faq("How do I reduce fee risk?", "Use conservative fill and validate included tons before loading.")
                    )
            );
            case MIXED -> new CopyBlock(
                    "Mixed loads vary widely by composition and packing. Use the safe recommendation when schedule risk is high or the heavy portion is uncertain.",
                    List.of(
                            "Separate dense debris from mixed loads whenever possible.",
                            "Account for packing inefficiency in volume planning.",
                            "Use safe sizing when timeline certainty matters."
                    ),
                    List.of(
                            faq("Why is mixed C and D less predictable?", "Density swings with what is actually inside the load."),
                            faq("Can mixed loads increase cost risk?", "Yes, dense items can push tonnage past included limits."),
                            faq("What is the safest approach?", "Split heavy components and keep an allowance margin.")
                    )
            );
            case LIGHT -> new CopyBlock(
                    "Light debris is usually volume-driven, but moisture and awkward packing still affect final hauling outcomes. Validate load conditions before choosing the smallest option.",
                    List.of(
                            "Run dry and wet assumptions before final sizing.",
                            "Avoid overfilling based on visual estimates only.",
                            "Confirm local restrictions for green or seasonal waste."
                    ),
                    List.of(
                            faq("Is light debris always low risk?", "Not always, moisture and packing can still trigger surprises."),
                            faq("Does weather matter?", "Yes, rain can shift weights and risk bands."),
                            faq("How can I avoid last-minute changes?", "Use conservative assumptions when timing is tight.")
                    )
            );
        };
    }

    private CopyBlock projectCopyFor(ProjectSeed seed) {
        if (seed == null) {
            return defaultProjectCopy();
        }
        return PROJECT_COPY.getOrDefault(seed.projectId(), defaultProjectCopy());
    }

    private static CopyBlock defaultMaterialCopy() {
        return new CopyBlock(
                "Use this material profile as a weight-first planning guide to reduce overage and haul-limit surprises.",
                List.of(
                        "Check included tons and max haul tons separately.",
                        "Use conservative assumptions for wet or mixed loads.",
                        "When uncertain, pick the safe recommendation."
                ),
                List.of(
                        faq("Why use range-based planning?", "Real loads vary with moisture, packing, and contamination."),
                        faq("Can larger bins still be risky?", "Yes, haul rules can bind before volume limits."),
                        faq("What should I ask operators first?", "Included tons, overage fee, and heavy-load rules.")
                )
        );
    }

    private static CopyBlock defaultProjectCopy() {
        return new CopyBlock(
                "Start with a safe recommendation and validate hauling constraints before choosing a lower-cost option.",
                List.of(
                        "Use project presets to reduce conversion mistakes.",
                        "Verify overage policy before loading starts.",
                        "Prioritize safe sizing on schedule-critical jobs."
                ),
                List.of(
                        faq("How should I pick between safe and budget options?", "Use budget only when overage tolerance is high."),
                        faq("What creates the biggest surprises?", "Mixed dense debris and timeline pressure near pickup day."),
                        faq("What is the fastest way to reduce risk?", "Confirm haul limits and plan swap logistics early.")
                )
        );
    }

    private static Map<String, CopyBlock> buildMaterialCopy() {
        Map<String, CopyBlock> copy = new LinkedHashMap<>();
        copy.put("asphalt_shingles", new CopyBlock(
                "Asphalt shingles are weight-first debris. A full load can hit haul limits before the dumpster looks full, so smaller bins or multi-haul plans are often safer.",
                List.of(
                        "Check max haul tons before selecting a larger bin.",
                        "Treat wet shingles as higher risk than dry loads.",
                        "Do not mix shingles with light debris unless approved."
                ),
                List.of(
                        faq("Why can a bigger dumpster still fail for shingles?", "Haul caps can bind before the visible fill reaches the top."),
                        faq("How full should shingle loads be?", "Follow heavy-debris fill rules and local operator limits."),
                        faq("What is the most common mistake?", "Sizing by volume only and ignoring wet-load weight.")
                )
        ));
        copy.put("concrete", new CopyBlock(
                "Concrete is one of the heaviest debris categories. Most jobs require conservative fill and explicit multi-haul planning.",
                List.of(
                        "Do not plan to load concrete to the rim.",
                        "Use slab area and thickness as the primary input.",
                        "Confirm clean-load requirements up front."
                ),
                List.of(
                        faq("Can I use a larger bin for concrete?", "Volume may fit, but haul constraints often do not."),
                        faq("Do concrete jobs need dedicated loads?", "Many operators require concrete-only containers."),
                        faq("What causes pickup-day failure?", "Volume-first planning that ignores hauling limits.")
                )
        ));
        copy.put("dirt_soil", new CopyBlock(
                "Dirt and soil are dense and moisture-sensitive. Haul caps usually constrain the job before volume capacity.",
                List.of(
                        "Treat soil as a haul-limited material.",
                        "Raise assumptions when material is wet or rocky.",
                        "Plan multi-haul when high-range tons are near cap."
                ),
                List.of(
                        faq("Why are soil jobs often multi-haul?", "Dense loads hit transport caps quickly."),
                        faq("Does moisture change the plan?", "Yes, wet soil can materially increase tonnage."),
                        faq("Should I mix soil with other debris?", "Avoid mixing unless pricing and rules are clear.")
                )
        ));
        copy.put("brick", new CopyBlock(
                "Brick and masonry behave as heavy debris with strict hauling constraints. Safe plans usually use controlled fill and separation checks.",
                List.of(
                        "Verify heavy-debris haul limits before booking.",
                        "Assume mortar residue increases effective density.",
                        "Check clean-load rules to avoid rejections."
                ),
                List.of(
                        faq("Can 20-yard bins work for brick?", "Sometimes by volume, but haul limits often force staged pulls."),
                        faq("How should masonry be loaded?", "Evenly, conservatively, and within operator fill limits."),
                        faq("When is multi-haul needed?", "When estimated high-range tons approach haul caps.")
                )
        ));
        copy.put("tile_ceramic", new CopyBlock(
                "Tile and ceramic demo can be deceptively heavy due to mortar and thinset. Weight-first sizing prevents avoidable overage.",
                List.of(
                        "Use conservative assumptions when mortar is attached.",
                        "Prefer smaller staged loads when tile dominates.",
                        "Confirm mixed-load acceptance with drywall or wood."
                ),
                List.of(
                        faq("Why do tile jobs get overweight surprises?", "Mortar and dense fragments raise tonnage faster than expected."),
                        faq("Can tile be mixed with other debris?", "Rules vary, ask before mixing."),
                        faq("What input is most reliable?", "Use measured quantity and treat tile-heavy loads conservatively.")
                )
        ));
        copy.put("gravel_rock", new CopyBlock(
                "Gravel and rock are dense enough that haul limits usually dominate. Plan around controlled fill and swap logistics.",
                List.of(
                        "Optimize for haul constraints, not nominal volume.",
                        "Assume heavier output when mixed with soil.",
                        "Confirm local rules for crushed rock disposal."
                ),
                List.of(
                        faq("Can rock loads be filled to the top?", "Usually no, dense loads reach haul caps first."),
                        faq("Does rock size matter?", "Yes, composition and packing shift effective density."),
                        faq("What is the safest booking pattern?", "Smaller bins with planned multi-haul.")
                )
        ));
        copy.put("asphalt_pavement", new CopyBlock(
                "Asphalt pavement behaves similarly to other heavy debris classes. Conservative fill and haul-cap checks are required for reliable pickup.",
                List.of(
                        "Assume higher density for thick pavement chunks.",
                        "Avoid mixing with light debris unless priced accordingly.",
                        "Confirm clean-load and disposal constraints."
                ),
                List.of(
                        faq("Is asphalt treated like concrete?", "Operationally often yes, both are haul-limited."),
                        faq("Can large bins still require multiple pulls?", "Yes, haul caps can force staged hauling."),
                        faq("How can I minimize overage?", "Use weight-first sizing and stay below heavy-load fill caps.")
                )
        ));
        copy.put("drywall", new CopyBlock(
                "Drywall is typically mixed debris, but moisture can materially widen weight risk. Range-based planning is safer than visual estimates.",
                List.of(
                        "Assume higher tonnage for wet drywall loads.",
                        "Account for mixed-load packing inefficiency.",
                        "Use safe sizing if included tons are uncertain."
                ),
                List.of(
                        faq("Why does drywall trigger overages?", "High volume and moisture can push loads past included tons."),
                        faq("Can drywall be mixed with wood?", "Often yes, but pricing and contamination rules vary."),
                        faq("How to reduce last-day risk?", "Avoid saturated loads and keep a margin on allowance.")
                )
        ));
        copy.put("lumber", new CopyBlock(
                "Lumber is often volume-driven, but wet wood and hardware can still increase weight beyond expectations.",
                List.of(
                        "Include nails, brackets, and connectors in estimates.",
                        "Increase assumptions for wet or treated lumber.",
                        "Use safe sizing when loading density is uncertain."
                ),
                List.of(
                        faq("Is lumber heavy debris?", "Usually not, but wet or mixed hardware loads can rise in risk."),
                        faq("Do fasteners matter?", "Yes, they affect both weight and packing behavior."),
                        faq("What is commonly underestimated?", "Bulky stacking inefficiency in mixed wood demo.")
                )
        ));
        copy.put("mixed_cd", new CopyBlock(
                "Mixed construction debris is composition-sensitive and can swing quickly from budget to high risk. Safe recommendations are usually better for uncertain loads.",
                List.of(
                        "Separate dense components whenever possible.",
                        "Enable mixed-load assumptions for realistic volume.",
                        "Use safe mode on tight timelines."
                ),
                List.of(
                        faq("Why is mixed C and D unpredictable?", "Actual density depends on the heaviest components in the mix."),
                        faq("Can separation reduce cost risk?", "Yes, isolating heavy debris stabilizes pricing and feasibility."),
                        faq("Which operator details matter most?", "Included tons, overage rates, and mixed-load restrictions.")
                )
        ));
        return copy;
    }

    private static Map<String, CopyBlock> buildProjectCopy() {
        Map<String, CopyBlock> copy = new LinkedHashMap<>();
        copy.put("roof_tearoff", new CopyBlock(
                "Roof tear-off decisions should be weight-first. Even when volume appears safe, shingle loads often hit hauling limits first.",
                List.of(
                        "Verify haul limits specific to shingle loads.",
                        "Treat multi-layer or wet roofs as high-risk loads.",
                        "Plan swap logistics before tear-off starts."
                ),
                List.of(
                        faq("Why can a 20-yard roof load still fail?", "Shingle density can exceed haul caps before the bin is full."),
                        faq("When should I plan multiple pulls?", "When high-range tons approach local haul limits."),
                        faq("What should I ask first?", "Ask max haul tons and clean-load rules for shingles.")
                )
        ));
        copy.put("kitchen_remodel", new CopyBlock(
                "Kitchen remodel debris can shift from light to dense quickly. Countertops and cabinetry often drive unexpected weight risk.",
                List.of(
                        "Estimate heavy components before mixed debris.",
                        "Use safe sizing when countertop disposal is included.",
                        "Compare included tons against overage exposure."
                ),
                List.of(
                        faq("Is a small bin enough for kitchen demo?", "Sometimes, but dense items can force larger or split-haul plans."),
                        faq("What causes surprise fees?", "Underestimating dense surfaces and mixed-load packing loss."),
                        faq("How do I lower risk?", "Separate heavy debris and validate allowance up front.")
                )
        ));
        copy.put("bathroom_remodel", new CopyBlock(
                "Bathroom demo loads are commonly tile-dominant and heavier than expected. Mortar and fixtures can push feasibility quickly.",
                List.of(
                        "Treat tile-heavy loads with conservative assumptions.",
                        "Validate mixed-load policy for tile plus drywall.",
                        "Plan staged hauling if feasibility is borderline."
                ),
                List.of(
                        faq("Why do bathroom loads spike?", "Tile, mortar, and fixture density add up fast."),
                        faq("Should I use smaller bins?", "Often yes when tile is the dominant material."),
                        faq("What question avoids rework?", "Ask if mixed tile loads are accepted under standard pricing.")
                )
        ));
        copy.put("deck_demolition", new CopyBlock(
                "Deck demolition is mostly volume-driven, but moisture and hardware can move outcomes toward higher-risk ranges.",
                List.of(
                        "Include railings and connectors in estimates.",
                        "Use wet-load assumptions after rain.",
                        "Choose safe sizing when turnaround is tight."
                ),
                List.of(
                        faq("Does treated lumber change planning?", "Some operators handle treated wood under different rules."),
                        faq("What gets underestimated most?", "Bulky stacking inefficiency and hidden hardware weight."),
                        faq("When is one haul unrealistic?", "Large wet deck loads often need staged pickups.")
                )
        ));
        copy.put("garage_cleanout", new CopyBlock(
                "Garage cleanouts often miss by volume due to bulky shapes. Furniture and awkward items reduce packing efficiency.",
                List.of(
                        "Expect dead space from irregular items.",
                        "Check appliance and e-waste handling before loading.",
                        "Use safe sizing if dense items are mixed in."
                ),
                List.of(
                        faq("Why do garage loads overrun?", "Bulky items consume space inefficiently."),
                        faq("Can I include appliances?", "Rules vary and surcharges may apply."),
                        faq("What is a safer choice?", "Choose the safe option before considering downgrade.")
                )
        ));
        copy.put("estate_cleanout", new CopyBlock(
                "Estate cleanouts usually mix bulky and bagged items, making one-pass loading unpredictable. Split strategies often perform better.",
                List.of(
                        "Use staged pulls for uncertain load composition.",
                        "Compare dumpster and junk-removal routes.",
                        "Confirm swap availability during the rental window."
                ),
                List.of(
                        faq("When does junk removal fit better?", "When operational feasibility is poor or speed is critical."),
                        faq("How do I improve predictability?", "Separate dense categories and avoid one oversized mixed load."),
                        faq("What operator capability matters?", "Fast swaps and partial pulls during active cleanout.")
                )
        ));
        copy.put("yard_cleanup", new CopyBlock(
                "Yard cleanup outcomes depend heavily on moisture. Wet green waste can increase weight enough to change feasibility.",
                List.of(
                        "Run both dry and wet assumptions before booking.",
                        "Avoid overfilling after rainfall.",
                        "Check local green-waste rules."
                ),
                List.of(
                        faq("Does rain really change cost risk?", "Yes, moisture can materially increase hauled tonnage."),
                        faq("Can yard waste be mixed with household debris?", "Operator rules differ by market."),
                        faq("What is safest for tight timelines?", "Use conservative assumptions and safe sizing.")
                )
        ));
        copy.put("dirt_grading", new CopyBlock(
                "Dirt grading projects are mainly constrained by hauling limits. Multi-haul planning is usually required for reliable execution.",
                List.of(
                        "Optimize around haul caps, not nominal container size.",
                        "Assume higher tons when soil contains rock.",
                        "Pre-book swaps for larger grading jobs."
                ),
                List.of(
                        faq("Why are grading jobs often multi-haul?", "Dense soil reaches max haul limits quickly."),
                        faq("Is the largest dumpster always best?", "No, haul limits can make large bins impractical."),
                        faq("What should be confirmed first?", "Per-container haul cap for soil and rock.")
                )
        ));
        copy.put("concrete_removal", new CopyBlock(
                "Concrete removal is almost always haul-limited before volume-limited. Plan conservative fills and explicit haul count.",
                List.of(
                        "Estimate from slab thickness and area.",
                        "Use dedicated heavy-debris load assumptions.",
                        "Confirm clean-concrete requirements before dispatch."
                ),
                List.of(
                        faq("Can concrete go in larger bins?", "Sometimes by volume, but hauling constraints often block it."),
                        faq("Is dedicated concrete loading required?", "Frequently yes for compliance and pricing."),
                        faq("What causes schedule failure?", "Skipping multi-haul planning for dense loads.")
                )
        ));
        copy.put("light_commercial_fitout", new CopyBlock(
                "Commercial fit-out loads are mixed and schedule-sensitive. Safe recommendations generally reduce end-of-project risk.",
                List.of(
                        "Use safe allowance margins for deadline work.",
                        "Confirm same-day swap capability in advance.",
                        "Route high-risk loads to alternate options when needed."
                ),
                List.of(
                        faq("Why are fit-out loads risky?", "Daily material mix changes make density hard to predict."),
                        faq("What should be confirmed with haulers?", "Included tons, overage rates, and swap lead time."),
                        faq("How do teams avoid day-five surprises?", "Choose safe sizing and lock logistics early.")
                )
        ));
        return copy;
    }

    private static FaqItemViewModel faq(String question, String answer) {
        return new FaqItemViewModel(question, answer);
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

    private record CopyBlock(
            String answerFirst,
            List<String> quickRules,
            List<FaqItemViewModel> faqItems
    ) {
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

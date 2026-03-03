package com.dumpster.calculator.web.content;

import com.dumpster.calculator.domain.reference.MaterialCategory;
import com.dumpster.calculator.domain.reference.MaterialFactor;
import com.dumpster.calculator.infra.persistence.DumpsterSizeRepository;
import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import com.dumpster.calculator.web.viewmodel.FaqItemViewModel;
import com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel;
import com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel;
import com.dumpster.calculator.web.viewmodel.IntentPageViewModel;
import com.dumpster.calculator.web.viewmodel.LinkItemViewModel;
import com.dumpster.calculator.web.viewmodel.MaterialPageViewModel;
import com.dumpster.calculator.web.viewmodel.ProjectPageViewModel;
import com.dumpster.calculator.web.viewmodel.SpecialSeoPageViewModel;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SeoContentService {

    private final MaterialFactorRepository materialFactorRepository;
    private final DumpsterSizeRepository dumpsterSizeRepository;
    private final Map<String, ProjectSeed> projectSeeds = new LinkedHashMap<>();
    private final int seoMaxWave;
    private static final DateTimeFormatter SOURCE_MONTH_YEAR = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US);
    private static final LocalDate DEFAULT_SEO_LAST_MODIFIED = LocalDate.of(2026, 3, 1);
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
    private static final Map<String, String> MATERIAL_SLUG_TO_ID = Map.of(
            "concrete", "concrete",
            "shingles", "asphalt_shingles",
            "drywall", "drywall",
            "dirt", "dirt_soil",
            "brick-block", "brick"
    );
    private static final Map<String, String> MATERIAL_ID_TO_CANONICAL_PATH = Map.of(
            "concrete", "/dumpster/weight/concrete",
            "asphalt_shingles", "/dumpster/weight/shingles",
            "drywall", "/dumpster/weight/drywall",
            "dirt_soil", "/dumpster/weight/dirt",
            "brick", "/dumpster/weight/brick-block"
    );
    private static final Map<String, String> PROJECT_SLUG_TO_ID = Map.of(
            "bathroom-remodel", "bathroom_remodel",
            "roof-tear-off", "roof_tearoff",
            "deck-removal", "deck_demolition",
            "garage-cleanout", "garage_cleanout",
            "kitchen-remodel", "kitchen_remodel",
            "estate-cleanout", "estate_cleanout",
            "yard-cleanup", "yard_cleanup",
            "dirt-grading", "dirt_grading",
            "concrete-removal", "concrete_removal",
            "light-commercial-fitout", "light_commercial_fitout"
    );
    private static final Map<String, String> PROJECT_ID_TO_CANONICAL_PATH = Map.of(
            "bathroom_remodel", "/dumpster/size/bathroom-remodel",
            "roof_tearoff", "/dumpster/size/roof-tear-off",
            "deck_demolition", "/dumpster/size/deck-removal",
            "garage_cleanout", "/dumpster/size/garage-cleanout",
            "kitchen_remodel", "/dumpster/size/kitchen-remodel",
            "estate_cleanout", "/dumpster/size/estate-cleanout",
            "yard_cleanup", "/dumpster/size/yard-cleanup",
            "dirt_grading", "/dumpster/size/dirt-grading",
            "concrete_removal", "/dumpster/size/concrete-removal",
            "light_commercial_fitout", "/dumpster/size/light-commercial-fitout"
    );
    private static final Map<String, Integer> MATERIAL_INDEX_WAVE = Map.of(
            "concrete", 1,
            "asphalt_shingles", 1,
            "drywall", 1,
            "dirt_soil", 1,
            "brick", 3
    );
    private static final Map<String, Integer> PROJECT_INDEX_WAVE = Map.of(
            "bathroom_remodel", 2,
            "roof_tearoff", 2,
            "deck_demolition", 2,
            "garage_cleanout", 3,
            "kitchen_remodel", 2
    );
    private static final Map<String, Integer> SPECIAL_PAGE_INDEX_WAVE = Map.ofEntries(
            Map.entry("what-size-dumpster-do-i-need", 1),
            Map.entry("10-yard-dumpster-weight-limit-overage", 1),
            Map.entry("can-you-put-concrete-in-a-dumpster", 1),
            Map.entry("can-you-mix-concrete-and-wood-in-a-dumpster", 2),
            Map.entry("dumpster-vs-junk-removal-which-is-cheaper", 1),
            Map.entry("pickup-truck-loads-to-dumpster-size", 1),
            Map.entry("roof-shingles-dumpster-size-calculator", 1),
            Map.entry("drywall-disposal-dumpster-rules", 2),
            Map.entry("bagster-vs-dumpster", 3),
            Map.entry("fill-line-rules-for-heavy-debris", 3),
            Map.entry("one-20-yard-vs-two-10-yard", 3)
    );
    private static final Map<String, String> SPECIAL_PAGE_ALIASES = Map.of(
            "how-many-tons-can-a-10-yard-dumpster-hold", "10-yard-dumpster-weight-limit-overage",
            "dumpster-vs-junk-removal", "dumpster-vs-junk-removal-which-is-cheaper",
            "roofing-squares-to-dumpster-size", "roof-shingles-dumpster-size-calculator",
            "drywall-sheets-to-dumpster-size", "drywall-disposal-dumpster-rules"
    );
    private static final String CALCULATOR_PATH = "/dumpster/size-weight-calculator";
    private static final String MATERIAL_GUIDES_PATH = "/dumpster/material-guides";
    private static final String PROJECT_GUIDES_PATH = "/dumpster/project-guides";
    private static final String HEAVY_RULES_PATH = "/dumpster/heavy-debris-rules";
    private static final String INTENT_BASE_PATH = "/dumpster/answers";
    private static final List<IntentType> INTENT_TYPES = List.of(
            IntentType.SIZE_GUIDE,
            IntentType.WEIGHT_ESTIMATE,
            IntentType.OVERAGE_RISK
    );
    private static final Map<String, List<String>> PROJECT_INTENT_MATERIALS = Map.of(
            "roof_tearoff", List.of("asphalt_shingles", "tile_ceramic", "metal_scrap_light"),
            "kitchen_remodel", List.of("mixed_cd", "drywall", "plaster"),
            "bathroom_remodel", List.of("tile_ceramic", "drywall", "mixed_cd"),
            "deck_demolition", List.of("decking_wood", "mixed_cd", "yard_waste"),
            "garage_cleanout", List.of("household_junk", "furniture", "cardboard_packaging"),
            "estate_cleanout", List.of("household_junk", "furniture", "mixed_cd"),
            "yard_cleanup", List.of("yard_waste", "green_waste_brush", "dirt_soil"),
            "dirt_grading", List.of("dirt_soil", "gravel_rock", "concrete"),
            "concrete_removal", List.of("concrete", "brick", "asphalt_pavement"),
            "light_commercial_fitout", List.of("mixed_cd", "drywall", "cardboard_packaging")
    );
    private static final Map<String, CopyBlock> MATERIAL_COPY = buildMaterialCopy();
    private static final Map<String, CopyBlock> PROJECT_COPY = buildProjectCopy();

    public SeoContentService(
            MaterialFactorRepository materialFactorRepository,
            DumpsterSizeRepository dumpsterSizeRepository,
            @Value("${app.seo.max-wave:2}") int seoMaxWave
    ) {
        this.materialFactorRepository = materialFactorRepository;
        this.dumpsterSizeRepository = dumpsterSizeRepository;
        this.seoMaxWave = Math.max(1, Math.min(seoMaxWave, 3));
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
        if (!isMaterialEnabled(materialId)) {
            return Optional.empty();
        }
        return materialFactorRepository.findById(materialId)
                .map(material -> {
                    double exampleVolume = material.category() == MaterialCategory.HEAVY ? 4.0d : 8.0d;
                    double lowWeight = (exampleVolume * material.densityLow()) / 2000.0d;
                    double typWeight = (exampleVolume * material.densityTyp()) / 2000.0d;
                    double highWeight = (exampleVolume * material.densityHigh()) / 2000.0d;
                    String categoryLabel = categoryLabel(material.category());
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
                    List<MaterialPageViewModel.SizeWeightRow> sizeWeightTable = sizeWeightRows(material);
                    String exampleRange = round2(lowWeight) + " to " + round2(highWeight) + " tons";
                    String answerFirst = material.name() + " is typically " + (int) material.densityTyp() + " lbs/yd3."
                            + " A " + (int) exampleVolume + " yd3 load is about " + exampleRange
                            + " (typical " + round2(typWeight) + " tons). " + copy.answerFirst();
                    String seoTitle = material.name() + " Dumpster Weight: " + (int) material.densityTyp()
                            + " lbs/yd3 Typical | Size & Overage Chart";
                    String metaDescription = material.name() + " weighs around " + (int) material.densityTyp()
                            + " lbs/yd3. A " + (int) exampleVolume + " yd3 load is "
                            + exampleRange + ". Compare dumpster-size weight ranges and overage risk.";
                    LocalDate materialUpdatedDate = material.sourceVersionDate() == null
                            ? DEFAULT_SEO_LAST_MODIFIED
                            : material.sourceVersionDate();
                    String sourceDateDisplay = materialUpdatedDate.format(SOURCE_MONTH_YEAR);
                    return new MaterialPageViewModel(
                            material.materialId(),
                            material.name(),
                            material.name() + " Dumpster Weight Guide",
                            seoTitle,
                            metaDescription,
                            absoluteUrl(baseUrl, materialCanonicalPath(material.materialId())),
                            ogImageUrl(baseUrl),
                            absoluteUrl(baseUrl, CALCULATOR_PATH),
                            categoryLabel,
                            material.densityLow(),
                            material.densityTyp(),
                            material.densityHigh(),
                            material.wetMultiplierLow(),
                            material.wetMultiplierHigh(),
                            exampleVolume,
                            round2(lowWeight),
                            round2(typWeight),
                            round2(highWeight),
                            materialUpdatedDate.toString(),
                            materialUpdatedDate.toString(),
                            sourceDateDisplay,
                            material.source(),
                            cautionNote,
                            operatorQuestion,
                            scenario.input(),
                            scenario.decision(),
                            answerFirst,
                            sizeWeightTable,
                            copy.quickRules(),
                            copy.faqItems(),
                            absoluteUrl(baseUrl, MATERIAL_GUIDES_PATH),
                            intentClusterLinksForMaterial(material.materialId()),
                            relatedMaterialsForMaterial(material),
                            relatedProjectsForMaterial(material.materialId())
                    );
                });
    }

    public Optional<ProjectPageViewModel> projectPage(String projectId, String baseUrl) {
        ProjectSeed seed = projectSeeds.get(projectId);
        if (seed == null) {
            return Optional.empty();
        }
        if (!isProjectEnabled(projectId)) {
            return Optional.empty();
        }
        CopyBlock copy = projectCopyFor(seed);
        String canonicalPath = projectCanonicalPath(seed.projectId());
        String defaultMaterialName = materialFactorRepository.findById(seed.defaultMaterialId())
                .map(MaterialFactor::name)
                .orElse(seed.defaultMaterialId().replace('_', ' '));
        String seoTitle = seed.title() + " | Dumpster Strategy + Live Calculator";
        String metaDescription = seed.title() + " guide with " + seed.recommendedUnit()
                + " input defaults, " + defaultMaterialName + " baseline, and risk-aware recommendation notes.";
        String modifiedDateIso = defaultLastModifiedDate().toString();
        return Optional.of(new ProjectPageViewModel(
                seed.projectId(),
                seed.title(),
                seoTitle,
                metaDescription,
                absoluteUrl(baseUrl, canonicalPath),
                ogImageUrl(baseUrl),
                absoluteUrl(baseUrl, CALCULATOR_PATH),
                seed.recommendedUnit(),
                seed.defaultMaterialId(),
                defaultMaterialName,
                seed.commonMistake(),
                seed.recommendedStrategy(),
                seed.operatorQuestion(),
                canonicalPath,
                seed.sampleInput(),
                seed.sampleDecision(),
                modifiedDateIso,
                copy.answerFirst(),
                copy.quickRules(),
                copy.faqItems(),
                absoluteUrl(baseUrl, PROJECT_GUIDES_PATH),
                intentClusterLinksForProject(seed.projectId()),
                relatedMaterialsForProject(seed),
                relatedProjectsForProject(seed)
        ));
    }

    public List<String> projectIndexPaths() {
        return projectIndexPaths(3);
    }

    public List<String> projectIndexPaths(int maxWave) {
        int waveLimit = Math.max(1, Math.min(maxWave, 3));
        return PROJECT_INDEX_WAVE.entrySet().stream()
                .filter(entry -> entry.getValue() <= waveLimit)
                .map(Map.Entry::getKey)
                .filter(projectSeeds::containsKey)
                .map(this::projectCanonicalPath)
                .toList();
    }

    public List<String> intentIndexPaths() {
        return projectSeeds.keySet().stream()
                .flatMap(projectId -> projectIntentMaterialsForProject(projectId).stream()
                        .flatMap(materialId -> INTENT_TYPES.stream()
                                .map(intentType -> intentPath(projectId, materialId, intentType))))
                .toList();
    }

    public Optional<IntentPageViewModel> intentPage(
            String projectId,
            String materialId,
            String intentSlug,
            String baseUrl
    ) {
        ProjectSeed seed = projectSeeds.get(projectId);
        if (seed == null) {
            return Optional.empty();
        }
        IntentType intentType = IntentType.fromSlug(intentSlug).orElse(null);
        if (intentType == null) {
            return Optional.empty();
        }
        if (!projectIntentMaterialsForProject(projectId).contains(materialId)) {
            return Optional.empty();
        }
        Optional<MaterialFactor> materialOptional = materialFactorRepository.findById(materialId);
        if (materialOptional.isEmpty()) {
            return Optional.empty();
        }
        MaterialPageViewModel materialPage = materialPage(materialId, baseUrl).orElse(null);
        ProjectPageViewModel projectPage = projectPage(projectId, baseUrl).orElse(null);
        if (materialPage == null || projectPage == null) {
            return Optional.empty();
        }

        String canonicalPath = intentPath(projectId, materialId, intentType);
        String canonicalUrl = absoluteUrl(baseUrl, canonicalPath);
        String materialName = materialPage.materialName();
        String projectTitle = projectPage.title();
        MaterialPageViewModel.SizeWeightRow anchorRow = anchorSizeRow(materialPage.sizeWeightTable());
        String directAnswer = intentDirectAnswer(intentType, materialPage, projectPage, anchorRow);
        String intentQuestion = intentQuestion(intentType, projectTitle, materialName);
        String intentSummary = intentSummary(intentType, materialPage, projectPage, anchorRow);
        String evidenceNote = "Evidence baseline: density "
                + round2(materialPage.densityLow()) + " to "
                + round2(materialPage.densityHigh()) + " lbs/yd3,"
                + " size-level included tonnage, and project workflow assumptions for "
                + projectTitle.toLowerCase(Locale.US) + ".";
        String modifiedDateIso = materialLastModifiedDate(materialId).toString();

        return Optional.of(new IntentPageViewModel(
                intentQuestion,
                intentType.seoTitle(projectTitle, materialName, anchorRow),
                intentType.metaDescription(projectTitle, materialName, anchorRow),
                canonicalUrl,
                ogImageUrl(baseUrl),
                absoluteUrl(baseUrl, CALCULATOR_PATH),
                absoluteUrl(baseUrl, MATERIAL_GUIDES_PATH),
                absoluteUrl(baseUrl, PROJECT_GUIDES_PATH),
                materialName,
                materialId,
                projectTitle,
                projectId,
                intentType.label(),
                intentQuestion,
                directAnswer,
                intentSummary,
                evidenceNote,
                modifiedDateIso,
                materialPage.sizeWeightTable(),
                intentChecklist(intentType, materialPage, projectPage),
                intentFaq(intentType, materialPage, projectPage, anchorRow),
                relatedIntentLinks(projectId, materialId, intentType),
                relatedMaterialsForMaterial(materialOptional.get()),
                relatedProjectsForMaterial(materialId)
        ));
    }

    public List<String> indexableMaterialIds() {
        return indexableMaterialIds(3);
    }

    public List<String> indexableMaterialIds(int maxWave) {
        int waveLimit = Math.max(1, Math.min(maxWave, 3));
        List<String> available = materialFactorRepository.findAll().stream()
                .map(MaterialFactor::materialId)
                .toList();
        return MATERIAL_INDEX_WAVE.entrySet().stream()
                .filter(entry -> entry.getValue() <= waveLimit)
                .map(Map.Entry::getKey)
                .filter(available::contains)
                .toList();
    }

    public String resolveMaterialId(String materialPathToken) {
        return MATERIAL_SLUG_TO_ID.getOrDefault(materialPathToken, materialPathToken);
    }

    public String resolveProjectId(String projectPathToken) {
        return PROJECT_SLUG_TO_ID.getOrDefault(projectPathToken, projectPathToken);
    }

    public String materialCanonicalPath(String materialId) {
        return MATERIAL_ID_TO_CANONICAL_PATH.getOrDefault(materialId, "/dumpster/weight/" + materialId);
    }

    public String projectCanonicalPath(String projectId) {
        return PROJECT_ID_TO_CANONICAL_PATH.getOrDefault(projectId, "/dumpster/size/" + projectId);
    }

    public String resolveSpecialSlug(String slug) {
        return SPECIAL_PAGE_ALIASES.getOrDefault(slug, slug);
    }

    public boolean isSpecialPageSlug(String slug) {
        return SPECIAL_PAGE_INDEX_WAVE.containsKey(resolveSpecialSlug(slug));
    }

    public boolean isSpecialPageEnabled(String slug) {
        String resolvedSlug = resolveSpecialSlug(slug);
        return isSpecialPageSlug(resolvedSlug)
                && SPECIAL_PAGE_INDEX_WAVE.getOrDefault(resolvedSlug, Integer.MAX_VALUE) <= seoMaxWave;
    }

    public List<String> specialPageIndexPaths(int maxWave) {
        int waveLimit = Math.max(1, Math.min(maxWave, 3));
        return SPECIAL_PAGE_INDEX_WAVE.entrySet().stream()
                .filter(entry -> entry.getValue() <= waveLimit)
                .map(entry -> "/dumpster/" + entry.getKey())
                .toList();
    }

    public Optional<SpecialSeoPageViewModel> specialPage(String slug, String baseUrl) {
        String resolvedSlug = resolveSpecialSlug(slug);
        if (!isSpecialPageEnabled(resolvedSlug)) {
            return Optional.empty();
        }
        String canonicalPath = "/dumpster/" + resolvedSlug;
        String modifiedDateIso = defaultLastModifiedDate().toString();

        return switch (resolvedSlug) {
            case "what-size-dumpster-do-i-need" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "What Size Dumpster Do I Need?",
                    "What Size Dumpster Do I Need? Fast Decision Guide",
                    "Answer the size question fast, then validate weight limits and overage risk before you book.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Decision Intent",
                    "Most projects fit a range, not a single guaranteed size. The safest path is to match debris type, volume, and ton limits together.",
                    "Start from project type and material. Then check included tons and max-haul feasibility before finalizing container size.",
                    List.of(
                            "Do not pick by cubic yards alone when debris is dense.",
                            "Use safe recommendation if schedule cannot absorb swap delays.",
                            "Validate both quote allowance and haul constraints before dispatch."
                    ),
                    List.of(
                            row("Light to mixed cleanup", "Volume-led sizing", "10 to 20-yard is common when tonnage is moderate."),
                            row("Heavy debris share", "Weight-led sizing", "Smaller staged pulls often outperform one larger bin."),
                            row("High uncertainty jobs", "Risk-first sizing", "Use safe option to reduce overage and refusal risk.")
                    ),
                    List.of(
                            faq("What is the fastest way to choose size?", "Pick project and material presets first, then compare safe versus budget recommendations."),
                            faq("Why can larger bins still fail?", "Heavy materials can exceed haul limits before the container is visually full."),
                            faq("What should I confirm with a hauler?", "Ask included tons, overage pricing, and max-haul policy for your debris profile.")
                    ),
                    "Start size decision",
                    "/dumpster/size-weight-calculator",
                    "See heavy-debris rules",
                    "/dumpster/heavy-debris-rules",
                    List.of(
                            link("/dumpster/10-yard-dumpster-weight-limit-overage", "10-Yard Weight Limits", "Check included tons versus max-haul risk."),
                            link("/dumpster/dumpster-vs-junk-removal-which-is-cheaper", "Dumpster vs Junk Removal", "Compare by urgency, labor, and risk.")
                    )
            ));
            case "10-yard-dumpster-weight-limit-overage" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "10-Yard Dumpster Weight Limit and Overage Risk",
                    "10-Yard Dumpster Weight Limit and Overage Risk",
                    "See the difference between included tons and haul limits for a 10-yard dumpster before loading heavy debris.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Limit Intent",
                    "Typical included tons are lower than physical haul capacity, so policy and feasibility must be checked separately.",
                    "Use included tons for quote risk and max-haul limits for pickup feasibility. Heavy debris can fail before visual capacity.",
                    List.of(
                            "Do not treat included tons as transport limit.",
                            "Heavy debris often requires low fill strategy.",
                            "Confirm overage fee and rejection trigger before loading."
                    ),
                    List.of(
                            row("Included tons", "Usually 1 to 2 tons", "Crossing this can trigger overage fees."),
                            row("Max haul tons", "Higher than included tons", "Pickup can still fail if heavy-fill rules are ignored."),
                            row("Heavy fill behavior", "Part-way loading common", "Volume fit does not guarantee feasible transport.")
                    ),
                    List.of(
                            faq("Why are included tons and haul limits different?", "Included tons are pricing terms, while haul limits are operational transport constraints."),
                            faq("Can I load a 10-yard to the top with concrete?", "Usually no. Heavy loads often require partial fill and sometimes clean-load separation."),
                            faq("What should I ask the vendor first?", "Ask included tons, overage rate, and maximum haul policy for heavy debris.")
                    ),
                    "Check if a 10-yard is enough",
                    "/dumpster/size-weight-calculator?material=concrete&unit=sqft_4in",
                    "See heavy-debris rules",
                    "/dumpster/heavy-debris-rules",
                    List.of(
                            link("/dumpster/heavy-debris-rules", "Heavy Debris Rules", "Understand fill-line and clean-load constraints."),
                            link("/dumpster/can-you-put-concrete-in-a-dumpster", "Concrete Rules", "See when concrete is allowed and how to load safely.")
                    )
            ));
            case "can-you-put-concrete-in-a-dumpster" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "Can You Put Concrete in a Dumpster?",
                    "Can You Put Concrete in a Dumpster? Rules Limits and Options",
                    "Learn when concrete is allowed, when clean-load rules apply, and when multiple small hauls are safer.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Rule Intent",
                    "Yes, often with strict conditions: concrete-only loads, partial fill, and haul-cap checks are common requirements.",
                    "Concrete is transport-limited material. The safest plan is to validate clean-load and max-haul rules before choosing container size.",
                    List.of(
                            "Assume concrete-only separation unless confirmed otherwise.",
                            "Do not fill to rim for heavy concrete loads.",
                            "Use slab area plus thickness for planning, not visual volume only."
                    ),
                    List.of(
                            row("Load type", "Concrete-only often required", "Mixed loads can be rejected or repriced."),
                            row("Fill behavior", "Partial fill for heavy debris", "Top-fill assumptions cause pickup risk."),
                            row("Hauling strategy", "Multi-haul frequently needed", "One-bin plan can fail operationally.")
                    ),
                    List.of(
                            faq("Is concrete always allowed?", "Usually yes, but many operators require clean-load separation and conservative fill."),
                            faq("Can I mix concrete with wood?", "Sometimes not. Mixed heavy loads frequently face stricter rules."),
                            faq("What is the safest booking pattern?", "Use smaller staged pulls and confirm max haul constraints upfront.")
                    ),
                    "Check concrete options",
                    "/dumpster/size-weight-calculator?material=concrete&project=concrete_removal&unit=sqft_4in",
                    "See concrete calculator",
                    "/dumpster/weight/concrete",
                    List.of(
                            link("/dumpster/weight/concrete", "Concrete Dumpster Calculator", "Estimate tons by size and risk band."),
                            link("/dumpster/can-you-mix-concrete-and-wood-in-a-dumpster", "Mixing Rules", "Review mixed-load risk before pickup.")
                    )
            ));
            case "can-you-mix-concrete-and-wood-in-a-dumpster" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "Can You Mix Concrete and Wood in a Dumpster?",
                    "Can You Mix Concrete and Wood in a Dumpster? Safer Options",
                    "See when mixed loads cause pickup or fee issues and when clean-load separation is safer.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Rule Intent",
                    "Sometimes, but many operators discourage or restrict this because dense concrete changes haul feasibility and pricing.",
                    "Mixing heavy and light materials can break both pricing and transport assumptions. Separation usually reduces rejection risk.",
                    List.of(
                            "Treat mixed concrete loads as high-risk by default.",
                            "Ask whether clean-load separation is mandatory.",
                            "If uncertain, split into dedicated heavy and mixed bins."
                    ),
                    List.of(
                            row("Policy clarity", "Varies by hauler", "Unconfirmed mixing can trigger refusal or reclassification."),
                            row("Weight behavior", "Concrete dominates tonnage", "Light debris does not offset heavy-load risk."),
                            row("Best practice", "Split heavy from mixed", "Improves pricing predictability and pickup success.")
                    ),
                    List.of(
                            faq("Why is mixing problematic?", "Concrete can push loads into heavy-debris handling while wood suggests mixed-load pricing."),
                            faq("Does mixing ever work?", "It can, but only when operator policy explicitly allows it."),
                            faq("What is safer for schedule certainty?", "Separate concrete into dedicated heavy loads and keep mixed debris separate.")
                    ),
                    "See safer disposal option",
                    "/dumpster/size-weight-calculator?material=concrete&project=concrete_removal",
                    "See heavy-debris rules",
                    "/dumpster/heavy-debris-rules",
                    List.of(
                            link("/dumpster/can-you-put-concrete-in-a-dumpster", "Concrete Allowed Rules", "Check baseline concrete acceptance."),
                            link("/dumpster/dumpster-vs-junk-removal-which-is-cheaper", "Dumpster vs Junk Removal", "Compare alternatives when feasibility is weak.")
                    )
            ));
            case "fill-line-rules-for-heavy-debris" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "Heavy Debris Fill Line Rules",
                    "Heavy Debris Fill Line Rules: Avoid Refusals and Extra Fees",
                    "Understand why heavy debris is often loaded below the top and how to reduce pickup refusal risk.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Rule Intent",
                    "For dense debris, operators commonly require loading below normal top-fill level to stay within safe haul limits.",
                    "Fill-line guidance is operational safety policy. It should be treated as non-optional when handling concrete, dirt, shingles, or masonry.",
                    List.of(
                            "Do not use top edge as target for dense loads.",
                            "Confirm heavy-debris fill constraints before loading starts.",
                            "Keep a visible margin to avoid pickup-day rejection."
                    ),
                    List.of(
                            row("Fill threshold", "Below top line for heavy debris", "Overfill risk includes refusal and rework."),
                            row("Policy source", "Operator transport constraints", "Rule is tied to safe hauling, not preference."),
                            row("Practical strategy", "Use staged hauling", "Reduces rejection and overage uncertainty.")
                    ),
                    List.of(
                            faq("Is fill-line rule the same for all materials?", "No. Heavy debris commonly has stricter fill expectations than light loads."),
                            faq("What happens if I overfill?", "Pickup can be delayed, refused, or repriced depending on policy."),
                            faq("How can I prevent refusal?", "Load conservatively and confirm heavy-debris constraints in writing.")
                    ),
                    "Check heavy-debris rules",
                    "/dumpster/heavy-debris-rules",
                    "Start calculator",
                    "/dumpster/size-weight-calculator?material=concrete",
                    List.of(
                            link("/dumpster/heavy-debris-rules", "Heavy Debris Rules", "Review included tons vs haul constraints."),
                            link("/dumpster/10-yard-dumpster-weight-limit-overage", "10-Yard Ton Limits", "Check limit intent before loading.")
                    )
            ));
            case "dumpster-vs-junk-removal-which-is-cheaper" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "Dumpster vs Junk Removal",
                    "Dumpster vs Junk Removal: Which Costs Less for Heavy Debris?",
                    "Compare dumpster rental and junk removal by labor, urgency, and heavy-debris risk to choose the better option.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Comparison Intent",
                    "Dumpster usually wins for larger planned loads; junk removal often wins when speed and labor convenience matter most.",
                    "Use decision criteria, not headline price alone. Labor availability, loading certainty, and heavy-load feasibility can flip the best option.",
                    List.of(
                            "If labor is constrained, convenience can outweigh nominal disposal cost.",
                            "If debris volume is large and staged, dumpster often scales better.",
                            "When heavy-feasibility is poor, compare alternate method early."
                    ),
                    List.of(
                            row("Small urgent cleanout", "High convenience need", "Junk removal can be lower-friction."),
                            row("Large multi-day project", "Predictable loading window", "Dumpster often has lower unit economics."),
                            row("Heavy dense debris", "Strict haul constraints", "Route by feasibility first, then cost.")
                    ),
                    List.of(
                            faq("Is junk removal always more expensive?", "Not always. For small urgent jobs, convenience can offset price differences."),
                            faq("When is dumpster usually better?", "When you control loading schedule and total debris is large enough to amortize rental."),
                            faq("How do I choose quickly?", "Start with size and weight risk, then compare labor and urgency requirements.")
                    ),
                    "Compare your options",
                    "/dumpster/size-weight-calculator?project=garage_cleanout",
                    "Start calculator",
                    "/dumpster/size-weight-calculator",
                    List.of(
                            link("/dumpster/pickup-truck-loads-to-dumpster-size", "Pickup Load Converter", "Estimate whether job size favors dumpster flow."),
                            link("/dumpster/heavy-debris-rules", "Heavy Rules", "Check feasibility constraints before comparing cost.")
                    )
            ));
            case "bagster-vs-dumpster" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "Bagster vs Dumpster",
                    "Bagster vs Dumpster: Weight Limits Cost and Best Fit",
                    "See when a bagster is enough and when a roll-off dumpster is safer for capacity and heavy-load risk.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Comparison Intent",
                    "Bagster can work for smaller lighter cleanups; dumpster is safer for larger or heavy debris where capacity and haul flexibility matter.",
                    "Treat this as a capacity-plus-risk decision. Weight-sensitive debris quickly pushes one-bag assumptions past safe limits.",
                    List.of(
                            "Do not use bag solution as default for dense debris.",
                            "Estimate total volume and expected tonnage before choosing.",
                            "If uncertainty is high, use dumpster-first strategy."
                    ),
                    List.of(
                            row("Capacity headroom", "Bag is limited", "Large project variance favors dumpster."),
                            row("Heavy debris tolerance", "Lower", "Concrete/dirt/masonry usually need roll-off strategy."),
                            row("Operational flexibility", "Dumpster supports staged hauls", "Improves schedule reliability.")
                    ),
                    List.of(
                            faq("When is bagster enough?", "Smaller light-to-mixed loads with low variability can fit."),
                            faq("When should I skip bagster?", "When load may include dense heavy debris or volume uncertainty is high."),
                            faq("What is the safer default under uncertainty?", "Use dumpster sizing with risk-aware allowance.")
                    ),
                    "Compare bagster vs dumpster",
                    "/dumpster/size-weight-calculator?project=garage_cleanout",
                    "See if a 10-yard is enough",
                    "/dumpster/10-yard-dumpster-weight-limit-overage",
                    List.of(
                            link("/dumpster/dumpster-vs-junk-removal-which-is-cheaper", "Dumpster vs Junk Removal", "Compare alternatives by scenario."),
                            link("/dumpster/pickup-truck-loads-to-dumpster-size", "Pickup Load Converter", "Estimate realistic volume first.")
                    )
            ));
            case "one-20-yard-vs-two-10-yard" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "One 20-Yard vs Two 10-Yard Dumpsters",
                    "One 20-Yard vs Two 10-Yard Dumpsters: Which Is Safer?",
                    "Compare cost, haul flexibility, and heavy-debris risk when choosing one larger dumpster versus two smaller pulls.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Comparison Intent",
                    "Two 10-yard pulls are often safer for dense debris because haul constraints can bind before a 20-yard bin is fully utilized.",
                    "This choice is not volume-only. Feasibility, fill rules, and swap logistics can make smaller staged hauling the lower-risk path.",
                    List.of(
                            "Use heavy-material share as primary split trigger.",
                            "Compare operational flexibility, not just quoted rental.",
                            "Model both strategies before booking."
                    ),
                    List.of(
                            row("Heavy debris dominance", "High", "Two 10-yard strategy often reduces haul-failure risk."),
                            row("Schedule rigidity", "Tight deadlines", "Single large bin may simplify logistics if feasible."),
                            row("Risk tolerance", "Low tolerance for pickup failure", "Favor staged smaller pulls.")
                    ),
                    List.of(
                            faq("Is one 20-yard always cheaper?", "Not always. Heavy-load constraints can force rework that erodes quoted savings."),
                            faq("When do two 10-yard bins help most?", "When debris density is high and fill-line constraints are strict."),
                            faq("How should I decide quickly?", "Compare both plans in calculator with high-side weight assumptions.")
                    ),
                    "Compare haul options",
                    "/dumpster/size-weight-calculator?project=concrete_removal&material=concrete",
                    "See concrete calculator",
                    "/dumpster/weight/concrete",
                    List.of(
                            link("/dumpster/10-yard-dumpster-weight-limit-overage", "10-Yard Limit Page", "Check risk profile for staged hauling."),
                            link("/dumpster/heavy-debris-rules", "Heavy Rules", "Use rule baseline before cost comparison.")
                    )
            ));
            case "pickup-truck-loads-to-dumpster-size" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "Pickup Truck Loads to Dumpster Size Calculator",
                    "Pickup Truck Loads to Dumpster Size Calculator",
                    "Convert pickup truck loads into dumpster size ranges and avoid underestimating cleanup volume before booking.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Unit Intent",
                    "Use pickup-load conversion as a planning shortcut, then validate weight risk for dense materials.",
                    "Load-count shortcuts are useful for volume estimate, but heavy materials still require tonnage checks before final size decision.",
                    List.of(
                            "Use load-count estimate as starting point, not final answer.",
                            "Add margin for irregular bulky items and poor packing.",
                            "Run heavy-material scenarios separately for risk control."
                    ),
                    List.of(
                            row("10-yard baseline", "About 3 pickup loads", "Small projects can fit when density is moderate."),
                            row("20-yard baseline", "About 6 pickup loads", "Common remodel range when loading is staged."),
                            row("30 to 40-yard", "About 9 to 12 loads", "Large cleanouts need feasibility and scheduling checks.")
                    ),
                    List.of(
                            faq("Are pickup-load conversions exact?", "No. They are directional and vary with bed size and packing efficiency."),
                            faq("What most often breaks the estimate?", "Bulky items, moisture, and dense material share."),
                            faq("What should I do after conversion?", "Run calculator with material and project context for risk-aware sizing.")
                    ),
                    "Convert pickup loads",
                    "/dumpster/size-weight-calculator?unit=pickup_load",
                    "Start calculator",
                    "/dumpster/size-weight-calculator",
                    List.of(
                            link("/dumpster/dumpster-vs-junk-removal-which-is-cheaper", "Dumpster vs Junk Removal", "Use when convenience tradeoff is unclear."),
                            link("/dumpster/heavy-debris-rules", "Heavy Rules", "Validate dense-load constraints.")
                    )
            ));
            case "roof-shingles-dumpster-size-calculator" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "Roofing Squares to Dumpster Size Calculator",
                    "Roofing Squares to Dumpster Size Calculator",
                    "Convert roofing squares into dumpster size and ton-range estimates with risk cues before your tear-off starts.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Unit Intent",
                    "Use roofing squares as input, then validate shingle type, layer count, and haul limits before selecting container size.",
                    "Square conversion is powerful for roof jobs, but weight variance across material types means risk should be modeled as range.",
                    List.of(
                            "Distinguish 3-tab, architectural, and premium shingles.",
                            "Account for multiple layers in tear-off planning.",
                            "Validate heavy-load limits before final size choice."
                    ),
                    List.of(
                            row("Input unit", "Roofing square", "Faster estimate than loose volume guess."),
                            row("Weight behavior", "Material and layer sensitive", "Range-based output prevents false precision."),
                            row("Operational check", "Haul constraints first", "Large bin by volume can still fail transport.")
                    ),
                    List.of(
                            faq("Can one square map to one fixed tonnage?", "No. Product type and tear-off conditions create range variance."),
                            faq("Why can a larger bin still be risky?", "Shingle density can exceed haul limits before visual capacity is reached."),
                            faq("What is the safest next step?", "Run calculator with roof-square input and compare safe versus budget recommendations.")
                    ),
                    "Convert roofing squares",
                    "/dumpster/size-weight-calculator?project=roof_tearoff&material=asphalt_shingles&unit=roof_square",
                    "See roof tear-off calculator",
                    "/dumpster/size/roof-tear-off",
                    List.of(
                            link("/dumpster/weight/shingles", "Shingles Calculator", "Review tonnage ranges by dumpster size."),
                            link("/dumpster/heavy-debris-rules", "Heavy Rules", "Validate fill-line and haul policies.")
                    )
            ));
            case "drywall-disposal-dumpster-rules" -> Optional.of(new SpecialSeoPageViewModel(
                    resolvedSlug,
                    "Drywall Disposal Dumpster Rules",
                    "Drywall Disposal Dumpster Rules: Size, Moisture, and Overage Risk",
                    "Plan drywall disposal with realistic sheet-to-ton ranges, moisture caveats, and mixed-load rules that affect overage.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Rule Intent",
                    "Drywall is often accepted as mixed debris, but moisture and board type can materially change risk and allowed loading strategy.",
                    "Use sheet count for speed, then validate moisture assumptions, mixed-load policy, and included tons before booking.",
                    List.of(
                            "Differentiate 1/2-inch and 5/8-inch board weights.",
                            "Increase assumptions for wet or demolition-loaded sheets.",
                            "Use safe option when schedule cannot absorb rework."
                    ),
                    List.of(
                            row("Per-sheet estimate", "Range by board type", "Use range output instead of single fixed value."),
                            row("Moisture effect", "Can move risk tier upward", "Dry vs wet assumptions should be compared."),
                            row("Mixed load behavior", "Packing inefficiency common", "Volume and weight both need margin.")
                    ),
                    List.of(
                            faq("Is drywall mostly volume or weight constrained?", "It can be both, especially when moisture is present."),
                            faq("Why does sheet-count planning miss sometimes?", "Board type, water content, and mixed debris affect final tons."),
                            faq("What is safest for remodel projects?", "Convert sheets, then run project preset with conservative assumptions.")
                    ),
                    "Check drywall disposal plan",
                    "/dumpster/size-weight-calculator?material=drywall&unit=drywall_sheet",
                    "See drywall dumpster calculator",
                    "/dumpster/weight/drywall",
                    List.of(
                            link("/dumpster/size/kitchen-remodel", "Kitchen Remodel Guide", "Use for cabinet + drywall mixed workflows."),
                            link("/dumpster/size/bathroom-remodel", "Bathroom Remodel Guide", "Use for tile + drywall scenario planning.")
                    )
            ));
            default -> Optional.empty();
        };
    }

    private static SpecialSeoPageViewModel.DecisionRow row(String factor, String baseline, String implication) {
        return new SpecialSeoPageViewModel.DecisionRow(factor, baseline, implication);
    }

    private static LinkItemViewModel link(String href, String label, String summary) {
        return new LinkItemViewModel(href, label, summary);
    }

    public List<LinkItemViewModel> featuredMaterialLinks(int limit) {
        return sortedIndexableMaterials().stream()
                .limit(limit)
                .map(material -> new LinkItemViewModel(
                        materialCanonicalPath(material.materialId()),
                        material.name() + " weight guide",
                        material.category().name().toLowerCase() + " debris profile with tonnage range"
                ))
                .toList();
    }

    public List<LinkItemViewModel> featuredProjectLinks(int limit) {
        return sortedIndexableProjects().stream()
                .limit(limit)
                .map(seed -> new LinkItemViewModel(
                        projectCanonicalPath(seed.projectId()),
                        seed.title(),
                        seed.sampleInput()
                ))
                .toList();
    }

    public List<LinkItemViewModel> materialGuideLinks() {
        return sortedIndexableMaterials().stream()
                .map(material -> new LinkItemViewModel(
                        materialCanonicalPath(material.materialId()),
                        material.name() + " weight guide",
                        material.category().name().toLowerCase() + " load behavior and decision notes"
                ))
                .toList();
    }

    public List<LinkItemViewModel> projectGuideLinks() {
        return sortedIndexableProjects().stream()
                .map(seed -> new LinkItemViewModel(
                        projectCanonicalPath(seed.projectId()),
                        seed.title(),
                        seed.sampleDecision()
                ))
                .toList();
    }

    public List<LinkItemViewModel> intentClusterLinksForMaterialHub() {
        LinkedHashMap<String, LinkItemViewModel> deduped = new LinkedHashMap<>();
        for (String projectId : projectSeeds.keySet()) {
            for (String materialId : projectIntentMaterialsForProject(projectId)) {
                String materialName = materialDisplayName(materialId);
                String href = intentPath(projectId, materialId, IntentType.WEIGHT_ESTIMATE);
                String label = "How much does " + materialName + " weigh for " + projectSeeds.get(projectId).title() + "?";
                deduped.putIfAbsent(href, new LinkItemViewModel(
                        href,
                        label,
                        "Weight-estimate intent page with size-level tonnage and overage risk context."
                ));
                if (deduped.size() >= 18) {
                    return List.copyOf(deduped.values());
                }
            }
        }
        return List.copyOf(deduped.values());
    }

    public List<LinkItemViewModel> intentClusterLinksForProjectHub() {
        LinkedHashMap<String, LinkItemViewModel> deduped = new LinkedHashMap<>();
        for (String projectId : projectSeeds.keySet()) {
            for (String materialId : projectIntentMaterialsForProject(projectId)) {
                String href = intentPath(projectId, materialId, IntentType.SIZE_GUIDE);
                String label = "What size dumpster for "
                        + projectSeeds.get(projectId).title()
                        + " with "
                        + materialDisplayName(materialId)
                        + "?";
                deduped.putIfAbsent(href, new LinkItemViewModel(
                        href,
                        label,
                        "Size-guide intent page with direct answer, checklist, and scenario constraints."
                ));
                if (deduped.size() >= 18) {
                    return List.copyOf(deduped.values());
                }
            }
        }
        return List.copyOf(deduped.values());
    }

    public List<GuideHubPageViewModel.MaterialGroupViewModel> materialGroupsByCategory() {
        List<MaterialFactor> materials = sortedIndexableMaterials();
        return List.of(
                new GuideHubPageViewModel.MaterialGroupViewModel(
                        "Heavy Debris",
                        "Heavy debris reaches haul limits quickly. Plan by tonnage first and assume conservative fill ratios.",
                        materials.stream()
                                .filter(material -> material.category() == MaterialCategory.HEAVY)
                                .map(material -> new LinkItemViewModel(
                                        materialCanonicalPath(material.materialId()),
                                        material.name() + " weight guide",
                                        "Typical density " + (int) material.densityTyp() + " lbs/yd3"
                                ))
                                .toList()
                ),
                new GuideHubPageViewModel.MaterialGroupViewModel(
                        "Mixed Debris",
                        "Mixed loads shift with composition and packing inefficiency. Safe recommendations work better for uncertain mixes.",
                        materials.stream()
                                .filter(material -> material.category() == MaterialCategory.MIXED)
                                .map(material -> new LinkItemViewModel(
                                        materialCanonicalPath(material.materialId()),
                                        material.name() + " weight guide",
                                        "Typical density " + (int) material.densityTyp() + " lbs/yd3"
                                ))
                                .toList()
                ),
                new GuideHubPageViewModel.MaterialGroupViewModel(
                        "Light Debris",
                        "Light debris is usually volume-driven, but moisture and odd-shaped items still create surprises.",
                        materials.stream()
                                .filter(material -> material.category() == MaterialCategory.LIGHT)
                                .map(material -> new LinkItemViewModel(
                                        materialCanonicalPath(material.materialId()),
                                        material.name() + " weight guide",
                                        "Typical density " + (int) material.densityTyp() + " lbs/yd3"
                                ))
                                .toList()
                )
        );
    }

    public List<GuideHubPageViewModel.MaterialSummaryRow> materialComparisonTable() {
        return sortedIndexableMaterials().stream()
                .map(material -> {
                    double exampleVolume = material.category() == MaterialCategory.HEAVY ? 4.0d : 8.0d;
                    double exampleWeight = round2((exampleVolume * material.densityTyp()) / 2000.0d);
                    return new GuideHubPageViewModel.MaterialSummaryRow(
                            material.name(),
                            materialCanonicalPath(material.materialId()),
                            categoryLabel(material.category()),
                            material.densityTyp(),
                            exampleWeight
                    );
                })
                .toList();
    }

    public List<FaqItemViewModel> materialHubFaq() {
        return List.of(
                faq("What matters most for heavy debris planning?", "Check max haul tons, heavy fill ratio, and clean-load requirements first."),
                faq("Why do included tons and haul limits differ?", "Included tons are pricing thresholds; haul limits are operational transport constraints."),
                faq("How can I reduce overage risk?", "Use weight-first assumptions and compare safe versus budget options before booking.")
        );
    }

    public List<FaqItemViewModel> projectHubFaq() {
        return List.of(
                faq("Should I choose project preset or manual material input?", "Start with project preset, then adjust material mix if your load is unusual."),
                faq("When should I plan multi-haul upfront?", "Plan multi-haul when dense material share is high or timeline risk is critical."),
                faq("What should I ask the vendor before booking?", "Ask included tons, overage fee per ton, and same-day swap availability.")
        );
    }

    public List<HeavyRulesViewModel.HeavyLimitRow> heavyLimitRows() {
        return dumpsterSizeRepository.findAllOrdered().stream()
                .map(policy -> new HeavyRulesViewModel.HeavyLimitRow(
                        policy.sizeYd(),
                        policy.dimensionsApprox(),
                        round2(policy.maxHaulTonsTyp()),
                        round2(policy.heavyDebrisMaxFillRatio() * 100.0d),
                        round2(policy.sizeYd() * policy.heavyDebrisMaxFillRatio()),
                        policy.cleanLoadRequiredForHeavy()
                ))
                .toList();
    }

    public String ogImageUrl(String baseUrl) {
        return absoluteUrl(baseUrl, "/og-image.png");
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

    public String heavyRulesIncludedVsMaxExplanation() {
        return "Included tons are pricing allowances in your quote, while max haul tons are transport limits that can reject pickup."
                + " A load can stay within included tons and still fail operational haul limits.";
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
                .map(material -> material.sourceVersionDate() == null ? DEFAULT_SEO_LAST_MODIFIED : material.sourceVersionDate())
                .orElse(DEFAULT_SEO_LAST_MODIFIED);
    }

    public LocalDate defaultLastModifiedDate() {
        return DEFAULT_SEO_LAST_MODIFIED;
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
        Set<String> allowedIds = Set.copyOf(indexableMaterialIds(seoMaxWave));
        Map<String, Integer> priorityRank = new LinkedHashMap<>();
        for (int i = 0; i < MATERIAL_PRIORITY.size(); i++) {
            priorityRank.put(MATERIAL_PRIORITY.get(i), i);
        }
        return materialFactorRepository.findAll().stream()
                .filter(material -> allowedIds.contains(material.materialId()))
                .sorted(Comparator
                        .comparingInt((MaterialFactor material) -> priorityRank.getOrDefault(material.materialId(), Integer.MAX_VALUE))
                        .thenComparing(material -> material.materialId()))
                .limit(20)
                .toList();
    }

    public boolean isMaterialEnabled(String materialId) {
        return MATERIAL_INDEX_WAVE.getOrDefault(materialId, Integer.MAX_VALUE) <= seoMaxWave;
    }

    public boolean isProjectEnabled(String projectId) {
        return PROJECT_INDEX_WAVE.getOrDefault(projectId, Integer.MAX_VALUE) <= seoMaxWave;
    }

    private List<ProjectSeed> sortedIndexableProjects() {
        return projectSeeds.values().stream()
                .filter(seed -> isProjectEnabled(seed.projectId()))
                .toList();
    }

    private List<MaterialPageViewModel.SizeWeightRow> sizeWeightRows(MaterialFactor material) {
        return dumpsterSizeRepository.findAllOrdered().stream()
                .map(policy -> {
                    double effectiveVolume = material.category() == MaterialCategory.HEAVY
                            ? policy.sizeYd() * policy.heavyDebrisMaxFillRatio()
                            : policy.sizeYd();
                    double weightLow = round2((effectiveVolume * material.densityLow()) / 2000.0d);
                    double weightTyp = round2((effectiveVolume * material.densityTyp()) / 2000.0d);
                    double weightHigh = round2((effectiveVolume * material.densityHigh()) / 2000.0d);
                    return new MaterialPageViewModel.SizeWeightRow(
                            policy.sizeYd(),
                            policy.dimensionsApprox(),
                            round2(effectiveVolume),
                            weightLow,
                            weightTyp,
                            weightHigh,
                            round2(policy.includedTonsTyp()),
                            overageRiskLabel(weightLow, weightHigh, policy.includedTonsTyp())
                    );
                })
                .toList();
    }

    private static String overageRiskLabel(double weightLow, double weightHigh, double includedTonsTyp) {
        if (weightHigh <= includedTonsTyp) {
            return "Low";
        }
        if (weightLow > includedTonsTyp) {
            return "High";
        }
        return "Medium";
    }

    private static String categoryLabel(MaterialCategory category) {
        return switch (category) {
            case HEAVY -> "Heavy debris";
            case MIXED -> "Mixed debris";
            case LIGHT -> "Light debris";
        };
    }

    private List<String> projectIntentMaterialsForProject(String projectId) {
        ProjectSeed seed = projectSeeds.get(projectId);
        if (seed == null) {
            return List.of();
        }
        List<String> configured = PROJECT_INTENT_MATERIALS.getOrDefault(
                projectId,
                List.of(seed.defaultMaterialId())
        );
        return configured.stream()
                .filter(materialId -> materialFactorRepository.findById(materialId).isPresent())
                .toList();
    }

    private String intentPath(String projectId, String materialId, IntentType intentType) {
        return INTENT_BASE_PATH + "/" + projectId + "/" + materialId + "/" + intentType.slug();
    }

    private String materialDisplayName(String materialId) {
        return materialFactorRepository.findById(materialId)
                .map(MaterialFactor::name)
                .orElse(materialId.replace('_', ' '));
    }

    private List<LinkItemViewModel> intentClusterLinksForMaterial(String materialId) {
        String materialName = materialDisplayName(materialId);
        LinkedHashMap<String, LinkItemViewModel> links = new LinkedHashMap<>();
        List<String> candidateProjects = projectSeeds.keySet().stream()
                .filter(projectId -> projectIntentMaterialsForProject(projectId).contains(materialId))
                .limit(2)
                .toList();

        for (String projectId : candidateProjects) {
            ProjectSeed seed = projectSeeds.get(projectId);
            for (IntentType intentType : INTENT_TYPES) {
                String href = intentPath(projectId, materialId, intentType);
                links.putIfAbsent(href, new LinkItemViewModel(
                        href,
                        intentType.linkLabel(seed.title(), materialName),
                        intentType.linkSummary()
                ));
                if (links.size() >= 6) {
                    return List.copyOf(links.values());
                }
            }
        }
        return List.copyOf(links.values());
    }

    private List<LinkItemViewModel> intentClusterLinksForProject(String projectId) {
        ProjectSeed seed = projectSeeds.get(projectId);
        if (seed == null) {
            return List.of();
        }
        LinkedHashMap<String, LinkItemViewModel> links = new LinkedHashMap<>();
        List<String> materials = projectIntentMaterialsForProject(projectId).stream()
                .limit(2)
                .toList();
        for (String materialId : materials) {
            String materialName = materialDisplayName(materialId);
            for (IntentType intentType : INTENT_TYPES) {
                String href = intentPath(projectId, materialId, intentType);
                links.putIfAbsent(href, new LinkItemViewModel(
                        href,
                        intentType.linkLabel(seed.title(), materialName),
                        intentType.linkSummary()
                ));
                if (links.size() >= 6) {
                    return List.copyOf(links.values());
                }
            }
        }
        return List.copyOf(links.values());
    }

    private static MaterialPageViewModel.SizeWeightRow anchorSizeRow(List<MaterialPageViewModel.SizeWeightRow> rows) {
        return rows.stream()
                .filter(row -> !"High".equals(row.overageRisk()))
                .findFirst()
                .orElse(rows.getFirst());
    }

    private static String intentQuestion(IntentType intentType, String projectTitle, String materialName) {
        return switch (intentType) {
            case SIZE_GUIDE -> "What size dumpster is safest for " + projectTitle + " with " + materialName + "?";
            case WEIGHT_ESTIMATE -> "How much does " + materialName + " weigh for " + projectTitle + "?";
            case OVERAGE_RISK -> "How likely are overage fees for " + materialName + " in " + projectTitle + "?";
        };
    }

    private static String intentDirectAnswer(
            IntentType intentType,
            MaterialPageViewModel materialPage,
            ProjectPageViewModel projectPage,
            MaterialPageViewModel.SizeWeightRow anchorRow
    ) {
        return switch (intentType) {
            case SIZE_GUIDE -> "Start with a "
                    + anchorRow.sizeYd()
                    + "-yard baseline for "
                    + projectPage.title().toLowerCase(Locale.US)
                    + " and validate "
                    + anchorRow.weightLowTons()
                    + " to "
                    + anchorRow.weightHighTons()
                    + " tons against your included-ton quote."
                    + " Keep the safe strategy if timing is tight or material mix is uncertain.";
            case WEIGHT_ESTIMATE -> "A typical "
                    + materialPage.exampleVolumeYd3()
                    + " yd3 load of "
                    + materialPage.materialName().toLowerCase(Locale.US)
                    + " is "
                    + materialPage.exampleWeightLowTons()
                    + " to "
                    + materialPage.exampleWeightHighTons()
                    + " tons."
                    + " That range is the baseline for comparing size-level overage and feasibility risk.";
            case OVERAGE_RISK -> "Overage risk is driven by the gap between high-side estimated tons and included tons."
                    + " For this material profile, "
                    + anchorRow.sizeYd()
                    + "-yard loads trend "
                    + anchorRow.overageRisk().toLowerCase(Locale.US)
                    + " risk at "
                    + anchorRow.weightHighTons()
                    + " high-side tons versus "
                    + anchorRow.includedTonsTyp()
                    + " included tons.";
        };
    }

    private static String intentSummary(
            IntentType intentType,
            MaterialPageViewModel materialPage,
            ProjectPageViewModel projectPage,
            MaterialPageViewModel.SizeWeightRow anchorRow
    ) {
        return switch (intentType) {
            case SIZE_GUIDE -> "Project strategy: " + projectPage.recommendedStrategy()
                    + " Anchor size: " + anchorRow.sizeYd() + " yd ("
                    + anchorRow.overageRisk() + " overage risk at typical assumptions).";
            case WEIGHT_ESTIMATE -> "Density reference: "
                    + materialPage.densityTyp()
                    + " lbs/yd3 typical, with moisture multiplier "
                    + materialPage.wetMultiplierLow()
                    + " to "
                    + materialPage.wetMultiplierHigh()
                    + ".";
            case OVERAGE_RISK -> "Risk is calculated with included tons, max-haul constraints, and scenario uncertainty."
                    + " Use this page to choose when to stay safe versus when budget mode is acceptable.";
        };
    }

    private static List<String> intentChecklist(
            IntentType intentType,
            MaterialPageViewModel materialPage,
            ProjectPageViewModel projectPage
    ) {
        List<String> base = List.of(
                "Confirm included tons and overage fee per ton before dispatch.",
                "Validate max haul tons and clean-load rules for heavy or mixed debris.",
                "Use wet-load assumptions when weather exposure is possible.",
                "If schedule is tight, keep the safe recommendation instead of volume-only downsizing.",
                "Ask this vendor question: " + projectPage.operatorQuestion()
        );
        return switch (intentType) {
            case SIZE_GUIDE -> base;
            case WEIGHT_ESTIMATE -> List.of(
                    "Start from measured quantity in the project's recommended unit: " + projectPage.recommendedUnit() + ".",
                    "Use density range " + materialPage.densityLow() + " to " + materialPage.densityHigh() + " lbs/yd3 for low/high scenarios.",
                    "Compare the full size table, not only one example load.",
                    "Apply moisture multiplier when recent rain or wet storage is likely.",
                    "Keep a buffer between high-side tons and included tons for pickup-day variance."
            );
            case OVERAGE_RISK -> List.of(
                    "Flag any size where high-side tons exceed included tons as overage-sensitive.",
                    "Check whether max-haul limits are stricter than price allowance in your market.",
                    "If risk is Medium or High, pre-plan swap timing and multi-haul logistics.",
                    "Separate dense material from mixed loads whenever possible.",
                    "Use the calculator preset to compare safe versus budget outcomes before booking."
            );
        };
    }

    private static List<FaqItemViewModel> intentFaq(
            IntentType intentType,
            MaterialPageViewModel materialPage,
            ProjectPageViewModel projectPage,
            MaterialPageViewModel.SizeWeightRow anchorRow
    ) {
        return switch (intentType) {
            case SIZE_GUIDE -> List.of(
                    faq(
                            "What dumpster size should I start with for this scenario?",
                            "Start with the " + anchorRow.sizeYd() + "-yard baseline and verify both included tons and max-haul policy."
                                    + " Volume alone is not enough when material density can push pickup constraints before the bin looks full."
                    ),
                    faq(
                            "When should I choose a safer size instead of budget size?",
                            "Use the safer recommendation when timing is strict, moisture is possible, or load mix is uncertain."
                                    + " Those conditions widen the weight range and make budget-sized assumptions less reliable on pickup day."
                    ),
                    faq(
                            "What should I confirm with the vendor first?",
                            "Confirm included tons, overage fee per ton, same-day swap availability, and any clean-load requirements."
                                    + " These four checks prevent most avoidable surprises for " + projectPage.title().toLowerCase(Locale.US) + "."
                    )
            );
            case WEIGHT_ESTIMATE -> List.of(
                    faq(
                            "How is this weight estimate calculated?",
                            "The estimate multiplies material density range by effective loaded volume, then converts pounds to tons."
                                    + " It includes scenario variance so you can compare low, typical, and high outcomes before choosing a container size."
                    ),
                    faq(
                            "Why can real loads differ from one-number estimates?",
                            "Moisture, packing behavior, contamination, and mixed debris can all shift final tonnage."
                                    + " That is why this page keeps a range and pairs it with size-level included-ton benchmarks."
                    ),
                    faq(
                            "Does moisture materially change the estimate?",
                            "Yes. For this material profile, wet-load assumptions can move tonnage enough to change risk tier."
                                    + " If conditions are damp, plan against the high-side range rather than typical values."
                    )
            );
            case OVERAGE_RISK -> List.of(
                    faq(
                            "How is overage risk classified on this page?",
                            "Risk is low when high-side tons stay below included tons, high when low-side tons already exceed included tons,"
                                    + " and medium when estimates straddle the allowance threshold."
                    ),
                    faq(
                            "Can a load be under included tons but still fail pickup?",
                            "Yes. Included tons are a pricing threshold, but max-haul tons are operational transport limits."
                                    + " A load can be priced correctly and still be non-feasible if haul constraints are stricter."
                    ),
                    faq(
                            "What is the fastest way to lower overage exposure?",
                            "Reduce uncertainty first: separate dense debris, avoid wet loading, and keep a margin versus included tons."
                                    + " If the scenario remains medium or high risk, pre-plan multi-haul instead of hoping one pull works."
                    )
            );
        };
    }

    private List<LinkItemViewModel> relatedIntentLinks(String projectId, String materialId, IntentType currentIntent) {
        LinkedHashMap<String, LinkItemViewModel> links = new LinkedHashMap<>();
        String projectTitle = projectSeeds.get(projectId).title();
        String materialName = materialDisplayName(materialId);

        for (IntentType intentType : INTENT_TYPES) {
            if (intentType == currentIntent) {
                continue;
            }
            String href = intentPath(projectId, materialId, intentType);
            links.putIfAbsent(href, new LinkItemViewModel(
                    href,
                    intentType.linkLabel(projectTitle, materialName),
                    intentType.linkSummary()
            ));
        }

        for (String candidateMaterialId : projectIntentMaterialsForProject(projectId)) {
            if (candidateMaterialId.equals(materialId)) {
                continue;
            }
            String href = intentPath(projectId, candidateMaterialId, IntentType.SIZE_GUIDE);
            links.putIfAbsent(href, new LinkItemViewModel(
                    href,
                    IntentType.SIZE_GUIDE.linkLabel(projectTitle, materialDisplayName(candidateMaterialId)),
                    "Project-level size intent comparison for a different dominant material."
            ));
            if (links.size() >= 8) {
                return List.copyOf(links.values());
            }
        }

        for (String candidateProjectId : projectSeeds.keySet()) {
            if (candidateProjectId.equals(projectId)) {
                continue;
            }
            if (!projectIntentMaterialsForProject(candidateProjectId).contains(materialId)) {
                continue;
            }
            String href = intentPath(candidateProjectId, materialId, IntentType.WEIGHT_ESTIMATE);
            links.putIfAbsent(href, new LinkItemViewModel(
                    href,
                    IntentType.WEIGHT_ESTIMATE.linkLabel(projectSeeds.get(candidateProjectId).title(), materialName),
                    "Same material, alternate project context for cross-intent comparison."
            ));
            if (links.size() >= 8) {
                return List.copyOf(links.values());
            }
        }
        return List.copyOf(links.values());
    }

    private List<LinkItemViewModel> relatedProjectsForMaterial(String materialId) {
        List<LinkItemViewModel> directMatches = sortedIndexableProjects().stream()
                .filter(seed -> seed.defaultMaterialId().equals(materialId))
                .map(seed -> new LinkItemViewModel(
                        projectCanonicalPath(seed.projectId()),
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
                .filter(Objects::nonNull)
                .filter(seed -> isProjectEnabled(seed.projectId()))
                .map(seed -> new LinkItemViewModel(
                        projectCanonicalPath(seed.projectId()),
                        seed.title(),
                        seed.sampleDecision()
                ))
                .toList();
    }

    private List<LinkItemViewModel> relatedMaterialsForMaterial(MaterialFactor material) {
        List<MaterialFactor> sameCategory = sortedIndexableMaterials().stream()
                .filter(candidate -> !candidate.materialId().equals(material.materialId()))
                .filter(candidate -> candidate.category() == material.category())
                .limit(3)
                .toList();

        if (!sameCategory.isEmpty()) {
            return sameCategory.stream()
                    .map(candidate -> new LinkItemViewModel(
                            materialCanonicalPath(candidate.materialId()),
                            candidate.name() + " weight guide",
                            "Compare " + categoryLabel(candidate.category()).toLowerCase() + " behavior"
                    ))
                    .toList();
        }

        return sortedIndexableMaterials().stream()
                .filter(candidate -> !candidate.materialId().equals(material.materialId()))
                .limit(3)
                .map(candidate -> new LinkItemViewModel(
                        materialCanonicalPath(candidate.materialId()),
                        candidate.name() + " weight guide",
                        "Compare density and overage risk profile"
                ))
                .toList();
    }

    private List<LinkItemViewModel> relatedMaterialsForProject(ProjectSeed seed) {
        List<MaterialFactor> materials = sortedIndexableMaterials();
        List<LinkItemViewModel> links = materials.stream()
                .filter(material -> material.materialId().equals(seed.defaultMaterialId()))
                .map(material -> new LinkItemViewModel(
                        materialCanonicalPath(material.materialId()),
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
                        materialCanonicalPath(material.materialId()),
                        material.name() + " weight guide",
                        "Compare against this material profile"
                ))
                .toList();
    }

    private List<LinkItemViewModel> relatedProjectsForProject(ProjectSeed seed) {
        List<LinkItemViewModel> sameMaterial = sortedIndexableProjects().stream()
                .filter(candidate -> !candidate.projectId().equals(seed.projectId()))
                .filter(candidate -> candidate.defaultMaterialId().equals(seed.defaultMaterialId()))
                .limit(3)
                .map(candidate -> new LinkItemViewModel(
                        projectCanonicalPath(candidate.projectId()),
                        candidate.title(),
                        "Project pattern with similar material mix"
                ))
                .toList();

        if (!sameMaterial.isEmpty()) {
            return sameMaterial;
        }

        return sortedIndexableProjects().stream()
                .filter(candidate -> !candidate.projectId().equals(seed.projectId()))
                .limit(3)
                .map(candidate -> new LinkItemViewModel(
                        projectCanonicalPath(candidate.projectId()),
                        candidate.title(),
                        "Compare strategy and operator constraints"
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

    private enum IntentType {
        SIZE_GUIDE("size-guide", "Size Guide"),
        WEIGHT_ESTIMATE("weight-estimate", "Weight Estimate"),
        OVERAGE_RISK("overage-risk", "Overage Risk");

        private final String slug;
        private final String label;

        IntentType(String slug, String label) {
            this.slug = slug;
            this.label = label;
        }

        public String slug() {
            return slug;
        }

        public String label() {
            return label;
        }

        public static Optional<IntentType> fromSlug(String slug) {
            for (IntentType value : values()) {
                if (value.slug.equals(slug)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }

        public String seoTitle(String projectTitle, String materialName, MaterialPageViewModel.SizeWeightRow anchorRow) {
            return switch (this) {
                case SIZE_GUIDE -> "What size dumpster for " + projectTitle + " with " + materialName
                        + "? " + anchorRow.sizeYd() + "yd baseline + risk chart";
                case WEIGHT_ESTIMATE -> "How much does " + materialName + " weigh for " + projectTitle
                        + "? Tons estimate + size chart";
                case OVERAGE_RISK -> materialName + " overage risk for " + projectTitle
                        + ": included tons vs estimated load";
            };
        }

        public String metaDescription(String projectTitle, String materialName, MaterialPageViewModel.SizeWeightRow anchorRow) {
            return switch (this) {
                case SIZE_GUIDE -> "Direct size guidance for " + projectTitle + " using " + materialName
                        + " load assumptions. Starts from a " + anchorRow.sizeYd()
                        + "yd baseline and compares overage risk by size.";
                case WEIGHT_ESTIMATE -> "Weight estimate for " + materialName + " in " + projectTitle
                        + " scenarios, with low/typical/high tons and dumpster-size comparisons.";
                case OVERAGE_RISK -> "Overage-risk breakdown for " + materialName + " during " + projectTitle
                        + ". Compare included tons, high-side weight, and safer strategy checklists.";
            };
        }

        public String linkLabel(String projectTitle, String materialName) {
            return switch (this) {
                case SIZE_GUIDE -> "What size dumpster for " + projectTitle + " with " + materialName + "?";
                case WEIGHT_ESTIMATE -> "How much does " + materialName + " weigh for " + projectTitle + "?";
                case OVERAGE_RISK -> "Overage risk for " + materialName + " in " + projectTitle;
            };
        }

        public String linkSummary() {
            return switch (this) {
                case SIZE_GUIDE -> "Intent page focused on size-selection decision logic.";
                case WEIGHT_ESTIMATE -> "Intent page focused on range-based tonnage estimates.";
                case OVERAGE_RISK -> "Intent page focused on included tons versus risk exposure.";
            };
        }
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

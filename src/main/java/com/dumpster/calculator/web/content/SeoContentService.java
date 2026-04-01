package com.dumpster.calculator.web.content;

import com.dumpster.calculator.domain.reference.MaterialCategory;
import com.dumpster.calculator.domain.reference.MaterialFactor;
import com.dumpster.calculator.infra.persistence.DumpsterSizeRepository;
import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import com.dumpster.calculator.web.content.catalog.SeoCopyCatalog;
import com.dumpster.calculator.web.content.catalog.SeoCopyCatalog.CopyBlock;
import com.dumpster.calculator.web.content.catalog.SeoRoutingCatalog;
import com.dumpster.calculator.web.viewmodel.FaqItemViewModel;
import com.dumpster.calculator.web.viewmodel.GuideHubPageViewModel;
import com.dumpster.calculator.web.viewmodel.HeavyRulesViewModel;
import com.dumpster.calculator.web.viewmodel.IntentDecisionBlockViewModel;
import com.dumpster.calculator.web.viewmodel.IntentPageViewModel;
import com.dumpster.calculator.web.viewmodel.LinkItemViewModel;
import com.dumpster.calculator.web.viewmodel.MaterialPageViewModel;
import com.dumpster.calculator.web.viewmodel.ProjectPageViewModel;
import com.dumpster.calculator.web.viewmodel.SpecialSeoPageViewModel;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

    private static final int MAX_WAVE = 3;
    private static final LocalDate FALLBACK_CONTENT_LASTMOD = LocalDate.of(2026, 3, 4);
    private final MaterialFactorRepository materialFactorRepository;
    private final DumpsterSizeRepository dumpsterSizeRepository;
    private final Map<String, ProjectSeed> projectSeeds = new LinkedHashMap<>();
    private final int seoMaxWave;
    private final LocalDate resolvedDefaultLastModifiedDate;
    private final String intentIndexMode;
    private static final DateTimeFormatter SOURCE_MONTH_YEAR = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US);
    private static final List<String> MATERIAL_PRIORITY = List.of(
            "concrete",
            "dirt_soil",
            "asphalt_shingles",
            "brick",
            "gravel_rock",
            "drywall",
            "mixed_cd",
            "tile_ceramic",
            "decking_wood",
            "household_junk",
            "furniture",
            "cardboard_packaging",
            "asphalt_pavement",
            "lumber",
            "carpet_pad",
            "plaster",
            "insulation_wet",
            "yard_waste",
            "green_waste_brush",
            "metal_scrap_light"
    );
    private static final List<String> PROJECT_PRIORITY = List.of(
            "concrete_removal",
            "dirt_grading",
            "roof_tearoff",
            "bathroom_remodel",
            "kitchen_remodel",
            "deck_demolition",
            "garage_cleanout",
            "estate_cleanout",
            "yard_cleanup",
            "light_commercial_fitout"
    );
    private static final Map<String, Integer> MATERIAL_INDEX_WAVE = Map.of(
            "concrete", 1,
            "asphalt_shingles", 1,
            "drywall", 1,
            "dirt_soil", 1,
            "brick", 3
    );
    private static final Map<String, Integer> PROJECT_INDEX_WAVE = Map.ofEntries(
            Map.entry("bathroom_remodel", 2),
            Map.entry("roof_tearoff", 2),
            Map.entry("deck_demolition", 2),
            Map.entry("kitchen_remodel", 2),
            Map.entry("garage_cleanout", 3),
            Map.entry("estate_cleanout", 3),
            Map.entry("yard_cleanup", 3),
            Map.entry("dirt_grading", 3),
            Map.entry("concrete_removal", 3),
            Map.entry("light_commercial_fitout", 3)
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
    private static final Set<String> PRIORITY_INDEXABLE_MATERIAL_IDS = Set.of(
            "concrete",
            "asphalt_shingles",
            "drywall",
            "dirt_soil",
            "brick",
            "household_junk",
            "furniture",
            "mixed_cd"
    );
    private static final Set<String> PRIORITY_INDEXABLE_PROJECT_IDS = Set.of(
            "bathroom_remodel",
            "roof_tearoff",
            "deck_demolition",
            "garage_cleanout",
            "kitchen_remodel",
            "estate_cleanout",
            "dirt_grading",
            "concrete_removal"
    );
    private static final Set<String> EXPERIMENT_INDEXABLE_PROJECT_IDS = Set.of(
            "yard_cleanup",
            "light_commercial_fitout"
    );
    private static final Set<String> PRIORITY_INDEXABLE_SPECIAL_SLUGS = Set.of(
            "10-yard-dumpster-weight-limit-overage",
            "can-you-put-concrete-in-a-dumpster",
            "can-you-mix-concrete-and-wood-in-a-dumpster",
            "dumpster-vs-junk-removal-which-is-cheaper",
            "bagster-vs-dumpster",
            "fill-line-rules-for-heavy-debris",
            "one-20-yard-vs-two-10-yard",
            "pickup-truck-loads-to-dumpster-size",
            "roof-shingles-dumpster-size-calculator",
            "drywall-disposal-dumpster-rules"
    );
    private static final Set<String> EXPERIMENT_INDEXABLE_SPECIAL_SLUGS = Set.of(
            "what-size-dumpster-do-i-need"
    );
    private static final Set<String> MEDIUM_CONFIDENCE_MATERIAL_IDS = Set.of("brick");
    private static final Set<String> MEDIUM_CONFIDENCE_PROJECT_IDS = Set.of("garage_cleanout", "kitchen_remodel");
    private static final Set<String> MEDIUM_CONFIDENCE_SPECIAL_SLUGS = Set.of(
            "fill-line-rules-for-heavy-debris",
            "one-20-yard-vs-two-10-yard"
    );
    private static final Map<String, MaterialSeoOverride> MATERIAL_SEO_OVERRIDES = Map.of(
            "concrete",
            new MaterialSeoOverride(
                    "Concrete Dumpster Calculator",
                    "Concrete Dumpster Calculator: Size Tons and Overage Risk",
                    "Estimate concrete disposal by size and ton range before you rent. See when clean loads or multi-haul plans are safer than one larger bin.",
                    "Concrete dumpster size depends on ton limits first, not bin volume. Start with a weight-first estimate before booking."
            ),
            "asphalt_shingles",
            new MaterialSeoOverride(
                    "Shingles Dumpster Size Calculator",
                    "Shingles Dumpster Size Calculator: Squares Tons and Risk",
                    "Convert roofing squares into dumpster size and weight range with overage risk signals. Compare safer options before your tear-off starts.",
                    "Asphalt shingles are weight-first debris. Convert roofing squares to a realistic ton range before choosing dumpster size."
            ),
            "drywall",
            new MaterialSeoOverride(
                    "Drywall Dumpster Calculator",
                    "Drywall Dumpster Calculator: Sheets Size and Weight Risk",
                    "Estimate drywall disposal by sheet count and size with weight-risk ranges. See when a small bin stops being practical for the job.",
                    "Drywall sheet count can look light, but weight climbs quickly when moisture and mixed-load assumptions change."
            ),
            "dirt_soil",
            new MaterialSeoOverride(
                    "Dirt Dumpster Calculator",
                    "Dirt Dumpster Calculator: Weight Limits Size and Overage",
                    "See how fast dirt weight escalates by dumpster size. Check realistic limits and when heavy-debris rules become the main constraint.",
                    "Dirt weight escalates faster than volume suggests, so haul limits usually decide the safest dumpster size."
            ),
            "brick",
            new MaterialSeoOverride(
                    "Brick and Block Dumpster Calculator",
                    "Brick and Block Dumpster Calculator: Size Tons and Limits",
                    "Estimate brick and block disposal by dumpster size and ton range. Identify when clean-load or staged-haul strategy is the safer choice.",
                    "Brick and block loads are haul-limited heavy debris, so controlled fill and staged pulls are usually safer than one oversized load."
            )
    );
    private static final Map<String, ProjectSeoOverride> PROJECT_SEO_OVERRIDES = Map.of(
            "bathroom_remodel",
            new ProjectSeoOverride(
                    "Bathroom Remodel Dumpster Size Calculator",
                    "Bathroom Remodel Dumpster Size: 10 vs 20 Yard Calculator",
                    "Estimate bathroom remodel dumpster size from tile drywall and fixture scope. See when heavy tile shifts the safest option upward.",
                    "What size dumpster for a bathroom remodel? Start at 10-yard, then size up when tile or mortar dominates the job."
            ),
            "roof_tearoff",
            new ProjectSeoOverride(
                    "Roof Tear-Off Dumpster Size Calculator",
                    "Roof Tear-Off Dumpster Size Calculator: Squares to Size",
                    "Convert roofing squares into dumpster size and ton range with overage risk cues. Plan safer hauling before your tear-off begins.",
                    "Roof tear-off decisions should be weight-first. Convert squares to tonnage before picking container size."
            ),
            "deck_demolition",
            new ProjectSeoOverride(
                    "Deck Removal Dumpster Size Calculator",
                    "Deck Removal Dumpster Size Calculator: Material and Weight Risk",
                    "Estimate deck removal dumpster size by material mix and moisture conditions. See when safer sizing beats low-cost assumptions.",
                    "Deck removal size depends on bulk and moisture. Wet lumber and hardware often push jobs toward safer sizing."
            ),
            "garage_cleanout",
            new ProjectSeoOverride(
                    "Garage Cleanout Dumpster Size Calculator",
                    "Garage Cleanout Dumpster Size Calculator: What Size Fits Best?",
                    "Estimate garage cleanout size by volume and bulky item mix. Compare dumpster rental versus junk removal for risk and convenience.",
                    "Garage cleanout size depends more on bulky-item packing than it looks. Start with load count, then compare routes."
            ),
            "kitchen_remodel",
            new ProjectSeoOverride(
                    "Kitchen Remodel Dumpster Size Calculator",
                    "Kitchen Remodel Dumpster Size Calculator: Cabinets Drywall Flooring",
                    "Estimate kitchen remodel dumpster size by cabinet countertop drywall and flooring removal scope with risk-aware sizing guidance.",
                    "Kitchen remodel debris mixes bulky and dense materials. Cabinet and countertop share can quickly change the safest size."
            )
    );
    private static final String CALCULATOR_PATH = "/dumpster/size-weight-calculator";
    private static final String MATERIAL_GUIDES_PATH = "/dumpster/material-guides";
    private static final String PROJECT_GUIDES_PATH = "/dumpster/project-guides";
    private static final String HEAVY_RULES_PATH = "/dumpster/heavy-debris-rules";
    private static final String COMPARISON_HUB_PATH = "/dumpster/dumpster-vs-junk-removal-which-is-cheaper";
    private static final String PICKUP_CONVERTER_PATH = "/dumpster/pickup-truck-loads-to-dumpster-size";
    private static final String DECISION_OVERVIEW_PATH = "/dumpster/what-size-dumpster-do-i-need";
    private static final String QUOTE_MATCH_BETA_PATH = "/about/quote-match-beta";
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
    private static final List<IndexableIntentSeed> INDEXABLE_INTENT_SEEDS = List.of(
            new IndexableIntentSeed("roof_tearoff", "asphalt_shingles", "size-guide"),
            new IndexableIntentSeed("roof_tearoff", "asphalt_shingles", "overage-risk"),
            new IndexableIntentSeed("roof_tearoff", "asphalt_shingles", "weight-estimate"),
            new IndexableIntentSeed("concrete_removal", "concrete", "size-guide"),
            new IndexableIntentSeed("concrete_removal", "concrete", "weight-estimate"),
            new IndexableIntentSeed("concrete_removal", "concrete", "overage-risk"),
            new IndexableIntentSeed("dirt_grading", "dirt_soil", "size-guide"),
            new IndexableIntentSeed("dirt_grading", "dirt_soil", "overage-risk"),
            new IndexableIntentSeed("dirt_grading", "dirt_soil", "weight-estimate"),
            new IndexableIntentSeed("dirt_grading", "gravel_rock", "weight-estimate"),
            new IndexableIntentSeed("concrete_removal", "brick", "size-guide"),
            new IndexableIntentSeed("concrete_removal", "brick", "overage-risk"),
            new IndexableIntentSeed("kitchen_remodel", "drywall", "size-guide"),
            new IndexableIntentSeed("kitchen_remodel", "mixed_cd", "overage-risk"),
            new IndexableIntentSeed("bathroom_remodel", "drywall", "size-guide"),
            new IndexableIntentSeed("bathroom_remodel", "tile_ceramic", "size-guide"),
            new IndexableIntentSeed("bathroom_remodel", "tile_ceramic", "overage-risk"),
            new IndexableIntentSeed("bathroom_remodel", "tile_ceramic", "weight-estimate"),
            new IndexableIntentSeed("light_commercial_fitout", "drywall", "size-guide"),
            new IndexableIntentSeed("deck_demolition", "decking_wood", "size-guide"),
            new IndexableIntentSeed("deck_demolition", "decking_wood", "overage-risk"),
            new IndexableIntentSeed("garage_cleanout", "household_junk", "size-guide"),
            new IndexableIntentSeed("garage_cleanout", "furniture", "size-guide"),
            new IndexableIntentSeed("garage_cleanout", "cardboard_packaging", "weight-estimate"),
            new IndexableIntentSeed("estate_cleanout", "household_junk", "size-guide"),
            new IndexableIntentSeed("estate_cleanout", "household_junk", "overage-risk"),
            new IndexableIntentSeed("estate_cleanout", "furniture", "size-guide"),
            new IndexableIntentSeed("estate_cleanout", "furniture", "weight-estimate"),
            new IndexableIntentSeed("yard_cleanup", "yard_waste", "size-guide")
    );
    private static final List<IndexableIntentSeed> EXPERIMENT_INDEXABLE_INTENT_SEEDS = List.of(
            new IndexableIntentSeed("yard_cleanup", "yard_waste", "overage-risk"),
            new IndexableIntentSeed("yard_cleanup", "green_waste_brush", "size-guide"),
            new IndexableIntentSeed("bathroom_remodel", "drywall", "weight-estimate"),
            new IndexableIntentSeed("kitchen_remodel", "drywall", "weight-estimate"),
            new IndexableIntentSeed("kitchen_remodel", "mixed_cd", "size-guide")
    );
    private static final Set<IndexableIntentSeed> WAVE_THREE_INTENT_EXCLUSIONS = Set.of(
            new IndexableIntentSeed("roof_tearoff", "tile_ceramic", "size-guide")
    );
    private static final Map<String, CopyBlock> MATERIAL_COPY = SeoCopyCatalog.materialCopy();
    private static final Map<String, CopyBlock> PROJECT_COPY = SeoCopyCatalog.projectCopy();

    public SeoContentService(
            MaterialFactorRepository materialFactorRepository,
            DumpsterSizeRepository dumpsterSizeRepository,
            @Value("${app.seo.max-wave:3}") int seoMaxWave,
            @Value("${app.seo.intent-index-mode:expanded}") String intentIndexMode
    ) {
        this.materialFactorRepository = materialFactorRepository;
        this.dumpsterSizeRepository = dumpsterSizeRepository;
        this.seoMaxWave = Math.max(1, Math.min(seoMaxWave, MAX_WAVE));
        this.intentIndexMode = intentIndexMode == null ? "expanded" : intentIndexMode.trim().toLowerCase(Locale.US);
        this.resolvedDefaultLastModifiedDate = materialFactorRepository.findAll().stream()
                .map(MaterialFactor::sourceVersionDate)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(FALLBACK_CONTENT_LASTMOD);
        addProject(
                "roof_tearoff",
                "Roof tear-off dumpster size guide",
                "roof_square",
                "asphalt_shingles",
                "Choosing 20yd by volume alone can trigger overweight fees.",
                "Use weight-first sizing and check clean-load requirements.",
                "What weight limit applies per container for roof shingle loads?",
                "Input 22 roof squares with asphalt shingles and compare 15yd vs 20yd risk.",
                "Even when volume looks close, weight can force a safer or split-haul strategy."
        );
        addProject(
                "kitchen_remodel",
                "Kitchen remodel dumpster size guide",
                "pickup_load",
                "mixed_cd",
                "Underestimating cabinet and countertop weight is common.",
                "Pick safe size first, then compare budget option if risk is low.",
                "What included tons and overage fee apply to mixed construction debris?",
                "Input 5 pickup loads with mixed C&D and enable mixed-load bulking.",
                "Budget option is viable only when overage exposure stays acceptable."
        );
        addProject(
                "bathroom_remodel",
                "Bathroom remodel dumpster size guide",
                "pickup_load",
                "tile_ceramic",
                "Tile and mortar can turn a small cleanup into a heavy load.",
                "If tile share is high, use smaller bins with staged hauling.",
                "Can tile and drywall go in one container without extra fees?",
                "Input 4 pickup loads where tile is dominant and compare haul feasibility.",
                "Tile-dominant jobs often need weight-first planning over pure cubic volume."
        );
        addProject(
                "deck_demolition",
                "Deck demolition dumpster size guide",
                "pickup_load",
                "decking_wood",
                "Ignoring nails, railings, and wet lumber inflates risk.",
                "Estimate with mixed-load bulking and weather adjustment.",
                "Is treated lumber accepted at standard pricing?",
                "Input 7 pickup loads for decking wood with wet toggle after rain.",
                "Moisture and mixed hardware can move the job from budget to safe recommendation."
        );
        addProject(
                "garage_cleanout",
                "Garage cleanout dumpster size guide",
                "pickup_load",
                "household_junk",
                "Volume looks small until furniture and bulky items stack poorly.",
                "Choose risk-aware sizing with mixed-load inefficiency enabled.",
                "Are appliance or e-waste add-on fees charged separately?",
                "Input 6 pickup loads with furniture and household junk mixed together.",
                "Stack inefficiency can require more volume than visual estimates suggest."
        );
        addProject(
                "estate_cleanout",
                "Estate cleanout dumpster size guide",
                "pickup_load",
                "household_junk",
                "One-trip assumptions fail when bulky items and recyclables mix.",
                "Plan for two-stage loading or compare junk removal routing.",
                "Can partial pickups be scheduled in the same rental window?",
                "Input 10 pickup loads and evaluate dumpster vs junk-removal handoff.",
                "Large mixed cleanouts often convert better with a split strategy."
        );
        addProject(
                "yard_cleanup",
                "Yard cleanup dumpster size guide",
                "pickup_load",
                "yard_waste",
                "Wet yard waste can spike weight quickly after rain.",
                "Toggle wet-load assumptions before selecting budget option.",
                "Do wet green-waste loads have stricter weight limits?",
                "Input 8 pickup loads of yard waste and compare dry vs wet assumptions.",
                "Moisture variance is the main reason risk bands widen for green waste."
        );
        addProject(
                "dirt_grading",
                "Dirt and grading debris dumpster strategy guide",
                "sqft_4in",
                "dirt_soil",
                "Large bins can become operationally infeasible for dense soil.",
                "Use low fill ratio and multi-haul planning from the start.",
                "What weight limit applies per container for soil and rock?",
                "Input 240 sqft at 4in for dirt/soil and check feasibility before booking.",
                "For dense soil, transport constraints dominate container size decisions."
        );
        addProject(
                "concrete_removal",
                "Concrete removal dumpster strategy guide",
                "sqft_4in",
                "concrete",
                "Large bins are often not feasible for heavy concrete loads.",
                "Default to small container strategy with explicit haul count.",
                "Do you require clean concrete-only loads for pickup day?",
                "Input 180 sqft at 4in for concrete and validate multi-haul requirement.",
                "Concrete usually reaches haul limits long before volume capacity."
        );
        addProject(
                "light_commercial_fitout",
                "Light commercial fit-out dumpster plan guide",
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
                        case HEAVY -> "What weight limit applies per container for this material?";
                        case MIXED -> "Are mixed loads billed with sorting or contamination add-on fees?";
                        default -> "Do wet loads have seasonal restrictions or extra charges?";
                    };
                    MaterialScenario scenario = materialScenario(material);
                    CopyBlock copy = materialCopyFor(material);
                    MaterialSeoOverride seoOverride = MATERIAL_SEO_OVERRIDES.get(material.materialId());
                    List<MaterialPageViewModel.SizeWeightRow> sizeWeightTable = sizeWeightRows(material);
                    String exampleRange = round2(lowWeight) + " to " + round2(highWeight) + " tons";
                    String defaultAnswerFirst = material.name() + " is typically " + (int) material.densityTyp() + " lbs/yd3."
                            + " A " + (int) exampleVolume + " yd3 load is about " + exampleRange
                            + " (typical " + round2(typWeight) + " tons). " + copy.answerFirst();
                    String defaultSeoTitle = material.name() + " Dumpster Weight: " + (int) material.densityTyp()
                            + " lbs/yd3 Typical | Size & Overage Chart";
                    String defaultMetaDescription = material.name() + " weighs around " + (int) material.densityTyp()
                            + " lbs/yd3. A " + (int) exampleVolume + " yd3 load is "
                            + exampleRange + ". Compare dumpster-size weight ranges and overage risk.";
                    String pageTitle = seoOverride != null
                            ? seoOverride.pageTitle()
                            : material.name() + " Dumpster Weight Guide";
                    String answerFirst = seoOverride != null ? seoOverride.answerFirst() : defaultAnswerFirst;
                    String seoTitle = seoOverride != null ? seoOverride.seoTitle() : defaultSeoTitle;
                    String metaDescription = seoOverride != null ? seoOverride.metaDescription() : defaultMetaDescription;
                    EvidenceProfile evidenceProfile = materialEvidenceProfile(material);
                    LocalDate materialUpdatedDate = defaultLastModifiedDate();
                    LocalDate sourceVersionDate = material.sourceVersionDate();
                    String sourceDateDisplay = (sourceVersionDate == null ? materialUpdatedDate : sourceVersionDate)
                            .format(SOURCE_MONTH_YEAR);
                    return new MaterialPageViewModel(
                            material.materialId(),
                            material.name(),
                            pageTitle,
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
                            evidenceProfile.confidenceTier(),
                            evidenceProfile.varianceNote(),
                            evidenceProfile.vendorChecklist(),
                            scenario.input(),
                            scenario.decision(),
                            answerFirst,
                            sizeWeightTable,
                            copy.quickRules(),
                            copy.faqItems(),
                            absoluteUrl(baseUrl, MATERIAL_GUIDES_PATH),
                            intentClusterLinksForMaterial(material.materialId()),
                            materialDecisionStageLinks(material.materialId()),
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
        ProjectSeoOverride seoOverride = PROJECT_SEO_OVERRIDES.get(seed.projectId());
        String projectTopic = IntentType.humanProjectTopic(seed.title()).toLowerCase(Locale.US);
        String defaultSeoTitle = "Best dumpster size for " + projectTopic + " | homeowner decision guide";
        String defaultMetaDescription = "Find the safer disposal route for " + projectTopic + ": dumpster size baseline, "
                + defaultMaterialName + " weight risk, and when junk removal is the better move.";
        String pageTitle = seoOverride != null ? seoOverride.pageTitle() : seed.title();
        String seoTitle = seoOverride != null ? seoOverride.seoTitle() : defaultSeoTitle;
        String metaDescription = seoOverride != null ? seoOverride.metaDescription() : defaultMetaDescription;
        String answerFirst = seoOverride != null ? seoOverride.answerFirst() : copy.answerFirst();
        EvidenceProfile evidenceProfile = projectEvidenceProfile(seed, defaultMaterialName);
        String modifiedDateIso = defaultLastModifiedDate().toString();
        return Optional.of(new ProjectPageViewModel(
                seed.projectId(),
                pageTitle,
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
                evidenceProfile.confidenceTier(),
                evidenceProfile.varianceNote(),
                evidenceProfile.vendorChecklist(),
                answerFirst,
                copy.quickRules(),
                copy.faqItems(),
                absoluteUrl(baseUrl, PROJECT_GUIDES_PATH),
                intentClusterLinksForProject(seed.projectId()),
                projectDecisionStageLinks(seed.projectId(), seed.defaultMaterialId()),
                relatedMaterialsForProject(seed),
                relatedProjectsForProject(seed)
        ));
    }

    public List<String> projectIndexPaths() {
        return projectIndexPaths(3);
    }

    public List<String> projectIndexPaths(int maxWave) {
        int waveLimit = Math.max(1, Math.min(maxWave, MAX_WAVE));
        return PROJECT_INDEX_WAVE.entrySet().stream()
                .filter(entry -> entry.getValue() <= waveLimit)
                .map(Map.Entry::getKey)
                .filter(this::isProjectIndexable)
                .filter(PRIORITY_INDEXABLE_PROJECT_IDS::contains)
                .map(this::projectCanonicalPath)
                .toList();
    }

    public List<String> experimentProjectIndexPaths(int maxWave) {
        int waveLimit = Math.max(1, Math.min(maxWave, MAX_WAVE));
        return PROJECT_INDEX_WAVE.entrySet().stream()
                .filter(entry -> entry.getValue() <= waveLimit)
                .map(Map.Entry::getKey)
                .filter(this::isProjectIndexable)
                .filter(EXPERIMENT_INDEXABLE_PROJECT_IDS::contains)
                .map(this::projectCanonicalPath)
                .toList();
    }

    public List<String> intentIndexPaths() {
        return projectSeeds.keySet().stream()
                .filter(this::isProjectEnabled)
                .flatMap(projectId -> projectIntentMaterialsForProject(projectId).stream()
                        .flatMap(materialId -> INTENT_TYPES.stream()
                                .map(intentType -> intentPath(projectId, materialId, intentType))))
                .toList();
    }

    public List<String> priorityIntentPaths() {
        return activePrimaryIntentSeeds().stream()
                .map(seed -> intentPath(seed.projectId(), seed.materialId(), seed.intentSlug()))
                .toList();
    }

    public List<String> experimentIntentPaths() {
        return activeExperimentIntentSeeds().stream()
                .map(seed -> intentPath(seed.projectId(), seed.materialId(), seed.intentSlug()))
                .toList();
    }

    public List<String> indexableIntentPaths() {
        LinkedHashSet<String> paths = new LinkedHashSet<>(priorityIntentPaths());
        paths.addAll(experimentIntentPaths());
        return List.copyOf(paths);
    }

    public boolean isIndexableIntentPath(String projectId, String materialId, String intentSlug) {
        String canonicalPath = intentPath(projectId, materialId, intentSlug);
        return indexableIntentPaths().contains(canonicalPath);
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
        String projectTopic = IntentType.humanProjectTopic(projectTitle).toLowerCase(Locale.US);
        MaterialPageViewModel.SizeWeightRow anchorRow = anchorSizeRow(materialPage.sizeWeightTable());
        String directAnswer = intentDirectAnswer(intentType, materialPage, projectPage, anchorRow);
        String intentQuestion = intentQuestion(intentType, projectTitle, materialName);
        String intentSummary = intentSummary(intentType, materialPage, projectPage, anchorRow);
        EvidenceProfile evidenceProfile = intentEvidenceProfile(projectId, materialId, intentType, materialName);
        String evidenceNote = "Evidence baseline: density "
                + round2(materialPage.densityLow()) + " to "
                + round2(materialPage.densityHigh()) + " lbs/yd3,"
                + " size-level included tonnage, and project workflow assumptions for "
                + projectTopic + ".";
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
                evidenceProfile.confidenceTier(),
                evidenceProfile.varianceNote(),
                evidenceProfile.vendorChecklist(),
                modifiedDateIso,
                materialPage.sizeWeightTable(),
                intentChecklist(intentType, materialPage, projectPage),
                intentDecisionBlocks(intentType, materialPage, projectPage, materialOptional.get(), anchorRow),
                intentFaq(intentType, materialPage, projectPage, anchorRow),
                decisionStageLinks(projectId, materialId, intentType),
                relatedIntentLinks(projectId, materialId, intentType),
                relatedMaterialsForMaterial(materialOptional.get()),
                relatedProjectsForMaterial(materialId)
        ));
    }

    public List<String> indexableMaterialIds() {
        return indexableMaterialIds(3);
    }

    public List<String> indexableMaterialIds(int maxWave) {
        int waveLimit = Math.max(1, Math.min(maxWave, MAX_WAVE));
        List<String> available = materialFactorRepository.findAll().stream()
                .map(MaterialFactor::materialId)
                .toList();
        return MATERIAL_INDEX_WAVE.entrySet().stream()
                .filter(entry -> entry.getValue() <= waveLimit)
                .map(Map.Entry::getKey)
                .filter(PRIORITY_INDEXABLE_MATERIAL_IDS::contains)
                .filter(available::contains)
                .toList();
    }

    public String resolveMaterialId(String materialPathToken) {
        return SeoRoutingCatalog.resolveMaterialId(
                materialPathToken,
                candidate -> materialFactorRepository.findById(candidate).isPresent()
        );
    }

    public String resolveProjectId(String projectPathToken) {
        return SeoRoutingCatalog.resolveProjectId(projectPathToken, projectSeeds::containsKey);
    }

    public boolean isIntentSlugSupported(String intentSlug) {
        return IntentType.fromSlug(intentSlug).isPresent();
    }

    public String intentCanonicalPath(String projectId, String materialId, String intentSlug) {
        return intentPath(projectId, materialId, intentSlug);
    }

    public String materialCanonicalPath(String materialId) {
        return SeoRoutingCatalog.materialCanonicalPath(materialId);
    }

    public String projectCanonicalPath(String projectId) {
        return SeoRoutingCatalog.projectCanonicalPath(projectId);
    }

    public String resolveSpecialSlug(String slug) {
        return SeoRoutingCatalog.resolveSpecialSlug(slug);
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
        int waveLimit = Math.max(1, Math.min(maxWave, MAX_WAVE));
        return SPECIAL_PAGE_INDEX_WAVE.entrySet().stream()
                .filter(entry -> entry.getValue() <= waveLimit)
                .map(Map.Entry::getKey)
                .filter(PRIORITY_INDEXABLE_SPECIAL_SLUGS::contains)
                .map(entry -> "/dumpster/" + entry)
                .toList();
    }

    public List<String> experimentSpecialPageIndexPaths(int maxWave) {
        int waveLimit = Math.max(1, Math.min(maxWave, MAX_WAVE));
        return SPECIAL_PAGE_INDEX_WAVE.entrySet().stream()
                .filter(entry -> entry.getValue() <= waveLimit)
                .map(Map.Entry::getKey)
                .filter(EXPERIMENT_INDEXABLE_SPECIAL_SLUGS::contains)
                .map(entry -> "/dumpster/" + entry)
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
                    "How Many Tons Can a 10-Yard Dumpster Hold?",
                    "How Many Tons Can a 10-Yard Dumpster Hold? Limits and Risk",
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
                    "Compare dumpster rental and junk removal for homeowner jobs using labor, urgency, heavy-load risk, and worked cost examples.",
                    absoluteUrl(baseUrl, canonicalPath),
                    ogImageUrl(baseUrl),
                    absoluteUrl(baseUrl, CALCULATOR_PATH),
                    modifiedDateIso,
                    "Comparison Intent",
                    "Dumpster often wins on larger planned jobs, while junk removal often wins on small urgent jobs where convenience is critical.",
                    "Pick the route with the best execution profile for your situation. Headline price alone misses labor burden, speed requirements, and heavy-load feasibility risk.",
                    List.of(
                            "Do not choose by headline price only. Include labor and timeline cost.",
                            "If debris is dense, verify feasibility before booking any single-haul plan.",
                            "If you are unsure about composition, compare routes before you request quotes.",
                            "Use worked examples, then run your live estimate with your actual material mix."
                    ),
                    List.of(
                            row("Small urgent cleanout", "High convenience priority", "Junk removal can be the lower-friction path."),
                            row("Large staged remodel", "Predictable loading window", "Dumpster usually gives better unit economics."),
                            row("Single bulky-item cleanup", "Unknown stacking and carry burden", "Junk removal can prevent labor bottlenecks."),
                            row("Uncertain composition", "Debris mix unknown before teardown", "Compare both routes before booking to avoid false assumptions."),
                            row("Labor burden", "DIY loading required for dumpster", "If labor is unavailable, junk removal value increases."),
                            row("Speed requirement", "Need same-day completion", "Junk route frequently wins on completion time."),
                            row("Heavy dense debris", "Strict haul and fill constraints", "Route by feasibility first, then compare cost."),
                            row("No labor availability", "Weekend-only or one-person crew", "Convenience-first junk routing can beat nominal dumpster savings."),
                            row("Two smaller vs one larger", "High pickup-failure risk on dense loads", "Smaller staged hauls may beat one oversized attempt.")
                    ),
                    List.of(
                            faq("Is junk removal always more expensive?", "No. For small urgent jobs, convenience and labor inclusion can offset price differences."),
                            faq("When is dumpster usually better?", "When you can stage loading and total volume is large enough to spread rental cost."),
                            faq("What changes the answer fastest?", "Urgency, available labor, and heavy-material feasibility constraints."),
                            faq("Should I compare junk removal before running the calculator?", "Yes. If your top priority is speed or low effort, comparing routes first prevents overfocusing on dumpster-only assumptions."),
                            faq("What if I am unsure about debris weight?", "Use the weight-estimate and overage-risk intent pages before booking. If uncertainty remains high, convenience-first routing is often safer."),
                            faq("Can two smaller hauls beat one larger dumpster?", "Yes, especially when dense debris pushes haul limits and pickup risk."),
                            faq("How should a homeowner choose quickly?", "Start with feasibility and overage risk, then compare speed and labor tradeoffs.")
                    ),
                    "Run the live estimate",
                    "/dumpster/size-weight-calculator?project=garage_cleanout&material=household_junk&unit=pickup_load&qty=4",
                    "Check heavy-load rules first",
                    HEAVY_RULES_PATH,
                    List.of(
                            link(PICKUP_CONVERTER_PATH, "Pickup Load Converter", "Estimate whether job size favors dumpster flow."),
                            link(HEAVY_RULES_PATH, "Heavy Rules", "Check feasibility constraints before comparing cost."),
                            link("/dumpster/one-20-yard-vs-two-10-yard", "One 20-Yard vs Two 10-Yard", "See when staged smaller hauls are safer."),
                            link("/dumpster/answers/garage-cleanout/household-junk/size-guide", "Garage cleanout size guide", "See homeowner-focused size guidance for uncertain mixed-junk cleanup."),
                            link("/dumpster/answers/concrete-removal/concrete/overage-risk", "Concrete overage-risk answer", "If heavy material appears, route by feasibility first.")
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
                        material.name() + " debris guide",
                        material.category().name().toLowerCase() + " material profile with size and tonnage risk"
                ))
                .toList();
    }

    public List<LinkItemViewModel> featuredProjectLinks(int limit) {
        return sortedIndexableProjects().stream()
                .limit(limit)
                .map(seed -> new LinkItemViewModel(
                        projectCanonicalPath(seed.projectId()),
                        seed.title(),
                        seed.sampleDecision()
                ))
                .toList();
    }

    public List<LinkItemViewModel> materialGuideLinks() {
        return sortedIndexableMaterials().stream()
                .map(material -> new LinkItemViewModel(
                        materialCanonicalPath(material.materialId()),
                        material.name() + " debris guide",
                        material.category().name().toLowerCase() + " load behavior, tonnage, and decision notes"
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
        for (IndexableIntentSeed seed : allActiveIndexableIntentSeeds()) {
            Optional<IntentType> intentTypeOptional = IntentType.fromSlug(seed.intentSlug());
            ProjectSeed projectSeed = projectSeeds.get(seed.projectId());
            if (intentTypeOptional.isEmpty() || projectSeed == null) {
                continue;
            }
            IntentType intentType = intentTypeOptional.get();
            String materialName = materialDisplayName(seed.materialId());
            String href = intentPath(seed.projectId(), seed.materialId(), intentType);
            deduped.putIfAbsent(href, new LinkItemViewModel(
                    href,
                    intentType.linkLabel(projectSeed.title(), materialName),
                    intentType.linkSummary()
            ));
            if (deduped.size() >= 20) {
                return List.copyOf(deduped.values());
            }
        }
        return List.copyOf(deduped.values());
    }

    public List<LinkItemViewModel> intentClusterLinksForProjectHub() {
        LinkedHashMap<String, LinkItemViewModel> deduped = new LinkedHashMap<>();
        for (IndexableIntentSeed seed : allActiveIndexableIntentSeeds()) {
            Optional<IntentType> intentTypeOptional = IntentType.fromSlug(seed.intentSlug());
            ProjectSeed projectSeed = projectSeeds.get(seed.projectId());
            if (intentTypeOptional.isEmpty() || projectSeed == null) {
                continue;
            }
            IntentType intentType = intentTypeOptional.get();
            String materialName = materialDisplayName(seed.materialId());
            String href = intentPath(seed.projectId(), seed.materialId(), intentType);
            deduped.putIfAbsent(href, new LinkItemViewModel(
                    href,
                    intentType.linkLabel(projectSeed.title(), materialName),
                    intentType.linkSummary()
            ));
            if (deduped.size() >= 20) {
                return List.copyOf(deduped.values());
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
                .map(MaterialFactor::sourceVersionDate)
                .filter(Objects::nonNull)
                .orElse(defaultLastModifiedDate());
    }

    public LocalDate defaultLastModifiedDate() {
        return resolvedDefaultLastModifiedDate;
    }

    public String calculatorConfidenceTier() {
        return "High";
    }

    public String calculatorVarianceNote() {
        return "Calculator outputs are range-based estimates. Operator haul limits, clean-load policy, and local handling rules vary.";
    }

    public List<String> calculatorVendorChecklist() {
        return List.of(
                "What included tons are in this quote, and what is overage per ton?",
                "What max haul limit and heavy fill-line rule apply to this material mix?",
                "If this load is borderline, can you guarantee same-day swap or staged pulls?"
        );
    }

    public String heavyRulesConfidenceTier() {
        return "High";
    }

    public String heavyRulesVarianceNote() {
        return "Heavy-debris rules are directionally consistent, but fill-line enforcement and clean-load requirements vary by operator.";
    }

    public List<String> heavyRulesVendorChecklist() {
        return List.of(
                "What max haul limit applies to this specific heavy material?",
                "Is heavy-debris fill-line enforced below the top edge on pickup day?",
                "Are clean-load separation and rejection triggers documented in the quote?"
        );
    }

    public String specialPageConfidenceTier(String slug) {
        return specialEvidenceProfile(slug).confidenceTier();
    }

    public String specialPageVarianceNote(String slug) {
        return specialEvidenceProfile(slug).varianceNote();
    }

    public List<String> specialPageVendorChecklist(String slug) {
        return specialEvidenceProfile(slug).vendorChecklist();
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
        return MATERIAL_INDEX_WAVE.getOrDefault(materialId, MAX_WAVE) <= seoMaxWave
                && materialFactorRepository.findById(materialId).isPresent();
    }

    public boolean isMaterialIndexable(String materialId) {
        return isMaterialEnabled(materialId) && PRIORITY_INDEXABLE_MATERIAL_IDS.contains(materialId);
    }

    public boolean isProjectEnabled(String projectId) {
        return PROJECT_INDEX_WAVE.getOrDefault(projectId, MAX_WAVE) <= seoMaxWave
                && projectSeeds.containsKey(projectId);
    }

    public boolean isProjectIndexable(String projectId) {
        return isProjectEnabled(projectId)
                && (PRIORITY_INDEXABLE_PROJECT_IDS.contains(projectId)
                || EXPERIMENT_INDEXABLE_PROJECT_IDS.contains(projectId));
    }

    public boolean isSpecialPageIndexable(String slug) {
        String resolvedSlug = resolveSpecialSlug(slug);
        return isSpecialPageEnabled(resolvedSlug)
                && (PRIORITY_INDEXABLE_SPECIAL_SLUGS.contains(resolvedSlug)
                || EXPERIMENT_INDEXABLE_SPECIAL_SLUGS.contains(resolvedSlug));
    }

    public boolean isMaterialGuidesIndexable() {
        return false;
    }

    public boolean isProjectGuidesIndexable() {
        return false;
    }

    private List<ProjectSeed> sortedIndexableProjects() {
        Map<String, Integer> priorityRank = new LinkedHashMap<>();
        for (int i = 0; i < PROJECT_PRIORITY.size(); i++) {
            priorityRank.put(PROJECT_PRIORITY.get(i), i);
        }
        return projectSeeds.values().stream()
                .filter(seed -> isProjectEnabled(seed.projectId()))
                .sorted(Comparator
                        .comparingInt((ProjectSeed seed) -> priorityRank.getOrDefault(seed.projectId(), Integer.MAX_VALUE))
                        .thenComparing(ProjectSeed::projectId))
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
        if (seed == null || !isProjectEnabled(projectId)) {
            return List.of();
        }
        List<String> configured = PROJECT_INTENT_MATERIALS.getOrDefault(
                projectId,
                List.of(seed.defaultMaterialId())
        );
        return configured.stream()
                .filter(this::isMaterialEnabled)
                .toList();
    }

    private String intentPath(String projectId, String materialId, IntentType intentType) {
        return INTENT_BASE_PATH
                + "/" + projectPublicSlug(projectId)
                + "/" + materialPublicSlug(materialId)
                + "/" + intentType.slug();
    }

    private String intentPath(String projectId, String materialId, String intentSlug) {
        return INTENT_BASE_PATH
                + "/" + projectPublicSlug(projectId)
                + "/" + materialPublicSlug(materialId)
                + "/" + intentSlug;
    }

    private String materialPublicSlug(String materialId) {
        return SeoRoutingCatalog.materialPublicSlug(materialId);
    }

    private String projectPublicSlug(String projectId) {
        return SeoRoutingCatalog.projectPublicSlug(projectId);
    }

    private List<IndexableIntentSeed> activePrimaryIntentSeeds() {
        if (seoMaxWave >= 3 && "expanded".equals(intentIndexMode)) {
            LinkedHashMap<String, IndexableIntentSeed> expanded = new LinkedHashMap<>();
            for (String projectId : projectSeeds.keySet()) {
                if (!isProjectEnabled(projectId)) {
                    continue;
                }
                for (String materialId : projectIntentMaterialsForProject(projectId)) {
                    for (IntentType intentType : INTENT_TYPES) {
                        IndexableIntentSeed seed = new IndexableIntentSeed(projectId, materialId, intentType.slug());
                        if (WAVE_THREE_INTENT_EXCLUSIONS.contains(seed)) {
                            continue;
                        }
                        String key = projectId + "|" + materialId + "|" + intentType.slug();
                        expanded.putIfAbsent(key, seed);
                    }
                }
            }
            return List.copyOf(expanded.values());
        }

        return INDEXABLE_INTENT_SEEDS.stream()
                .filter(seed -> isProjectEnabled(seed.projectId()))
                .filter(seed -> projectIntentMaterialsForProject(seed.projectId()).contains(seed.materialId()))
                .filter(seed -> IntentType.fromSlug(seed.intentSlug()).isPresent())
                .toList();
    }

    private List<IndexableIntentSeed> activeExperimentIntentSeeds() {
        return EXPERIMENT_INDEXABLE_INTENT_SEEDS.stream()
                .filter(seed -> isProjectEnabled(seed.projectId()))
                .filter(seed -> projectIntentMaterialsForProject(seed.projectId()).contains(seed.materialId()))
                .filter(seed -> IntentType.fromSlug(seed.intentSlug()).isPresent())
                .toList();
    }

    private List<IndexableIntentSeed> allActiveIndexableIntentSeeds() {
        LinkedHashMap<String, IndexableIntentSeed> combined = new LinkedHashMap<>();
        for (IndexableIntentSeed seed : activePrimaryIntentSeeds()) {
            combined.put(seed.projectId() + "|" + seed.materialId() + "|" + seed.intentSlug(), seed);
        }
        for (IndexableIntentSeed seed : activeExperimentIntentSeeds()) {
            combined.putIfAbsent(seed.projectId() + "|" + seed.materialId() + "|" + seed.intentSlug(), seed);
        }
        return List.copyOf(combined.values());
    }

    private String materialDisplayName(String materialId) {
        return materialFactorRepository.findById(materialId)
                .map(MaterialFactor::name)
                .orElse(materialId.replace('_', ' '));
    }

    private List<LinkItemViewModel> intentClusterLinksForMaterial(String materialId) {
        String materialName = materialDisplayName(materialId);
        LinkedHashMap<String, LinkItemViewModel> links = new LinkedHashMap<>();
        for (IndexableIntentSeed seed : allActiveIndexableIntentSeeds()) {
            if (!seed.materialId().equals(materialId)) {
                continue;
            }
            Optional<IntentType> intentTypeOptional = IntentType.fromSlug(seed.intentSlug());
            ProjectSeed projectSeed = projectSeeds.get(seed.projectId());
            if (intentTypeOptional.isEmpty() || projectSeed == null) {
                continue;
            }
            IntentType intentType = intentTypeOptional.get();
            String href = intentPath(seed.projectId(), materialId, intentType);
            links.putIfAbsent(href, new LinkItemViewModel(
                    href,
                    intentType.linkLabel(projectSeed.title(), materialName),
                    intentType.linkSummary()
            ));
            if (links.size() >= 6) {
                return List.copyOf(links.values());
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
        for (IndexableIntentSeed indexableSeed : allActiveIndexableIntentSeeds()) {
            if (!indexableSeed.projectId().equals(projectId)) {
                continue;
            }
            Optional<IntentType> intentTypeOptional = IntentType.fromSlug(indexableSeed.intentSlug());
            if (intentTypeOptional.isEmpty()) {
                continue;
            }
            IntentType intentType = intentTypeOptional.get();
            String materialName = materialDisplayName(indexableSeed.materialId());
            String href = intentPath(projectId, indexableSeed.materialId(), intentType);
            links.putIfAbsent(href, new LinkItemViewModel(
                    href,
                    intentType.linkLabel(seed.title(), materialName),
                    intentType.linkSummary()
            ));
            if (links.size() >= 6) {
                return List.copyOf(links.values());
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
        String projectTopic = IntentType.humanProjectTopic(projectTitle).toLowerCase(Locale.US);
        String humanMaterial = materialName.toLowerCase(Locale.US);
        return switch (intentType) {
            case SIZE_GUIDE -> "Best dumpster size for " + projectTopic + " with " + humanMaterial;
            case WEIGHT_ESTIMATE -> "How much does " + humanMaterial + " weigh in a dumpster for " + projectTopic + "?";
            case OVERAGE_RISK -> "Will " + humanMaterial + " exceed dumpster weight limits for " + projectTopic + "?";
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
                    + IntentType.humanProjectTopic(projectPage.title()).toLowerCase(Locale.US)
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
                "Ask before booking: " + projectPage.operatorQuestion()
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

    private List<IntentDecisionBlockViewModel> intentDecisionBlocks(
            IntentType intentType,
            MaterialPageViewModel materialPage,
            ProjectPageViewModel projectPage,
            MaterialFactor materialFactor,
            MaterialPageViewModel.SizeWeightRow anchorRow
    ) {
        double pickupLoads = Math.max(1.0d, anchorRow.effectiveVolumeYd3() / 2.5d);
        double wetHighDeltaPct = Math.max(0.0d, (materialPage.wetMultiplierHigh() - 1.0d) * 100.0d);
        String weightRange = round2(anchorRow.weightLowTons()) + " to " + round2(anchorRow.weightHighTons()) + " tons";

        String changesAnswer = switch (intentType) {
            case SIZE_GUIDE -> "Material density mix, weather exposure, and timeline urgency are the three fastest variables. "
                    + "If heavy share or urgency rises, safer or staged routing usually beats budget-first sizing.";
            case WEIGHT_ESTIMATE -> "Moisture, contamination, and packing behavior move the estimate most. "
                    + "Use the high-side range when load certainty is low or weather is unstable.";
            case OVERAGE_RISK -> "Included tons, wet load exposure, and mixed-heavy contamination change risk tier quickly. "
                    + "If any two move against you, route to safer option before booking.";
        };

        String expensiveMistake = switch (intentType) {
            case SIZE_GUIDE -> "Choosing by cubic yards only and skipping haul-limit verification can force swap delays plus overage on pickup day.";
            case WEIGHT_ESTIMATE -> "Treating typical tons as guaranteed and ignoring the high-side range is the most expensive miss.";
            case OVERAGE_RISK -> "Using included tons as if it were max-haul policy creates double risk: surcharge plus pickup refusal.";
        };

        String junkSmarter = "Junk removal usually wins when speed or labor convenience is the top priority, or when composition is too uncertain for one-container confidence.";
        String pickupTranslation = "A " + anchorRow.sizeYd() + "-yard scenario is roughly " + round2(pickupLoads)
                + " pickup-load equivalents, but heavy material can hit ton limits before that visual volume is used.";
        String wetRisk = "Wet-load multiplier can push this material up to +" + round2(wetHighDeltaPct)
                + "% versus dry assumptions. Re-run after rain before confirming quotes.";
        String vendorScript = "Ask in this order: included tons, overage per ton, max haul tons, and same-day swap availability.";
        String realJob = "Example workflow: " + projectPage.sampleInput() + " Then action: " + projectPage.sampleDecision();
        String confidence = "Confidence is range-based, not single-point. Anchor row " + anchorRow.sizeYd()
                + "yd currently spans " + weightRange + " with source-backed density assumptions.";

        return List.of(
                decisionBlock("What changes the answer?", changesAnswer, null, null),
                decisionBlock("Most expensive mistake", expensiveMistake, null, null),
                decisionBlock(
                        "When junk removal is smarter",
                        junkSmarter,
                        "Compare junk removal",
                        COMPARISON_HUB_PATH
                ),
                decisionBlock(
                        "Pickup-truck translation",
                        pickupTranslation,
                        "Use pickup converter",
                        PICKUP_CONVERTER_PATH
                ),
                decisionBlock("Wet-load risk", wetRisk, null, null),
                decisionBlock(
                        "Vendor call script",
                        vendorScript,
                        "Check heavy-load rules first",
                        HEAVY_RULES_PATH
                ),
                decisionBlock("Real job example", realJob, null, null),
                decisionBlock("Confidence and assumptions", confidence, null, null)
        );
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
                                    + " These four checks prevent most avoidable surprises for "
                                    + IntentType.humanProjectTopic(projectPage.title()).toLowerCase(Locale.US)
                                    + "."
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

    private List<LinkItemViewModel> decisionStageLinks(String projectId, String materialId, IntentType intentType) {
        String calculatorHref = CALCULATOR_PATH
                + "?project=" + projectId
                + "&material=" + materialId
                + "&unit=pickup_load&qty=6";
        IntentType followupIntentType = switch (intentType) {
            case SIZE_GUIDE -> IntentType.WEIGHT_ESTIMATE;
            case WEIGHT_ESTIMATE -> IntentType.OVERAGE_RISK;
            case OVERAGE_RISK -> IntentType.SIZE_GUIDE;
        };
        String followupIntent = intentPath(projectId, materialId, followupIntentType);

        LinkedHashMap<String, LinkItemViewModel> links = new LinkedHashMap<>();
        links.put(calculatorHref, link(calculatorHref, "Run live estimate", "Validate this exact scenario in the decision engine."));
        links.put(COMPARISON_HUB_PATH, link(COMPARISON_HUB_PATH, "Compare dumpster vs crew pickup", "Decide dumpster versus crew pickup based on speed, labor, and risk."));
        links.put(QUOTE_MATCH_BETA_PATH, link(QUOTE_MATCH_BETA_PATH, "Check local heavy-load options", "Move from research into local heavy-load follow-up once the route looks feasible."));
        links.put(PICKUP_CONVERTER_PATH, link(PICKUP_CONVERTER_PATH, "Use pickup-load converter", "Translate visual load count into safer size planning."));
        links.put(HEAVY_RULES_PATH, link(HEAVY_RULES_PATH, "Check heavy-load rules first", "Confirm fill-line and haul constraints before booking."));
        links.put(followupIntent, link(followupIntent, "Next intent question", "Move to the next decision-stage question for this scenario."));
        String projectGuideHref = projectCanonicalPath(projectId);
        links.put(projectGuideHref, link(projectGuideHref, "Open project workflow guide", "See the full project strategy and operator questions."));
        String materialGuideHref = materialCanonicalPath(materialId);
        links.put(materialGuideHref, link(materialGuideHref, "Open material weight guide", "Review density range and size-level risk table."));
        addConcreteClusterLinks(links, projectId, materialId);
        return List.copyOf(links.values());
    }

    private List<LinkItemViewModel> materialDecisionStageLinks(String materialId) {
        String projectId = primaryProjectForMaterial(materialId)
                .orElseGet(() -> sortedIndexableProjects().stream()
                        .findFirst()
                        .map(ProjectSeed::projectId)
                        .orElse("roof_tearoff"));
        String intentMaterialId = intentMaterialForProject(projectId, materialId);
        String calculatorHref = CALCULATOR_PATH
                + "?project=" + projectId
                + "&material=" + materialId
                + "&unit=pickup_load&qty=6";
        LinkedHashMap<String, LinkItemViewModel> links = new LinkedHashMap<>();
        links.put(calculatorHref, link(calculatorHref, "Run live estimate", "Start with this material and validate route feasibility."));
        String sizeGuideHref = intentPath(projectId, intentMaterialId, IntentType.SIZE_GUIDE);
        links.put(sizeGuideHref, link(sizeGuideHref, "Check size-guide answer", "Confirm a safe starting bin before comparing price options."));
        String overageHref = intentPath(projectId, intentMaterialId, IntentType.OVERAGE_RISK);
        links.put(overageHref, link(overageHref, "Check overage-risk answer", "See where included tons and haul limits diverge."));
        String projectGuideHref = projectCanonicalPath(projectId);
        links.put(projectGuideHref, link(projectGuideHref, "Open related project guide", "Move from material-only logic into a full project workflow."));
        links.put(QUOTE_MATCH_BETA_PATH, link(QUOTE_MATCH_BETA_PATH, "Check local heavy-load options", "Use the current material profile to request local heavy-load follow-up."));
        links.put(COMPARISON_HUB_PATH, link(COMPARISON_HUB_PATH, "Compare dumpster vs crew pickup", "Use labor, speed, and risk tradeoffs before booking."));
        links.put(PICKUP_CONVERTER_PATH, link(PICKUP_CONVERTER_PATH, "Use pickup-load converter", "Translate rough load count into safer size planning."));
        links.put(HEAVY_RULES_PATH, link(HEAVY_RULES_PATH, "Check heavy-load rules first", "Validate fill and feasibility constraints for dense loads."));
        addConcreteClusterLinks(links, projectId, materialId);
        return List.copyOf(links.values());
    }

    private List<LinkItemViewModel> projectDecisionStageLinks(String projectId, String preferredMaterialId) {
        String intentMaterialId = intentMaterialForProject(projectId, preferredMaterialId);
        String calculatorHref = CALCULATOR_PATH
                + "?project=" + projectId
                + "&material=" + intentMaterialId
                + "&unit=pickup_load&qty=6";
        LinkedHashMap<String, LinkItemViewModel> links = new LinkedHashMap<>();
        links.put(calculatorHref, link(calculatorHref, "Run live estimate", "Launch this project preset and confirm the current best route."));
        String sizeGuideHref = intentPath(projectId, intentMaterialId, IntentType.SIZE_GUIDE);
        links.put(sizeGuideHref, link(sizeGuideHref, "Check size-guide answer", "Validate the safer baseline size for this project/material pair."));
        String overageHref = intentPath(projectId, intentMaterialId, IntentType.OVERAGE_RISK);
        links.put(overageHref, link(overageHref, "Check overage-risk answer", "Validate allowance pressure before requesting quotes."));
        links.put(QUOTE_MATCH_BETA_PATH, link(QUOTE_MATCH_BETA_PATH, "Check local heavy-load options", "Request local heavy-load follow-up after this project path looks feasible."));
        links.put(COMPARISON_HUB_PATH, link(COMPARISON_HUB_PATH, "Compare dumpster vs crew pickup", "Decide whether convenience or staged pricing should win."));
        links.put(PICKUP_CONVERTER_PATH, link(PICKUP_CONVERTER_PATH, "Use pickup-load converter", "Map pickup-load intuition to container size."));
        links.put(HEAVY_RULES_PATH, link(HEAVY_RULES_PATH, "Check heavy-load rules first", "Confirm fill-line and haul constraints for dense debris."));
        links.put(DECISION_OVERVIEW_PATH, link(DECISION_OVERVIEW_PATH, "Open decision overview", "Recheck size, overage, and feasibility framework in one page."));
        addConcreteClusterLinks(links, projectId, intentMaterialId);
        return List.copyOf(links.values());
    }

    private void addConcreteClusterLinks(
            LinkedHashMap<String, LinkItemViewModel> links,
            String projectId,
            String materialId
    ) {
        if (!isConcreteCluster(projectId, materialId)) {
            return;
        }

        links.putIfAbsent(
                "/dumpster/can-you-put-concrete-in-a-dumpster",
                link(
                        "/dumpster/can-you-put-concrete-in-a-dumpster",
                        "Check concrete rules and load limits",
                        "Use the strongest early-performing rules page before booking a heavy load."
                )
        );
        links.putIfAbsent(
                "/dumpster/size/concrete-removal",
                link(
                        "/dumpster/size/concrete-removal",
                        "Open concrete removal project guide",
                        "Stay inside the concrete-focused project workflow instead of falling back to generic sizing."
                )
        );
        links.putIfAbsent(
                "/dumpster/weight/concrete",
                link(
                        "/dumpster/weight/concrete",
                        "Open concrete debris guide",
                        "Review tonnage behavior and small-bin haul constraints for concrete."
                )
        );
    }

    private boolean isConcreteCluster(String projectId, String materialId) {
        return "concrete_removal".equals(projectId)
                || "concrete".equals(materialId)
                || "brick".equals(materialId)
                || "asphalt_pavement".equals(materialId);
    }

    private Optional<String> primaryProjectForMaterial(String materialId) {
        Optional<String> direct = sortedIndexableProjects().stream()
                .filter(seed -> seed.defaultMaterialId().equals(materialId))
                .map(ProjectSeed::projectId)
                .findFirst();
        if (direct.isPresent()) {
            return direct;
        }
        return sortedIndexableProjects().stream()
                .filter(seed -> projectIntentMaterialsForProject(seed.projectId()).contains(materialId))
                .map(ProjectSeed::projectId)
                .findFirst();
    }

    private String intentMaterialForProject(String projectId, String preferredMaterialId) {
        List<String> materials = projectIntentMaterialsForProject(projectId);
        if (materials.contains(preferredMaterialId)) {
            return preferredMaterialId;
        }
        if (!materials.isEmpty()) {
            return materials.getFirst();
        }
        return projectSeeds.get(projectId).defaultMaterialId();
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
                        "Compare strategy and booking constraints"
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
            return SeoCopyCatalog.defaultProjectCopy();
        }
        return PROJECT_COPY.getOrDefault(seed.projectId(), SeoCopyCatalog.defaultProjectCopy());
    }

    private static CopyBlock defaultMaterialCopy() {
        return SeoCopyCatalog.defaultMaterialCopy();
    }

    private static FaqItemViewModel faq(String question, String answer) {
        return new FaqItemViewModel(question, answer);
    }

    private static IntentDecisionBlockViewModel decisionBlock(String title, String body, String ctaLabel, String ctaHref) {
        return new IntentDecisionBlockViewModel(title, body, ctaLabel, ctaHref);
    }

    private record MaterialSeoOverride(
            String pageTitle,
            String seoTitle,
            String metaDescription,
            String answerFirst
    ) {
    }

    private record ProjectSeoOverride(
            String pageTitle,
            String seoTitle,
            String metaDescription,
            String answerFirst
    ) {
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

    private EvidenceProfile materialEvidenceProfile(MaterialFactor material) {
        String confidence = MEDIUM_CONFIDENCE_MATERIAL_IDS.contains(material.materialId()) ? "Medium" : "High";
        String varianceNote = switch (material.category()) {
            case HEAVY -> "Heavy-debris outcomes vary with moisture, contamination, and operator haul constraints.";
            case MIXED -> "Mixed-load estimates vary with packing efficiency and dense-material share.";
            case LIGHT -> "Light debris estimates vary with compaction and occasional moisture effects.";
        };
        List<String> vendorChecklist = List.of(
                "What included tons and overage fee apply to " + material.name().toLowerCase(Locale.US) + "?",
                "Do you enforce clean-load separation or special handling for this material?",
                "What fill-line or haul-limit rule would trigger rejection on pickup day?"
        );
        return new EvidenceProfile(confidence, varianceNote, vendorChecklist);
    }

    private EvidenceProfile projectEvidenceProfile(ProjectSeed seed, String defaultMaterialName) {
        String confidence = MEDIUM_CONFIDENCE_PROJECT_IDS.contains(seed.projectId()) ? "Medium" : "High";
        String varianceNote = "Project outcomes vary with material mix, load sequencing, and timeline pressure."
                + " Recheck risk if debris composition changes mid-job.";
        List<String> vendorChecklist = List.of(
                "For this project scope, what included tons and overage rate should I expect?",
                "If the mix shifts toward heavier debris, what container or hauling change do you require?",
                "What swap or pickup timing is guaranteed if I need staged hauling?"
        );
        return new EvidenceProfile(confidence, varianceNote, vendorChecklist);
    }

    private EvidenceProfile intentEvidenceProfile(
            String projectId,
            String materialId,
            IntentType intentType,
            String materialName
    ) {
        boolean mediumConfidence = MEDIUM_CONFIDENCE_PROJECT_IDS.contains(projectId)
                || MEDIUM_CONFIDENCE_MATERIAL_IDS.contains(materialId);
        String confidence = mediumConfidence ? "Medium" : "High";
        String varianceNote = switch (intentType) {
            case SIZE_GUIDE -> "Size guidance can shift when heavy-material share or moisture differs from baseline assumptions.";
            case WEIGHT_ESTIMATE -> "Weight ranges vary by packing, moisture, and contamination; treat outputs as bands, not fixed points.";
            case OVERAGE_RISK -> "Overage and rejection risk varies by operator policy and included-ton contract terms.";
        };
        String materialToken = materialName.toLowerCase(Locale.US);
        List<String> vendorChecklist = switch (intentType) {
            case SIZE_GUIDE -> List.of(
                    "For this scenario, what size do you recommend when " + materialToken + " is dominant?",
                    "What rule would force a size-up or staged-haul change on pickup day?",
                    "Can you confirm swap availability if the first load runs heavier than expected?"
            );
            case WEIGHT_ESTIMATE -> List.of(
                    "What per-container haul limit applies to " + materialToken + "?",
                    "Do wet or mixed loads change weight handling policy or pricing?",
                    "Which estimate assumptions should be adjusted for my local service area?"
            );
            case OVERAGE_RISK -> List.of(
                    "What is overage per ton above included allowance in this quote?",
                    "At what point does load condition trigger rejection instead of overage?",
                    "Can you provide written fill-line and heavy-load acceptance rules?"
            );
        };
        return new EvidenceProfile(confidence, varianceNote, vendorChecklist);
    }

    private EvidenceProfile specialEvidenceProfile(String slug) {
        String resolvedSlug = resolveSpecialSlug(slug);
        String confidence = MEDIUM_CONFIDENCE_SPECIAL_SLUGS.contains(resolvedSlug) ? "Medium" : "High";
        String varianceNote = switch (resolvedSlug) {
            case "fill-line-rules-for-heavy-debris" ->
                    "Fill-line details vary by hauler and route constraints, so confirm exact enforcement before loading.";
            case "one-20-yard-vs-two-10-yard" ->
                    "Cost inversion between one large and two smaller hauls depends on operator policy and dense-load feasibility.";
            case "dumpster-vs-junk-removal-which-is-cheaper" ->
                    "Route economics vary by labor availability, urgency, and local provider pricing.";
            default ->
                    "Final decisions should be validated against operator policy because handling rules vary by market.";
        };
        List<String> vendorChecklist = List.of(
                "Can you confirm this rule or comparison assumption for my ZIP and material mix?",
                "What policy detail most often changes the answer on pickup day?",
                "If assumptions change, what fallback option avoids delay or refusal?"
        );
        return new EvidenceProfile(confidence, varianceNote, vendorChecklist);
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
            String projectTopic = humanProjectTopic(projectTitle).toLowerCase(Locale.US);
            String humanMaterial = materialName.toLowerCase(Locale.US);
            return switch (this) {
                case SIZE_GUIDE -> "Best dumpster size for " + projectTopic + " with " + humanMaterial
                        + " | " + anchorRow.sizeYd() + "-yard baseline";
                case WEIGHT_ESTIMATE -> "How much does " + humanMaterial + " weigh in a dumpster for "
                        + projectTopic + "?";
                case OVERAGE_RISK -> "Will " + humanMaterial + " exceed dumpster weight limits for "
                        + projectTopic + "?";
            };
        }

        public String metaDescription(String projectTitle, String materialName, MaterialPageViewModel.SizeWeightRow anchorRow) {
            String projectTopic = humanProjectTopic(projectTitle).toLowerCase(Locale.US);
            return switch (this) {
                case SIZE_GUIDE -> "Find the safest dumpster size for " + projectTopic + " with " + materialName
                        + ". Starts from a " + anchorRow.sizeYd()
                        + "yd baseline and compares overage risk by size.";
                case WEIGHT_ESTIMATE -> "Estimate how much " + materialName + " can weigh in " + projectTopic
                        + " scenarios, with low/typical/high tons and size comparisons.";
                case OVERAGE_RISK -> "See when " + materialName + " may exceed included tons for " + projectTopic
                        + ". Compare allowance, high-side weight, and safer next-step options.";
            };
        }

        public String linkLabel(String projectTitle, String materialName) {
            String projectTopic = humanProjectTopic(projectTitle).toLowerCase(Locale.US);
            String humanMaterial = materialName.toLowerCase(Locale.US);
            return switch (this) {
                case SIZE_GUIDE -> "Best dumpster size for " + projectTopic + " with " + humanMaterial;
                case WEIGHT_ESTIMATE -> "How much does " + humanMaterial + " weigh for " + projectTopic + "?";
                case OVERAGE_RISK -> "Will " + humanMaterial + " exceed weight limits for " + projectTopic + "?";
            };
        }

        public String linkSummary() {
            return switch (this) {
                case SIZE_GUIDE -> "Intent page focused on size-selection decision logic.";
                case WEIGHT_ESTIMATE -> "Intent page focused on range-based tonnage estimates.";
                case OVERAGE_RISK -> "Intent page focused on included tons versus risk exposure.";
            };
        }

        private static String humanProjectTopic(String projectTitle) {
            String topic = projectTitle
                    .replace("Dumpster Size for ", "")
                    .replace("Dumpster Strategy for ", "")
                    .replace("Dumpster Plan for ", "")
                    .replaceAll("(?i)\\s+dumpster\\s+size\\s+calculator$", "")
                    .replaceAll("(?i)\\s+calculator$", "")
                    .replaceAll("(?i)\\s+dumpster\\s+size\\s+guide$", "")
                    .replaceAll("(?i)\\s+dumpster\\s+strategy\\s+guide$", "")
                    .replaceAll("(?i)\\s+dumpster\\s+plan\\s+guide$", "")
                    .trim();
            return topic;
        }
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

    private record IndexableIntentSeed(
            String projectId,
            String materialId,
            String intentSlug
    ) {
    }

    private record MaterialScenario(
            String input,
            String decision
    ) {
    }

    private record EvidenceProfile(
            String confidenceTier,
            String varianceNote,
            List<String> vendorChecklist
    ) {
    }
}

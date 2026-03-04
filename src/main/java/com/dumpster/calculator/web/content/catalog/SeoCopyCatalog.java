package com.dumpster.calculator.web.content.catalog;

import com.dumpster.calculator.web.viewmodel.FaqItemViewModel;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SeoCopyCatalog {

    private SeoCopyCatalog() {
    }

    public static CopyBlock defaultMaterialCopy() {
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

    public static CopyBlock defaultProjectCopy() {
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

    public static Map<String, CopyBlock> materialCopy() {
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

    public static Map<String, CopyBlock> projectCopy() {
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

    public record CopyBlock(
            String answerFirst,
            List<String> quickRules,
            List<FaqItemViewModel> faqItems
    ) {
    }
}


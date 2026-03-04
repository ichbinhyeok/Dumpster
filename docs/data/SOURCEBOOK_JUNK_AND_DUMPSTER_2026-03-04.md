# Sourcebook: Junk + Dumpster Benchmarks

- Version date: **2026-03-04**
- Purpose: source-backed baseline for B2C decision engine pricing/risk copy
- Scope: national homeowner decision support (not ZIP-accurate quoting)

## 1) Source Tiers

- `high`: official operator pages with explicit capacity/rule statements
- `medium`: major market guides with broad range estimates
- `low`: local/regional examples, used only as directional checks

## 2) Core Source Set

1. 1-800-GOT-JUNK pricing model and truck-volume billing  
   - URL: https://www.1800gotjunk.com/us_en/how-our-pricing-works  
   - Use: junk billing increment behavior and minimum-charge framing  
   - Tier: high

2. Republic Services dumpster capacity guidance  
   - URL: https://www.republicservices.com/residents/dumpster-rental/10-yard-dumpster  
   - URL: https://www.republicservices.com/residents/dumpster-rental/20-yard-dumpster  
   - URL: https://www.republicservices.com/residents/dumpster-rental/30-yard-dumpster  
   - URL: https://www.republicservices.com/residents/dumpster-rental/40-yard-dumpster  
   - Use: pickup-load equivalence and size intuition  
   - Tier: high

3. WM roll-off size guidance  
   - URL: https://www.wm.com/us/en/home/bagster  
   - Use: homeowner-facing load translation language and sizing context  
   - Tier: high

4. Budget Dumpster price and overage context  
   - URL: https://www.budgetdumpster.com/blog/average-cost-of-a-dumpster-rental/  
   - URL: https://www.budgetdumpster.com/blog/dumpster-rental-prices/  
   - Use: national dumpster rent/overage range anchoring  
   - Tier: medium

5. Angi junk removal cost guide  
   - URL: https://www.angi.com/articles/how-much-does-junk-removal-cost.htm  
   - Use: homeowner junk-removal range baseline  
   - Tier: medium

6. Forbes Home junk removal cost guide  
   - URL: https://www.forbes.com/home-improvement/moving-services/junk-removal-cost/  
   - Use: national range triangulation for junk pricing  
   - Tier: medium

7. HomeGuide junk removal prices  
   - URL: https://homeguide.com/costs/junk-removal-prices  
   - Use: additional range triangulation for low/high bounds  
   - Tier: medium

8. EPA C&D context (materials macro baseline)  
   - URL: https://www.epa.gov/facts-and-figures-about-materials-waste-and-recycling/construction-and-demolition-debris-material-specific-data  
   - Use: editorial methodology support and material context  
   - Tier: high

## 3) Mapping to Runtime Fields

Mapped into `junk_pricing_profiles.csv`:

- `min_service_fee_low/typ/high`
- `per_cy_fee_low/typ/high`
- `minimum_billable_volume_cy`
- `truck_capacity_cy`
- `billing_increment_fraction`
- `dense_material_threshold_ton_per_cy`
- `dense_material_multiplier_low/typ/high`

Mapped into `junk_pricing_profile_rules.csv`:

- `market_tier` (`national`, `urban`, `value`, `coastal`, `mountain`, `heartland`, `any`)
- `need_timing` (`research`, `this_week`, `48h`, `any`)
- `profile_id`
- `priority` (lower means stronger match)

Mapped into `material_factors.csv`:

- `source`
- `source_url`
- `source_version_date`

## 4) Decision Rules from Source Synthesis

- Junk pricing should be modeled as range-based and volume-increment based.
- Small loads should not be treated as pure linear zero-minimum pricing.
- Dense loads require surcharge behavior in estimate ranges.
- Dumpster vs junk comparison must surface speed/labor/access tradeoffs, not only price.
- Profile selection should be data-driven by market tier and urgency, not hardcoded in service logic.

## 5) Refresh Policy

- Re-check core sources quarterly.
- Trigger immediate refresh if:
  - major operator pricing model changes,
  - billing increment policy changes,
  - sustained 15%+ variance in observed lead conversion patterns.

Automation hooks:

- `npm run data:source-freshness:report`
- `npm run data:source-freshness:check`

## 6) Confidence Notes

- These numbers are **decision-support ranges**, not quote guarantees.
- The engine should continue to route users to live local quotes after route selection.

## 7) ZIP-tier override dataset (v1)

Runtime file:

- `src/main/resources/data/market_tier_zip_overrides.csv`

Purpose:

- Refine junk profile selection prior to quote-match stage.
- Keep deterministic routing while avoiding full ZIP-level pricing claims.

Current model:

- `urban` overrides: major metro ZIP bands.
- `value` overrides: lower dispatch-pressure regional ZIP bands.
- `coastal` overrides: coastal corridor + island logistics bands.
- `mountain` overrides: sparse-dispatch mountain bands.
- `heartland` overrides: balanced mid-continent bands.
- fallback: `app.pricing.market-tier` default (`national`).

Source basis:

1. U.S. Census ZCTA guidance  
   https://www.census.gov/programs-surveys/geography/guidance/geo-areas/zctas.html

2. ZIP-range reference by state/city (operational mapping support)  
   https://www.unitedstateszipcodes.org/

Guardrail:

- This layer is a **market-tier routing heuristic**, not a final quote engine.
- Final pricing still requires local operator quote confirmation.

Regeneration pipeline:

- [ZIP_MARKET_TIER_PIPELINE_2026-03-04.md](C:/Development/Owner/dumpster-calculator/docs/data/ZIP_MARKET_TIER_PIPELINE_2026-03-04.md)
- Generator script:
  - [build-market-tier-zip-overrides.mjs](C:/Development/Owner/dumpster-calculator/scripts/build-market-tier-zip-overrides.mjs)
  - [build_market_tier_zip_overrides.py](C:/Development/Owner/dumpster-calculator/scripts/build_market_tier_zip_overrides.py)

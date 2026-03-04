# Execution Anchor (v2)

- Anchor date: **2026-03-04**
- Scope: **B2C debris-disposal decision engine refactor**
- Sequence lock: **Data -> Backend -> Frontend**
- Repo: `dumpster-calculator`

## 1) Mission Lock

Convert the current product from a calculator-first experience into a homeowner-first decision engine:

- Should I rent a dumpster or call junk removal?
- How do I avoid overage fees?
- What is the safest next move for this job?

This is a structural refactor, not a cosmetic rewrite.

## 2) Non-Negotiables

Keep these intact:

- Range-based size + weight model
- Included tons vs max haul semantics
- Heavy-debris feasibility logic
- CTA risk routing
- Selective indexing and split sitemaps
- Existing test contracts (SEO + business rules + journeys)

Do not do in this wave:

- Full intent-route merge
- City/ZIP pSEO expansion
- Contractor-first IA pivot
- Math-engine replacement

## 3) Workstreams

## Workstream A: Source Data System

Goal: make source data a first-class product asset with provenance.

### A1. Data model and provenance

- Add dedicated junk pricing dataset (`junk_pricing_profiles.csv`).
- Remove junk proxy dependence on `pricing_assumptions(size_yd=0)`.
- Include per-row provenance:
  - `source`
  - `source_url`
  - `source_version_date`
  - `data_quality`

### A2. Sourcebook operations

- Build sourcebook document with:
  - URL
  - extracted range
  - mapped field
  - confidence and recency
- Introduce quarterly refresh cadence and diff review.

### A3. Data guardrails

- CSV integrity tests for:
  - ordered low/typ/high ranges
  - valid billing increment rules
  - non-empty source fields

## Workstream B: Backend Decision Core

Goal: promote junk branch to first-class decision branch.

### B1. Persistence + ingestion

- Add schema table `junk_pricing_profiles`.
- Add `JunkPricingProfile` domain record + repository.
- Seed table from CSV through `CsvBootstrapService`.

### B2. Cost comparison engine

- Refactor `CostComparisonService` junk option:
  - minimum billable volume
  - truck increment billing (1/8)
  - dense-material multiplier based on tons/yd
- Keep output shape stable (`CostComparisonOption`) for compatibility.

### B3. Behavioral safeguards

- Preserve existing CTA routing contract:
  - non-OK -> junk path
  - urgent timing -> dumpster call
  - high risk -> junk branch eligible

### B4. ZIP-tier precision layer

- Add `market_tier_zip_overrides.csv` as controlled override dataset.
- Resolve market tier by 5-digit ZIP before junk profile selection.
- Fall back to app default market tier when ZIP is missing/unmapped.
- Expose resolved tier in junk comparison notes for auditability.

## Workstream C: Frontend Product Layer

Goal: make the product feel like a decision board, not an operator utility.

### C1. Entry and messaging

- Hero rewrite to decision-first language.
- Add quick decision strip:
  - cheapest route
  - easiest route
  - heavy material
  - need fast removal

### C2. Result board reframing

- Replace single verdict sentence with:
  - Best next move
  - Why this route wins
  - When junk is smarter
- Keep recommendations + comparison cards; improve explanation text.

### C3. CTA clarity

- Replace vague labels:
  - `Get dumpster quotes`
  - `Compare junk removal`
  - `Run the live estimate`
  - `Check heavy-load rules first`

### C4. Decision analytics

- Track `decision_mode_selected` at:
  - entry strip click
  - result decision resolution
- Keep existing completion and CTA events.

### C5. Comparison hub interaction model

- Add priority-mode toggles:
  - lowest cost
  - fastest completion
  - least effort
  - heavy-load safety
- Reorder and highlight scenario plan cards by selected priority mode.
- Track `comparison_priority_selected`.

### C6. Calculator decision scorecard

- Add four decision-stage scores on result board:
  - cost route
  - speed route
  - labor effort
  - safety margin
- Keep deterministic scoring logic in frontend (no random weighting).

## Workstream D: SEO Humanization

Goal: remove system-composed phrasing from intent surfaces.

### D1. Title/H1 sentence normalization

- Strip operator/system prefixes in project title composition.
- Use human query style for `size`, `weight`, `overage` intents.

### D2. Controlled rollout

- Keep allowlist/wave policy.
- No route explosion in this wave.

## 4) Acceptance Criteria (Wave Gate)

All must be true:

- Junk is backed by dedicated data model (no size-0 proxy requirement).
- Decision board language is visible on calculator result layer.
- CTA copy is consumer-specific, not generic.
- Intent title generation no longer exposes internal naming composition.
- Java test suite passes.
- Critical E2E smoke remains green after label updates.

## 5) Execution Timeline (Anchored)

### Phase 0 (2026-03-04) - Foundation lock

- [x] New junk dataset + schema + repository + bootstrap
- [x] CostComparison junk logic refactor
- [x] CSV integrity tests extended
- [x] Hero/result/CTA language update start
- [x] Title/H1 generation humanization pass (intent enum layer)
- [x] Junk profile selection moved to data rules (`market_tier + need_timing`)
- [x] ZIP-tier override table + resolver connected to junk profile routing
- [x] Comparison hub priority toggles + interaction tracking
- [x] Result-board decision scorecard with explicit score bars
- [x] Node-based ZIP-tier regeneration script for local runtime

### Phase 1 (2026-03-05 to 2026-03-08) - Decision UX stabilization

- [x] Expand result board decision blocks with richer scenario guidance.
- [x] Strengthen dumpster-vs-junk hub content depth and matrix.
- [x] Wire additional decision-stage events:
  - `comparison_page_view`
  - `comparison_page_exit_to_calculator`
  - `vendor_questions_expand`
  - `pickup_converter_used`
  - `answer_page_group`
  - `content_gate_pass`
  - `content_gate_fail`
  - priority-mode payload on `decision_scorecard_rendered`

### Phase 2 (2026-03-09 to 2026-03-15) - Source-data expansion

- [x] Extend source-backed pricing profiles by regional tier.
- [x] Add material-factor provenance granularity (URL-level evidence).
- [x] Add data freshness report and stale-source alert.

### Phase 3 (2026-03-16 to 2026-03-19) - Intent decision UX completion

- [x] Add homeowner decision block set to intent pages (8 deterministic blocks).
- [x] Add decision-stage link cluster (`calculator`, `dumpster-vs-junk`, `heavy rules`, `pickup converter`, `next intent`).
- [x] Extend SEO E2E contract to require homeowner blocks + next-step links.
- [x] Regenerate extended visual baselines for expanded intent page length.

### Phase 4 (2026-03-20 to 2026-03-22) - Decision graph linking hardening

- [x] Add `Next decision steps` section to material pages.
- [x] Add `Next decision steps` section to project pages.
- [x] Route material/project next steps by decision stage (live estimate -> overage intent -> comparison -> converter/heavy rules).
- [x] Extend Java + E2E contracts for material/project decision-stage links.

### Phase 5 (2026-03-23 to 2026-03-24) - Comparison hub promotion

- [x] Add `Compare dumpster vs junk` entry points in material-guides hub.
- [x] Add `Compare dumpster vs junk` entry points in project-guides hub.
- [x] Add `Compare dumpster vs junk` entry point in heavy-rules page.
- [x] Extend rendering + SEO E2E contracts to enforce comparison-hub discoverability.

### Phase 6 (2026-03-25 to 2026-03-26) - Decision-stage analytics hardening

- [x] Emit server-side events for `data-analytics-event` click telemetry.
- [x] Tag intent/material/project next-step links with `decision_stage_link_click`.
- [x] Tag comparison-hub entry links with `comparison_hub_entry_click`.
- [x] Extend beta matrix with `heavy + urgent + uncertain mix` API scenario.
- [x] Extend SEO E2E contract to verify analytics emission for decision-stage links.

## 6) Risk Register

- Risk: pricing sources are market-variable by ZIP.
  - Mitigation: store ranges and confidence tiers; avoid false precision.

- Risk: copy-only changes can break E2E label assertions.
  - Mitigation: update tests with semantic CTA expectations.

- Risk: intent-humanization can alter SEO snapshots.
  - Mitigation: wave rollout with selective index control.

## 7) Change Log (This Anchor)

- Added:
  - `src/main/resources/data/junk_pricing_profiles.csv`
  - `src/main/resources/data/market_tier_zip_overrides.csv`
  - `scripts/build_market_tier_zip_overrides.py`
  - `docs/data/ZIP_MARKET_TIER_PIPELINE_2026-03-04.md`
  - `src/main/java/com/dumpster/calculator/domain/reference/JunkPricingProfile.java`
  - `src/main/java/com/dumpster/calculator/domain/reference/MarketTierZipRule.java`
  - `src/main/java/com/dumpster/calculator/infra/persistence/JunkPricingProfileRepository.java`
  - `src/main/java/com/dumpster/calculator/infra/persistence/MarketTierZipRuleRepository.java`
  - `src/main/resources/static/js/comparison-hub.js`
  - `tests/e2e/beta-scenario-matrix.spec.ts`
  - `tests/e2e/iphone-se-beta-expansion.spec.ts`
  - `docs/qa/BETA_TEST_EXECUTION_REPORT_2026-03-04.md`
  - `scripts/source-freshness-report.mjs`
  - `docs/data/SOURCE_FRESHNESS_REPORT_2026-03-04.md`
  - `src/main/resources/data/market_tier_zip_overrides_regional.csv`
  - `src/main/java/com/dumpster/calculator/web/viewmodel/IntentDecisionBlockViewModel.java`
- Updated:
  - `schema.sql`
  - `material_factors.csv` (added `source_url` provenance column)
  - `CsvBootstrapService`
  - `MaterialFactor`
  - `MaterialFactorRepository`
  - `CostComparisonService` (regional market-tier normalization)
  - `junk_pricing_profiles.csv` (coastal/mountain/heartland profiles)
  - `junk_pricing_profile_rules.csv` (regional routing rules)
  - `market_tier_zip_overrides.csv` (base generated set; regionals split to overlay CSV)
  - `EstimationFacade`
  - `EstimateOptions`
  - `CsvDataIntegrityTests`
  - `MarketTierZipRuleRepositoryTests`
  - `EstimationFacadeTests`
  - `build-market-tier-zip-overrides.mjs` (regional overlay merge)
  - `calculator.js`
  - `comparison-hub.js`
  - `calculator/index.jte`
  - `app.css`
  - `intent-page.jte` (homeowner decision blocks + next decision steps sections)
  - `material-page.jte` (next decision steps section)
  - `project-page.jte` (next decision steps section)
  - `MaterialPageViewModel`
  - `ProjectPageViewModel`
  - `IntentPageViewModel`
  - `SeoContentService` (intent/material/project decision-stage linking)
  - `SeoContentServiceTests`
  - `SeoPageRenderingTests`
  - `tests/e2e/seo-aeo.spec.ts` (intent + material/project decision-stage contracts)
  - `material-guides.jte` (comparison hub entry links)
  - `project-guides.jte` (comparison hub entry links)
  - `heavy-rules.jte` (comparison hub entry link)
  - `analytics.js` (data-analytics-event now posts server events)
  - `tests/e2e/smoke-critical.spec.ts`
  - `tests/e2e/beta-scenario-matrix.spec.ts` (priority + content-gate + heavy/urgent/uncertain scenario)
  - `tests/e2e/visual-regression.spec.ts-snapshots/*`
  - `tests/e2e/visual-regression-extended.spec.ts-snapshots/*`

## 8) Validation Snapshot (2026-03-04, latest rerun)

- `./gradlew test` -> pass
- `npm run data:source-freshness:check` -> pass (86/86 rows fresh)
- `npm run e2e:beta` -> pass (63 passed, 1 skipped)
- `npm run e2e:full` -> pass (87 passed, 1 skipped)
- `npx playwright test tests/e2e/seo-aeo.spec.ts` -> pass (8 passed)
- `npx playwright test tests/e2e/visual-regression.spec.ts tests/e2e/visual-regression-extended.spec.ts --update-snapshots` -> pass (10 passed, snapshots refreshed after hub-link additions)

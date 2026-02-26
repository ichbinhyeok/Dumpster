# Dumpster Calculator Implementation Master Plan v1.0

Reference PRD: `DUMPSTER_CALCULATOR_PRD_v1.2_LOCK.md`  
Created: 2026-02-26 (KST)  
Scope: MVP 14-day build plan with design-first execution

## 0. Stack Profile (Locked)

1. Backend: Spring Boot 3.x (Java 21), Spring MVC, Spring Validation.
2. View: JTE server-side templates (SSR-first).
3. Database: H2 (file mode for local/prod-lite MVP).
4. Storage policy: H2 as source-of-truth + CSV import/export snapshots.
5. Build: Gradle.
6. Runtime shape: monolith app (web + API + engine in one service).

## 1. Objective

Build a decision-grade dumpster size/weight calculator that:

1. Completes in ~60 seconds on mobile.
2. Produces explainable range-based output (`volume`, `weight`, `price risk`, `feasibility`).
3. Drives dual monetization actions (`Dumpster quote/call` + `Junk removal`).
4. Stays safe for new-domain SEO (single canonical calculator URL, no scaled-index abuse).

## 2. Locked Constraints (Do Not Change During MVP)

1. Keep PRD unchanged (`v1.2 Lock` is source of truth).
2. Single index focus URL: `/dumpster/size-weight-calculator/`.
3. Query-based state pages must be `noindex + canonical` to calculator.
4. Shared estimates use UUID/ULID, 30-day TTL, and noindex headers.
5. `included_tons` (price policy) and `max_haul_tons` (operational limit) are separate models.
6. Output must include `feasibility` and never use deterministic "guaranteed" language.

## 2.1 Spring Boot + JTE Architecture Mapping

1. Web routes (JTE SSR):
   - `GET /dumpster/size-weight-calculator`
   - `GET /dumpster/estimate/{id}` (read-only, noindex)
2. API routes (JSON):
   - `POST /api/estimates` (calculate + persist)
   - `GET /api/estimates/{id}` (retrieve)
3. Service layers:
   - `NormalizationService`
   - `EstimationService`
   - `RecommendationService`
   - `CostComparisonService`
   - `CtaRoutingService`
4. Persistence:
   - H2 tables for runtime queries and estimates
   - CSV seed files for reference data bootstrapping
   - CSV export job for backup/snapshot
5. Rendering strategy:
   - SSR first render via JTE
   - minimal progressive enhancement JS for interactions

## 3. Workstreams

1. Product/UX
2. UI Design System
3. Data Pipeline
4. Estimation Engine
5. API/Storage/Security
6. Server-Rendered Web (JTE)
7. SEO/Indexing Controls
8. Tracking/Attribution
9. QA/Release

## 4. Delivery Milestones (14 Days)

### Day 1-2: Foundations

1. Finalize input/output contracts and error taxonomy.
2. Create data schemas for:
   - `material_factors`
   - `unit_conversions`
   - `dumpster_sizes` (`included_tons` + `max_haul_tons`)
   - `pricing_assumptions`
3. Set up Spring modules and package boundaries:
   - `web` (controllers + JTE)
   - `api` (REST controllers)
   - `domain` (engine services)
   - `infra` (H2 repositories, CSV loaders)
4. Build low-fidelity flow and wireframe for:
   - Quick mode
   - Detailed mode (max 3 line items)
   - Result screen (11 components)
5. Define microcopy rules:
   - No guaranteed pricing/regulatory claims
   - Mandatory assumption/warning language

### Day 3-5: Engine Core

1. Implement normalization pipeline:
   - Unit conversion to standard `line_item`
   - Uncertainty factors by input type
2. Implement range computation:
   - `V_low/typ/high`
   - `W_low/typ/high`
   - Condition modifiers (`wet`, `mixed`, `compacted`)
3. Implement dual constraint system:
   - `price_risk` via `included_tons`
   - `feasibility` via `max_haul_tons`
4. Implement recommendation strategy:
   - `standard_mode`
   - `heavy_mode`
   - Safe/Budget outputs
5. Implement cost comparison:
   - single dumpster
   - multi-haul (10/15yd)
   - junk removal

### Day 6-8: UX/UI + Integration

1. Build design tokens and component library.
2. Implement JTE templates and shared layout fragments:
   - calculator page
   - result card fragments
   - estimate share page
3. Implement input forms and validation UX.
4. Implement result composition:
   - Recommendation cards
   - Feasibility state card
   - Risk meter
   - Cost comparison card
   - Input-impact summary
   - Vendor-call checklist
5. Implement CTA routing rules:
   - `feasibility != OK` => promote junk CTA
   - `price_risk = High` => promote junk CTA
   - `persona = contractor` => promote share + checklist
   - `need_timing = 48h` => promote call CTA

### Day 9-10: Storage, Security, SEO Safety

1. Implement estimate persistence:
   - POST estimate (`/api/estimates`)
   - GET estimate by id (`/api/estimates/{id}`)
2. Implement H2 + CSV data lifecycle:
   - startup CSV seed import
   - periodic CSV snapshot export
   - restore procedure test
3. Implement estimate security rules:
   - UUID/ULID IDs only
   - TTL expiration
   - noindex meta + `X-Robots-Tag`
   - canonical to calculator
4. Implement SEO controls:
   - robots/sitemap filtering
   - canonical rules
   - noindex for non-indexable routes

### Day 11-12: Tracking + Content Surface

1. Implement event logging:
   - funnel events
   - routing decision events
   - downstream events
2. Publish constrained Phase1 pages:
   - material: 10-20 max
   - project: 5-10 max
   - heavy rules: 1
3. Verify every indexable page has:
   - unique data table
   - unique scenario calculation
   - mistake-prevention block
   - logical CTA mapping

### Day 13-14: QA + Launch

1. Run engine regression suite (30+ scenarios).
2. Run failure-required tests (`Not recommended`, `Multi-haul required`).
3. Validate mobile performance and accessibility baseline.
4. Final release checklist and production launch.

## 5. Detailed Logic Plan

### 5.1 Input Contract

Required:

1. `project_id`
2. `persona` (`homeowner|contractor|business`)
3. `items[]` (`material_id`, `quantity`, `unit_id`, `conditions`)
4. `options` (`mixed_load`, `allowance_tons`, `bulking_factor`)
5. `need_timing` (`48h|this_week|research`)

Validation:

1. quantity > 0
2. valid material/unit pair
3. max item count for detailed mode = 3
4. fallback assumptions flagged in response

### 5.2 Normalization Pipeline

1. Convert all inputs into canonical `line_item` format.
2. Resolve conversion formulas by unit and material.
3. Attach uncertainty coefficient by unit type.
4. Attach condition multipliers by material category.
5. Emit normalization audit object for debugging.

### 5.3 Estimation Pipeline

1. Compute volume range:
   - direct volume conversion OR area-thickness conversion
2. Compute weight range:
   - via density range (`effective loaded bulk density`)
3. Apply modifiers:
   - wet
   - mixed load
   - compaction
4. Aggregate totals:
   - sum item ranges
   - apply bulking factor to volume safety estimate

### 5.4 Constraints & Decision

Price risk:

1. compare weight range with `included_tons`.
2. classify `Low/Medium/High`.

Feasibility:

1. compare weight range with `max_haul_tons`.
2. enforce heavy rules (`clean load required`, fill ratio guidance).
3. classify:
   - `OK`
   - `Multi-haul required`
   - `Not recommended`

### 5.5 Recommendation Generation

1. Decide mode:
   - `heavy_mode` if heavy threshold/flags reached
   - otherwise `standard_mode`
2. Enumerate candidate scenarios:
   - single container candidates
   - multi-haul candidates
3. Score candidates by:
   - feasibility first
   - then price risk
   - then volume fit
4. Emit:
   - `Safe` recommendation
   - `Budget` recommendation (always with warning condition)
5. Generate `hard_stop_reasons[]` where applicable.

### 5.6 Cost Comparison

For each option:

1. `single_dumpster`
2. `multi_haul_10_or_15yd`
3. `junk_removal`

Calculate low/typ/high cost range with:

`total = rental + haul + max(0, weight - included_tons) * overage + extras`

Output language constraints:

1. "likely lower"
2. "similar"
3. "can invert due to overage risk"

No absolute pricing guarantees.

### 5.7 Explanation Layer

Generate:

1. assumptions list
2. input impact summary line
3. vendor checklist (included tons, overage, heavy acceptance rules)
4. source/version disclosure
5. confidence signal (from data quality and uncertainty spread)

## 6. UI/UX Precision Plan

### 6.1 Primary Screens

1. Calculator Landing (input-focused)
2. Result Screen (decision-focused)
3. Share Estimate Screen (read-only, noindex)

### 6.2 Result Screen Information Priority

1. Safe recommendation
2. Budget recommendation
3. Feasibility
4. Risk meter
5. Volume/weight ranges
6. Cost comparison
7. Input impact summary
8. Vendor checklist
9. CTA block

### 6.3 Interaction & State Matrix

States:

1. idle
2. validating
3. calculating
4. result-ok
5. result-warning
6. result-hard-stop
7. validation-error
8. service-error

Every state must define:

1. visible message
2. available actions
3. analytics event

### 6.4 CTA Routing UX

Rules:

1. if `feasibility != OK` => junk CTA primary
2. if `price_risk = High` => junk CTA primary
3. if `need_timing = 48h` => call CTA emphasized
4. if `persona = contractor` => share/checklist emphasized

## 7. Data Governance Plan

### 7.1 Source Registry

For each source entry store:

1. source name
2. URL
3. access date
4. extraction method
5. mapped fields
6. reviewer

### 7.2 Quality Gates

Automated checks:

1. no null critical fields
2. numeric bounds sane
3. low <= typ <= high
4. included_tons <= max_haul_tons
5. unit conversion round-trip sanity

Manual checks:

1. sampled record audit
2. high-impact material review (concrete, dirt, shingles)
3. update diff sign-off

## 8. API/Storage/Security Plan

### 8.1 Endpoints

1. `GET /dumpster/size-weight-calculator` (JTE SSR entry page)
2. `POST /api/estimates` (JSON calculation + persistence)
3. `GET /api/estimates/{id}` (JSON fetch)
4. `GET /dumpster/estimate/{id}` (JTE SSR share page, noindex)

### 8.2 Response Requirements

Must include:

1. `price_risk`
2. `feasibility`
3. `recommendations[]`
4. `cost_comparison[]`
5. `used_assumed_allowance`
6. `assumptions[]`
7. `input_impact_summary[]`
8. `hard_stop_reasons[]`
9. `calc_engine_version`
10. `data_version`

### 8.3 Estimate Access Controls

1. random ID (UUID/ULID)
2. TTL expiration
3. route-level noindex headers
4. canonical to main calculator
5. no sitemap inclusion

### 8.4 H2 and CSV Persistence Rules

1. H2 is runtime source-of-truth for:
   - reference tables
   - calculated estimates
   - event logs (MVP local mode)
2. CSV is used for:
   - initial data seeding
   - controlled bulk updates
   - backup snapshots
3. Import rules:
   - validate schema before insert
   - reject partial invalid rows
   - log import report with counts/errors
4. Export rules:
   - daily full snapshot
   - on-demand snapshot before release
   - include `data_version` manifest file

## 9. Tracking Plan

### 9.1 Core Events

1. `calc_started`
2. `calc_completed`
3. `result_viewed`
4. `allowance_entered`
5. `heavy_debris_flagged`
6. `feasibility_not_ok`
7. `cta_click_dumpster_call`
8. `cta_click_dumpster_form`
9. `cta_click_junk_call`
10. `share_estimate_created`

### 9.2 Downstream Events

1. `lead_submitted_valid`
2. `call_connected_60s`
3. `quote_received`
4. `job_booked`
5. `revenue_postback_received`

## 10. QA Plan

### 10.1 Test Layers

1. Unit tests (conversion, ranges, constraints, routing)
2. Scenario tests (30+)
3. Failure-required tests
4. Regression snapshots

### 10.2 Must-Fail Cases

1. heavy concrete large load should not return large single container as feasible.
2. high wet mixed load should elevate weight risk significantly.
3. no-allowance input must mark assumption badge.
4. feasibility hard stop must disable unsafe recommendation path.

### 10.3 Release Gate

All required before launch:

1. no P0 logic bug
2. all must-fail scenarios behaving correctly
3. noindex/canonical/robots checks pass
4. analytics events verified end-to-end

## 11. Build Order (Exact Execution Sequence)

1. Contracts + schema freeze.
2. H2 schema + CSV seed/import/export scripts.
3. Engine normalization + estimation.
4. Risk + feasibility separation.
5. Recommendation + cost comparison.
6. Spring REST controllers (`/api/estimates`) integration.
7. JTE SSR pages + state handling.
8. CTA routing and analytics.
9. Estimate persistence/TTL and noindex controls.
10. Test pass + launch.

## 12. Ownership Template (Fill Before Start)

1. Product owner:
2. Design owner:
3. Engine owner:
4. Web/JTE owner:
5. SEO/analytics owner:
6. QA owner:
7. Release owner:

## 13. Definition of Done

MVP is done only when:

1. input->result flow completes under 60s on mobile.
2. output includes both `price_risk` and `feasibility`.
3. result provides Safe/Budget + explanation + dual CTA.
4. estimate sharing is secure and noindex-safe.
5. 30+ scenarios plus must-fail tests pass.
6. tracking is connected from click to downstream conversion.

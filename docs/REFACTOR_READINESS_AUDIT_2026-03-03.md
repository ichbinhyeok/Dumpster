# Refactor Readiness Audit (2026-03-03)

## 1) Audit Purpose
Confirm whether the repository is ready for a refactor-grade repositioning from calculator-first UX to B2C decision-engine UX, and identify what must be preserved versus rebuilt.

## 2) Audit Scope
- Product shell and copy
- Domain estimation and routing logic
- Source data model and coverage
- Frontend architecture
- SEO/content generation
- Tracking/event model
- Test contracts and migration risk

## 3) Baseline Inventory
- Backend test classes: 16 (`src/test/java/com/dumpster/calculator/*Tests.java`)
- E2E specs: 15 (`tests/e2e/*.spec.ts`)
- Material factors: 20 rows (`src/main/resources/data/material_factors.csv`)
- Unit conversions: 10 rows (`src/main/resources/data/unit_conversions.csv`)
- Dumpster size policy rows: 5 (`src/main/resources/data/dumpster_sizes.csv`)
- Pricing rows: 6 (`src/main/resources/data/pricing_assumptions.csv`)
- Frontend runtime JS: `calculator.js` ~44KB monolith

## 4) Findings by Layer

### 4.1 Product Language and Entry UX
Status: **red (high priority)**

Findings:
- Consumer-facing pages still expose operator/system wording:
  - `Decision board`, `Decision output`, `verdict`, `Open simulator`, `Optional routing signal`.
- Primary CTA language remains generic:
  - `Contact for quote`.

Evidence:
- `src/main/jte/calculator/index.jte`
- `src/main/jte/seo/intent-page.jte`
- `src/main/jte/seo/material-page.jte`
- `src/main/jte/seo/project-page.jte`
- `src/main/resources/static/js/calculator.js`

Impact:
- High mismatch risk with homeowner mental model.
- Reduces trust and lowers clarity of "what do I do next?".

### 4.2 Domain Logic and Routing
Status: **green (preserve and extend)**

Findings:
- Estimation pipeline is coherent and test-covered.
- CTA routing already supports scenario branch logic:
  - non-feasible -> junk-first,
  - high risk -> junk-first,
  - urgent timing -> dumpster call.
- Multi-item command shape already exists in API model.

Evidence:
- `src/main/java/com/dumpster/calculator/domain/service/EstimationFacade.java`
- `src/main/java/com/dumpster/calculator/domain/service/CtaRoutingService.java`
- `src/main/java/com/dumpster/calculator/domain/model/EstimateCommand.java`

Impact:
- Core engine should be retained; output framing should be upgraded.

### 4.3 Data Model and Coverage
Status: **yellow (expand and normalize)**

Findings:
- Current CSV model is compact and practical.
- Junk-removal pathway is represented mainly via pricing proxy row (`size_yd=0`), not a rich service dataset.
- Source provenance exists partially in SEO assets but not yet as unified runtime data contract.

Evidence:
- `src/main/resources/data/pricing_assumptions.csv`
- `docs/seo/data/source_registry_filled_v1.csv`

Impact:
- Decision quality and content defensibility cap out without richer junk/comparison data.

### 4.4 Frontend Architecture
Status: **red (refactor required)**

Findings:
- Single JS file handles state/render/events/network/lead flow.
- Harder to evolve decision-mode UX and instrumentation cleanly.

Evidence:
- `src/main/resources/static/js/calculator.js`

Impact:
- High change cost and regression risk for strategic UX shifts.

### 4.5 SEO and Content Generation
Status: **yellow (rewrite generation rules, keep policy controls)**

Findings:
- Selective indexing and split sitemap controls are robust and should be retained.
- Intent question/title generation is deterministic but still system-shaped in phrasing.
- Comparison hub exists but should be deepened to finish decisions.

Evidence:
- `src/main/java/com/dumpster/calculator/web/content/SeoContentService.java`
- `docs/seo/EXECUTION_ANCHOR_ORGANIC_PSEO_v1.md`

Impact:
- SEO structure is strong; language-level generation needs refactor.

### 4.6 Tracking and Analytics
Status: **yellow (expand taxonomy)**

Findings:
- Tracking API has guardrails and explicit allowlist.
- Needed decision-stage events are not yet included.

Evidence:
- `src/main/java/com/dumpster/calculator/api/controller/TrackingApiController.java`
- `src/main/resources/static/js/calculator.js`

Impact:
- Current measurement is insufficient for decision-flow optimization.

### 4.7 Test Contracts
Status: **yellow (good coverage, copy-coupled assertions exist)**

Findings:
- Business-rule and SEO safety tests are strong.
- Some E2E checks assert exact UI strings (`Contact for quote`) and must be updated with copy migration plan.

Evidence:
- `tests/e2e/smoke-critical.spec.ts`
- `tests/e2e/seo-aeo.spec.ts`
- `tests/e2e/intent-cluster-mass.spec.ts`

Impact:
- Refactor is safe if test updates are sequenced with feature work.

## 5) Preserve vs Refactor Matrix
| Layer | Preserve | Refactor |
|---|---|---|
| Core estimate math | yes | no direct redesign in first cycle |
| Heavy/allowance/feasibility semantics | yes | wording and surfacing only |
| CTA branch logic intent | yes | relabel and expose rationale |
| Frontend structure | no | split monolith into modules |
| Content generation phrasing | no | deterministic human-language rewrite |
| Data model for junk/comparison | no | expand into explicit dataset family |
| Selective indexing policy | yes | keep and extend with quality gates |
| Tracking taxonomy | partial | expand to decision-stage model |

## 6) Readiness Verdict
Verdict: **GO for refactor-grade execution**, with compatibility-first migration.

Rationale:
- Strongest components (engine + tests + SEO controls) are reusable.
- Weakest components (language + frontend modularity + data depth) are exactly the parts that can be improved without discarding engine value.

## 7) Required Next Anchor
Use this audit with:
- `docs/EXECUTION_ANCHOR_B2C_DECISION_ENGINE_REFACTOR_v1_2026-03-03.md`

Execution without this anchor should be treated as out-of-process.


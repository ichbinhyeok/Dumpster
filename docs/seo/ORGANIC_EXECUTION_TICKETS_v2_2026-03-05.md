# Organic SEO Execution Tickets v2 (2026-03-05)

## Goal
Shift from broad index expansion to a curated, evidence-first set that wins heavy-debris and risk-driven queries.

## Current Repo Snapshot
- Default runtime policy is broad:
  - `app.seo.max-wave=3`
  - `app.seo.intent-index-mode=expanded`
  - Source: `src/main/resources/application.properties`
- Intent index controls already exist in code:
  - curated seed list and expanded mode switch in `SeoContentService`
  - source: `src/main/java/com/dumpster/calculator/web/content/SeoContentService.java`
- Sitemap split already exists (`core`, `money`, `experiments`):
  - source: `src/main/java/com/dumpster/calculator/web/controller/SeoInfrastructureController.java`
- Existing phase-1 SEO docs/data already exist under `docs/seo/` and can be reused.

## Output Contract (A-E)
1. A: `docs/seo/data/url_classification_v2_curated.csv`
2. B: `docs/seo/data/priority_pages_top20_v2.csv`
3. C: `docs/seo/data/title_h1_meta_intro_rewrite_v2.csv`
4. D: `docs/seo/data/evidence_gap_v2.csv`
5. E: `docs/seo/ORGANIC_EXECUTION_TICKETS_v2_2026-03-05.md` (this file)

## Priority Tickets

### P0

#### [ ] SEO-001 Freeze baseline and guardrails
- Why: prevent subjective changes and measure impact of index contraction.
- Touchpoints:
  - `docs/seo/PHASE1_EXECUTION_PACK_v1.md`
  - `docs/seo/EXECUTION_ANCHOR_ORGANIC_PSEO_v1.md`
- Tasks:
  - Record current indexable route counts by type (core/material/project/special/intent).
  - Record last 28-day GSC baseline: impressions, clicks, CTR by landing URL group.
  - Lock "Do Not Build" list (city/zip/near-me mass pages).
- Acceptance criteria:
  - Baseline table committed with date stamp.
  - Guardrails section updated with explicit no-doorway rule.

#### [ ] SEO-002 Switch default intent indexing to curated
- Why: expanded mode can outrun evidence and uniqueness.
- Touchpoints:
  - `src/main/resources/application.properties`
  - `src/test/resources/application.properties`
  - `src/test/java/com/dumpster/calculator/SeoInfrastructureTests.java`
- Tasks:
  - Change default `app.seo.intent-index-mode` to `curated`.
  - Keep override path available via env var for rollback/experiments.
  - Update tests that assume expanded-by-default route volume.
- Acceptance criteria:
  - Default boot mode resolves to curated.
  - Non-curated intent pages remain `noindex, follow`.
  - SEO test suite passes.
- Rollback:
  - Set `APP_SEO_INTENT_INDEX_MODE=expanded`.

#### [ ] SEO-003 Enforce top-20 seed as index contract
- Why: one intent => one winner page, avoid cannibalization.
- Touchpoints:
  - `docs/seo/data/page_intent_roi_map_v1.csv`
  - `docs/seo/data/priority_slug_title_meta_h1_final.csv`
  - `src/main/java/com/dumpster/calculator/web/content/SeoContentService.java`
  - `src/main/java/com/dumpster/calculator/web/content/catalog/SeoRoutingCatalog.java`
- Tasks:
  - Create v2 top-20 seed file with confidence tier and canonical winner.
  - Map each seed to exactly one canonical URL.
  - Mark non-seed overlaps as merge or canonical-to.
- Acceptance criteria:
  - Top-20 CSV exists with no duplicate target query ownership.
  - Every non-seed overlap has explicit action (`noindex`, `merge`, `canonical`).

#### [ ] SEO-004 URL classification v2 (Index/Noindex/Merge/Canonical)
- Why: make index policy explicit at URL-pattern and page level.
- Touchpoints:
  - `docs/seo/data/url_classification_v1.csv`
  - `src/main/java/com/dumpster/calculator/web/controller/SeoPageController.java`
  - `src/main/java/com/dumpster/calculator/web/controller/SeoInfrastructureController.java`
- Tasks:
  - Produce v2 classification table with fields:
    - `url_or_pattern`
    - `intent`
    - `target_query`
    - `unique_value`
    - `confidence`
    - `action`
  - Align sitemap inclusion rules with v2 action policy.
- Acceptance criteria:
  - v2 classification committed.
  - Sitemap output does not include URLs marked `noindex` or `merge`.

#### [ ] SEO-005 Rewrite search-language copy for top-20
- Why: current product language must be translated into user query language.
- Touchpoints:
  - `src/main/java/com/dumpster/calculator/web/content/SeoContentService.java`
  - `src/main/java/com/dumpster/calculator/web/content/catalog/SeoCopyCatalog.java`
  - `src/main/jte/seo/material-page.jte`
  - `src/main/jte/seo/project-page.jte`
  - `src/main/jte/seo/intent-page.jte`
  - `src/main/jte/seo/special-page.jte`
- Tasks:
  - For each top-20 page, define: current title, proposed title, H1, meta, first paragraph draft.
  - Apply "question -> short answer -> key reason -> variance warning -> next action" structure.
- Acceptance criteria:
  - Rewrite CSV exists and is fully populated for 20 pages.
  - Rendered pages follow the intro structure above.

### P1

#### [ ] SEO-006 Add evidence confidence and variance blocks
- Why: trust depends on transparent uncertainty, not false precision.
- Touchpoints:
  - `src/main/java/com/dumpster/calculator/web/viewmodel/*.java`
  - `src/main/java/com/dumpster/calculator/web/content/SeoContentService.java`
  - `src/main/jte/seo/material-page.jte`
  - `src/main/jte/seo/project-page.jte`
  - `src/main/jte/seo/intent-page.jte`
  - `docs/seo/data/evidence_gap_v2.csv`
- Tasks:
  - Add fields for confidence tier and variance note per page.
  - Add UI badges: `High`, `Medium`, `Low`.
  - Add operator checklist block where confidence is not high.
- Acceptance criteria:
  - Every top-20 page shows confidence + variance note.
  - Evidence gap CSV maps each page to missing evidence fields.

#### [ ] SEO-007 Quick estimate first, advanced mode second
- Why: organic traffic needs immediate result before deep form completion.
- Touchpoints:
  - `src/main/jte/calculator/index.jte`
  - `src/main/resources/static/js/calculator.js`
  - `tests/e2e/calculator.spec.ts`
  - `tests/e2e/seo-aeo.spec.ts`
- Tasks:
  - Split calculator flow into `Quick estimate` and `Advanced assumptions`.
  - Keep first-step fields minimal: project + material + quantity.
  - Reveal modifiers after first result render.
- Acceptance criteria:
  - Result appears on first screen on mobile for quick mode.
  - No regression in current critical calculator flows.

#### [ ] SEO-008 Reduce internal link fan-out
- Why: excessive related links dilute topic focus and create noisy graphs.
- Touchpoints:
  - `src/main/java/com/dumpster/calculator/web/content/SeoContentService.java`
  - `src/main/jte/seo/*.jte`
- Tasks:
  - Cap default related links to 2-3 decision-relevant links.
  - Remove broad reciprocal linking on weak pages.
  - Keep hub-to-child routing intentional.
- Acceptance criteria:
  - Fan-out cap enforced in templates/service.
  - Link blocks pass manual spot-check for next-decision relevance.

### P2

#### [ ] SEO-009 Canonical and merge cleanup for weak variants
- Why: remove near-duplicate answer clusters from index competition.
- Touchpoints:
  - `src/main/java/com/dumpster/calculator/web/content/catalog/SeoRoutingCatalog.java`
  - `src/main/java/com/dumpster/calculator/web/controller/SeoPageController.java`
  - `docs/seo/data/url_classification_v2_curated.csv`
- Tasks:
  - Add or refine redirect aliases to canonical winners.
  - Convert low-confidence overlaps to noindex + merge targets.
- Acceptance criteria:
  - Every merge target has deterministic redirect/canonical behavior.

#### [ ] SEO-010 Measurement and rollout checkpoints
- Why: policy changes must be validated with traffic and behavior data.
- Touchpoints:
  - `src/main/resources/static/js/analytics.js`
  - `src/main/resources/static/js/calculator.js`
  - `docs/seo/PHASE1_EXECUTION_PACK_v1.md`
- Tasks:
  - Track funnel events by landing page type.
  - Define 2-week and 4-week checkpoints.
  - Monitor per-page impressions/clicks/CTR and result-view rate.
- Acceptance criteria:
  - Dashboard slice exists for top-20 only.
  - Rollout decision log updated at each checkpoint.

## Suggested Execution Order (1 sprint)
1. SEO-001
2. SEO-002
3. SEO-003
4. SEO-004
5. SEO-005
6. SEO-006
7. SEO-007
8. SEO-008
9. SEO-009
10. SEO-010

## Validation Checklist
- Backend tests:
  - `./gradlew test --tests com.dumpster.calculator.SeoIntentIndexModeTests --tests com.dumpster.calculator.SeoInfrastructureTests --tests com.dumpster.calculator.SeoIntentQualityGateTests`
- E2E smoke for SEO-critical routes:
  - `npm run e2e:smoke`
  - `npx playwright test tests/e2e/seo-aeo.spec.ts tests/e2e/high-intent-pages.spec.ts`
- Manual checks:
  - `robots.txt` entries align with policy.
  - `sitemap-money.xml` contains only intended index set.
  - sample non-seed intent URLs return `noindex, follow`.

## Definition of Done
- Top-20 index set is explicit and enforced.
- Default runtime policy is curated, with rollback switch documented.
- Every top-20 page has search-language copy and confidence/variance disclosure.
- URL classification, title rewrite, and evidence gap artifacts are all updated in `docs/seo/data/`.

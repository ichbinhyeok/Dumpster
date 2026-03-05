# Organic SEO Execution State v2 (2026-03-05)

## Goal
Run a curated, evidence-first organic strategy that wins heavy-debris and risk-driven queries without index bloat.

## Current Policy (Active)

- Default intent index mode: `curated`
- Index contract: priority seed set only (top material/project/special paths)
- Non-priority overlaps: `noindex` / canonical / merge handling
- Experiments sitemap: intentionally empty while seed quality is enforced

## Implemented Changes

1. Curated indexing enabled by default in runtime and test config.
2. Priority indexable sets enforced in SEO content/routing logic.
3. X-Robots behavior aligned by page type (indexable vs non-indexable).
4. Search-language title/H1/meta rewrites applied for priority pages.
5. Evidence UI added: confidence tier, variance note, vendor checklist.
6. Calculator split: quick estimate first, advanced controls on demand.

## Active Output Contract (A-E)

1. A: `docs/seo/data/url_classification_v2_curated.csv`
2. B: `docs/seo/data/priority_pages_top20_v2.csv`
3. C: `docs/seo/data/title_h1_meta_intro_rewrite_v2.csv`
4. D: `docs/seo/data/evidence_gap_v2.csv`
5. E: `docs/seo/ORGANIC_EXECUTION_TICKETS_v2_2026-03-05.md` (this file)

## Validation Baseline

- Backend:
  - `./gradlew test --tests "com.dumpster.calculator.SeoPageRenderingTests"`
  - `./gradlew test --tests "com.dumpster.calculator.SeoInfrastructureTests"`
- E2E:
  - `npx playwright test tests/e2e/calculator.spec.ts --grep "quick mode hides advanced controls"`

## Remaining Backlog (Current)

1. Enforce internal-link fan-out cap consistently on all weak pages.
2. Complete canonical/merge cleanup for residual near-duplicates.
3. Track 2-week and 4-week GSC/GA4 checkpoints:
   - index coverage quality
   - page-level CTR concentration
   - calculator result reach rate

## Context Rule
Only v2 artifacts listed in this file are active for SEO execution decisions.

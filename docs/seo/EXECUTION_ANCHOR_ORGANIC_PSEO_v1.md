# Dumpster Organic Execution Anchor v1

## Parent Anchor (2026-03-03)
- SEO execution remains active under this document.
- Program-level refactor governance is anchored at:
  - `docs/EXECUTION_ANCHOR_B2C_DECISION_ENGINE_REFACTOR_v1_2026-03-03.md`
- If a conflict appears between this file and the parent anchor, follow the parent anchor and update this file in the same change set.

## Scope Lock
- This track covers only organic/SEO/pSEO execution.
- UI/presentation rebuild tasks are explicitly out of scope.
- The product is treated as a decision engine, not an informational blog.

## Non-Negotiables
- Keep `/dumpster/size-weight-calculator` as the canonical hub.
- Keep index growth controlled with explicit wave and quality gates.
- Do not build city/ZIP/near-me mass pages.
- Every indexable page must include quick answer, calculator/preset entry, unique data, mistakes block, and CTA.
- Do not publish numeric claims that are missing from source registry.
- Resolve conflicting values as ranges with caveats, not single-value certainty.

## Current Operating Policy (2026-03-03)
- `app.seo.max-wave` default is `3`.
- `/sitemap.xml` is a sitemap index that points to:
  - `/sitemap-core.xml`
  - `/sitemap-money.xml`
  - `/sitemap-experiments.xml`
- `/dumpster/answers/` is crawl-allowed in `robots.txt`.
- Intent pages use selective indexing:
  - curated allowlist paths (`19` routes as of 2026-03-04): `index, follow`
  - all other intent combinations: `noindex, follow`
- Hub pages (`/dumpster/material-guides`, `/dumpster/project-guides`) remain crawlable but currently `noindex, follow` and tracked in experiments sitemap.

## Execution Order (A->G)
1. URL inventory classification (`KEEP`, `MERGE`, `REWRITE`, `NOINDEX`, `DO NOT BUILD`).
2. Phase 1 roadmap and build waves.
3. Page-type template spec lock.
4. Source gap audit against required decision fields.
5. Source registry fill with citations and confidence.
6. Slug/title/meta/H1 finalization table.
7. Launch order, internal link map, and CTA routing.

## Drift Guard Checklist
- Did we accidentally add broad wiki-style pages?
- Did any page type lose calculator-entry behavior?
- Did any route expansion increase thin indexable combinations without evidence?
- Are intent pages creating cannibalization or duplicate SERP targets?
- Are confidence labels present for all filled data fields?
- Are split sitemap buckets (`core`, `money`, `experiments`) still aligned with current indexing policy?

## Output Contract
- `docs/seo/PHASE1_EXECUTION_PACK_v1.md`
- `docs/seo/SOURCE_GAP_AUDIT_v1.md`
- `docs/seo/data/priority_slug_title_meta_h1_final.csv`
- `docs/seo/data/source_registry_filled_v1.csv`

## Execution Log
- 2026-03-03: Created phase-one execution pack, source gap audit, URL classification, intent map, final slug/meta table, and filled source registry.
- 2026-03-03: Synced final CSV outputs to user-provided Downloads files for immediate operational use.
- 2026-03-03: Applied initial conservative policy in code (intent/hub mostly noindex, reduced sitemap exposure).
- 2026-03-03: Added canonical slug normalization (public slugs) for key material/project routes with permanent redirects from legacy ID paths.
- 2026-03-03: Implemented dedicated phase-one special decision pages (`rule/limit/comparison/unit`) and connected sitemap wave gating via `app.seo.max-wave`.
- 2026-03-03: Updated default canonical base domain to `https://debrisdecision.com` via `APP_BASE_URL` default.
- 2026-03-03: Updated rollout default to `app.seo.max-wave=3` to include validated wave3 assets.
- 2026-03-03: Switched to split sitemap strategy (`/sitemap.xml` index + `core/money/experiments` children).
- 2026-03-03: Re-opened `/dumpster/answers/` crawling and introduced curated indexable intent allowlist while keeping remaining intent pages `noindex, follow`.
- 2026-03-04: Expanded curated intent allowlist from `10` to `19` routes (homeowner cleanup/remodel + heavy-risk scenarios), while preserving selective noindex policy for non-curated combinations.
- 2026-03-04: Rewired decision-stage internal links to prioritize indexable intent adjacency and added richer comparison-hub scenario/FAQ coverage.

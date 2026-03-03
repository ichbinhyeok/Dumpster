# Dumpster Organic Execution Anchor v1

## Scope Lock
- This track covers only organic/SEO/pSEO execution.
- UI/presentation rebuild tasks are explicitly out of scope.
- The product is treated as a decision engine, not an informational blog.

## Non-Negotiables
- Keep `/dumpster/size-weight-calculator` as the canonical hub.
- Keep Phase 1 indexable surface to 20-35 URLs.
- Do not build city/ZIP/near-me mass pages.
- Every indexable page must include quick answer, calculator/preset entry, unique data, mistakes block, and CTA.
- Do not publish numeric claims that are missing from source registry.
- Resolve conflicting values as ranges with caveats, not single-value certainty.

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
- Did any route expansion increase thin indexable combinations?
- Are intent pages creating cannibalization or duplicate SERP targets?
- Are confidence labels present for all filled data fields?

## Output Contract
- `docs/seo/PHASE1_EXECUTION_PACK_v1.md`
- `docs/seo/SOURCE_GAP_AUDIT_v1.md`
- `docs/seo/data/priority_slug_title_meta_h1_final.csv`
- `docs/seo/data/source_registry_filled_v1.csv`

## Execution Log
- 2026-03-03: Created phase-one execution pack, source gap audit, URL classification, intent map, final slug/meta table, and filled source registry.
- 2026-03-03: Synced final CSV outputs to user-provided Downloads files for immediate operational use.
- 2026-03-03: Applied indexing policy in code: intent/hub pages noindex, robots disallow for intent/hub routes, sitemap reduced to phase-one indexables.
- 2026-03-03: Added canonical slug normalization (public slugs) for key material/project routes with permanent redirects from legacy ID paths.
- 2026-03-03: Implemented dedicated phase-one special decision pages (`rule/limit/comparison/unit`) and connected sitemap wave gating via `app.seo.max-wave`.
- 2026-03-03: Set default rollout to `app.seo.max-wave=2` for launch mode (`wave1 + wave2` indexed by default).

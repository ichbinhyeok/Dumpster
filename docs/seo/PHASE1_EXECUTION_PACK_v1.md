# Dumpster Phase 1 Execution Pack v1

## 0) Scope and Anchor
- Source anchor: `docs/seo/EXECUTION_ANCHOR_ORGANIC_PSEO_v1.md`
- Execution model: `A -> B -> C -> D -> E -> F -> G`
- Objective: capture decision-intent organic traffic, not broad informational traffic.

## 1) A. URL Classification (Current + Planned)

### KEEP (Indexable)
- `/dumpster/size-weight-calculator`
- `/dumpster/heavy-debris-rules`
- `/dumpster/weight/concrete`
- `/dumpster/weight/shingles`
- `/dumpster/weight/drywall`
- `/dumpster/weight/dirt`
- `/dumpster/weight/brick-block`
- `/dumpster/how-many-tons-can-a-10-yard-dumpster-hold`
- `/dumpster/can-you-put-concrete-in-a-dumpster`
- `/dumpster/can-you-mix-concrete-and-wood-in-a-dumpster`
- `/dumpster/dumpster-vs-junk-removal`
- `/dumpster/pickup-truck-loads-to-dumpster-size`
- `/dumpster/roofing-squares-to-dumpster-size`
- project family `/dumpster/size/{project-slug}` for enabled wave set
- intent family `/dumpster/answers/{project}/{material}/{intent}` with mode-gated indexing (`curated` or `expanded`)

### MERGE
- Query variants of 10-yard tonnage intent into one canonical limit page.
- Query variants of concrete calculator intent into one canonical concrete page.

### REWRITE
- `/dumpster/weight/{materialId}` dynamic family: keep mapped high-ROI materials as indexable; rewrite copy by template to avoid thin duplication.
- `/dumpster/size/{projectId}` dynamic family: normalize slug conventions and tighten scenario-specific differentiation.
- `/dumpster/material-guides` and `/dumpster/project-guides`: remain decision-routing hubs with stronger internal routing.

### NOINDEX
- Intent combinations outside active `app.seo.intent-index-mode` policy.
- `/dumpster/estimate/{estimateId}` share/session pages.
- Query/preset parameter permutations.

### DO NOT BUILD
- City/state/county doorway pages.
- ZIP/near-me mass pages.
- Broad glossary/encyclopedia content not tied to decision flow.

## 2) B. Phase 1 Roadmap (Build Waves)

### Wave 1
- Calculator hub + heavy-debris core.
- Concrete/shingles/drywall/dirt material pages.
- Core special pages:
  - what-size intent
  - 10-yard limit
  - concrete rule
  - dumpster vs junk removal
  - pickup-load converter
  - roofing squares converter

### Wave 2
- `bathroom_remodel`, `roof_tearoff`, `deck_demolition`, `kitchen_remodel` project pages.
- supporting special pages and intent candidates validated by CTR/CVR.

### Wave 3 (Current default enabled)
- `garage_cleanout`, `estate_cleanout`, `yard_cleanup`, `dirt_grading`, `concrete_removal`, `light_commercial_fitout`.
- guide hubs remain indexable; experiments sitemap is used as monitoring bucket, not a noindex bucket.

## 3) C. Template Spec Lock

### Calculator
- Must include: quick answer, calculator input, result summary, assumptions, risk links, comparison links, one strong CTA.

### Material
- Must include: quick answer, material preset, size-vs-weight range table, mistakes block, rule links, comparison CTA.
- Must avoid: encyclopedia style repetition.

### Rule/Limit
- Must include: yes/no/depends answer, consequence if ignored, mini estimator/rule card, vendor checklist, CTA to calculator.

### Comparison
- Must include: one-screen answer, decision table, inversion triggers, dual CTA.

### Project
- Must include: preset scenario, toggle-based branching, size-up/down triggers, heavy-material branch, alternative compare.

### Unit/Conversion
- Must include: converter input, mapping ranges, underestimation mistakes, calculator handoff CTA.

## 4) D. Source Gap Snapshot
- Separate detailed audit: `docs/seo/SOURCE_GAP_AUDIT_v1.md`.
- Core gap themes: regional variance handling, conversion assumptions for project presets, explicit provider policy provenance, and conflict resolution notes.

## 5) E/F. Data and Metadata Outputs
- Source registry filled file: `docs/seo/data/source_registry_filled_v1.csv`.
- Slug/title/meta/H1 final file: `docs/seo/data/priority_slug_title_meta_h1_final.csv`.
- Page intent/ROI map: `docs/seo/data/page_intent_roi_map_v1.csv`.
- URL classification table: `docs/seo/data/url_classification_v1.csv`.

## 6) G. Internal Link and CTA Routing

### Core link graph
- Calculator hub links to: material cluster, rule/limit cluster, comparison cluster, unit-intent cluster.
- Material pages link to: relevant rule pages, calculator preset, project pages, comparison page.
- Project pages link to: relevant material pages, rule pages, calculator preset, comparison page.
- Comparison pages link to: calculator, relevant material/project pages, heavy rules.

### CTA routing matrix
- High heavy-debris risk -> `See your safest dumpster size` (primary), `Compare dumpster vs junk removal` (secondary).
- Feasibility uncertainty -> `Check heavy-debris rules` (primary), `Start calculator` (secondary).
- Budget-sensitive but feasible -> `See lowest-risk budget option` (primary), `Check if a 10-yard is enough` (secondary).
- Project uncertainty -> `Start calculator with project preset` (primary), `Compare disposal options` (secondary).

## 7) Measurement Plan (Decision Progression)
Track at minimum:
- `page_view`
- `organic_landing_page`
- `calculator_start`
- `calculator_complete`
- `result_view`
- `material_page_to_calculator_click`
- `project_page_to_calculator_click`
- `rules_page_to_calculator_click`
- `comparison_page_cta_click`
- `quote_cta_click`
- `junk_compare_cta_click`

## 8) Indexing and Sitemap Layers
- `/sitemap.xml` -> sitemap index file only.
- `/sitemap-core.xml` -> core trust + calculator pages.
- `/sitemap-money.xml` -> indexable special/material/project + active intent index set (`expanded` or `curated` mode).
- `/sitemap-experiments.xml` -> crawlable test pages (currently guide hubs).

## 9) Conflict Resolution Notes
- `included_tons` and `max_haul_tons` must remain separate fields in both data and page copy.
- Where provider rules conflict, publish range + caveat, not a single fixed statement.
- Local policy variance can appear as note blocks only; no local landing-page expansion in Phase 1.

## 10) Intent Index Modes (Current)
- `expanded` (default): wave 3 decision-intent matrix (`89` routes as of 2026-03-04) is indexable.
- `curated`: quality-screened seed set (`19` routes) remains indexable.
- both modes keep non-active combinations as `noindex, follow`.

## 11) Implementation Status (2026-03-03)
- Canonical slug normalization added for key material/project routes.
- Dedicated phase-one special pages shipped.
- `robots.txt` now crawl-allows `/dumpster/answers/`.
- Intent pages now use route-level selective robots (`index, follow` vs `noindex, follow`).
- Sitemap is split into `core`, `money`, and `experiments` layers with sitemap index entrypoint.
- Default rollout uses `app.seo.max-wave=3`.
- Default intent indexing mode uses `app.seo.intent-index-mode=expanded` with `curated` fallback available.

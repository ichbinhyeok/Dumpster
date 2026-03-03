# Dumpster Phase 1 Execution Pack v1

## 0) Scope and Anchor
- Source anchor: `docs/seo/EXECUTION_ANCHOR_ORGANIC_PSEO_v1.md`
- Execution model: `A -> B -> C -> D -> E -> F -> G`
- Objective: capture decision-intent organic traffic, not broad informational traffic.

## 1) A. URL Classification (Current + Planned)

### KEEP
- `/dumpster/size-weight-calculator`
- `/dumpster/heavy-debris-rules`
- `/dumpster/weight/concrete/`
- `/dumpster/weight/shingles/`
- `/dumpster/weight/drywall/`
- `/dumpster/weight/dirt/`
- `/dumpster/how-many-tons-can-a-10-yard-dumpster-hold/`
- `/dumpster/can-you-put-concrete-in-a-dumpster/`
- `/dumpster/can-you-mix-concrete-and-wood-in-a-dumpster/`
- `/dumpster/dumpster-vs-junk-removal/`
- `/dumpster/pickup-truck-loads-to-dumpster-size/`
- `/dumpster/roofing-squares-to-dumpster-size/`

### MERGE
- Query variants of 10-yard tonnage intent into one canonical limit page.
- Query variants of concrete calculator intent into one canonical concrete page.

### REWRITE
- `/dumpster/weight/{materialId}` dynamic family: keep only mapped high-ROI materials as indexable; rewrite copy by template to avoid thin duplication.
- `/dumpster/size/{projectId}` dynamic family: normalize slug conventions and tighten scenario-specific differentiation.
- `/dumpster/material-guides` and `/dumpster/project-guides`: convert to decision-routing hubs, not broad guide hubs.

### NOINDEX
- `/dumpster/answers/{projectId}/{materialId}/{intent}` (combinatorial long-tail pages).
- `/dumpster/estimate/{estimateId}` share/session pages (already noindex header on response).
- Query/preset parameter permutations.

### DO NOT BUILD
- City/state/county doorway pages.
- ZIP/near-me mass pages.
- Broad glossary/encyclopedia content not tied to decision flow.

## 2) B. Phase 1 Roadmap (Build Waves)

### Wave 1 (Immediate)
- Calculator hub + heavy-debris core + concrete/shingles/drywall/dirt + 10-yard limit + concrete rule pages + dumpster vs junk removal + two converter pages.

### Wave 2 (After Wave 1 baseline)
- Bagster vs dumpster.
- Bathroom remodel.
- Roof tear-off.
- Deck removal.

### Wave 3 (Later in Phase 1)
- Brick/block.
- Fill-line rules standalone page.
- One 20-yard vs two 10-yard.
- Garage cleanout.
- Kitchen remodel.
- Drywall sheets converter.

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

## 8) Conflict Resolution Notes
- `included_tons` and `max_haul_tons` must remain separate fields in both data and page copy.
- Where provider rules conflict, publish range + caveat, not a single fixed statement.
- Local policy variance can appear as note blocks only; no local landing-page expansion in Phase 1.

## 9) Implementation Status (2026-03-03)
- Applied noindex policy to hub and intent pages.
- Removed combinatorial intent pages from sitemap and robots allowlist.
- Added canonical slug normalization for key material/project routes.
- Added dedicated phase-one special pages: `/dumpster/how-many-tons-can-a-10-yard-dumpster-hold`, `/dumpster/can-you-put-concrete-in-a-dumpster`, `/dumpster/can-you-mix-concrete-and-wood-in-a-dumpster`, `/dumpster/dumpster-vs-junk-removal`, `/dumpster/pickup-truck-loads-to-dumpster-size`, `/dumpster/roofing-squares-to-dumpster-size`.
- Added sitemap wave gating via `app.seo.max-wave` (default `2`).

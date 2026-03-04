# Dumpster Presentation Rebuild Runbook

## Status Note (2026-03-03)
- This document remains a valid historical record for the presentation-only rebuild track.
- Active execution control moved to:
  - `docs/EXECUTION_ANCHOR_B2C_DECISION_ENGINE_REFACTOR_v1_2026-03-03.md`
- The current program scope is broader than presentation and includes domain/data/API/analytics refactor workstreams.

## Locked Contract
- Source of truth: `Dumpster Presentation Bible` + kickoff prompt.
- Scope: presentation layer only (IA, layout, components, tokens, microcopy, conversion UX).
- Non-goals: no core engine formula/data semantics changes, no scaled SEO expansion.
- Platform rule: SSR-first, minimal JS for core decision flow.
- Visual rule: skeleton-first, grayscale must already feel like SaaS.

## Stable Entry Points
- Calculator pages:
  - `src/main/jte/calculator/index.jte`
  - `src/main/jte/calculator/share-estimate.jte`
  - `src/main/jte/calculator/estimate-not-found.jte`
- SEO pages:
  - `src/main/jte/seo/material-page.jte`
  - `src/main/jte/seo/project-page.jte`
  - `src/main/jte/seo/heavy-rules.jte`
  - `src/main/jte/seo/intent-page.jte`
  - `src/main/jte/seo/material-guides.jte`
  - `src/main/jte/seo/project-guides.jte`
- Trust pages:
  - `src/main/jte/trust/methodology.jte`
  - `src/main/jte/trust/editorial-policy.jte`
  - `src/main/jte/trust/contact.jte`
- Shared presentation layer:
  - `src/main/jte/components/*`
  - `src/main/resources/static/css/app.css`
  - `src/main/resources/static/css/calculator-modern.css`
  - `src/main/resources/static/js/calculator.js`

## PR Sequence
1. PR1: App shell + token system + base components.
2. PR2: Calculator two-column workflow + sticky result rail + mobile summary dock.
3. PR3: Result-state hierarchy + hard-stop alerts + trust drawer + CTA routing.
4. PR4: pSEO contextual page family redesign (material/project/heavy + hub alignment).
5. PR5: Microcopy/accessibility/typography polish + regression verification.

## Deliverables Status

### 1) PR plan (4-5 PRs)
- Done. Defined and executed as PR1-PR5 sequence above.

### 2) File-tree diff proposal
- Done (major paths)
  - Added: `src/main/jte/components/` (header/footer/container/card/buttonLink/badge/accordionItem/inputField)
  - Changed: calculator/seo/trust JTE templates, `app.css`, `calculator-modern.css`, `calculator.js`, `tests/e2e/helpers.ts`
  - Added docs: `docs/presentation-rebuild-runbook.md`

### 3) Implement PR1 and PR2 directly
- Done.
  - PR1: shared shell, tokens, and core components integrated.
  - PR2: calculator rebuilt into app workflow surface with sticky right rail + mobile-friendly summary.

### 4) For each PR: checklist + manual test + screenshot list
- Done below.

### 5) Grayscale SaaS quality bar on calculator
- Done. Layout hierarchy and component semantics are readable and actionable without color dependence.

## PR Checklists

### PR1 Checklist
- [x] Shared app shell partials added.
- [x] Pages switched to shared header/footer components.
- [x] Tokenized base CSS in grayscale-first scheme.
- [x] Core primitives/components added (card/button/input/badge/accordion/container).

Manual tests:
1. Open calculator + SEO + trust pages and verify same shell appears.
2. Verify spacing/radius/type scale consistency.
3. Verify pages remain SSR rendered and metadata preserved.

Screenshot list:
1. Desktop calculator shell before/after.
2. Desktop material page shell before/after.
3. Desktop project page shell before/after.
4. Desktop heavy-rules shell before/after.

### PR2 Checklist
- [x] Calculator converted to left-step workflow + right sticky decision rail (desktop).
- [x] Step groups reduced to compact decision-first flow.
- [x] Progressive disclosure via accordions (no long pre-form prose).
- [x] Mobile result dock added for persistent action context.

Manual tests:
1. `/dumpster/size-weight-calculator` renders 2-column desktop app frame.
2. Right rail remains sticky while editing left inputs.
3. Result summary order follows verdict -> risk/feasibility -> evidence -> CTA.
4. Mobile viewport shows summary dock and keeps primary action reachable.

Screenshot list:
1. Calculator desktop full page before/after.
2. Calculator desktop sticky rail before/after.
3. Calculator mobile flow before/after.

### PR3 Checklist
- [x] Added explicit result state banner (feasibility + translated risk).
- [x] Added hard-stop alert module for `hardStopReasons[]`.
- [x] Elevated `inputImpactSummary[]` near top decision zone.
- [x] Added collapsed trust drawer for assumptions + versions.
- [x] State-driven primary CTA routing with secondary alternatives.

Manual tests:
1. Trigger high-risk/heavy case and verify hard-stop treatment.
2. Verify state banner tone/content changes by feasibility/risk.
3. Open trust drawer and verify assumptions + engine/data version.
4. Validate primary CTA changes for urgency/risk/persona cases.

Screenshot list:
1. Result state banner.
2. Hard-stop module.
3. Trust drawer expanded.
4. Primary CTA routing variants.

### PR4 Checklist
- [x] Material pages redesigned as contextual calculator entry surfaces.
- [x] Project pages redesigned as workflow-template surfaces.
- [x] Heavy-rules redesigned as policy-help surface with direct calculator route.
- [x] Hubs and intent pages aligned to same product-entry language.
- [x] Metadata/schema/canonical policy preserved.

Manual tests:
1. Verify top quick-answer + preset launch on material pages.
2. Verify project page mistake-prevention + scenario modules.
3. Verify heavy-rules policy cards + direct calculator CTA.
4. Verify pages still share app shell and SSR metadata.

Screenshot list:
1. Material page top/middle/bottom sections.
2. Project page top/middle/bottom sections.
3. Heavy-rules page key modules.
4. Material/project hub pages.

### PR5 Checklist
- [x] Typography tokens aligned to `Manrope` (body) + `Space Grotesk` (display).
- [x] All JTE font includes standardized.
- [x] `font-display` class overridden to display token family.
- [x] Mobile floating CTA tap-target height fixed to >= 44px.
- [x] Regression tests rerun.

Manual tests:
1. Inspect calculator on desktop/mobile and confirm body/display font families.
2. Confirm floating CTA buttons meet touch comfort size on touch devices.
3. Confirm layout, result rendering, and lead flow behavior unchanged.

Screenshot list:
1. Calculator desktop typography close-up (h1/body).
2. iPad and phone floating CTA tap-target visuals.
3. Mobile result dock with primary CTA visible.

## Verification Log
- Passed: `./gradlew test`
- Passed: `npx playwright test tests/e2e/design-quality.spec.ts --project=chromium`
- Passed: `npx playwright test tests/e2e/calculator.spec.ts tests/e2e/persona-journeys.spec.ts tests/e2e/smoke-critical.spec.ts tests/e2e/seo-aeo.spec.ts --project=chromium`

## Current Risk Notes
- `tailwind.generated.css` still contains legacy font definitions, but runtime display is overridden by `app.css` and verified by E2E.
- No engine formula or SEO scaling policy changes were introduced.

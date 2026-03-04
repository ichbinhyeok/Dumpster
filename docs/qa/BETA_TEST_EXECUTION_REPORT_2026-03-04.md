# Beta Test Execution Report

- Anchor date: **2026-03-04**
- Scope: B2C decision engine refactor validation
- Environment: local Playwright (`chromium`) + Spring Boot local server (`127.0.0.1:4173`)

## 1) Test Scope Executed

### Backend / domain

- `./gradlew test` (Spring Boot + domain/integration tests)
- Includes:
  - estimation regression scenarios
  - cost/cta business rules
  - ZIP tier routing repository/service checks
  - CSV integrity validation
  - source metadata freshness window validation

### Source-data freshness gate

- `npm run data:source-freshness:check`
- Result:
  - **86/86 rows within freshness window (<=180 days)**
  - **0 stale / invalid rows**

### Playwright beta pack

- `npm run e2e:beta`
- 64 tests executed
- Result:
  - **63 passed**
  - **1 skipped** (desktop-only tap-target skip branch)
  - **0 failed**

### Playwright full pack (visual included)

- `npm run e2e:full`
- 88 tests executed
- Result:
  - **87 passed**
  - **1 skipped**
  - **0 failed**

## 2) Coverage Areas Verified

- API business rules and negative validation
- Regional tier routing (`coastal` / `mountain` / `heartland` with ZIP override)
- Persona journeys (homeowner / contractor)
- Intent-cluster and sitemap contracts
- SEO / AEO / schema / canonical
- Intent-page homeowner decision blocks + next decision-stage links
- Material/project/heavy hub comparison-route discoverability
- Decision-stage link-click analytics (`decision_stage_link_click`)
- Comparison-hub entry analytics (`comparison_hub_entry_click`)
- Decision priority propagation (comparison -> calculator)
- Content gate telemetry (`content_gate_pass/fail`) for lead-step validation
- Accessibility (axe + keyboard focus visibility)
- Responsive quality matrix:
  - desktop 1080p
  - iPad Pro
  - iPhone 13
  - **iPhone SE**
- iPhone SE scenario coverage:
  - lead flow completion
  - urgent contractor call path
  - heavy feasibility fallback
  - decision strip multi-path checks
  - comparison hub touch interaction
- Visual baselines:
  - desktop, tablet, iPhone 13, iPhone SE
  - baseline snapshots regenerated after intentional UI changes

## 3) New Beta Validation Assets Added

- [beta-scenario-matrix.spec.ts](C:/Development/Owner/dumpster-calculator/tests/e2e/beta-scenario-matrix.spec.ts)
  - API scenario matrix with diverse material/project/persona/risk branches
  - includes `heavy + urgent + uncertain mix` risk scenario
  - ZIP tier routing differential checks (urban/value/fallback)
  - UI decision scorecard + comparison priority event checks
  - priority query-mode score effect validation
  - lead content-gate pass/fail event validation

- [iphone-se-beta-expansion.spec.ts](C:/Development/Owner/dumpster-calculator/tests/e2e/iphone-se-beta-expansion.spec.ts)
  - iPhone SE decision-strip and comparison-hub touch workflow expansion

## 4) Visual Baseline Notes

- Updated test logic to align with current mobile UX:
  - mobile baseline now validates **result dock** visibility
  - floating CTA expected hidden on small viewports
- Updated snapshot baselines:
  - `tests/e2e/visual-regression.spec.ts-snapshots/*`
  - `tests/e2e/visual-regression-extended.spec.ts-snapshots/*`
  - intent pages resized after homeowner decision block expansion

## 5) Risk/Gap Status

- No blocking regression found in current beta/full suites.
- Remaining risk is data freshness of external source assumptions (pricing/source packs), not runtime correctness.

## 6) Organic Expansion Revalidation (2026-03-04 Addendum)

- Change scope:
  - curated money-intent allowlist expanded (`10` -> `19`)
  - decision-stage internal links strengthened on intent/material/project/special pages
  - comparison-hub scenario/FAQ content expanded for consumer query intent
  - intent title/H1 phrasing humanized (query-style, deterministic)
- Verification runs:
  - `./gradlew test --tests com.dumpster.calculator.SeoContentServiceTests --tests com.dumpster.calculator.SeoInfrastructureTests --tests com.dumpster.calculator.SeoPageRenderingTests`
  - `npm run e2e -- tests/e2e/intent-cluster-mass.spec.ts tests/e2e/high-intent-pages.spec.ts tests/e2e/seo-aeo.spec.ts`
  - `npm run e2e:visual:update`
  - `npm run e2e:full`
- Result:
  - JVM targeted suite: **pass**
  - Playwright targeted suite: **12/12 pass**
  - Visual baseline update suite: **10/10 pass**
  - Playwright full suite: **87 passed / 1 skipped / 0 failed**

# Dumpster B2C Decision Engine Refactor - Execution Anchor v1

## 0) Document Control
- Anchor date: 2026-03-03
- Anchor owner: Product + Engineering
- Repository: `dumpster-calculator`
- Scope type: Full-stack refactor planning anchor (not a code freeze)
- Supersedes as master execution reference:
  - `NEXT_STEPS_QA_LAUNCH.md` (launch checklist)
  - `docs/presentation-rebuild-runbook.md` (presentation-only track)
  - `docs/seo/EXECUTION_ANCHOR_ORGANIC_PSEO_v1.md` remains valid as SEO sub-track

## 1) Why This Anchor Exists
The current product has a strong engine and test backbone, but user-facing positioning is not aligned with B2C homeowner intent. The gap is no longer "missing calculator logic"; it is a system-level mismatch across:
- product entry language,
- decision framing,
- data coverage (especially junk-removal evidence),
- content generation tone,
- analytics model for decision-stage behavior.

This document locks a refactor-grade execution plan from current state to a consumer-first decision engine, without losing existing engine correctness and SEO control policy.

## 2) Current-State Audit Snapshot (As Of 2026-03-03)

### 2.1 Product and UX Surface
- Calculator hero and rail still use operator/system phrasing (`Decision board`, `Decision output`, `verdict`, `Open simulator`, `Optional routing signal`).
- Primary CTA text remains generic (`Contact for quote`), while routing logic is already scenario-aware.
- Result panel still leads with size output sentence before clear "best next move" decision narrative.

### 2.2 Domain and API
- Core estimate pipeline exists and is coherent:
  - normalization -> volume/weight range -> candidate scoring -> recommendation -> cost comparison -> CTA routing.
- Command model already supports multiple items (`items` list), but frontend currently submits single-item flows.
- CTA routing rules already encode branch intent (junk vs dumpster vs urgency call).

### 2.3 Data Baseline (CSV)
- Material factors: 20 records.
- Unit conversions: 10 records.
- Dumpster size policies: 5 records.
- Pricing assumptions: 6 records (`size_yd=0` used for junk-removal proxy).
- Current junk comparison is modeled as pricing shortcut, not a fully explicit "service profile" dataset.

### 2.4 Frontend Technical Baseline
- `calculator.js` is monolithic (~44KB) and mixes:
  - state handling,
  - API orchestration,
  - rendering,
  - CTA routing fallback,
  - lead flow,
  - tracking dispatch.
- This structure slows down strategic changes and makes decision-flow experimentation expensive.

### 2.5 SEO and Content Baseline
- Split sitemap and selective indexing policy are implemented and contract-tested.
- Intent title/H1 generation is deterministic but still template-shaped and can expose system composition artifacts.
- Comparison and special pages exist, but decision-completion depth is uneven.

### 2.6 Tracking Baseline
- Tracking guardrails and event persistence exist.
- Allowed event list is explicit and stable.
- Decision-stage events required for V2 are not yet modeled in API allowlist.

### 2.7 Test Baseline
- Backend tests: 16 JUnit test classes.
- E2E specs: 15 Playwright specs.
- Current suite validates business rules, SEO contracts, schema presence, CTA routing, and core journeys.
- Several E2E assertions are copy-string coupled and will need controlled updates during reframing.

## 3) Refactor Mission Lock
Turn the product from "expert-feeling dumpster calculator" into a consumer-first disposal decision engine:
- First-class question: "Dumpsters vs junk removal vs multi-haul: what is my safest next move?"
- Preserve engine truth:
  - range-based outputs,
  - allowance vs haul semantics,
  - heavy-debris feasibility logic,
  - controlled indexing policy,
  - trackable conversion paths.

## 4) Scope Definition

### 4.1 In Scope
1. Domain output contract redesign around decision summary.
2. Data model expansion:
   - dumpster source depth,
   - junk-removal source depth,
   - confidence and provenance fields.
3. Frontend architecture refactor from monolith JS to modular decision UI state model.
4. SEO/content generation rewrite for human-language titles/H1/lead blocks.
5. Internal linking redesign by decision stage, not only taxonomy.
6. Analytics taxonomy redesign and event pipeline update.
7. Documentation suite refresh (PRD, architecture, data dictionary, runbooks, testing contracts).

### 4.2 Out of Scope (For This Refactor Cycle)
1. City/ZIP mass pSEO expansion.
2. Full intent route merge and large redirect migration.
3. Contractor-first UX rewrite.
4. Replacing Spring Boot/JTE/H2 stack.

## 5) Target Architecture V2 (Planned)

### 5.1 Domain Model V2
- New top-level decision output object:
  - `bestNextMove` (`dumpster`, `junk_removal`, `multi_haul`)
  - `decisionReasonSummary[]`
  - `whenThisCanFlip[]`
  - `confidenceLevel`
  - `assumptionFlags[]`
- Recommendation cards remain, but become supporting evidence, not primary narrative.

### 5.2 Data Model V2
- Introduce explicit dataset families:
  - `dumpster_policy_facts`
  - `dumpster_pricing_ranges`
  - `junk_service_facts`
  - `comparison_scenarios`
  - `unit_mapping_profiles`
- Add source metadata fields:
  - `source_type`, `source_url`, `source_date`, `market_scope`, `confidence`, `notes`.

### 5.3 API Contract V2
- Keep `/api/estimates` route.
- Add backward-compatible response fields first.
- Deprecate legacy-only wording in response payload after frontend cutover.

### 5.4 Frontend V2
- Split calculator client into modules:
  - `state/` (input + decision state),
  - `api/` (transport and payload),
  - `view/` (panel renderers),
  - `tracking/` (event mapping),
  - `cta/` (UI labels and route semantics).
- Add explicit decision mode controls (`cheapest`, `easiest`, `heavy`, `urgent`) as preset layer, not hard branch gate.

### 5.5 SEO/Content V2
- Deterministic phrase map for humanized titles/H1.
- Answer pages keep skeleton blocks but add homeowner action blocks.
- Internal links reweight to "next question" adjacency graph.

### 5.6 Analytics V2
- Add decision-stage events:
  - `decision_mode_selected`
  - `comparison_page_view`
  - `comparison_page_exit_to_calculator`
  - `vendor_questions_expand`
  - `pickup_converter_used`
  - `answer_page_group`
  - `content_gate_pass`
  - `content_gate_fail`

## 6) Program Principles (Non-Negotiables)
1. No big-bang rewrite.
2. Introduce V2 contracts behind compatibility adapter boundaries.
3. Preserve existing business-rule correctness during migration.
4. Keep selective indexing and split sitemap safety rails active.
5. Treat copy changes as product behavior changes (tracked, tested, reviewed).
6. No anonymous data assumptions: every new numeric block requires source registration.

## 7) Phase Plan With Date Anchors

## Phase 0 - Audit Lock and Plan Freeze
- Window: 2026-03-03 to 2026-03-05
- Goal: lock current state, risks, and refactor plan.
- Exit criteria:
  - this anchor approved,
  - dependency map accepted,
  - migration gates agreed.

## Phase 1 - Architecture and Contract Design
- Window: 2026-03-06 to 2026-03-12
- Goal: finalize V2 domain/API/data contracts and compatibility strategy.
- Exit criteria:
  - ADR set approved,
  - payload schema draft complete,
  - rollout flags defined.

## Phase 2 - Data Expansion Foundation
- Window: 2026-03-10 to 2026-03-24
- Goal: build expanded source-backed dataset (dumpster + junk + scenarios).
- Exit criteria:
  - source registry v2 complete,
  - data quality checks automated,
  - confidence policy enforced.

## Phase 3 - Domain and API Refactor
- Window: 2026-03-17 to 2026-04-02
- Goal: implement decision-first output model and backward-compatible API.
- Exit criteria:
  - regression pass on business rules,
  - new output fields populated,
  - fallback compatibility verified.

## Phase 4 - Frontend Refactor and UX Reframing
- Window: 2026-03-24 to 2026-04-16
- Goal: modular frontend and decision-summary-first UI behavior.
- Exit criteria:
  - monolith JS split,
  - CTA and result language converted,
  - mobile and desktop parity verified.

## Phase 5 - SEO/Content and Link Graph Refactor
- Window: 2026-03-31 to 2026-04-23
- Goal: humanized query language generation and decision-stage linking.
- Exit criteria:
  - title/H1 rule rewrite deployed,
  - answer block upgrades complete,
  - index policy unchanged and validated.

## Phase 6 - Measurement, QA, and Controlled Cutover
- Window: 2026-04-17 to 2026-05-07
- Goal: validate outcomes and cut traffic to V2 behavior safely.
- Exit criteria:
  - event completeness threshold met,
  - quality gates passed,
  - rollback playbook ready.

## 8) Workstream WBS (Detailed)

### WS-ARCH Domain and System Architecture
| ID | Task | Output | Gate |
|---|---|---|---|
| ARCH-001 | Freeze legacy domain map and coupling points | dependency map | reviewed |
| ARCH-002 | Define `DecisionSummaryV2` schema | schema doc | approved |
| ARCH-003 | Define compatibility adapter from V1 result | adapter contract | tested |
| ARCH-004 | Introduce domain package boundaries for V2 | package blueprint | lintable |
| ARCH-005 | Define feature flags for staged rollout | flag matrix | approved |
| ARCH-006 | Add ADR for "no big-bang, adapter migration" | ADR-001 | merged |
| ARCH-007 | Add ADR for "decision-first output precedence" | ADR-002 | merged |
| ARCH-008 | Add ADR for "dataset provenance minimum fields" | ADR-003 | merged |
| ARCH-009 | Add architecture fitness checks | dependency tests | passing |
| ARCH-010 | Final architecture sign-off | architecture review note | signed |

### WS-DATA Source, Dataset, and Quality
| ID | Task | Output | Gate |
|---|---|---|---|
| DATA-001 | Define v2 data dictionary | `docs/data/DATA_DICTIONARY_v2.md` | approved |
| DATA-002 | Create junk service source registry template | csv schema | checked |
| DATA-003 | Expand dumpster policy records by scenario dimensions | v2 dataset | validated |
| DATA-004 | Add comparison scenario facts (labor/speed/risk) | v2 dataset | validated |
| DATA-005 | Add pickup mapping confidence notes and ranges | v2 dataset | validated |
| DATA-006 | Introduce source confidence rubric | quality rubric | approved |
| DATA-007 | Add CSV integrity tests for new fields | tests | passing |
| DATA-008 | Add stale source detection checks | validation rule | passing |
| DATA-009 | Build "unsupported claim" guard list | policy file | active |
| DATA-010 | Map old CSV columns to v2 fields | migration map | reviewed |
| DATA-011 | Add provenance display mapping for UI blocks | view mapping | tested |
| DATA-012 | Data sign-off for launch candidate | release checklist | signed |

### WS-API Estimate and Tracking Contracts
| ID | Task | Output | Gate |
|---|---|---|---|
| API-001 | Add V2 fields to estimate response (non-breaking) | API contract update | tests pass |
| API-002 | Version payload metadata (`contractVersion`) | response field | tests pass |
| API-003 | Add event names to allowlist with guard tests | tracking API update | tests pass |
| API-004 | Add schema validation tests for new payload fields | tests | passing |
| API-005 | Add deprecation markers for V1-only wording fields | migration note | reviewed |
| API-006 | Add preview endpoint parity tests | tests | passing |
| API-007 | API cutover gate checklist | ops runbook | approved |

### WS-FE Frontend Refactor and Decision UX
| ID | Task | Output | Gate |
|---|---|---|---|
| FE-001 | Split `calculator.js` into module files | modular JS structure | build pass |
| FE-002 | Introduce decision preset strip (`cheapest/easiest/heavy/urgent`) | new input module | UX review |
| FE-003 | Rebuild result header to `Best next move` narrative | result renderer | reviewed |
| FE-004 | Move recommendation cards under decision summary | UI hierarchy update | approved |
| FE-005 | Rewrite CTA labels to explicit actions | CTA mapping | tested |
| FE-006 | Upgrade lead copy from generic quote to route-confirmed wording | lead module update | reviewed |
| FE-007 | Add "when junk wins" block in result panel | result block | tested |
| FE-008 | Add "when multi-haul is safer" block | result block | tested |
| FE-009 | Replace operator jargon labels in shell and accordions | copy update | QA pass |
| FE-010 | Add event instrumentation hooks per decision stage | tracking hooks | validated |
| FE-011 | Ensure mobile dock mirrors primary decision action | mobile parity | visual pass |
| FE-012 | Remove dead rendering paths and duplicate CTA logic | cleanup patch | tests pass |

### WS-SEO Content, Generation, and Internal Links
| ID | Task | Output | Gate |
|---|---|---|---|
| SEO-001 | Rewrite intent question/title generator to human query language | generator v2 | tests pass |
| SEO-002 | Rewrite H1 patterns by intent type with deterministic phrase map | mapping table | approved |
| SEO-003 | Add homeowner action blocks to answer template | template update | reviewed |
| SEO-004 | Expand comparison hub depth with scenario matrix and worked examples | content update | reviewed |
| SEO-005 | Define decision adjacency internal link map | link map doc | approved |
| SEO-006 | Implement adjacency links in material/project/answer/special templates | template updates | tests pass |
| SEO-007 | Maintain allowlist policy and wave gates | policy lock | tests pass |
| SEO-008 | Add orphan detection check for decision pages | check script/test | passing |
| SEO-009 | Add duplicate title/H1 safety assertions | tests | passing |
| SEO-010 | Update structured data where wording changed | schema updates | validated |
| SEO-011 | Refresh SEO execution pack references | docs update | merged |
| SEO-012 | SEO launch review | checklist | signed |

### WS-ANL Measurement and Experimentation
| ID | Task | Output | Gate |
|---|---|---|---|
| ANL-001 | Define event taxonomy v2 | `docs/analytics/EVENT_TAXONOMY_v2.md` | approved |
| ANL-002 | Add decision-stage events in frontend | event dispatch | verified |
| ANL-003 | Expand backend allowlist for new events | API config/test | pass |
| ANL-004 | Add event contract tests (required payload keys) | tests | pass |
| ANL-005 | Define funnel dashboard spec (`mode -> result -> CTA -> lead`) | dashboard spec | approved |
| ANL-006 | Define attribution logic for comparison hub assist | analytics spec | approved |
| ANL-007 | Add quality monitor for event drop rates | monitor doc | active |
| ANL-008 | Analytics sign-off | release note | signed |

### WS-QA Test and Release Validation
| ID | Task | Output | Gate |
|---|---|---|---|
| QA-001 | Update E2E copy assertions to new labels | tests | pass |
| QA-002 | Add decision-summary flow tests | tests | pass |
| QA-003 | Add junk-first path regression tests | tests | pass |
| QA-004 | Add multi-haul recommendation assertions | tests | pass |
| QA-005 | Add title/H1 humanization assertions for intent pages | tests | pass |
| QA-006 | Add accessibility checks for new preset strip and result blocks | tests | pass |
| QA-007 | Add visual baselines for updated core routes | snapshots | pass |
| QA-008 | Add data drift tests for v2 datasets | tests | pass |
| QA-009 | Run full backend + e2e regression gate | CI result | green |
| QA-010 | Cutover rehearsal and rollback drill | runbook execution | complete |

### WS-DOC Documentation and Knowledge Sync
| ID | Task | Output | Gate |
|---|---|---|---|
| DOC-001 | Create master execution anchor (this doc) | anchor v1 | done |
| DOC-002 | Update PRD to B2C decision-engine framing | PRD v1.3+ | approved |
| DOC-003 | Publish architecture ADR index | ADR index | merged |
| DOC-004 | Publish data dictionary v2 | data doc | merged |
| DOC-005 | Publish tracking taxonomy v2 | analytics doc | merged |
| DOC-006 | Update SEO execution anchor links to master plan | docs sync | merged |
| DOC-007 | Update launch checklist to reference phase gates | checklist v2 | merged |
| DOC-008 | Publish content style guide (homeowner tone) | style guide | approved |
| DOC-009 | Add migration changelog template | changelog doc | merged |
| DOC-010 | Weekly status log updates in this anchor | execution log | active |

## 9) Documentation Refresh Matrix
| Document | Action | Deadline | Owner |
|---|---|---|---|
| `NEXT_STEPS_QA_LAUNCH.md` | convert to refactor-era gate checklist | 2026-03-04 | Eng |
| `docs/presentation-rebuild-runbook.md` | mark as historical track and link to master anchor | 2026-03-04 | Eng |
| `DUMPSTER_CALCULATOR_PRD_v1.2_LOCK.md` | release PRD v1.3 refactor addendum | 2026-03-08 | Product |
| `docs/seo/EXECUTION_ANCHOR_ORGANIC_PSEO_v1.md` | add parent-anchor reference and V2 scope note | 2026-03-05 | SEO |
| `docs/seo/PHASE1_EXECUTION_PACK_v1.md` | add V2 migration appendix | 2026-03-10 | SEO |
| `docs/smoke-test-checklist.md` | expand with decision-route and event checks | 2026-03-12 | QA |
| new `docs/analytics/EVENT_TAXONOMY_v2.md` | create | 2026-03-12 | Data/Eng |
| new `docs/data/DATA_DICTIONARY_v2.md` | create | 2026-03-12 | Data |
| new `docs/content/B2C_TONE_STYLE_GUIDE_v1.md` | create | 2026-03-10 | Content |
| new `docs/architecture/ADR/` | initialize and backfill ADR-001~003 | 2026-03-12 | Eng |

## 10) Milestone Gates and Quality Criteria

### Gate G1 (Architecture Freeze)
- Required:
  - ADR-001/002/003 merged.
  - V2 response schema drafted.
  - adapter strategy approved.

### Gate G2 (Data Integrity)
- Required:
  - v2 source registry complete for all new decision claims.
  - no missing provenance fields in active rows.
  - CSV integrity tests green.

### Gate G3 (UX and Contract Readiness)
- Required:
  - new result narrative rendered.
  - old CTA wording fully replaced where intended.
  - event payload and backend allowlist aligned.

### Gate G4 (SEO and Content Safety)
- Required:
  - title/H1 generator humanized and deterministic.
  - no duplicate titles in tracked money pages.
  - selective indexing policy unchanged.

### Gate G5 (Release Candidate)
- Required:
  - backend tests green.
  - e2e suite green.
  - rollback rehearsal completed.

## 11) Risk Register
| Risk | Impact | Mitigation |
|---|---|---|
| Big-bang rewrite temptation | regression and schedule slip | enforce adapter migration and gates |
| Copy changes break E2E contracts | CI noise and blocked delivery | update tests phase-by-phase with copy map |
| Data expansion without provenance | trust and legal risk | source registry mandatory field policy |
| SEO over-expansion during refactor | index quality loss | keep wave + allowlist controls intact |
| Frontend module split introduces state bugs | conversion drop | add state model tests and visual regression |
| Event taxonomy drift between FE and API | analytics blind spots | event contract tests + allowlist sync |

## 12) Success Metrics

### Product Metrics
- Decision completion rate.
- Result-to-primary-CTA click rate.
- Comparison hub assist rate.
- Lead submit rate after decision summary view.

### Route Metrics
- `dumpster` vs `junk_removal` route distribution by scenario.
- High-risk scenarios routed to safer branch rate.
- Urgent (`48h`) call-route performance.

### Quality Metrics
- Regression pass rate.
- Event delivery success rate.
- Duplicate title/H1 incidents.
- Orphan decision page count.

## 13) First 72-Hour Kickoff Checklist (2026-03-03 Start)
- [ ] Final sign-off of this anchor.
- [ ] Create ADR folder and ADR-001/002/003 drafts.
- [ ] Create data dictionary v2 draft skeleton.
- [ ] Create analytics taxonomy v2 draft skeleton.
- [ ] Create copy replacement map (legacy phrase -> B2C phrase).
- [ ] Break `calculator.js` split design into module tickets.
- [ ] Prepare E2E copy assertion migration list.
- [ ] Sync SEO sub-track owners on title/H1 generator rewrite.

## 14) Execution Log
- 2026-03-03: Created master refactor execution anchor v1.
- 2026-03-03: Locked scope as refactor-grade (domain + data + frontend + SEO + analytics + docs), not copy-only patching.
- 2026-03-03: Confirmed migration strategy as compatibility-first (no big-bang rewrite).


# Next Steps (Refactor-Era, 2026-03-03)

This checklist is now subordinate to the master refactor anchor:
- `docs/EXECUTION_ANCHOR_B2C_DECISION_ENGINE_REFACTOR_v1_2026-03-03.md`

Use this file as an operational quick list only.

## A) Immediate Program Actions
1. Approve the master anchor and phase windows.
2. Open workstreams `ARCH/DATA/API/FE/SEO/ANL/QA/DOC` as tracked tickets.
3. Create ADR and data/analytics documentation skeletons.

## B) Engineering Safety Baseline
1. Keep current regression baseline green before each phase transition.
2. Preserve selective indexing and split sitemap policies.
3. Keep compatibility-first migration (no big-bang rewrite).

## C) Refactor Gate Checklist
1. Gate G1: architecture freeze approved.
2. Gate G2: data integrity and provenance checks approved.
3. Gate G3: decision-summary UX + API contract parity approved.
4. Gate G4: SEO title/H1 and link graph safety approved.
5. Gate G5: release candidate + rollback rehearsal approved.

## D) Ongoing Verification
1. `./gradlew.bat test`
2. `npx playwright test`
3. Manual smoke for:
   - `/dumpster/size-weight-calculator`
   - `/dumpster/dumpster-vs-junk-removal-which-is-cheaper`
   - `/dumpster/heavy-debris-rules`
   - `/dumpster/answers/...` (allowlist and non-allowlist samples)
   - `/robots.txt`, `/sitemap.xml`, `/sitemap-core.xml`, `/sitemap-money.xml`, `/sitemap-experiments.xml`

## E) Metrics Watchlist
1. Existing: `calc_completed_client`, `feasibility_not_ok`, `cta_click_dumpster_call`, `cta_click_junk_call`, `lead_submitted`.
2. Planned V2 additions: `decision_mode_selected`, `comparison_page_view`, `comparison_page_exit_to_calculator`, `vendor_questions_expand`, `pickup_converter_used`, `answer_page_group`, `content_gate_pass`, `content_gate_fail`.

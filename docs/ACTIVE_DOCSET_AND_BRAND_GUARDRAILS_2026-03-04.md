# Active Docset and Brand Guardrails (2026-03-04)

## 1) Active decision-engine docs

Treat the following as source-of-truth for current product direction:

- `docs/EXECUTION_ANCHOR_B2C_DECISION_ENGINE_REFACTOR_v2_2026-03-04.md`
- `docs/beta-test-interview-report-v2-general-users.md`
- `docs/beta-test-interview-report-v2-domain-experts.md`
- `NEXT_STEPS_QA_LAUNCH.md`
- `docs/data/SOURCEBOOK_JUNK_AND_DUMPSTER_2026-03-04.md`

## 2) Messaging guardrails (consumer-first)

Preferred framing:

- "Debris disposal decision engine"
- "Dumpster vs junk removal"
- "Best next move"
- "Homeowner-first guidance"
- "Enter your ZIP for local pricing"

Avoid in current user-facing copy:

- "Decision OS"
- "Optional routing signal"
- "Verdict" (for result headline)
- internal system nouns like "pricing profile tier" without user context

## 3) Historical docs policy

Files such as `v1` audits/interview reports remain as historical evidence and may include legacy wording in quoted sections.
Do not reuse their wording for live UX copy, metadata generation, or go-forward strategy docs.

## 4) Operational update rule

When new anchor/version docs are created:

1. add the file to Section 1 if it becomes active,
2. keep this guardrail file updated in the same commit,
3. run `rg -n "Decision OS|Optional routing signal|Verdict" src/main/jte src/main/resources/static/js` and confirm no regressions in user-facing templates/scripts.

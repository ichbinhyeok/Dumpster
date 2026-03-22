# Active Docset and Brand Guardrails (2026-03-05)

## Active Source of Truth
Use only the documents below for current strategy and execution context.

- `docs/ACTIVE_DOCSET_AND_BRAND_GUARDRAILS_2026-03-05.md` (this file)
- `docs/EXECUTION_ANCHOR_B2C_DECISION_ENGINE_REFACTOR_v2_2026-03-04.md`
- `docs/BETA_TEST_INTERVIEW_REPORT_2026-03-05.md`
- `docs/seo/ORGANIC_EXECUTION_TICKETS_v2_2026-03-05.md`
- `docs/seo/SEO_TRACKING_CHECKPOINT_2026-03-22.md`
- `docs/seo/data/url_classification_v2_curated.csv`
- `docs/seo/data/priority_pages_top20_v2.csv`
- `docs/seo/data/title_h1_meta_intro_rewrite_v2.csv`
- `docs/seo/data/evidence_gap_v2.csv`
- `NEXT_STEPS_QA_LAUNCH.md`

## Messaging Guardrails
Preferred framing:

- "Dumpster vs junk removal"
- "Best next move"
- "Quick estimate first"
- "Confidence and variance shown"
- "Provider rules vary by market"

Avoid in user-facing copy:

- "Decision OS"
- "Optional routing signal"
- "Verdict" (for result headline)
- internal-only technical nouns without context

## Context Hygiene Policy
Historical doc versions were intentionally removed to prevent prompt/context contamination.

- Do not restore old `v1`, `part`, or legacy interview docs.
- Do not cite deleted docs in new planning or implementation docs.
- When a new active version is published, replace prior active doc and remove superseded files in the same commit.

## Operational Update Rule
When creating a new active doc:

1. Add it to `Active Source of Truth`.
2. Remove the superseded version in the same commit.
3. Update `README.md` active-doc links if paths changed.

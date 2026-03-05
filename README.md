# Debris Decision

Debris Decision is a homeowner-first debris disposal decision engine.
It helps users choose between dumpster rental, junk removal, or staged heavy-load strategy using range-based volume/weight logic.

## Product URLs

- `/dumpster/size-weight-calculator`
- `/dumpster/dumpster-vs-junk-removal-which-is-cheaper`
- `/dumpster/heavy-debris-rules`
- `/dumpster/material-guides`
- `/dumpster/project-guides`

## Stack

- Java 21
- Spring Boot 4
- JTE templates
- H2
- Playwright E2E suite

## Local run

```bash
./gradlew bootRun
```

## Quality checks

```bash
./gradlew test
npm run e2e:beta
```

## Active Docs (Current Only)

- `docs/ACTIVE_DOCSET_AND_BRAND_GUARDRAILS_2026-03-05.md`
- `docs/BETA_TEST_INTERVIEW_REPORT_2026-03-05.md`
- `docs/seo/ORGANIC_EXECUTION_TICKETS_v2_2026-03-05.md`

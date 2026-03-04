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


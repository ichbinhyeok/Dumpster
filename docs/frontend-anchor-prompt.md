# Dumpster Calculator Frontend Anchor Prompt (JTE + Spring Boot)

## Mission
Rebuild the frontend into a modern B2C SaaS simulator without changing backend domain logic. Keep Spring Boot + JTE architecture, preserve SEO schema/meta tags, and optimize for trust, conversion, and clarity.

## Non-Negotiables
- Do not modify Java domain/service/controller logic unless explicitly requested.
- Keep and preserve all existing JSON-LD, canonical tags, and critical SEO metadata.
- Keep stack compatible with Spring Boot + JTE rendering.
- Favor progressive enhancement and fast first render.

## Product North Star
- Replace static, textbook-style forms with an interactive decision cockpit.
- Inputs should feel visual and direct (chips/cards, steppers, toggles), not enterprise form-heavy.
- Results should update live with debounce and race-safe API calls.
- Every page should communicate trust: source transparency, assumptions, and practical operator guidance.

## UX Architecture
1. Global shell
- Sticky header with brand, trust badge, and strong CTA.
- Micro footer with source reference, terms, privacy, and lightweight legal copy.

2. Calculator experience
- Split-screen layout:
  - Left: input controls and advanced options.
  - Right: sticky live dashboard with risk gauges and recommendation updates.
- Keep a manual Calculate button as fallback, but default to live updates.
- Remove non-essential update chatter from primary calculator flow.

3. pSEO pages
- Add premium hero insight cards.
- Add micro intent widget that forwards presets into calculator.
- Render “mistake to avoid” as warning callout and FAQ in scannable cards.

## E-E-A-T Implementation Rules
- Experience: show practical operator checklist and scenario-based caveats.
- Expertise: expose assumptions and decision rationale in plain language.
- Authoritativeness: include data source lineage (EPA baseline + local checks).
- Trust: clearly separate estimate guidance from final vendor quote obligations.

## Persona Strategy
Support at least:
- Homeowner (default)
- Contractor
- Property Manager
- Flipper / Investor
- Facility / Business Ops

Personas can share backend `persona` values if needed, but UI copy and intent routing should reflect user context.

## Visual System Guardrails
- Use a clear token system (color, spacing, radius, shadows).
- Avoid generic 2010 form UI patterns.
- Prefer high readability and hierarchy over decorative noise.
- Keep animations subtle and meaningful (gauge fill, state transitions, panel reveal).

## Engineering Quality Checklist
- Accessibility: labels, keyboard support, visible focus, aria-live for status.
- Performance: debounce user input, cancel stale requests, avoid layout thrash.
- Reliability: graceful error states, preserve manual submit fallback.
- Maintainability: role separation in markup/CSS/JS and minimal coupling.

## Validation Protocol
1. Functional smoke
- Calculator renders with header/footer.
- Live updates trigger result changes without submit.
- Manual Calculate still works.
- Share link generates and opens.

2. SEO safety
- JSON-LD remains present on calculator and guide pages.
- Canonical and noindex behavior unchanged where applicable.

3. UX checks
- Mobile sticky CTA/dashboard usability.
- Mistake callouts and checklist readability.
- Trust/footer links visible and non-blocking.

## Output Expectation
Deliver:
- Updated JTE templates
- Updated CSS and calculator JS
- Updated and new test cases (e2e + relevant server render checks)
- Smoke test run summary and any known residual risks

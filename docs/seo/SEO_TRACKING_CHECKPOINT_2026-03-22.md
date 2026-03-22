# SEO Tracking Checkpoint (2026-03-22)

## Purpose
This document captures the exact SEO state observed on 2026-03-22, the reasoning behind today's changes, what was deployed, and what the next session should verify without re-discovering context.

## Snapshot

- Date of review: 2026-03-22
- Production domain: `https://debrisdecision.com`
- Deployed build after today's work: `9d22982`
- Prior production build before today's deploy: `aab0f16`
- Search Console property used: `sc-domain:debrisdecision.com`

## Search Console Readout

Primary observation window:

- `2026-03-01` through `2026-03-21`
- Result: `0 clicks / 1,391 impressions / 0% CTR / average position 5.50`

Interpretation notes:

- The `5.50` average position is directionally misleading. It is inflated by operator-style queries, brand-style queries, and anonymized long-tail testing impressions.
- The site is not invisible. Google is crawling, indexing, and testing pages.
- The site also does not yet have organic traction. Click count is still `0`.
- This is an early-stage test/discovery state, not a healthy growth state.

Observed pattern in the first 3 weeks:

- Large early discovery spikes appeared around `2026-03-03` and `2026-03-04`.
- After that, impressions dropped into low steady-state testing levels.
- Some later recovery appeared around `2026-03-17` to `2026-03-20`.
- The domain is still in a proving phase, not a compounding phase.

## What The Data Actually Said

### Positive signals

- Google showed early ranking/test behavior on job- and debris-specific pages, not on the broad calculator head term.
- The strongest early wedge was around:
  - `concrete removal`
  - `shingles`
  - `garage cleanout`
  - `estate cleanout`
  - `dirt / grading`
- Example query/page pattern seen earlier:
  - `can you put concrete in a dumpster`
  - `/dumpster/can-you-put-concrete-in-a-dumpster`

### Negative signals

- Head-term calculator intent remained weak.
- Broad `dumpster size calculator` / `dumpster weight calculator` style intent was still far back in rankings.
- Mobile signal was weaker than desktop in the observed sample.
- Some structured data had been flagged in Search Console with:
  - `Unparsable structured data`
  - `Bad escape sequence in string`

## Strategic Decision Taken Today

The conclusion was:

- The topic is not fundamentally wrong.
- The opening position was wrong.

We are not treating the site as a generic "dumpster calculator" SEO bet anymore.
We are treating it as a `job-specific + debris-specific recommendation engine` that happens to contain a calculator.

Practical framing change:

- Old framing: generic dumpster calculator
- New framing: pick the right dumpster route by job scope, debris type, weight risk, and haul constraints

This means:

- Win the first clicks on narrow, decision-stage pages.
- Use the calculator as the conversion and routing layer.
- Stop assuming the homepage calculator term will be the first thing that works.

## Actions Completed On 2026-03-22

### 1. Fixed structured-data escaping

Reason:

- Search Console had already shown parse failures on some concrete-related pages.

Change:

- JSON-LD escaping was hardened in:
  - `src/main/java/com/dumpster/calculator/web/support/JsonLd.java`
- Escaping now uses Jackson `JsonStringEncoder`.
- Unicode line separator edge cases `U+2028` and `U+2029` are forced to `\u2028` and `\u2029`.

Validation added:

- `src/test/java/com/dumpster/calculator/JsonLdTests.java`
- JSON-LD parse checks added for:
  - `/dumpster/can-you-put-concrete-in-a-dumpster`
  - `/dumpster/answers/concrete-removal/concrete/size-guide`

### 2. Repositioned the main calculator

Reason:

- The old entry framing was too broad and too similar to generic calculator SEO.

Change:

- Calculator title and meta were rewritten around job + material intent.
- Hero and support copy now frame the page as a recommendation engine rather than a simple volume calculator.

Key files:

- `src/main/java/com/dumpster/calculator/web/controller/CalculatorPageController.java`
- `src/main/jte/calculator/index.jte`
- `src/main/jte/components/appHeader.jte`

### 3. Repositioned the guide hubs

Reason:

- Early GSC signal favored debris-specific and job-specific entry pages.

Change:

- Material hub reframed as a debris-type guide surface.
- Project hub reframed as a job-preset surface.
- Supporting copy now emphasizes high-intent answer pages rather than generic browsing.

Key files:

- `src/main/java/com/dumpster/calculator/web/controller/SeoPageController.java`
- `src/main/jte/seo/material-guides.jte`
- `src/main/jte/seo/project-guides.jte`

### 4. Changed index priority toward the observed wedge

Reason:

- The site needed to bias crawl/index attention toward the pages already showing early search demand.

Change:

- Priority project set was reweighted toward:
  - `concrete_removal`
  - `estate_cleanout`
  - `dirt_grading`
  - `garage_cleanout`
  - `roof_tearoff`
  - remodel/deck clusters that support the same decision framing
- Priority special pages now include:
  - `roof-shingles-dumpster-size-calculator`
  - `drywall-disposal-dumpster-rules`
- Curated intent seeds were expanded toward concrete, dirt, estate, garage, bathroom, deck, and kitchen scenarios.
- Experiments were tightened into a smaller observation set rather than carrying more important pages there by default.

Key file:

- `src/main/java/com/dumpster/calculator/web/content/SeoContentService.java`

## Verification Completed

### Local verification

- Command run: `./gradlew test`
- Result: passed

### Deployment

- Commit pushed to `master`
- Commit message:
  - `Refocus SEO around job and debris intent`
- Production build verified through `/api/health`
- Observed production build after rollout:
  - `{"buildRef":"9d229820fb","status":"ok","service":"dumpster-calculator"}`

### Post-deploy spot checks

Verified on production:

- `/dumpster/size-weight-calculator`
  - new job/material positioning copy present
- `/dumpster/material-guides`
  - new debris-type framing present
- `/dumpster/project-guides`
  - new job-guide framing present
- `/dumpster/can-you-put-concrete-in-a-dumpster`
  - JSON-LD script count present
- `/dumpster/answers/concrete-removal/concrete/size-guide`
  - JSON-LD script count present

Robots/index spot checks:

- `/dumpster/size/concrete-removal`
  - `index, follow`
- `/dumpster/size/light-commercial-fitout`
  - `index, follow`

## Open Follow-Up Items

### Reinspection requests

These are the first URLs to request for Search Console reinspection:

1. `https://debrisdecision.com/dumpster/size-weight-calculator`
2. `https://debrisdecision.com/dumpster/can-you-put-concrete-in-a-dumpster`
3. `https://debrisdecision.com/dumpster/answers/concrete-removal/concrete/size-guide`
4. `https://debrisdecision.com/dumpster/size/concrete-removal`
5. `https://debrisdecision.com/dumpster/size/estate-cleanout`

Status:

- Reinspection was not submitted during this session because the `google-search-console` MCP later timed out during handshake.
- The MCP did work earlier in the day, so this should be retried rather than treated as a product issue.

### Next checkpoint dates

Use absolute dates, not relative timing:

- First follow-up checkpoint: `2026-03-29`
- Second follow-up checkpoint: `2026-04-05`

## What The Next Session Should Check

### On or after 2026-03-29

- Has the site recorded the first non-brand click yet?
- Did impressions concentrate further into:
  - concrete
  - shingles
  - garage cleanout
  - estate cleanout
  - dirt grading
- Did `/dumpster/size/concrete-removal` and `/dumpster/size/estate-cleanout` start receiving meaningful impression lift?
- Did the previously broken JSON-LD pages clear Search Console errors?

### On or after 2026-04-05

- If clicks are still `0`, do not keep the current scope unchanged.
- Escalate into a stronger wedge strategy:
  - tighter internal-link concentration
  - additional concrete / dirt / cleanout cluster expansion
  - more aggressive pruning or merge handling for weak overlaps
  - external authority acquisition for the priority cluster

## Operating Rule For Future Sessions

When continuing SEO work from this point:

- Start from this document first.
- Treat `job + debris + risk` framing as the active SEO positioning.
- Do not revert to generic calculator-first messaging unless new data proves the wedge is wrong.
- Preserve the concrete/estate/dirt/garage priority bias until the next checkpoint disproves it.

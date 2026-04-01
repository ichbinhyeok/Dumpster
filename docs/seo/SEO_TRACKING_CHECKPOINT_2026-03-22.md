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

## Follow-Up Checkpoint On 2026-04-01

### Why this follow-up happened

- The first follow-up checkpoint was scheduled for `2026-03-29`.
- Review was performed on `2026-04-01`.
- The main questions were:
  - Did the site record the first non-brand click?
  - Did impressions concentrate further into the intended wedge?
  - Was the concern about choosing the wrong topic becoming more likely?

### Search Console state on 2026-04-01

Observed 28-day window:

- `2026-03-01` through `2026-03-29`
- Result: `0 clicks / 1,509 impressions / 0% CTR / average position 6.56`

Short-period comparison used during review:

- `2026-03-22` through `2026-03-31`
- versus `2026-03-12` through `2026-03-21`
- Result:
  - impressions fell from `137` to `108`
  - average position improved from `33.71` to `14.19`

Interpretation:

- No click yet, so the site still does not have organic traction.
- Ranking tests are getting cleaner in some areas, so the site is not being ignored.
- This is still a live test phase, but it is now more concentrated and less noisy than the first checkpoint.

### What clearly worked

- The structured-data concern is materially improved.
- On `2026-04-01`, these URLs all showed `Submitted and indexed` plus Rich Results `PASS`:
  - `https://debrisdecision.com/dumpster/size-weight-calculator`
  - `https://debrisdecision.com/dumpster/can-you-put-concrete-in-a-dumpster`
  - `https://debrisdecision.com/dumpster/answers/concrete-removal/concrete/size-guide`
  - `https://debrisdecision.com/dumpster/size/concrete-removal`
  - `https://debrisdecision.com/dumpster/size/estate-cleanout`

- The concrete wedge is still the clearest real signal:
  - `/dumpster/can-you-put-concrete-in-a-dumpster`: `135 impressions / position 4.73`
  - `/dumpster/size/concrete-removal`: `14 impressions / position 6.43`
  - `/dumpster/answers/dirt-grading/concrete/size-guide`: `48 impressions / position 3.42`

### What did not work yet

- `estate-cleanout` still does not show meaningful lift in the current review window.
- `garage-cleanout` still does not show meaningful lift in the current review window.
- The broad calculator page is still not winning broad head terms:
  - `/dumpster/size-weight-calculator`: `154 impressions / position 17.26`

### Decision taken on 2026-04-01

- The topic selection is **not yet judged wrong**.
- The concern is understandable because impressions are still small and clicks remain `0`.
- The safer reading is:
  - topic is still viable
  - wedge is narrower than expected
  - `concrete` is proving itself faster than `estate` or `garage`

Practical conclusion:

- Do not pivot away from the topic yet.
- Do not spread effort evenly across all clusters either.
- Concentrate internal-link and decision-surface attention on the concrete cluster first.

### Action taken on 2026-04-01

- Concrete-focused internal links were strengthened in `SeoContentService`.
- Concrete cluster pages now explicitly reinforce:
  - `/dumpster/can-you-put-concrete-in-a-dumpster`
  - `/dumpster/size/concrete-removal`
  - `/dumpster/weight/concrete`

Reason:

- This is the clearest cluster with real impression signal.
- If the site is going to earn an early click, concrete is the most defensible place to concentrate.

### What was done during the 2026-04-01 review session

- Search Console was re-checked against the `2026-03-22` checkpoint instead of treating the site as a blank slate.
- The checkpoint result was written down explicitly:
  - no first click yet
  - technical indexing state improved
  - concrete is the clearest wedge
  - estate / garage are weaker than expected
- Concrete-cluster internal links were strengthened across decision-stage surfaces so the strongest early topic receives more internal reinforcement.
- Regression coverage was added for those concrete-focused internal links.
- Full local backend test suite was rerun after the link changes and passed.

### Explicit founder concern recorded on 2026-04-01

The concern raised during this session was not only "how do we improve rankings?" but also:

- "Is this too weak to ever make money?"
- "Did I choose the wrong topic?"
- "Is there a better pivot axis that I missed?"

This concern should not be dismissed in future sessions.

Current answer as of `2026-04-01`:

- It is too early to declare the topic wrong.
- It is reasonable to worry because the site is still at `0` clicks.
- The strongest evidence so far says the market response is narrower than expected, not absent.
- The next serious decision should be made on or after `2026-04-05` using both Search Console data and external market/SERP review, not instinct alone.

### Updated operating note

Until the next checkpoint:

- Keep the active framing: `job + debris + risk`
- Treat `concrete` as the lead wedge
- Treat `estate` and `garage` as observation clusters, not equal-priority growth bets
- If clicks are still `0` on or after `2026-04-05`, escalate from "focused concentration" to "harder scope reduction plus authority work"

### External market review performed on 2026-04-01

An external web/SERP review was run during the `2026-04-01` session because of the explicit concern that the topic might be too weak to monetize.

Main observations from the web review:

- Large operators and strong commercial sites keep winning broad informational terms with simple utility pages:
  - size guides
  - weight limits
  - accepted materials
  - pricing
  - permit guidance
- Examples seen during review included:
  - Republic Services dumpster guide
  - Waste Management quick guide
  - Dumpsters.com weight-limit guidance
  - city/government permit and pricing pages
  - local comparison / quote-market sites

Implication:

- The broad national "dumpster calculator" lane is crowded and utility-heavy.
- This does **not** prove the topic is bad.
- It does suggest that a generic calculator alone is unlikely to be the wedge that earns early traffic or revenue.

The strongest alternative axes visible from the market review were:

1. heavy-debris / concrete / dirt / roofing feasibility
2. local pricing and quote-comparison intent
3. permit / placement / city-specific constraints
4. project-type decision pages with obvious commercial handoff

Decision after market review:

- Keep the current topic for now.
- Do not widen back out into generic calculator-first SEO.
- If a pivot becomes necessary, the first serious pivot candidate should be toward:
  - concrete / heavy-debris execution depth, or
  - local price / permit / quote-comparison surfaces
not toward a broader generic cleanup calculator angle.

### Product-facing pivot implemented after the market review

The site was not only analyzed. The main surfaces were also shifted more aggressively toward the chosen wedge.

Product-facing changes implemented:

- calculator landing reframed from broad calculator language toward `heavy debris dumpster calculator`
- calculator hero now leads with concrete/dirt/shingles framing rather than general cleanup framing
- navigation now reads as a heavy-debris specialist surface rather than a generic guide library
- material hub reframed around heavy materials
- project hub reframed around heavy-debris jobs
- concrete-first quick routes and internal links were reinforced

Why this matters:

- The founder concern was not answered with words alone.
- The site now presents itself more like the wedge we are actually testing.
- This makes the next checkpoint more useful, because the product messaging is closer to the intended market position.

### Engine and routing refactor completed later on 2026-04-01

After the first `2026-04-01` pivot pass, the calculator was reviewed again with a stricter standard:

- do not keep adding copy on top of a generic engine
- make the engine itself speak in heavy-debris execution terms
- make CTA routing match actual calculator states
- demote weak broad-cleanup entry points from quick mode

What changed:

- A new backend execution-plan layer was added so the result object now carries:
  - dominant material focus
  - execution headline
  - execution summary
  - explicit local-check checkpoints
- CTA routing logic was corrected and tightened:
  - invalid legacy route keys were removed
  - heavy scenarios with assumed tons now route to `refine assumptions` before local handoff
  - non-feasible or high-risk scenarios still route to comparison/fallback behavior
- Quick mode on the calculator now defaults to:
  - `concrete_removal`
  - `concrete`
- Quick mode now visually prioritizes heavy-debris jobs and materials.
- Broad cleanup scenarios are still supported, but they were pushed into fallback accordions instead of being treated as equal-priority front-door choices.
- Local handoff language was reframed from generic `quote-match beta` wording toward:
  - local heavy-load availability
  - local heavy-debris match

Important engine note:

- During this review, a structural bug was found in CTA routing.
- Some backend defaults still used route keys like `dumpster_quote` and `junk_removal`, while the frontend only understood:
  - `dumpster_call`
  - `dumpster_form`
  - `junk_call`
- This meant part of the intended backend routing was effectively falling through to frontend fallback logic.
- That mismatch was fixed during the refactor.

Why this second refactor matters:

- Before this change, the site *looked* more concrete-first, but the engine still behaved too much like a generic calculator with patched messaging.
- After this change, the calculator result itself is closer to the real product thesis:
  - heavy-debris feasibility first
  - staged-haul planning when required
  - local heavy-load handoff only when the route is credible

Verification:

- targeted calculator, trust-page, SEO-link, quote-intake, and estimation tests passed after the refactor
- full `./gradlew test` also passed after the full set of changes

### Playwright verification and final cleanup completed later on 2026-04-01

After the engine/routing refactor, the calculator was also checked in a real browser on both desktop and mobile instead of assuming the new defaults were good enough.

What was verified:

- Desktop calculator flow in a real browser session
- Mobile calculator flow in a narrow viewport
- Result-state CTA visibility after the heavy-debris pivot
- First-load experience for the default quick preset

Important issue found during this check:

- The new concrete-first preset was still loading with a quantity value that made the first screen look like an immediate failure state.
- In practice this meant the page could open on a verdict like:
  - `Single-load dumpster plan looks weak for concrete.`
- That was judged a bad product first impression because the heavy-debris specialist framing should not open by scaring the user before they interact.

Final cleanup performed:

- The default quantity for the concrete-first preset was reduced so the calculator now opens in a feasible-looking state instead of a failure-looking state.
- On mobile, the floating CTA was hidden because the mobile result dock already carries the action and the duplicate button added clutter.
- User-facing comparison language on heavy-debris surfaces was normalized toward `crew pickup` instead of broader cleanup wording.

Observed result after cleanup:

- Desktop first load now presents a feasible concrete route rather than an immediate failure-state recommendation.
- Mobile keeps the dedicated result dock visible without stacking a second floating CTA on top of it.
- No critical UI blocker remained after the browser verification pass.

Artifacts kept from this verification pass:

- `build/playwright/calculator-desktop.png`
- `build/playwright/calculator-mobile.png`
- `build/playwright/calculator-check.json`

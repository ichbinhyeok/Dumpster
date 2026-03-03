# Next Steps (Plan-Aligned)

Current state: implementation + hardening complete, automated tests green.

## Step 1. Day 13-14 QA Completion

1. Run full regression tests (already automated, 30+ scenarios).
2. Perform manual smoke on key routes:
   - `/dumpster/size-weight-calculator`
   - `/dumpster/estimate/{id}`
   - `/dumpster/heavy-debris-rules`
   - `/robots.txt`
   - `/sitemap.xml`
   - `/sitemap-core.xml`
   - `/sitemap-money.xml`
   - `/sitemap-experiments.xml`
3. Validate must-fail paths:
   - concrete heavy load => `feasibility != OK`
   - no allowance => assumed allowance badge
   - high risk => junk CTA routing preference
4. Validate selective intent indexing:
   - allowlisted `/dumpster/answers/...` routes => `index, follow`
   - non-allowlisted `/dumpster/answers/...` routes => `noindex, follow`

## Step 2. Data Tuning for Launch

1. Adjust CSV values for your real market assumptions:
   - `material_factors.csv`
   - `dumpster_sizes.csv`
   - `pricing_assumptions.csv`
2. Re-run tests after each CSV revision.

## Step 3. Release Gate

1. Ensure `./gradlew.bat test` passes.
2. Verify canonical/noindex/sitemap behaviors on deployed environment.
3. Configure production `APP_BASE_URL` to `https://debrisdecision.com`.
4. Confirm production `APP_SEO_MAX_WAVE` value (default `3`).
5. Launch and monitor:
   - `calc_completed`
   - `feasibility_not_ok`
   - `cta_click_dumpster_call`
   - `cta_click_junk_call`

# Smoke Test Checklist

## Automated Baseline
- `./gradlew test`
- `npm run e2e -- --project=chromium`

## Launch Batch (Playwright)
- `npm run e2e:smoke` (deep smoke suite)
- `npm run e2e:launch` (legacy + deep smoke combined)

## Critical UX Flow
1. Open `/dumpster/size-weight-calculator`.
2. Confirm `header.site-header` and `footer.site-footer` are visible.
3. Confirm live status text transitions to `Updated:` without manual submit.
4. Change material, unit, and quantity with chips/stepper.
5. Confirm gauges and recommendation cards refresh.
6. Click `Calculate` manually and verify result + share link updates.
7. Open share URL and confirm:
   - `meta[name='robots']` is `noindex,follow`
   - canonical points to `/dumpster/size-weight-calculator`

## SEO Infrastructure Flow
1. Open `/robots.txt` and confirm:
   - `/dumpster/answers/` is `Allow`
   - sitemap lines include `/sitemap.xml`, `/sitemap-core.xml`, `/sitemap-money.xml`, `/sitemap-experiments.xml`
2. Open `/sitemap.xml` and confirm it is a `sitemapindex` (not a urlset).
3. Open each child sitemap and confirm 200 response:
   - `/sitemap-core.xml`
   - `/sitemap-money.xml`
   - `/sitemap-experiments.xml`

## Intent Indexing Policy Checks
1. Open an allowlisted intent route (example):
   - `/dumpster/answers/roof-tear-off/asphalt-shingles/overage-risk`
2. Confirm robots behavior is indexable (`index, follow` via meta/header).
3. Open a non-allowlisted intent route (example):
   - `/dumpster/answers/roof-tear-off/asphalt-shingles/size-guide`
4. Confirm robots behavior is `noindex, follow`.

## pSEO Conversion Flow
1. Open a material page (`/dumpster/weight/shingles`).
2. Use intent widget and submit to calculator.
3. Verify presets are applied (material, qty, optional persona/unit).
4. Confirm live result appears and CTA block is usable.

## Compliance/E-E-A-T Spot Checks
1. Calculator page no longer displays legacy `Data updated:` strip.
2. Footer contains source + methodology + legal links.
3. FAQ JSON-LD remains present on material/project pages.
4. Calculator WebApplication JSON-LD remains present.

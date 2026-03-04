# ZIP Market Tier Pipeline (Data Regeneration)

- Anchor date: **2026-03-04**
- Goal: regenerate `market_tier_zip_overrides.csv` from reproducible public sources
- Scope: tier routing for junk profile selection (`urban` / `value` / `coastal` / `mountain` / `heartland`, fallback `national`)

## Inputs

1. HUD USPS ZIP-CBSA Crosswalk (CSV export)
   - https://www.huduser.gov/portal/datasets/usps_crosswalk.html
   - Use ZIP and CBSA columns (`ZIP`, `CBSA`) plus ratio when present (`TOT_RATIO`)

2. Census ACS 1-year CBSA population (API)
   - Endpoint pattern:
     - `https://api.census.gov/data/{year}/acs/acs1?get=NAME,B01003_001E&for=metropolitan%20statistical%20area/micropolitan%20statistical%20area:*`
   - Variable `B01003_001E` = total population
   - API docs:
     - https://www.census.gov/data/developers/data-sets/acs-1year.html

## Generator Script

- Script: [build_market_tier_zip_overrides.py](C:/Development/Owner/dumpster-calculator/scripts/build_market_tier_zip_overrides.py)
- Script (runtime-ready in this repo): [build-market-tier-zip-overrides.mjs](C:/Development/Owner/dumpster-calculator/scripts/build-market-tier-zip-overrides.mjs)
- Output: [market_tier_zip_overrides.csv](C:/Development/Owner/dumpster-calculator/src/main/resources/data/market_tier_zip_overrides.csv)
- Regional overlay source:
  - [market_tier_zip_overrides_regional.csv](C:/Development/Owner/dumpster-calculator/src/main/resources/data/market_tier_zip_overrides_regional.csv)

## Run Example

```bash
node scripts/build-market-tier-zip-overrides.mjs \
  --hud-crosswalk path/to/HUD_ZIP_CROSSWALK.csv \
  --out src/main/resources/data/market_tier_zip_overrides.csv \
  --regional-overrides src/main/resources/data/market_tier_zip_overrides_regional.csv \
  --acs-year 2024 \
  --urban-threshold 1000000 \
  --value-threshold 250000
```

Optional:

- `--census-api-key <KEY>`
- `--include-national` (emit explicit national ranges too)
- `--no-regional-overrides` (skip curated regional overlay rows)
- npm shortcut:
  - `npm run data:market-tier -- --hud-crosswalk path/to/HUD_ZIP_CROSSWALK.csv`

## Tier Logic (Current)

- `urban`: CBSA population `>= 1,000,000`
- `value`: CBSA population `<= 250,000`
- else: `national` (usually omitted from output, handled by app fallback)
- Plus curated regional overlay tiers (appended after generated ranges):
  - `coastal`
  - `mountain`
  - `heartland`

## Why This Exists

- Removes hand-maintained metro ZIP bands as the only source.
- Makes tier updates reproducible and date-anchored.
- Keeps routing deterministic without pretending final quote precision.

## Validation Checklist

After regeneration:

1. Run tests:
   - `./gradlew test`
2. Verify CSV guardrails:
   - ZIP fields are 5 digits
   - `zip_start <= zip_end`
   - only allowed tiers (`urban`, `value`, `national`, `coastal`, `mountain`, `heartland`)
3. Spot-check known ZIPs:
   - metro core ZIPs should map to `urban`
   - low-dispatch regions should map to `value`

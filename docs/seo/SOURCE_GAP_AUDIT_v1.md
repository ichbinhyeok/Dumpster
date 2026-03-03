# Source Gap Audit v1

## Method
Assessment baseline used:
- `src/main/resources/data/material_factors.csv`
- `src/main/resources/data/dumpster_sizes.csv`
- `src/main/resources/data/unit_conversions.csv`
- `docs/seo/data/source_registry_filled_v1.csv`

## Gap Matrix

### 1) Material Weight Ranges and Conditions
- Status: Partial
- Covered: concrete, drywall, shingles baseline references.
- Gap: dirt/soil, brick/block, tile/plaster source-backed range details still weak.
- Action: add tier 1-2 references for dirt/brick/tile/plaster before final content lock.

### 2) Wet/Dry and Broken/Intact Differentiation
- Status: Partial
- Covered: concrete normal-weight baseline and broken-concrete caveat.
- Gap: clear wet/dry quantified deltas by material remain inconsistent.
- Action: require condition-specific range notes per high-ROI material page.

### 3) Included Tons vs Max Haul Tons Separation
- Status: Good
- Covered: separate fields exist in `dumpster_sizes.csv`, and source registry includes both policy and haul signals.
- Gap: cross-provider comparability and regional notes can still be tightened.

### 4) Fill Line / Clean Load / Mixed Load Rules
- Status: Partial
- Covered: heavy-debris fill-line and separation guidance captured from national provider docs.
- Gap: hard numeric fill-ratio values are not consistently specified by providers.
- Action: keep policy text conservative and avoid unsupported exact ratio claims.

### 5) Provider Variance Notes
- Status: Partial
- Covered: notes column includes "varies by hauler" caveats.
- Gap: standardized variance format is missing.
- Action: enforce one variance note template in content production.

### 6) Comparison Economics Triggers
- Status: Partial
- Covered: baseline price ranges and convenience/labor tradeoff references captured.
- Gap: scenario-level inversion trigger thresholds are still heuristic.
- Action: tag these as medium confidence until real conversion telemetry validates.

### 7) Project Preset Scenario Evidence
- Status: Partial
- Covered: preset mechanics exist in implementation.
- Gap: evidence references are not yet fully attached per scenario.
- Action: attach source registry keys to each project preset section.

### 8) Unit-Intent Conversion Backing
- Status: Partial
- Covered: pickup-load mapping and roofing square directional mapping captured.
- Gap: drywall-sheet and mixed material conversion evidence should be expanded.

## Weak Fields to Treat as Medium/Low Confidence
- `rule.concrete.clean_load_required`
- `rule.heavy_debris.fill_line_rule` (numeric ratio certainty)
- `comparison.dumpster_vs_junk_removal.cost_inversion_trigger`
- `conversion.roofing_squares_to_dumpster.range_mapping` (layer count/material variation)

## Publish Gate
Before publishing any page using weak fields:
1. confirm citation links are live,
2. include caveat text near the claim,
3. avoid single-value overprecision,
4. mark confidence in editorial notes.

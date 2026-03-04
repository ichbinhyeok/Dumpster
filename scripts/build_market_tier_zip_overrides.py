#!/usr/bin/env python3
"""
Build market tier ZIP overrides from HUD USPS ZIP-CBSA crosswalk + Census ACS CBSA population.

Usage:
  python scripts/build_market_tier_zip_overrides.py \
    --hud-crosswalk path/to/HUD_ZIP_CROSSWALK.csv \
    --out src/main/resources/data/market_tier_zip_overrides.csv

Notes:
- HUD crosswalk files are available via HUD USPS Crosswalk resources.
- This script expects a crosswalk CSV containing ZIP and CBSA columns.
"""

from __future__ import annotations

import argparse
import csv
import datetime as dt
import json
import re
import sys
import urllib.parse
import urllib.request
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, Iterable, List, Optional, Tuple


HUD_SOURCE_URL = "https://www.huduser.gov/portal/datasets/usps_crosswalk.html"
ACS_API_BASE = "https://api.census.gov/data/{year}/acs/acs1"


@dataclass(frozen=True)
class ZipAssignment:
    zip_code: str
    cbsa_code: str
    weight: float


@dataclass(frozen=True)
class TierRange:
    tier: str
    start_zip: str
    end_zip: str


def normalize_key(value: str) -> str:
    return re.sub(r"[^a-z0-9]", "", value.lower())


def normalize_zip(value: str) -> Optional[str]:
    digits = re.sub(r"[^0-9]", "", value or "")
    if len(digits) != 5:
        return None
    return digits


def normalize_cbsa(value: str) -> Optional[str]:
    digits = re.sub(r"[^0-9]", "", value or "")
    if len(digits) == 5:
        return digits
    return None


def detect_column(headers: Iterable[str], candidates: Iterable[str]) -> Optional[str]:
    normalized = {normalize_key(h): h for h in headers}
    for candidate in candidates:
        if candidate in normalized:
            return normalized[candidate]
    return None


def parse_float(value: str, default: float = 0.0) -> float:
    try:
        return float((value or "").strip())
    except ValueError:
        return default


def load_best_zip_assignments(path: Path) -> Dict[str, ZipAssignment]:
    with path.open("r", encoding="utf-8-sig", newline="") as handle:
        reader = csv.DictReader(handle)
        if reader.fieldnames is None:
            raise ValueError(f"No CSV headers found in {path}")

        zip_col = detect_column(
            reader.fieldnames,
            ["zip", "zipcode", "zip5", "zip_code", "uspszip", "zipcodetabulationarea"],
        )
        cbsa_col = detect_column(
            reader.fieldnames,
            ["cbsa", "cbsa", "cbsacode", "cbsacodeid", "cbsa_code"],
        )
        weight_col = detect_column(
            reader.fieldnames,
            ["totratio", "totalratio", "resratio", "busratio", "ratio", "tot_ratio", "res_ratio"],
        )

        if not zip_col or not cbsa_col:
            raise ValueError(
                "Could not detect ZIP/CBSA columns. Ensure HUD crosswalk includes ZIP and CBSA headers."
            )

        best: Dict[str, ZipAssignment] = {}
        for row in reader:
            zip_code = normalize_zip(row.get(zip_col, ""))
            cbsa_code = normalize_cbsa(row.get(cbsa_col, ""))
            if not zip_code or not cbsa_code:
                continue

            weight = parse_float(row.get(weight_col, "1.0"), default=1.0) if weight_col else 1.0
            candidate = ZipAssignment(zip_code=zip_code, cbsa_code=cbsa_code, weight=weight)
            current = best.get(zip_code)
            if current is None or candidate.weight > current.weight:
                best[zip_code] = candidate

    return best


def fetch_cbsa_population(acs_year: int, census_api_key: Optional[str]) -> Dict[str, int]:
    params = {
        "get": "NAME,B01003_001E",
        "for": "metropolitan statistical area/micropolitan statistical area:*",
    }
    if census_api_key:
        params["key"] = census_api_key
    query = urllib.parse.urlencode(params, quote_via=urllib.parse.quote)
    url = ACS_API_BASE.format(year=acs_year) + "?" + query

    with urllib.request.urlopen(url, timeout=60) as response:
        payload = json.loads(response.read().decode("utf-8"))

    if not payload or len(payload) < 2:
        raise ValueError(f"Unexpected ACS response. URL={url}")

    header = payload[0]
    try:
        population_index = header.index("B01003_001E")
        cbsa_index = header.index("metropolitan statistical area/micropolitan statistical area")
    except ValueError as exc:
        raise ValueError("ACS response missing expected columns") from exc

    out: Dict[str, int] = {}
    for row in payload[1:]:
        cbsa = normalize_cbsa(row[cbsa_index] if cbsa_index < len(row) else "")
        if not cbsa:
            continue
        try:
            pop_value = int(row[population_index])
        except (TypeError, ValueError):
            continue
        out[cbsa] = pop_value
    return out


def classify_market_tier(
    cbsa_code: str,
    cbsa_pop: Dict[str, int],
    urban_threshold: int,
    value_threshold: int,
) -> str:
    if cbsa_code == "99999":
        return "national"
    population = cbsa_pop.get(cbsa_code)
    if population is None:
        return "national"
    if population >= urban_threshold:
        return "urban"
    if population <= value_threshold:
        return "value"
    return "national"


def build_tier_ranges(zip_to_tier: Dict[str, str], include_national: bool) -> List[TierRange]:
    pairs: List[Tuple[int, str]] = []
    for zip_code, tier in zip_to_tier.items():
        if tier == "national" and not include_national:
            continue
        pairs.append((int(zip_code), tier))
    pairs.sort(key=lambda it: it[0])

    ranges: List[TierRange] = []
    current_tier: Optional[str] = None
    start_zip: Optional[int] = None
    end_zip: Optional[int] = None

    for zip_int, tier in pairs:
        if current_tier is None:
            current_tier = tier
            start_zip = zip_int
            end_zip = zip_int
            continue
        assert start_zip is not None and end_zip is not None
        if tier == current_tier and zip_int == end_zip + 1:
            end_zip = zip_int
            continue
        ranges.append(
            TierRange(
                tier=current_tier,
                start_zip=f"{start_zip:05d}",
                end_zip=f"{end_zip:05d}",
            )
        )
        current_tier = tier
        start_zip = zip_int
        end_zip = zip_int

    if current_tier is not None and start_zip is not None and end_zip is not None:
        ranges.append(
            TierRange(
                tier=current_tier,
                start_zip=f"{start_zip:05d}",
                end_zip=f"{end_zip:05d}",
            )
        )
    return ranges


def write_output(
    ranges: List[TierRange],
    output_path: Path,
    acs_year: int,
    urban_threshold: int,
    value_threshold: int,
) -> None:
    output_path.parent.mkdir(parents=True, exist_ok=True)
    now = dt.date.today().isoformat()
    source = f"HUD_USPS_CROSSWALK+ACS{acs_year}"
    source_url = (
        f"{HUD_SOURCE_URL}|"
        f"{ACS_API_BASE.format(year=acs_year)}?get=NAME,B01003_001E&for=metropolitan%20statistical%20area/micropolitan%20statistical%20area:*"
    )
    priority_map = {"urban": 20, "value": 50, "national": 90}

    with output_path.open("w", encoding="utf-8", newline="") as handle:
        writer = csv.writer(handle)
        writer.writerow(
            [
                "rule_id",
                "zip_start",
                "zip_end",
                "market_tier",
                "priority",
                "source",
                "source_url",
                "source_version_date",
                "notes",
            ]
        )
        for idx, item in enumerate(ranges, start=1):
            writer.writerow(
                [
                    f"auto_{item.tier}_{item.start_zip}_{item.end_zip}_{idx}",
                    item.start_zip,
                    item.end_zip,
                    item.tier,
                    priority_map.get(item.tier, 90),
                    source,
                    source_url,
                    now,
                    (
                        "Generated from dominant ZIP->CBSA mapping. "
                        f"Thresholds urban>={urban_threshold}, value<={value_threshold}."
                    ),
                ]
            )


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--hud-crosswalk",
        required=True,
        type=Path,
        help="Path to HUD USPS ZIP-CBSA crosswalk CSV.",
    )
    parser.add_argument(
        "--out",
        default=Path("src/main/resources/data/market_tier_zip_overrides.csv"),
        type=Path,
        help="Output CSV path.",
    )
    parser.add_argument(
        "--acs-year",
        type=int,
        default=2024,
        help="ACS 1-year dataset year used for CBSA populations.",
    )
    parser.add_argument(
        "--census-api-key",
        default=None,
        help="Optional Census API key.",
    )
    parser.add_argument(
        "--urban-threshold",
        type=int,
        default=1_000_000,
        help="Population threshold for urban tier.",
    )
    parser.add_argument(
        "--value-threshold",
        type=int,
        default=250_000,
        help="Population threshold for value tier.",
    )
    parser.add_argument(
        "--include-national",
        action="store_true",
        help="Also emit national ranges (default emits only urban/value).",
    )
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    if not args.hud_crosswalk.exists():
        print(f"[error] HUD crosswalk not found: {args.hud_crosswalk}", file=sys.stderr)
        return 2

    assignments = load_best_zip_assignments(args.hud_crosswalk)
    cbsa_population = fetch_cbsa_population(args.acs_year, args.census_api_key)

    zip_to_tier: Dict[str, str] = {}
    for zip_code, assignment in assignments.items():
        zip_to_tier[zip_code] = classify_market_tier(
            cbsa_code=assignment.cbsa_code,
            cbsa_pop=cbsa_population,
            urban_threshold=args.urban_threshold,
            value_threshold=args.value_threshold,
        )

    ranges = build_tier_ranges(zip_to_tier, include_national=args.include_national)
    write_output(
        ranges=ranges,
        output_path=args.out,
        acs_year=args.acs_year,
        urban_threshold=args.urban_threshold,
        value_threshold=args.value_threshold,
    )

    total_zip = len(zip_to_tier)
    urban_count = sum(1 for tier in zip_to_tier.values() if tier == "urban")
    value_count = sum(1 for tier in zip_to_tier.values() if tier == "value")
    national_count = sum(1 for tier in zip_to_tier.values() if tier == "national")
    print(
        f"[ok] ZIP classified={total_zip} "
        f"(urban={urban_count}, value={value_count}, national={national_count}), "
        f"ranges_emitted={len(ranges)}, output={args.out}"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())

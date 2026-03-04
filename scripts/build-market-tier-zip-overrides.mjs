#!/usr/bin/env node

import fs from "node:fs/promises";
import path from "node:path";
import https from "node:https";

const HUD_SOURCE_URL = "https://www.huduser.gov/portal/datasets/usps_crosswalk.html";
const ACS_API_BASE = (year) => `https://api.census.gov/data/${year}/acs/acs1`;

function normalizeKey(value) {
  return String(value || "").toLowerCase().replace(/[^a-z0-9]/g, "");
}

function normalizeZip(value) {
  const digits = String(value || "").replace(/[^0-9]/g, "");
  return digits.length === 5 ? digits : null;
}

function normalizeCbsa(value) {
  const digits = String(value || "").replace(/[^0-9]/g, "");
  return digits.length === 5 ? digits : null;
}

function parseNumber(value, fallback = 0) {
  const parsed = Number.parseFloat(String(value || "").trim());
  return Number.isFinite(parsed) ? parsed : fallback;
}

function detectColumn(headers, candidates) {
  const normalized = new Map(headers.map((h) => [normalizeKey(h), h]));
  for (const candidate of candidates) {
    if (normalized.has(candidate)) {
      return normalized.get(candidate);
    }
  }
  return null;
}

function parseCsvLine(line) {
  const out = [];
  let value = "";
  let inQuotes = false;
  for (let i = 0; i < line.length; i += 1) {
    const char = line[i];
    if (char === "\"") {
      if (inQuotes && line[i + 1] === "\"") {
        value += "\"";
        i += 1;
      } else {
        inQuotes = !inQuotes;
      }
      continue;
    }
    if (char === "," && !inQuotes) {
      out.push(value);
      value = "";
      continue;
    }
    value += char;
  }
  out.push(value);
  return out;
}

function parseCsvText(text) {
  const lines = text
    .replace(/^\uFEFF/, "")
    .split(/\r?\n/)
    .filter((line) => line.trim().length > 0);
  if (lines.length === 0) {
    return { headers: [], rows: [] };
  }
  const headers = parseCsvLine(lines[0]).map((h) => h.trim());
  const rows = lines.slice(1).map((line) => {
    const cells = parseCsvLine(line);
    const row = {};
    headers.forEach((header, idx) => {
      row[header] = (cells[idx] || "").trim();
    });
    return row;
  });
  return { headers, rows };
}

function httpsGetJson(url) {
  return new Promise((resolve, reject) => {
    https
      .get(url, (res) => {
        if (res.statusCode && res.statusCode >= 400) {
          reject(new Error(`HTTP ${res.statusCode}: ${url}`));
          return;
        }
        const chunks = [];
        res.on("data", (chunk) => chunks.push(chunk));
        res.on("end", () => {
          try {
            const payload = Buffer.concat(chunks).toString("utf8");
            resolve(JSON.parse(payload));
          } catch (error) {
            reject(error);
          }
        });
      })
      .on("error", reject);
  });
}

function loadBestZipAssignments(parsedCsv) {
  const zipColumn = detectColumn(parsedCsv.headers, [
    "zip",
    "zipcode",
    "zip5",
    "zip_code",
    "uspszip",
    "zipcodetabulationarea",
  ]);
  const cbsaColumn = detectColumn(parsedCsv.headers, [
    "cbsa",
    "cbsacode",
    "cbsa_code",
    "cbsacodeid",
  ]);
  const weightColumn = detectColumn(parsedCsv.headers, [
    "totratio",
    "totalratio",
    "tot_ratio",
    "resratio",
    "res_ratio",
    "ratio",
  ]);

  if (!zipColumn || !cbsaColumn) {
    throw new Error("Could not detect ZIP/CBSA columns in HUD crosswalk CSV.");
  }

  const best = new Map();
  for (const row of parsedCsv.rows) {
    const zipCode = normalizeZip(row[zipColumn]);
    const cbsaCode = normalizeCbsa(row[cbsaColumn]);
    if (!zipCode || !cbsaCode) {
      continue;
    }
    const weight = weightColumn ? parseNumber(row[weightColumn], 1) : 1;
    const existing = best.get(zipCode);
    if (!existing || weight > existing.weight) {
      best.set(zipCode, { zipCode, cbsaCode, weight });
    }
  }
  return best;
}

async function fetchCbsaPopulation({ acsYear, censusApiKey }) {
  const params = new URLSearchParams({
    get: "NAME,B01003_001E",
    for: "metropolitan statistical area/micropolitan statistical area:*",
  });
  if (censusApiKey) {
    params.set("key", censusApiKey);
  }
  const url = `${ACS_API_BASE(acsYear)}?${params.toString()}`;
  const payload = await httpsGetJson(url);
  if (!Array.isArray(payload) || payload.length < 2) {
    throw new Error(`Unexpected ACS response from ${url}`);
  }
  const header = payload[0];
  const populationIdx = header.indexOf("B01003_001E");
  const cbsaIdx = header.indexOf("metropolitan statistical area/micropolitan statistical area");
  if (populationIdx < 0 || cbsaIdx < 0) {
    throw new Error("ACS response missing expected columns.");
  }

  const out = new Map();
  for (const row of payload.slice(1)) {
    const cbsa = normalizeCbsa(row[cbsaIdx]);
    const population = Number.parseInt(row[populationIdx], 10);
    if (!cbsa || !Number.isFinite(population)) {
      continue;
    }
    out.set(cbsa, population);
  }
  return out;
}

function classifyTier({ cbsaCode, cbsaPopulation, urbanThreshold, valueThreshold }) {
  if (cbsaCode === "99999") {
    return "national";
  }
  const pop = cbsaPopulation.get(cbsaCode);
  if (!Number.isFinite(pop)) {
    return "national";
  }
  if (pop >= urbanThreshold) {
    return "urban";
  }
  if (pop <= valueThreshold) {
    return "value";
  }
  return "national";
}

function buildRanges(zipToTier, includeNational) {
  const items = [...zipToTier.entries()]
    .filter(([, tier]) => includeNational || tier !== "national")
    .map(([zip, tier]) => ({ zip: Number.parseInt(zip, 10), tier }))
    .sort((a, b) => a.zip - b.zip);

  const ranges = [];
  let current = null;
  for (const item of items) {
    if (!current) {
      current = { tier: item.tier, start: item.zip, end: item.zip };
      continue;
    }
    if (item.tier === current.tier && item.zip === current.end + 1) {
      current.end = item.zip;
      continue;
    }
    ranges.push(current);
    current = { tier: item.tier, start: item.zip, end: item.zip };
  }
  if (current) {
    ranges.push(current);
  }
  return ranges.map((range) => ({
    tier: range.tier,
    startZip: String(range.start).padStart(5, "0"),
    endZip: String(range.end).padStart(5, "0"),
  }));
}

function csvEscape(value) {
  const text = String(value ?? "");
  if (text.includes(",") || text.includes("\"") || text.includes("\n")) {
    return `"${text.replace(/"/g, "\"\"")}"`;
  }
  return text;
}

async function writeOutput({
  ranges,
  regionalOverrides,
  outputPath,
  acsYear,
  urbanThreshold,
  valueThreshold,
}) {
  const priorityByTier = {
    urban: 20,
    coastal: 35,
    mountain: 40,
    heartland: 45,
    value: 50,
    national: 90,
  };
  const nowDate = new Date();
  const now = `${nowDate.getFullYear()}-${String(nowDate.getMonth() + 1).padStart(2, "0")}-${String(
    nowDate.getDate()
  ).padStart(2, "0")}`;
  const source = `HUD_USPS_CROSSWALK+ACS${acsYear}`;
  const sourceUrl = `${HUD_SOURCE_URL}|${ACS_API_BASE(
    acsYear
  )}?get=NAME,B01003_001E&for=metropolitan%20statistical%20area/micropolitan%20statistical%20area:*`;

  const header = [
    "rule_id",
    "zip_start",
    "zip_end",
    "market_tier",
    "priority",
    "source",
    "source_url",
    "source_version_date",
    "notes",
  ];
  const lines = [header.join(",")];

  ranges.forEach((range, idx) => {
    const row = [
      `auto_${range.tier}_${range.startZip}_${range.endZip}_${idx + 1}`,
      range.startZip,
      range.endZip,
      range.tier,
      String(priorityByTier[range.tier] ?? 90),
      source,
      sourceUrl,
      now,
      `Generated from dominant ZIP->CBSA mapping. Thresholds urban>=${urbanThreshold}, value<=${valueThreshold}.`,
    ];
    lines.push(row.map(csvEscape).join(","));
  });

  regionalOverrides.forEach((row) => {
    const outRow = [
      row.rule_id,
      row.zip_start,
      row.zip_end,
      row.market_tier,
      String(row.priority || priorityByTier[row.market_tier] || 90),
      row.source || source,
      row.source_url || sourceUrl,
      row.source_version_date || now,
      row.notes || "Regional override row.",
    ];
    lines.push(outRow.map(csvEscape).join(","));
  });

  await fs.mkdir(path.dirname(outputPath), { recursive: true });
  await fs.writeFile(outputPath, lines.join("\n") + "\n", "utf8");
}

function parseArgs(argv) {
  const args = {
    hudCrosswalk: null,
    out: "src/main/resources/data/market_tier_zip_overrides.csv",
    regionalOverrides: "src/main/resources/data/market_tier_zip_overrides_regional.csv",
    acsYear: 2024,
    censusApiKey: "",
    urbanThreshold: 1000000,
    valueThreshold: 250000,
    includeNational: false,
  };

  for (let i = 2; i < argv.length; i += 1) {
    const token = argv[i];
    const next = argv[i + 1];
    if (token === "--hud-crosswalk" && next) {
      args.hudCrosswalk = next;
      i += 1;
      continue;
    }
    if (token === "--out" && next) {
      args.out = next;
      i += 1;
      continue;
    }
    if (token === "--regional-overrides" && next) {
      args.regionalOverrides = next;
      i += 1;
      continue;
    }
    if (token === "--no-regional-overrides") {
      args.regionalOverrides = "";
      continue;
    }
    if (token === "--acs-year" && next) {
      args.acsYear = Number.parseInt(next, 10);
      i += 1;
      continue;
    }
    if (token === "--census-api-key" && next) {
      args.censusApiKey = next;
      i += 1;
      continue;
    }
    if (token === "--urban-threshold" && next) {
      args.urbanThreshold = Number.parseInt(next, 10);
      i += 1;
      continue;
    }
    if (token === "--value-threshold" && next) {
      args.valueThreshold = Number.parseInt(next, 10);
      i += 1;
      continue;
    }
    if (token === "--include-national") {
      args.includeNational = true;
      continue;
    }
    if (token === "--help") {
      return { ...args, help: true };
    }
  }
  return args;
}

function printUsage() {
  console.log("Usage:");
  console.log("  node scripts/build-market-tier-zip-overrides.mjs \\");
  console.log("    --hud-crosswalk path/to/HUD_ZIP_CROSSWALK.csv \\");
  console.log("    --out src/main/resources/data/market_tier_zip_overrides.csv \\");
  console.log("    --regional-overrides src/main/resources/data/market_tier_zip_overrides_regional.csv \\");
  console.log("    --acs-year 2024");
}

async function loadRegionalOverrides(regionalOverridesPath) {
  if (!regionalOverridesPath) {
    return [];
  }
  const resolved = path.resolve(regionalOverridesPath);
  try {
    await fs.access(resolved);
  } catch (_) {
    return [];
  }

  const text = await fs.readFile(resolved, "utf8");
  const parsed = parseCsvText(text);
  return parsed.rows
    .map((row) => ({
      rule_id: String(row.rule_id || "").trim(),
      zip_start: String(row.zip_start || "").trim(),
      zip_end: String(row.zip_end || "").trim(),
      market_tier: String(row.market_tier || "").trim().toLowerCase(),
      priority: String(row.priority || "").trim(),
      source: String(row.source || "").trim(),
      source_url: String(row.source_url || "").trim(),
      source_version_date: String(row.source_version_date || "").trim(),
      notes: String(row.notes || "").trim(),
    }))
    .filter((row) => row.rule_id && row.zip_start && row.zip_end && row.market_tier);
}

async function main() {
  const args = parseArgs(process.argv);
  if (args.help || !args.hudCrosswalk) {
    printUsage();
    process.exit(args.help ? 0 : 2);
  }

  const crosswalkPath = path.resolve(args.hudCrosswalk);
  const outPath = path.resolve(args.out);
  const crosswalkText = await fs.readFile(crosswalkPath, "utf8");
  const parsed = parseCsvText(crosswalkText);
  const assignments = loadBestZipAssignments(parsed);
  const cbsaPopulation = await fetchCbsaPopulation({
    acsYear: args.acsYear,
    censusApiKey: args.censusApiKey || "",
  });

  const zipToTier = new Map();
  for (const assignment of assignments.values()) {
    zipToTier.set(
      assignment.zipCode,
      classifyTier({
        cbsaCode: assignment.cbsaCode,
        cbsaPopulation,
        urbanThreshold: args.urbanThreshold,
        valueThreshold: args.valueThreshold,
      })
    );
  }

  const ranges = buildRanges(zipToTier, args.includeNational);
  const regionalOverrides = await loadRegionalOverrides(args.regionalOverrides);
  await writeOutput({
    ranges,
    regionalOverrides,
    outputPath: outPath,
    acsYear: args.acsYear,
    urbanThreshold: args.urbanThreshold,
    valueThreshold: args.valueThreshold,
  });

  const summary = {
    zipClassified: zipToTier.size,
    urban: [...zipToTier.values()].filter((tier) => tier === "urban").length,
    value: [...zipToTier.values()].filter((tier) => tier === "value").length,
    national: [...zipToTier.values()].filter((tier) => tier === "national").length,
    rangesEmitted: ranges.length,
    regionalOverrides: regionalOverrides.length,
    output: outPath,
  };
  console.log("[ok]", JSON.stringify(summary));
}

main().catch((error) => {
  console.error("[error]", error?.message || error);
  process.exit(1);
});

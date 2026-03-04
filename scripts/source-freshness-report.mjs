#!/usr/bin/env node

import fs from "node:fs";
import path from "node:path";
import process from "node:process";
import { fileURLToPath } from "node:url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const REPO_ROOT = path.resolve(__dirname, "..");
const DEFAULT_TIMEZONE = process.env.SOURCE_FRESHNESS_TZ || "Asia/Seoul";

function parseArgs(argv) {
  const args = {
    maxAgeDays: 180,
    failOnStale: false,
    write: false,
  };
  for (let index = 0; index < argv.length; index += 1) {
    const token = argv[index];
    if (token === "--fail-on-stale") {
      args.failOnStale = true;
      continue;
    }
    if (token === "--write") {
      args.write = true;
      continue;
    }
    if (token === "--max-age-days") {
      const next = Number(argv[index + 1]);
      if (!Number.isFinite(next) || next <= 0) {
        throw new Error("--max-age-days must be a positive number");
      }
      args.maxAgeDays = Math.round(next);
      index += 1;
      continue;
    }
  }
  return args;
}

function parseCsvLine(line) {
  const values = [];
  let current = "";
  let inQuotes = false;
  for (let idx = 0; idx < line.length; idx += 1) {
    const ch = line[idx];
    if (ch === "\"") {
      const next = line[idx + 1];
      if (inQuotes && next === "\"") {
        current += "\"";
        idx += 1;
      } else {
        inQuotes = !inQuotes;
      }
      continue;
    }
    if (ch === "," && !inQuotes) {
      values.push(current);
      current = "";
      continue;
    }
    current += ch;
  }
  values.push(current);
  return values;
}

function parseCsvFile(filePath) {
  const raw = fs.readFileSync(filePath, "utf8");
  const lines = raw.split(/\r?\n/).filter((line) => line.trim().length > 0);
  if (!lines.length) {
    return [];
  }
  const headers = parseCsvLine(lines[0]).map((h) => h.trim());
  return lines.slice(1).map((line) => {
    const values = parseCsvLine(line);
    const row = {};
    headers.forEach((header, index) => {
      row[header] = (values[index] || "").trim();
    });
    return row;
  });
}

function resolveRowKey(row, idFields, fallbackIndex) {
  for (const field of idFields) {
    const value = String(row[field] || "").trim();
    if (value) {
      return value;
    }
  }
  return `row_${fallbackIndex}`;
}

function auditDataset(spec, todayIso, maxAgeDays) {
  const rows = [];
  const today = new Date(`${todayIso}T00:00:00Z`);
  const parsedRows = parseCsvFile(spec.path);
  parsedRows.forEach((row, index) => {
    const source = String(row.source || "").trim();
    const sourceUrl = String(row.source_url || "").trim();
    const sourceVersionDate = String(row.source_version_date || "").trim();
    const rowKey = resolveRowKey(row, spec.idFields, index + 1);

    const record = {
      dataset: spec.name,
      rowKey,
      source,
      sourceUrl,
      sourceVersionDate,
      ageDays: "",
      status: "OK",
      note: "within freshness window",
    };

    if (!source) {
      record.status = "MISSING_SOURCE";
      record.note = "source is empty";
      rows.push(record);
      return;
    }
    if (spec.requireSourceUrl && !sourceUrl) {
      record.status = "MISSING_URL";
      record.note = "source_url is empty";
      rows.push(record);
      return;
    }
    if (!sourceVersionDate) {
      record.status = "MISSING_DATE";
      record.note = "source_version_date is empty";
      rows.push(record);
      return;
    }

    const parsedDate = new Date(`${sourceVersionDate}T00:00:00Z`);
    if (Number.isNaN(parsedDate.getTime())) {
      record.status = "INVALID_DATE";
      record.note = "source_version_date is not ISO-8601";
      rows.push(record);
      return;
    }

    const ageDays = Math.floor((today.getTime() - parsedDate.getTime()) / (1000 * 60 * 60 * 24));
    record.ageDays = String(ageDays);
    if (ageDays < 0) {
      record.status = "FUTURE_DATE";
      record.note = "source_version_date is in the future";
      rows.push(record);
      return;
    }
    if (ageDays > maxAgeDays) {
      record.status = "STALE";
      record.note = `older than ${maxAgeDays} days`;
      rows.push(record);
      return;
    }

    rows.push(record);
  });
  return rows;
}

function summarize(rows) {
  return rows.reduce((acc, row) => {
    acc[row.status] = (acc[row.status] || 0) + 1;
    return acc;
  }, {});
}

function buildMarkdown(rows, maxAgeDays, todayIso) {
  const summary = summarize(rows);
  const order = ["OK", "STALE", "FUTURE_DATE", "MISSING_SOURCE", "MISSING_URL", "MISSING_DATE", "INVALID_DATE", "MISSING_FILE"];
  const total = rows.length;
  const nonOk = rows.filter((row) => row.status !== "OK").length;

  const lines = [];
  lines.push("# Source Freshness Report");
  lines.push("");
  lines.push(`- Generated: **${todayIso}**`);
  lines.push(`- Freshness window: **<= ${maxAgeDays} days**`);
  lines.push(`- Total audited rows: **${total}**`);
  lines.push(`- Non-OK rows: **${nonOk}**`);
  lines.push("");
  lines.push("## Summary");
  lines.push("");
  order.forEach((status) => {
    if (summary[status]) {
      lines.push(`- \`${status}\`: ${summary[status]}`);
    }
  });
  lines.push("");
  lines.push("## Row Detail");
  lines.push("");
  lines.push("| Dataset | Row Key | Status | Source Date | Age Days | Source | Source URL | Note |");
  lines.push("|---|---|---|---|---:|---|---|---|");
  rows.forEach((row) => {
    lines.push(
      `| ${row.dataset} | ${row.rowKey} | ${row.status} | ${row.sourceVersionDate || "-"} | ${row.ageDays || ""} | ${
        row.source || "-"
      } | ${row.sourceUrl || "-"} | ${row.note} |`
    );
  });
  lines.push("");
  lines.push("## Recommendation");
  lines.push("");
  if (nonOk === 0) {
    lines.push("- All audited rows are within freshness window.");
  } else {
    lines.push("- Refresh stale or invalid rows before the next release wave.");
    lines.push("- Update sourcebook URLs and `source_version_date` in corresponding CSV files.");
  }
  lines.push("");

  return lines.join("\n");
}

function main() {
  const args = parseArgs(process.argv.slice(2));
  const todayIso = localDateIso(DEFAULT_TIMEZONE);
  const datasets = [
    {
      name: "material_factors",
      path: path.join(REPO_ROOT, "src", "main", "resources", "data", "material_factors.csv"),
      idFields: ["material_id"],
      requireSourceUrl: true,
    },
    {
      name: "junk_pricing_profiles",
      path: path.join(REPO_ROOT, "src", "main", "resources", "data", "junk_pricing_profiles.csv"),
      idFields: ["profile_id"],
      requireSourceUrl: true,
    },
    {
      name: "junk_pricing_profile_rules",
      path: path.join(REPO_ROOT, "src", "main", "resources", "data", "junk_pricing_profile_rules.csv"),
      idFields: ["rule_id"],
      requireSourceUrl: true,
    },
    {
      name: "market_tier_zip_overrides",
      path: path.join(REPO_ROOT, "src", "main", "resources", "data", "market_tier_zip_overrides.csv"),
      idFields: ["rule_id"],
      requireSourceUrl: true,
    },
    {
      name: "market_tier_zip_overrides_regional",
      path: path.join(REPO_ROOT, "src", "main", "resources", "data", "market_tier_zip_overrides_regional.csv"),
      idFields: ["rule_id"],
      requireSourceUrl: true,
    },
  ];

  const auditedRows = [];
  datasets.forEach((dataset) => {
    if (!fs.existsSync(dataset.path)) {
      auditedRows.push({
        dataset: dataset.name,
        rowKey: "-",
        source: "-",
        sourceUrl: "-",
        sourceVersionDate: "-",
        ageDays: "",
        status: "MISSING_FILE",
        note: `${dataset.path} not found`,
      });
      return;
    }
    auditedRows.push(...auditDataset(dataset, todayIso, args.maxAgeDays));
  });

  const markdown = buildMarkdown(auditedRows, args.maxAgeDays, todayIso);
  process.stdout.write(`${markdown}\n`);

  if (args.write) {
    const outputDir = path.join(REPO_ROOT, "docs", "data");
    fs.mkdirSync(outputDir, { recursive: true });
    const outputPath = path.join(outputDir, `SOURCE_FRESHNESS_REPORT_${todayIso}.md`);
    fs.writeFileSync(outputPath, `${markdown}\n`, "utf8");
    process.stdout.write(`\nWrote report: ${outputPath}\n`);
  }

  if (args.failOnStale) {
    const blocking = new Set(["STALE", "FUTURE_DATE", "MISSING_SOURCE", "MISSING_URL", "MISSING_DATE", "INVALID_DATE", "MISSING_FILE"]);
    if (auditedRows.some((row) => blocking.has(row.status))) {
      process.exitCode = 2;
    }
  }
}

function localDateIso(timeZone) {
  const formatter = new Intl.DateTimeFormat("en-CA", {
    timeZone,
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
  const parts = formatter.formatToParts(new Date());
  const year = parts.find((part) => part.type === "year")?.value || "1970";
  const month = parts.find((part) => part.type === "month")?.value || "01";
  const day = parts.find((part) => part.type === "day")?.value || "01";
  return `${year}-${month}-${day}`;
}

try {
  main();
} catch (error) {
  process.stderr.write(`${error instanceof Error ? error.message : String(error)}\n`);
  process.exitCode = 1;
}

import { expect, test, type Page } from "@playwright/test";
import { baseEstimatePayload, captureEvents, createEstimate, selectChip, waitForLiveEstimate } from "./helpers";

type ScenarioCase = {
  name: string;
  payload: Record<string, unknown>;
  expectHighRisk?: boolean;
  expectNotOk?: boolean;
  expectPrimaryCta?:
    | "dumpster_call"
    | "dumpster_form"
    | "dumpster_quote"
    | "junk_call"
    | "junk_removal"
    | Array<"dumpster_call" | "dumpster_form" | "dumpster_quote" | "junk_call" | "junk_removal">;
};

function riskRank(value: unknown): number {
  const risk = String(value || "").toUpperCase();
  if (risk === "HIGH") {
    return 3;
  }
  if (risk === "MEDIUM") {
    return 2;
  }
  if (risk === "LOW") {
    return 1;
  }
  return 0;
}

async function readDecisionScore(page: Page, label: string): Promise<number> {
  const row = page.locator("#result-summary .decision-score-row").filter({ hasText: label }).first();
  const valueText = (await row.locator(".decision-score-value").innerText()).trim();
  const parsed = Number(valueText);
  return Number.isFinite(parsed) ? parsed : 0;
}

const scenarioMatrix: ScenarioCase[] = [
  {
    name: "heavy concrete extreme (contractor)",
    payload: baseEstimatePayload({
      projectId: "concrete_removal",
      persona: "contractor",
      items: [
        {
          materialId: "concrete",
          quantity: 600,
          unitId: "sqft_4in",
          conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: false, allowanceTons: 2.0, bulkingFactor: 1.2, zipCode: "94105" },
      needTiming: "research",
    }),
    expectNotOk: true,
    expectHighRisk: true,
    expectPrimaryCta: ["junk_call", "junk_removal"],
  },
  {
    name: "roof tear-off low allowance (contractor)",
    payload: baseEstimatePayload({
      projectId: "roof_tearoff",
      persona: "contractor",
      items: [
        {
          materialId: "asphalt_shingles",
          quantity: 20,
          unitId: "roof_square",
          conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: false, allowanceTons: 1.0, bulkingFactor: 1.2, zipCode: "10001" },
      needTiming: "research",
    }),
    expectHighRisk: true,
  },
  {
    name: "urgent kitchen remodel (homeowner)",
    payload: baseEstimatePayload({
      projectId: "kitchen_remodel",
      persona: "homeowner",
      items: [
        {
          materialId: "mixed_cd",
          quantity: 4,
          unitId: "pickup_load",
          conditions: { wet: false, mixedLoad: true, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: true, allowanceTons: 3.5, bulkingFactor: 1.2, zipCode: "30339" },
      needTiming: "48h",
    }),
    expectPrimaryCta: ["dumpster_call", "dumpster_quote"],
  },
  {
    name: "garage junk cleanout value-tier zip",
    payload: baseEstimatePayload({
      projectId: "garage_cleanout",
      persona: "homeowner",
      items: [
        {
          materialId: "household_junk",
          quantity: 6,
          unitId: "pickup_load",
          conditions: { wet: false, mixedLoad: true, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: true, allowanceTons: null, bulkingFactor: 1.2, zipCode: "58012" },
      needTiming: "this_week",
    }),
  },
  {
    name: "yard waste wet mixed (homeowner)",
    payload: baseEstimatePayload({
      projectId: "yard_cleanup",
      persona: "homeowner",
      items: [
        {
          materialId: "yard_waste",
          quantity: 10,
          unitId: "pickup_load",
          conditions: { wet: true, mixedLoad: true, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: true, allowanceTons: 2.0, bulkingFactor: 1.2, zipCode: "98101" },
      needTiming: "research",
    }),
  },
  {
    name: "deck demolition wood (investor)",
    payload: baseEstimatePayload({
      projectId: "deck_demolition",
      persona: "investor",
      items: [
        {
          materialId: "decking_wood",
          quantity: 9,
          unitId: "pickup_load",
          conditions: { wet: false, mixedLoad: true, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: true, allowanceTons: 2.5, bulkingFactor: 1.2, zipCode: "78701" },
      needTiming: "this_week",
    }),
  },
  {
    name: "bathroom remodel tile dense load",
    payload: baseEstimatePayload({
      projectId: "bathroom_remodel",
      persona: "homeowner",
      items: [
        {
          materialId: "tile_ceramic",
          quantity: 8,
          unitId: "pickup_load",
          conditions: { wet: false, mixedLoad: true, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: true, allowanceTons: 2.0, bulkingFactor: 1.2, zipCode: "60607" },
      needTiming: "research",
    }),
  },
  {
    name: "heavy urgent uncertain mix (homeowner)",
    payload: baseEstimatePayload({
      projectId: "concrete_removal",
      persona: "homeowner",
      items: [
        {
          materialId: "concrete",
          quantity: 220,
          unitId: "sqft_4in",
          conditions: { wet: true, mixedLoad: true, compaction: "LOW" },
        },
      ],
      options: { mixedLoad: true, allowanceTons: 1.5, bulkingFactor: 1.3, zipCode: "10001" },
      needTiming: "48h",
    }),
    expectHighRisk: true,
  },
  {
    name: "drywall interior remodel",
    payload: baseEstimatePayload({
      projectId: "kitchen_remodel",
      persona: "contractor",
      items: [
        {
          materialId: "drywall",
          quantity: 45,
          unitId: "drywall_sheet",
          conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: false, allowanceTons: 2.5, bulkingFactor: 1.2, zipCode: "33131" },
      needTiming: "this_week",
    }),
  },
];

test.describe("Beta matrix: API and decision integrity", () => {
  test("scenario matrix stays valid across risk/feasibility/CTA branches", async ({ request }) => {
    for (const scenario of scenarioMatrix) {
      const { response, json } = await createEstimate(request, scenario.payload);
      expect(response.ok(), `Scenario failed: ${scenario.name}`).toBeTruthy();

      const result = (json?.result ?? {}) as Record<string, any>;
      const recommendations = (result.recommendations ?? []) as Array<Record<string, unknown>>;
      const costComparison = (result.costComparison ?? []) as Array<Record<string, any>>;
      const ctaRouting = (result.ctaRouting ?? {}) as Record<string, unknown>;

      expect(recommendations.length, `${scenario.name}: recommendations`).toBeGreaterThan(0);
      expect(costComparison.length, `${scenario.name}: cost options`).toBeGreaterThanOrEqual(3);
      expect(["LOW", "MEDIUM", "HIGH"]).toContain(String(result.priceRisk));
      expect(["OK", "MULTI_HAUL_REQUIRED", "NOT_RECOMMENDED"]).toContain(String(result.feasibility));
      expect(String(ctaRouting.primaryCta)).toMatch(/^(dumpster|junk)_[a-z_]+$/);

      const junkOption = costComparison.find((opt) => opt.optionId === "junk_removal");
      expect(junkOption, `${scenario.name}: junk option`).toBeTruthy();
      expect(Array.isArray(junkOption?.notes)).toBeTruthy();
      expect(String((junkOption?.notes ?? []).join(" ")).toLowerCase()).toContain("market tier:");

      if (scenario.expectHighRisk) {
        expect(riskRank(result.priceRisk), `${scenario.name}: expected high risk`).toBeGreaterThanOrEqual(3);
      }
      if (scenario.expectNotOk) {
        expect(String(result.feasibility), `${scenario.name}: expected non-OK`).not.toBe("OK");
      }
      if (scenario.expectPrimaryCta) {
        const actualPrimary = String(ctaRouting.primaryCta);
        const expected = Array.isArray(scenario.expectPrimaryCta)
          ? scenario.expectPrimaryCta
          : [scenario.expectPrimaryCta];
        expect(expected, `${scenario.name}: expected primary CTA set`).toContain(actualPrimary);
      }
    }
  });

  test("zip-tier routing impacts junk pricing while keeping deterministic notes", async ({ request }) => {
    const basePayload = baseEstimatePayload({
      projectId: "garage_cleanout",
      persona: "homeowner",
      items: [
        {
          materialId: "household_junk",
          quantity: 7,
          unitId: "pickup_load",
          conditions: { wet: false, mixedLoad: true, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: true, allowanceTons: null, bulkingFactor: 1.2 },
      needTiming: "this_week",
    });

    const urban = await createEstimate(request, {
      ...basePayload,
      options: { ...(basePayload.options as Record<string, unknown>), zipCode: "94105" },
    });
    const value = await createEstimate(request, {
      ...basePayload,
      options: { ...(basePayload.options as Record<string, unknown>), zipCode: "58012" },
    });
    const coastal = await createEstimate(request, {
      ...basePayload,
      options: { ...(basePayload.options as Record<string, unknown>), zipCode: "07005" },
    });
    const mountain = await createEstimate(request, {
      ...basePayload,
      options: { ...(basePayload.options as Record<string, unknown>), zipCode: "83702" },
    });
    const heartland = await createEstimate(request, {
      ...basePayload,
      options: { ...(basePayload.options as Record<string, unknown>), zipCode: "63101" },
    });
    const fallback = await createEstimate(request, {
      ...basePayload,
      options: { ...(basePayload.options as Record<string, unknown>), zipCode: "12345" },
    });

    expect(urban.response.ok()).toBeTruthy();
    expect(value.response.ok()).toBeTruthy();
    expect(coastal.response.ok()).toBeTruthy();
    expect(mountain.response.ok()).toBeTruthy();
    expect(heartland.response.ok()).toBeTruthy();
    expect(fallback.response.ok()).toBeTruthy();

    const getJunkTypCost = (payload: Record<string, unknown>) => {
      const result = (payload.result ?? {}) as Record<string, any>;
      const cost = ((result.costComparison ?? []) as Array<Record<string, any>>).find(
        (opt) => opt.optionId === "junk_removal"
      );
      return {
        typ: Number(cost?.estimatedTotalCostUsd?.typ ?? 0),
        notes: String((cost?.notes ?? []).join(" ")).toLowerCase(),
      };
    };

    const urbanJunk = getJunkTypCost(urban.json as Record<string, unknown>);
    const valueJunk = getJunkTypCost(value.json as Record<string, unknown>);
    const coastalJunk = getJunkTypCost(coastal.json as Record<string, unknown>);
    const mountainJunk = getJunkTypCost(mountain.json as Record<string, unknown>);
    const heartlandJunk = getJunkTypCost(heartland.json as Record<string, unknown>);
    const fallbackJunk = getJunkTypCost(fallback.json as Record<string, unknown>);

    expect(urbanJunk.notes).toContain("market tier: urban");
    expect(valueJunk.notes).toContain("market tier: value");
    expect(coastalJunk.notes).toContain("market tier: coastal");
    expect(mountainJunk.notes).toContain("market tier: mountain");
    expect(heartlandJunk.notes).toContain("market tier: heartland");
    expect(fallbackJunk.notes).toContain("market tier: national");
    expect(fallbackJunk.notes).toContain("default tier");
    expect(urbanJunk.typ).toBeGreaterThan(valueJunk.typ);
    expect(coastalJunk.typ).toBeGreaterThan(fallbackJunk.typ);
    expect(fallbackJunk.typ).toBeGreaterThan(heartlandJunk.typ);
    expect(heartlandJunk.typ).toBeGreaterThan(valueJunk.typ);
    expect(mountainJunk.typ).toBeGreaterThanOrEqual(heartlandJunk.typ);
    expect(mountainJunk.typ).toBeLessThanOrEqual(fallbackJunk.typ);
  });
});

test.describe("Beta matrix: UI decision board integrity", () => {
  test("multiple preset flows render decision scorecard + CTA branch + analytics events", async ({ page }) => {
    const events = await captureEvents(page);

    const presetCases = [
      {
        label: "heavy preset",
        url: "/dumpster/size-weight-calculator?project=concrete_removal&material=concrete&unit=sqft_4in&qty=450&timing=research",
        expectedPrimaryId: "cta-junk",
      },
      {
        label: "urgent preset",
        url: "/dumpster/size-weight-calculator?project=kitchen_remodel&material=mixed_cd&unit=pickup_load&qty=4&timing=48h",
        expectedPrimaryId: "cta-dumpster-call",
      },
      {
        label: "value preset",
        url: "/dumpster/size-weight-calculator?project=garage_cleanout&material=household_junk&unit=pickup_load&qty=6&timing=this_week",
        expectedPrimaryId: "",
      },
    ];

    for (const preset of presetCases) {
      await page.goto(preset.url);
      await page.getByRole("button", { name: "Calculate" }).click();
      await waitForLiveEstimate(page);

      await expect(page.locator("#result-summary")).toContainText("Decision scorecard");
      await expect(page.locator("#result-summary .decision-score-row")).toHaveCount(4);
      await expect(page.locator("#result-summary .decision-score-fill")).toHaveCount(4);

      const primaryCta = page.locator("#result-actions .result-primary-cta");
      await expect(primaryCta, `${preset.label}: primary CTA visible`).toBeVisible();
      if (preset.expectedPrimaryId) {
        await expect(primaryCta).toHaveAttribute("id", preset.expectedPrimaryId);
      }
    }

    await expect
      .poll(
        () => events.some((event) => event.eventName === "decision_scorecard_rendered"),
        { timeout: 10_000 }
      )
      .toBeTruthy();
  });

  test("comparison hub priorities emit analytics and preserve calculator exits", async ({ page }) => {
    const events = await captureEvents(page);
    await page.goto("/dumpster/dumpster-vs-junk-removal-which-is-cheaper");

    const toggles = ["Lowest cost", "Fastest completion", "Least effort", "Heavy-load safety"];
    for (const label of toggles) {
      await page.getByRole("button", { name: label }).click();
    }
    await page.locator("a[href*='/dumpster/size-weight-calculator']").first().click();
    await expect(page).toHaveURL(/\/dumpster\/size-weight-calculator/);

    await expect
      .poll(
        () => events.filter((event) => event.eventName === "comparison_priority_selected").length,
        { timeout: 10_000 }
      )
      .toBeGreaterThanOrEqual(4);
  });

  test("calculator keeps scorecard stable across priority query while preserving analytics labels", async ({ page }) => {
    const events = await captureEvents(page);
    const baseUrl =
      "/dumpster/size-weight-calculator?project=garage_cleanout&material=household_junk&unit=pickup_load&qty=6&timing=this_week";

    await page.goto(baseUrl);
    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);
    await expect(page.locator("#result-summary")).toContainText("Priority mode: Balanced");
    const balancedSafety = await readDecisionScore(page, "Safety margin");

    await page.goto(baseUrl + "&priority=heavy");
    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);
    await expect(page.locator("#result-summary")).toContainText("Priority mode: Heavy-load safety");
    const heavySafety = await readDecisionScore(page, "Safety margin");

    expect(heavySafety).toBe(balancedSafety);

    await expect
      .poll(
        () =>
          events.some(
            (event) =>
              event.eventName === "decision_scorecard_rendered" &&
              String((event.payload as Record<string, unknown>).priorityMode || "") === "heavy"
          ),
        { timeout: 10_000 }
      )
      .toBeTruthy();
  });

  test("lead content gate emits pass/fail transitions", async ({ page }) => {
    const events = await captureEvents(page);
    await page.goto(
      "/dumpster/size-weight-calculator?project=garage_cleanout&material=household_junk&unit=pickup_load&qty=4&timing=this_week"
    );
    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);

    await page.locator("#lead-zip").fill("12");
    await page.locator("#lead-next").click();
    await expect(page.locator("#lead-status")).toContainText("Enter a valid 5-digit ZIP.");

    await page.locator("#lead-zip").fill("30339");
    await page.locator("#lead-next").click();
    await expect(page.locator("#lead-step-2")).toBeVisible();

    await page.locator("#lead-contact-method").selectOption("email");
    await page.locator("#lead-contact-value").fill("");
    await page.locator("#lead-submit").click();
    await expect(page.locator("#lead-status")).toContainText("Enter your contact information.");

    await page.locator("#lead-contact-value").fill("beta@example.com");
    await page.locator("#lead-submit").click();
    await expect(page.locator("#lead-status")).toContainText("Lead submitted.");

    await expect
      .poll(
        () => events.filter((event) => event.eventName === "content_gate_fail").length,
        { timeout: 10_000 }
      )
      .toBeGreaterThanOrEqual(2);
    await expect
      .poll(
        () => events.filter((event) => event.eventName === "content_gate_pass").length,
        { timeout: 10_000 }
      )
      .toBeGreaterThanOrEqual(2);
  });
});

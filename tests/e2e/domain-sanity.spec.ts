import { expect, test } from "@playwright/test";
import { baseEstimatePayload, createEstimate } from "./helpers";

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

function topSize(result: Record<string, unknown>): number {
  const recommendations = (result.recommendations ?? []) as Array<Record<string, unknown>>;
  const top = recommendations[0] ?? {};
  return Number(top.sizeYd ?? 0);
}

test.describe("Domain sanity checks (operator-view expectations)", () => {
  test("wet + mixed conditions should not reduce high-end weight estimate", async ({ request }) => {
    const basePayload = baseEstimatePayload({
      projectId: "kitchen_remodel",
      persona: "homeowner",
      items: [
        {
          materialId: "mixed_cd",
          quantity: 10,
          unitId: "pickup_load",
          conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
        },
      ],
      options: { mixedLoad: false, allowanceTons: 3.0, bulkingFactor: 1.2 },
    });

    const baseline = await createEstimate(request, basePayload);
    const stressed = await createEstimate(
      request,
      baseEstimatePayload({
        projectId: "kitchen_remodel",
        persona: "homeowner",
        items: [
          {
            materialId: "mixed_cd",
            quantity: 10,
            unitId: "pickup_load",
            conditions: { wet: true, mixedLoad: true, compaction: "MEDIUM" },
          },
        ],
        options: { mixedLoad: true, allowanceTons: 3.0, bulkingFactor: 1.2 },
      })
    );

    expect(baseline.response.ok()).toBeTruthy();
    expect(stressed.response.ok()).toBeTruthy();

    const baselineWeightHigh = Number((baseline.json?.result as Record<string, unknown>)?.weightTons?.high ?? 0);
    const stressedWeightHigh = Number((stressed.json?.result as Record<string, unknown>)?.weightTons?.high ?? 0);
    expect(stressedWeightHigh).toBeGreaterThanOrEqual(baselineWeightHigh);
  });

  test("low included-ton allowance should not produce lower risk than high allowance", async ({ request }) => {
    const lowAllowance = await createEstimate(
      request,
      baseEstimatePayload({
        projectId: "roof_tearoff",
        persona: "contractor",
        items: [
          {
            materialId: "asphalt_shingles",
            quantity: 16,
            unitId: "roof_square",
            conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
          },
        ],
        options: { mixedLoad: false, allowanceTons: 1.0, bulkingFactor: 1.2 },
      })
    );

    const highAllowance = await createEstimate(
      request,
      baseEstimatePayload({
        projectId: "roof_tearoff",
        persona: "contractor",
        items: [
          {
            materialId: "asphalt_shingles",
            quantity: 16,
            unitId: "roof_square",
            conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
          },
        ],
        options: { mixedLoad: false, allowanceTons: 4.0, bulkingFactor: 1.2 },
      })
    );

    expect(lowAllowance.response.ok()).toBeTruthy();
    expect(highAllowance.response.ok()).toBeTruthy();

    const lowRisk = riskRank((lowAllowance.json?.result as Record<string, unknown>)?.priceRisk);
    const highRisk = riskRank((highAllowance.json?.result as Record<string, unknown>)?.priceRisk);
    expect(lowRisk).toBeGreaterThanOrEqual(highRisk);
  });

  test("concrete should be at least as risk-heavy as household junk at same cubic volume", async ({ request }) => {
    const concrete = await createEstimate(
      request,
      baseEstimatePayload({
        projectId: "concrete_removal",
        persona: "contractor",
        items: [
          {
            materialId: "concrete",
            quantity: 8,
            unitId: "cubic_yard",
            conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
          },
        ],
        options: { mixedLoad: false, allowanceTons: 3.0, bulkingFactor: 1.2 },
      })
    );

    const junk = await createEstimate(
      request,
      baseEstimatePayload({
        projectId: "garage_cleanout",
        persona: "homeowner",
        items: [
          {
            materialId: "household_junk",
            quantity: 8,
            unitId: "cubic_yard",
            conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
          },
        ],
        options: { mixedLoad: false, allowanceTons: 3.0, bulkingFactor: 1.2 },
      })
    );

    expect(concrete.response.ok()).toBeTruthy();
    expect(junk.response.ok()).toBeTruthy();

    const concreteResult = (concrete.json?.result ?? {}) as Record<string, unknown>;
    const junkResult = (junk.json?.result ?? {}) as Record<string, unknown>;
    expect(riskRank(concreteResult.priceRisk)).toBeGreaterThanOrEqual(riskRank(junkResult.priceRisk));

    const concreteTopSize = topSize(concreteResult);
    const junkTopSize = topSize(junkResult);
    expect(concreteTopSize).toBeLessThanOrEqual(junkTopSize);
  });
});

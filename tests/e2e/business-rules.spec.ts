import { expect, test } from "@playwright/test";
import { baseEstimatePayload, createEstimate } from "./helpers";

test.describe("Business logic and heavy-rule edge coverage", () => {
  test("heavy concrete large-load scenario is non-feasible and constrained to smaller strategy", async ({
    request,
  }) => {
    const { response, json } = await createEstimate(
      request,
      baseEstimatePayload({
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
        options: { mixedLoad: false, allowanceTons: 2.0, bulkingFactor: 1.2 },
      })
    );

    expect(response.ok()).toBeTruthy();
    const result = (json?.result ?? {}) as Record<string, unknown>;
    const recommendations = (result.recommendations ?? []) as Array<Record<string, unknown>>;

    expect(result.feasibility).not.toBe("OK");
    expect(recommendations.length).toBeGreaterThan(0);
    expect(
      recommendations.some((rec) => Boolean(rec.multiHaul)) ||
        ((result.hardStopReasons ?? []) as unknown[]).length > 0
    ).toBeTruthy();

    const largest = recommendations
      .map((rec) => Number(rec.sizeYd ?? 0))
      .reduce((max, current) => (current > max ? current : max), 0);
    expect(largest).toBeLessThanOrEqual(20);
  });

  test("low allowance for shingle tear-off elevates price risk to HIGH", async ({ request }) => {
    const { response, json } = await createEstimate(
      request,
      baseEstimatePayload({
        projectId: "roof_tearoff",
        persona: "contractor",
        items: [
          {
            materialId: "asphalt_shingles",
            quantity: 18,
            unitId: "roof_square",
            conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
          },
        ],
        options: { mixedLoad: false, allowanceTons: 1.0, bulkingFactor: 1.2 },
      })
    );

    expect(response.ok()).toBeTruthy();
    const result = (json?.result ?? {}) as Record<string, unknown>;
    expect(result.priceRisk).toBe("HIGH");
    expect(result.ctaRouting).toBeTruthy();
  });

  test("urgent timing route prioritizes call CTA", async ({ request }) => {
    const { response, json } = await createEstimate(
      request,
      baseEstimatePayload({
        projectId: "kitchen_remodel",
        persona: "homeowner",
        needTiming: "48h",
        items: [
          {
            materialId: "mixed_cd",
            quantity: 5,
            unitId: "pickup_load",
            conditions: { wet: false, mixedLoad: true, compaction: "MEDIUM" },
          },
        ],
        options: { mixedLoad: true, allowanceTons: 3.0, bulkingFactor: 1.2 },
      })
    );

    expect(response.ok()).toBeTruthy();
    const result = (json?.result ?? {}) as Record<string, unknown>;
    const ctaRouting = (result.ctaRouting ?? {}) as Record<string, unknown>;
    expect(ctaRouting.primaryCta).toBe("dumpster_call");
  });

  test("unsupported material input is rejected (prohibited/unknown item guard)", async ({
    request,
  }) => {
    const { response, text } = await createEstimate(
      request,
      baseEstimatePayload({
        items: [
          {
            materialId: "paint",
            quantity: 2,
            unitId: "pickup_load",
            conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
          },
        ],
      })
    );

    expect(response.status()).toBe(400);
    expect(text ?? "").toContain("Unknown material_id");
  });

  test("roof_square unit is rejected when material is not asphalt_shingles", async ({ request }) => {
    const { response, text } = await createEstimate(
      request,
      baseEstimatePayload({
        projectId: "concrete_removal",
        items: [
          {
            materialId: "concrete",
            quantity: 40,
            unitId: "roof_square",
            conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
          },
        ],
      })
    );

    expect(response.status()).toBe(400);
    expect(text ?? "").toContain("roof_square");
  });
});

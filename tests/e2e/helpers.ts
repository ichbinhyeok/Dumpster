import { expect, type APIRequestContext, type Page } from "@playwright/test";

export async function selectChip(page: Page, target: string, value: string) {
  await page.locator(`[data-choice-target='${target}'] [data-choice-value='${value}']`).click();
  await expect(page.locator(`#${target}`)).toHaveValue(value);
}

export async function waitForLiveEstimate(page: Page) {
  await expect(page.locator("#result-panel")).toBeVisible({ timeout: 20_000 });
  await expect(page.locator("#share-link")).toHaveAttribute(
    "href",
    /\/dumpster\/estimate\/[a-zA-Z0-9-]+/,
    { timeout: 20_000 }
  );
}

export async function captureEvents(page: Page) {
  const events: Array<{ eventName: string; estimateId: string | null; payload: Record<string, unknown> }> = [];
  await page.route("**/api/events", async (route) => {
    const raw = route.request().postData() ?? "{}";
    try {
      const parsed = JSON.parse(raw) as {
        eventName: string;
        estimateId?: string | null;
        payload?: Record<string, unknown>;
      };
      events.push({
        eventName: parsed.eventName,
        estimateId: parsed.estimateId ?? null,
        payload: parsed.payload ?? {},
      });
    } catch {
      events.push({
        eventName: "invalid_json",
        estimateId: null,
        payload: { raw },
      });
    }
    await route.continue();
  });
  return events;
}

export function baseEstimatePayload(overrides?: Record<string, unknown>) {
  return {
    projectId: "roof_tearoff",
    persona: "homeowner",
    items: [
      {
        materialId: "asphalt_shingles",
        quantity: 12,
        unitId: "roof_square",
        conditions: { wet: false, mixedLoad: false, compaction: "MEDIUM" },
      },
    ],
    options: { mixedLoad: false, allowanceTons: 2.0, bulkingFactor: 1.2 },
    needTiming: "research",
    ...overrides,
  };
}

export async function createEstimate(request: APIRequestContext, payload: Record<string, unknown>) {
  const response = await request.post("/api/estimates", { data: payload });
  return {
    response,
    json: response.ok() ? ((await response.json()) as Record<string, unknown>) : null,
    text: response.ok() ? null : await response.text(),
  };
}

export function jsonLdTypes(jsonLdBlocks: string[]): string[] {
  return jsonLdBlocks.flatMap((block) => {
    const matches = [...block.matchAll(/"@type"\s*:\s*"([^"]+)"/g)];
    return matches.map((match) => match[1]).filter(Boolean);
  });
}

export async function assertNoHorizontalOverflow(page: Page) {
  const hasOverflow = await page.evaluate(() => {
    const viewportWidth = window.innerWidth;
    return document.documentElement.scrollWidth > viewportWidth + 1;
  });
  expect(hasOverflow).toBeFalsy();
}

export async function assertTapTargetMinSize(page: Page, selector: string, minPx = 44) {
  const box = await page.locator(selector).boundingBox();
  expect(box, `Missing element for selector: ${selector}`).not.toBeNull();
  expect((box?.width ?? 0) >= minPx, `${selector} width is below ${minPx}px`).toBeTruthy();
  expect((box?.height ?? 0) >= minPx, `${selector} height is below ${minPx}px`).toBeTruthy();
}

export function sitemapLocs(xml: string): string[] {
  return [...xml.matchAll(/<loc>(.*?)<\/loc>/g)].map((match) => match[1]);
}

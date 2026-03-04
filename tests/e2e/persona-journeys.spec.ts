import { expect, test } from "@playwright/test";
import { captureEvents, selectChip, waitForLiveEstimate } from "./helpers";

test.describe("Persona E2E journeys", () => {
  test("Homeowner journey: yard cleanup flow to lead submit", async ({ page }) => {
    const events = await captureEvents(page);
    await page.goto("/dumpster/size-weight-calculator");

    await selectChip(page, "project-id", "yard_cleanup");
    await selectChip(page, "material-id", "yard_waste");
    await selectChip(page, "persona", "homeowner");
    await page.locator("#quantity").fill("8");

    const start = Date.now();
    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);
    const durationMs = Date.now() - start;
    expect(durationMs).toBeLessThan(3_500);

    await expect(page.locator("#result-panel")).toContainText("Risk:");
    await expect(page.locator("#result-panel")).toContainText("Feasibility:");

    await page.locator("#lead-zip").fill("30339");
    await page.locator("#lead-next").click();
    await expect(page.locator("#lead-step-2")).toBeVisible();
    await page.selectOption("#lead-contact-method", "email");
    await page.locator("#lead-contact-value").fill("owner@example.com");
    await page.locator("#lead-submit").click();
    await expect(page.locator("#lead-status")).toContainText(/Submitting to quote-match beta queue|Queued:/);

    await expect.poll(
      () => events.some((event) => event.eventName === "lead_submitted"),
      { timeout: 10_000 }
    ).toBeTruthy();
  });

  test("Contractor journey: roof tear-off with roof-square and urgent call intent", async ({ page }) => {
    const events = await captureEvents(page);
    await page.goto("/dumpster/size-weight-calculator");

    await selectChip(page, "persona", "contractor");
    await selectChip(page, "project-id", "roof_tearoff");
    await selectChip(page, "material-id", "asphalt_shingles");
    await selectChip(page, "need-timing", "48h");
    await selectChip(page, "unit-id", "roof_square");
    await page.locator("#quantity").fill("20");
    await page.locator("#allowance-tons").fill("1.2");

    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);

    await expect(page.locator("#unit-id")).toHaveValue("roof_square");
    const recommendationTitles = await page.locator("#result-recommendations h3").allTextContents();
    expect(recommendationTitles.length).toBeGreaterThan(0);
    expect(recommendationTitles.some((title) => /\d+yd/i.test(title))).toBeTruthy();
    expect(recommendationTitles.some((title) => /(10|15|20)yd/i.test(title))).toBeTruthy();

    await page.locator("#lead-zip").fill("10001");
    await page.locator("#lead-next").click();
    await page.locator("#lead-contact-method").selectOption("phone");
    await page.locator("#lead-contact-value").fill("(212) 555-1111");
    await page.locator("#cta-dumpster-call").click();
    await expect(page).toHaveURL(/\/about\/quote-match-beta/);

    await expect.poll(
      () => events.some((event) => event.eventName === "call_qualified"),
      { timeout: 10_000 }
    ).toBeTruthy();
  });
});

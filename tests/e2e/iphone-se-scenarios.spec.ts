import { devices, expect, test } from "@playwright/test";
import { captureEvents, selectChip, waitForLiveEstimate } from "./helpers";

test.use({
  viewport: devices["iPhone SE"].viewport,
  userAgent: devices["iPhone SE"].userAgent,
  deviceScaleFactor: devices["iPhone SE"].deviceScaleFactor,
  isMobile: devices["iPhone SE"].isMobile,
  hasTouch: devices["iPhone SE"].hasTouch,
});

test.describe("iPhone SE scenario coverage", () => {
  test("homeowner journey keeps result and lead capture actionable on 320px", async ({ page }) => {
    const events = await captureEvents(page);
    await page.goto("/dumpster/size-weight-calculator");

    await selectChip(page, "project-id", "yard_cleanup");
    await selectChip(page, "material-id", "yard_waste");
    await page.locator("#quantity").fill("8");
    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);
    await expect(page.locator("#submit-button")).toHaveText("Calculate");
    await expect(page.locator("#live-note")).toContainText("Live update is on.");

    await expect(page.locator("#mobile-result-dock")).toBeVisible();
    await expect(page.locator("#result-panel")).toContainText("Risk:");
    await expect(page.locator("#result-panel")).toContainText("Feasibility:");

    await page.locator("#lead-zip").scrollIntoViewIfNeeded();
    await page.locator("#lead-zip").fill("30339");
    await expect(page.locator("#lead-zip")).toHaveValue("30339");
    await page.locator("#lead-next").click();
    await expect(page.locator("#lead-step-2")).toBeVisible();
    await page.selectOption("#lead-contact-method", "email");
    await page.locator("#lead-contact-value").fill("owner@example.com");
    await expect(page.locator("#lead-contact-value")).toHaveValue("owner@example.com");
    await page.locator("#lead-submit").click();
    await expect(page.locator("#lead-status")).toContainText("Queued:");
    await expect
      .poll(() => events.some((event) => event.eventName === "lead_submitted"), { timeout: 10_000 })
      .toBeTruthy();
  });

  test("contractor urgent journey promotes call-first action on iPhone SE", async ({ page }) => {
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
    await expect(page.locator("#submit-button")).toHaveText("Calculate");
    await expect(page.locator("#live-note")).toContainText("Live update is on.");

    await expect(page.locator("#result-actions .result-primary-cta#cta-dumpster-call")).toBeVisible();

    await page.locator("#lead-zip").fill("10001");
    await page.locator("#lead-next").click();
    await page.locator("#lead-contact-method").selectOption("phone");
    await page.locator("#lead-contact-value").fill("(212) 555-1111");
    await page.locator("#cta-dumpster-call").click();
    await expect(page).toHaveURL(/\/about\/quote-match-beta/);

    await expect
      .poll(() => events.some((event) => event.eventName === "call_qualified"), { timeout: 10_000 })
      .toBeTruthy();
  });

  test("heavy concrete scenario surfaces constrained feasibility and fallback route", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");

    await selectChip(page, "persona", "contractor");
    await selectChip(page, "project-id", "concrete_removal");
    await selectChip(page, "material-id", "concrete");
    await selectChip(page, "unit-id", "sqft_4in");
    await page.locator("#quantity").fill("600");
    await page.locator("#allowance-tons").fill("2.0");

    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);
    await expect(page.locator("#submit-button")).toHaveText("Calculate");
    await expect(page.locator("#live-note")).toContainText("Live update is on.");

    const bannerText = (await page.locator("#result-state-banner").innerText()).toLowerCase();
    expect(
      bannerText.includes("likely needs multiple hauls") ||
        bannerText.includes("not recommended as a single load")
    ).toBeTruthy();

    const hardStop = page.locator("#result-hard-stops");
    if (await hardStop.isVisible()) {
      await expect(hardStop).toContainText("Hard");
    }
    await expect(page.locator("#result-actions .result-primary-cta#cta-junk")).toBeVisible();
  });
});

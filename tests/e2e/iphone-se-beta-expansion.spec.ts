import { devices, expect, test } from "@playwright/test";
import { captureEvents, waitForLiveEstimate } from "./helpers";

test.use({
  viewport: devices["iPhone SE"].viewport,
  userAgent: devices["iPhone SE"].userAgent,
  deviceScaleFactor: devices["iPhone SE"].deviceScaleFactor,
  isMobile: devices["iPhone SE"].isMobile,
  hasTouch: devices["iPhone SE"].hasTouch,
});

test.describe("iPhone SE beta expansion", () => {
  test("decision strip presets are all actionable on 320px and render scorecard", async ({ page }) => {
    const events = await captureEvents(page);
    await page.goto("/dumpster/size-weight-calculator");

    const stripModes = [
      { mode: "cheapest_route", label: "I want the cheapest route" },
      { mode: "easiest_option", label: "I want the easiest route" },
      { mode: "heavy_material", label: "I have heavy material" },
      { mode: "need_fast", label: "I need it gone fast" },
    ];

    for (const entry of stripModes) {
      await page.getByRole("link", { name: entry.label }).click();
      await expect(page).toHaveURL(/\/dumpster\/size-weight-calculator\?/);
      await page.getByRole("button", { name: "Calculate" }).click();
      await waitForLiveEstimate(page);
      await expect(page.locator("#mobile-result-dock")).toBeVisible();
      await expect(page.locator("#result-summary .decision-score-row")).toHaveCount(4);
    }

    await expect
      .poll(
        () =>
          events.filter(
            (event) =>
              event.eventName === "decision_mode_selected" && event.payload && event.payload.source === "entry_strip"
          ).length,
        { timeout: 10_000 }
      )
      .toBeGreaterThanOrEqual(4);
  });

  test("comparison hub remains touch-usable and returns to calculator on iPhone SE", async ({ page }) => {
    const events = await captureEvents(page);
    await page.goto("/dumpster/dumpster-vs-junk-removal-which-is-cheaper");

    await page.getByRole("button", { name: "Fastest completion" }).click();
    await page.getByRole("button", { name: "Heavy-load safety" }).click();
    await expect(page.locator("#priority-explainer")).toContainText("Current mode: Heavy-load safety");

    const firstCalculatorLink = page.locator("a[href*='/dumpster/size-weight-calculator']").first();
    await expect(firstCalculatorLink).toHaveAttribute("href", /priority=heavy/);
    await firstCalculatorLink.click();
    await expect(page).toHaveURL(/\/dumpster\/size-weight-calculator/);
    await waitForLiveEstimate(page);
    await expect(page.locator("#mobile-primary-cta")).toBeVisible();

    await expect
      .poll(
        () => events.some((event) => event.eventName === "comparison_page_exit_to_calculator"),
        { timeout: 10_000 }
      )
      .toBeTruthy();
  });
});

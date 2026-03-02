import { expect, test } from "@playwright/test";
import { waitForLiveEstimate } from "./helpers";

test.describe("Negative and validation coverage", () => {
  test("invalid ZIP is blocked in lead capture flow", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);

    await page.locator("#lead-zip").fill("123");
    await page.locator("#lead-next").click();
    await expect(page.locator("#lead-status")).toContainText("valid 5-digit ZIP");
    await expect(page.locator("#lead-step-1")).toBeVisible();
  });

  test("quantity below minimum blocks new calculation request", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    await waitForLiveEstimate(page);
    const originalShare = await page.locator("#share-link").getAttribute("href");

    await page.locator("#quantity").fill("0");
    await page.getByRole("button", { name: "Calculate" }).click();
    await page.waitForTimeout(800);

    const nextShare = await page.locator("#share-link").getAttribute("href");
    expect(nextShare).toBe(originalShare);
  });

  test("network delay shows loading state and recovers", async ({ page }) => {
    await page.route("**/api/estimates", async (route) => {
      await new Promise((resolve) => setTimeout(resolve, 1_800));
      await route.continue();
    });

    await page.goto("/dumpster/size-weight-calculator");
    await page.locator("#quantity").fill("9");
    await page.getByRole("button", { name: "Calculate" }).click();

    await expect(page.locator("#submit-button")).toHaveText("Calculating...");
    await expect(page.locator("#live-note")).toContainText("Calculating:");
    await waitForLiveEstimate(page);
    await expect(page.locator("#submit-button")).toHaveText("Calculate");
    await page.unroute("**/api/estimates");
  });

  test("network failure renders graceful retry signal", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    await waitForLiveEstimate(page);

    await page.route("**/api/estimates", async (route) => {
      await route.abort();
    });

    await page.locator("#quantity-inc").click();
    await expect(page.locator("#live-status")).toContainText("Live update failed", { timeout: 10_000 });

    await page.unroute("**/api/estimates");
    await page.locator("#quantity-inc").click();
    await waitForLiveEstimate(page);
  });
});

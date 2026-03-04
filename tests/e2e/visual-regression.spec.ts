import { devices, expect, test, type Page } from "@playwright/test";

async function assertNoHorizontalOverflow(page: Page) {
  const hasOverflow = await page.evaluate(() => {
    const viewportWidth = window.innerWidth;
    return document.documentElement.scrollWidth > viewportWidth + 1;
  });
  expect(hasOverflow).toBeFalsy();
}

async function waitForLiveEstimate(page: Page) {
  await expect(page.locator("#result-panel")).toBeVisible({ timeout: 20_000 });
  await expect(page.locator("#share-link")).toHaveAttribute("href", /\/dumpster\/estimate\/[a-zA-Z0-9-]+/, {
    timeout: 20_000,
  });
}

test.describe("Visual regression and responsive rendering", () => {
  test.describe("Desktop 1080p", () => {
    test.use({ viewport: { width: 1920, height: 1080 } });

    test("calculator shell baseline", async ({ page }) => {
      await page.route("**/js/calculator.js*", async (route) => route.abort());
      await page.goto("/dumpster/size-weight-calculator");
      await assertNoHorizontalOverflow(page);
      await expect(page).toHaveScreenshot("calculator-desktop-1080.png", {
        fullPage: true,
        animations: "disabled",
      });
    });

    test("material guide baseline", async ({ page }) => {
      await page.goto("/dumpster/weight/asphalt_shingles");
      await assertNoHorizontalOverflow(page);
      await expect(page).toHaveScreenshot("material-asphalt-desktop-1080.png", {
        fullPage: true,
        animations: "disabled",
      });
    });
  });

  test.describe("iPad Pro", () => {
    const iPadPro = devices["iPad Pro 11"];
    test.use({
      viewport: iPadPro.viewport,
      userAgent: iPadPro.userAgent,
      deviceScaleFactor: iPadPro.deviceScaleFactor,
      isMobile: iPadPro.isMobile,
      hasTouch: iPadPro.hasTouch,
    });

    test("project guides baseline", async ({ page }) => {
      await page.goto("/dumpster/project-guides");
      await assertNoHorizontalOverflow(page);
      await expect(page).toHaveScreenshot("project-guides-ipad-pro.png", {
        fullPage: true,
        animations: "disabled",
      });
    });
  });

  test.describe("iPhone 13", () => {
    const iphone13 = devices["iPhone 13"];
    test.use({
      viewport: iphone13.viewport,
      userAgent: iphone13.userAgent,
      deviceScaleFactor: iphone13.deviceScaleFactor,
      isMobile: iphone13.isMobile,
      hasTouch: iphone13.hasTouch,
    });

    test("calculator mobile baseline with result dock", async ({ page }) => {
      await page.goto("/dumpster/size-weight-calculator");
      await waitForLiveEstimate(page);
      await expect(page.locator("#floating-cta")).toBeHidden();
      await expect(page.locator("#mobile-result-dock")).toBeVisible();
      await assertNoHorizontalOverflow(page);
      await expect(page).toHaveScreenshot("calculator-iphone13.png", {
        fullPage: true,
        animations: "disabled",
        mask: [page.locator("#share-link"), page.locator("#live-status")],
      });
    });
  });
});

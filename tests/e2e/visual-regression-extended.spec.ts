import { devices, expect, test } from "@playwright/test";

const desktopPages = [
  { path: "/dumpster/project-guides", name: "project-guides-desktop-extended.png" },
  { path: "/dumpster/heavy-debris-rules", name: "heavy-rules-desktop-extended.png" },
  { path: "/dumpster/answers/roof-tear-off/shingles/size-guide", name: "intent-size-desktop-extended.png" },
  { path: "/dumpster/answers/concrete-removal/concrete/overage-risk", name: "intent-overage-desktop-extended.png" },
];

async function waitForLiveEstimate(page: import("@playwright/test").Page) {
  await expect(page.locator("#result-panel")).toBeVisible({ timeout: 20_000 });
  await expect(page.locator("#share-link")).toHaveAttribute("href", /\/dumpster\/estimate\/[a-zA-Z0-9-]+/, {
    timeout: 20_000,
  });
}

test.describe("Extended visual baselines", () => {
  test.describe("Desktop", () => {
    test.use({ viewport: { width: 1440, height: 900 } });

    for (const entry of desktopPages) {
      test(`snapshot ${entry.path}`, async ({ page }) => {
        await page.goto(entry.path);
        await expect(page).toHaveScreenshot(entry.name, {
          fullPage: true,
          animations: "disabled",
        });
      });
    }
  });

  test.describe("iPhone SE", () => {
    const iPhoneSE = devices["iPhone SE"];
    test.use({
      viewport: iPhoneSE.viewport,
      userAgent: iPhoneSE.userAgent,
      deviceScaleFactor: iPhoneSE.deviceScaleFactor,
      isMobile: iPhoneSE.isMobile,
      hasTouch: iPhoneSE.hasTouch,
    });

    test("calculator mobile shell", async ({ page }) => {
      await page.goto("/dumpster/size-weight-calculator");
      await waitForLiveEstimate(page);
      await expect(page.locator("#floating-cta")).toBeHidden();
      await expect(page.locator("#mobile-result-dock")).toBeVisible();
      await expect(page).toHaveScreenshot("calculator-iphone-se-extended.png", {
        fullPage: true,
        animations: "disabled",
        mask: [page.locator("#share-link"), page.locator("#live-status")],
      });
    });

    test("intent page mobile shell", async ({ page }) => {
      await page.goto("/dumpster/answers/roof-tear-off/shingles/weight-estimate");
      await expect(page).toHaveScreenshot("intent-weight-iphone-se-extended.png", {
        fullPage: true,
        animations: "disabled",
      });
    });
  });
});

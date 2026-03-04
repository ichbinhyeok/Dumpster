import { devices, expect, test, type BrowserContextOptions } from "@playwright/test";
import { assertNoHorizontalOverflow, assertTapTargetMinSize, waitForLiveEstimate } from "./helpers";

const pagePaths = [
  "/dumpster/size-weight-calculator",
  "/dumpster/material-guides",
  "/dumpster/project-guides",
  "/dumpster/weight/shingles",
  "/dumpster/size/roof-tear-off",
  "/dumpster/heavy-debris-rules",
  "/dumpster/answers/roof-tear-off/shingles/size-guide",
];

const viewports: Array<{ name: string; use: BrowserContextOptions }> = [
  { name: "desktop-1080p", use: { viewport: { width: 1920, height: 1080 } } },
  {
    name: "ipad-pro-11",
    use: {
      viewport: devices["iPad Pro 11"].viewport,
      userAgent: devices["iPad Pro 11"].userAgent,
      deviceScaleFactor: devices["iPad Pro 11"].deviceScaleFactor,
      isMobile: devices["iPad Pro 11"].isMobile,
      hasTouch: devices["iPad Pro 11"].hasTouch,
    },
  },
  {
    name: "iphone-13",
    use: {
      viewport: devices["iPhone 13"].viewport,
      userAgent: devices["iPhone 13"].userAgent,
      deviceScaleFactor: devices["iPhone 13"].deviceScaleFactor,
      isMobile: devices["iPhone 13"].isMobile,
      hasTouch: devices["iPhone 13"].hasTouch,
    },
  },
  {
    name: "iphone-se",
    use: {
      viewport: devices["iPhone SE"].viewport,
      userAgent: devices["iPhone SE"].userAgent,
      deviceScaleFactor: devices["iPhone SE"].deviceScaleFactor,
      isMobile: devices["iPhone SE"].isMobile,
      hasTouch: devices["iPhone SE"].hasTouch,
    },
  },
];

test.describe("Design quality matrix (layout / typography / tap target)", () => {
  for (const preset of viewports) {
    test.describe(`${preset.name}`, () => {
      test.use(preset.use);

      test("critical pages keep shell integrity and no horizontal overflow", async ({ page }) => {
        for (const path of pagePaths) {
          await page.goto(path);
          await expect(page.locator("header.site-header")).toBeVisible();
          await expect(page.locator("footer.site-footer")).toBeVisible();
          await expect(page.locator("h1")).toBeVisible();
          await assertNoHorizontalOverflow(page);
        }
      });

      test("interactive controls stay in viewport bounds", async ({ page }) => {
        await page.goto("/dumpster/size-weight-calculator");
        await waitForLiveEstimate(page);
        const viewportWidth = page.viewportSize()?.width ?? 390;
        const criticalSelectors = [
          "#quantity",
          "#quantity-inc",
          "#quantity-dec",
          "#allowance-tons",
          "#submit-button",
          "#lead-zip",
          "#lead-contact-method",
          "#lead-contact-value",
        ];

        for (const selector of criticalSelectors) {
          const locator = page.locator(selector);
          if ((await locator.count()) === 0 || !(await locator.isVisible())) {
            continue;
          }
          await locator.scrollIntoViewIfNeeded();
          const box = await locator.boundingBox();
          expect(box, `Missing element for selector: ${selector}`).not.toBeNull();
          expect((box?.left ?? 0) >= -1, `${selector} is clipped on the left side`).toBeTruthy();
          expect((box?.right ?? 0) <= viewportWidth + 1, `${selector} is clipped on the right side`).toBeTruthy();
        }
      });

      test("typography tokens are loaded (Manrope body + Space Grotesk heading)", async ({ page }) => {
        await page.goto("/dumpster/size-weight-calculator");
        const fonts = await page.evaluate(() => {
          const bodyFont = getComputedStyle(document.body).fontFamily;
          const h1 = document.querySelector("h1");
          const h1Font = h1 ? getComputedStyle(h1).fontFamily : "";
          return { bodyFont, h1Font };
        });

        expect(fonts.bodyFont.toLowerCase()).toContain("manrope");
        expect(fonts.h1Font.toLowerCase()).toContain("space grotesk");
      });

      test("mobile CTA tap targets meet 44px guideline", async ({ page }) => {
        test.skip(!preset.use.hasTouch, "Tap target checks are touch-device specific.");
        await page.goto("/dumpster/size-weight-calculator");
        await waitForLiveEstimate(page);
        await assertTapTargetMinSize(page, "#submit-button", 44);
        await assertTapTargetMinSize(page, "#quantity-inc", 44);
        await assertTapTargetMinSize(page, "#quantity-dec", 44);

        for (const selector of ["#floating-call", "#floating-quote"]) {
          const locator = page.locator(selector);
          if ((await locator.count()) === 0 || !(await locator.isVisible())) {
            continue;
          }
          await assertTapTargetMinSize(page, selector, 44);
        }
      });
    });
  }
});

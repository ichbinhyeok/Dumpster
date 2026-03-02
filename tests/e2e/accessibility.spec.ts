import AxeBuilder from "@axe-core/playwright";
import { expect, test } from "@playwright/test";

const auditPaths = [
  "/dumpster/size-weight-calculator",
  "/dumpster/material-guides",
  "/dumpster/project-guides",
  "/dumpster/weight/asphalt_shingles",
  "/dumpster/size/roof_tearoff",
  "/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide",
];

test.describe("Accessibility (WCAG / keyboard / ARIA) checks", () => {
  for (const path of auditPaths) {
    test(`axe scan: ${path}`, async ({ page }) => {
      await page.goto(path);
      const results = await new AxeBuilder({ page }).withTags(["wcag2a", "wcag2aa"]).analyze();

      const criticalViolations = results.violations.filter((violation) => violation.impact === "critical");
      const colorContrastViolations = results.violations.filter(
        (violation) => violation.id === "color-contrast"
      );

      expect(
        criticalViolations,
        `Critical violations on ${path}: ${JSON.stringify(criticalViolations, null, 2)}`
      ).toEqual([]);
      expect(
        colorContrastViolations,
        `Color contrast violations on ${path}: ${JSON.stringify(colorContrastViolations, null, 2)}`
      ).toEqual([]);
    });
  }

  test("keyboard-only navigation keeps visible focus indicator", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    await page.keyboard.press("Tab");
    await page.keyboard.press("Tab");
    await page.keyboard.press("Tab");

    const focused = page.locator(":focus");
    await expect(focused).toBeVisible();

    const focusStyles = await focused.evaluate((el) => {
      const style = getComputedStyle(el);
      return {
        outlineStyle: style.outlineStyle,
        outlineWidth: style.outlineWidth,
        boxShadow: style.boxShadow,
      };
    });

    const hasVisibleOutline =
      (focusStyles.outlineStyle && focusStyles.outlineStyle !== "none" && focusStyles.outlineWidth !== "0px") ||
      (focusStyles.boxShadow && focusStyles.boxShadow !== "none");
    expect(hasVisibleOutline).toBeTruthy();
  });
});

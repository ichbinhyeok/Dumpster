import { expect, test } from "@playwright/test";
import { sitemapLocs } from "./helpers";

const highIntentPaths = [
  "/dumpster/what-size-dumpster-do-i-need",
  "/dumpster/10-yard-dumpster-weight-limit-overage",
  "/dumpster/can-you-put-concrete-in-a-dumpster",
  "/dumpster/roof-shingles-dumpster-size-calculator",
  "/dumpster/pickup-truck-loads-to-dumpster-size",
  "/dumpster/dumpster-vs-junk-removal-which-is-cheaper",
  "/dumpster/size/deck-removal",
  "/dumpster/size/kitchen-remodel",
  "/dumpster/size/bathroom-remodel",
  "/dumpster/drywall-disposal-dumpster-rules",
  "/dumpster/answers/garage_cleanout/household_junk/size-guide",
  "/dumpster/answers/concrete_removal/concrete/weight-estimate",
  "/dumpster/answers/deck_demolition/decking_wood/size-guide",
  "/dumpster/answers/kitchen_remodel/mixed_cd/overage-risk",
];

test.describe("High-intent page pack", () => {
  test("money sitemap includes all curated high-intent pages", async ({ request }) => {
    const sitemap = await request.get("/sitemap-money.xml");
    expect(sitemap.ok()).toBeTruthy();
    const xml = await sitemap.text();
    const locs = sitemapLocs(xml);

    for (const path of highIntentPaths) {
      expect(locs).toContain(`http://127.0.0.1:4173${path}`);
    }
  });

  test("each high-intent page is crawlable and points to calculator CTA", async ({ page }) => {
    for (const path of highIntentPaths) {
      await page.goto(path);
      await expect(page.locator("h1")).toBeVisible();

      const canonical = await page.locator("link[rel='canonical']").getAttribute("href");
      expect(canonical).toBe(`http://127.0.0.1:4173${path}`);

      const robots = await page.locator("meta[name='robots']").getAttribute("content");
      expect((robots ?? "").toLowerCase()).not.toContain("noindex");

      await expect(page.locator("a[href*='/dumpster/size-weight-calculator']").first()).toBeVisible();
    }
  });
});

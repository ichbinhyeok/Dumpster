import { expect, test } from "@playwright/test";
import { sitemapLocs } from "./helpers";

function extractFirstMatch(input: string, pattern: RegExp): string | null {
  const match = input.match(pattern);
  return match ? match[1] : null;
}

test.describe("Mass intent-cluster coverage", () => {
  test("money sitemap includes only whitelisted intent pages", async ({ request }) => {
    const sitemap = await request.get("/sitemap-money.xml");
    expect(sitemap.ok()).toBeTruthy();
    const xml = await sitemap.text();
    const locs = sitemapLocs(xml);
    const intentLocs = locs.filter((loc) => loc.includes("/dumpster/answers/"));
    expect(intentLocs.length).toBe(19);
    expect(intentLocs).toContain("http://127.0.0.1:4173/dumpster/answers/roof_tearoff/asphalt_shingles/overage-risk");
    expect(intentLocs).toContain("http://127.0.0.1:4173/dumpster/answers/garage_cleanout/household_junk/size-guide");
    expect(intentLocs).not.toContain("http://127.0.0.1:4173/dumpster/answers/roof_tearoff/tile_ceramic/size-guide");
  });

  test("phase-one decision pages return 200 with direct-answer structure", async ({ request }) => {
    const locs = [
      "http://127.0.0.1:4173/dumpster/what-size-dumpster-do-i-need",
      "http://127.0.0.1:4173/dumpster/10-yard-dumpster-weight-limit-overage",
      "http://127.0.0.1:4173/dumpster/can-you-put-concrete-in-a-dumpster",
      "http://127.0.0.1:4173/dumpster/can-you-mix-concrete-and-wood-in-a-dumpster",
      "http://127.0.0.1:4173/dumpster/dumpster-vs-junk-removal-which-is-cheaper",
      "http://127.0.0.1:4173/dumpster/pickup-truck-loads-to-dumpster-size",
      "http://127.0.0.1:4173/dumpster/roof-shingles-dumpster-size-calculator",
      "http://127.0.0.1:4173/dumpster/drywall-disposal-dumpster-rules",
    ];
    const titles = new Set<string>();

    for (const loc of locs) {
      const response = await request.get(loc);
      expect(response.ok(), `Failed page: ${loc}`).toBeTruthy();
      const html = await response.text();

      const title = extractFirstMatch(html, /<title>(.*?)<\/title>/i);
      const canonical = extractFirstMatch(html, /<link rel="canonical" href="(.*?)">/i);

      expect(html).toContain("Direct answer:");
      expect(html).toContain("Decision matrix");
      expect(html).toContain("Common mistakes to avoid");
      expect(html).toContain("\"@type\": \"FAQPage\"");
      expect(html).toContain("\"@type\": \"BreadcrumbList\"");
      expect(title).toBeTruthy();
      expect(canonical).toBe(loc);

      if (title) {
        expect(titles.has(title), `Duplicate title: ${title}`).toBeFalsy();
        titles.add(title);
      }
    }
  });
});

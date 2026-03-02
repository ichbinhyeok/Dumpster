import { expect, test } from "@playwright/test";
import { sitemapLocs } from "./helpers";

function extractFirstMatch(input: string, pattern: RegExp): string | null {
  const match = input.match(pattern);
  return match ? match[1] : null;
}

test.describe("Mass intent-cluster coverage", () => {
  test("sitemap exposes broad intent-cluster surface", async ({ request }) => {
    const sitemap = await request.get("/sitemap.xml");
    expect(sitemap.ok()).toBeTruthy();
    const xml = await sitemap.text();
    const locs = sitemapLocs(xml);
    const intentLocs = locs.filter((loc) => loc.includes("/dumpster/answers/"));

    expect(intentLocs.length).toBeGreaterThanOrEqual(80);
    expect(intentLocs).toContain("http://127.0.0.1:4173/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide");
    expect(intentLocs).toContain("http://127.0.0.1:4173/dumpster/answers/concrete_removal/concrete/overage-risk");
  });

  test("all intent pages return 200 with direct-answer structure", async ({ request }) => {
    const sitemap = await request.get("/sitemap.xml");
    const locs = sitemapLocs(await sitemap.text()).filter((loc) => loc.includes("/dumpster/answers/"));
    const titles = new Set<string>();

    for (const loc of locs) {
      const response = await request.get(loc);
      expect(response.ok(), `Failed page: ${loc}`).toBeTruthy();
      const html = await response.text();

      const title = extractFirstMatch(html, /<title>(.*?)<\/title>/i);
      const canonical = extractFirstMatch(html, /<link rel="canonical" href="(.*?)">/i);

      expect(html).toContain("Direct answer:");
      expect(html).toContain("Size-by-size load comparison");
      expect(html).toContain("Decision checklist");
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

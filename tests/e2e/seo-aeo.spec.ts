import { expect, test } from "@playwright/test";
import { jsonLdTypes } from "./helpers";

type SeoExpectation = {
  path: string;
  expectedTitlePart: string;
  expectedType: string;
};

const pages: SeoExpectation[] = [
  {
    path: "/dumpster/size-weight-calculator",
    expectedTitlePart: "Dumpster Size & Weight Calculator",
    expectedType: "WebApplication",
  },
  {
    path: "/dumpster/material-guides",
    expectedTitlePart: "Material Weight Guides",
    expectedType: "CollectionPage",
  },
  {
    path: "/dumpster/project-guides",
    expectedTitlePart: "Project Guides",
    expectedType: "CollectionPage",
  },
  {
    path: "/dumpster/weight/shingles",
    expectedTitlePart: "Asphalt shingles",
    expectedType: "HowTo",
  },
  {
    path: "/dumpster/size/roof-tear-off",
    expectedTitlePart: "Roof Tear-off",
    expectedType: "HowTo",
  },
  {
    path: "/dumpster/heavy-debris-rules",
    expectedTitlePart: "Heavy Debris",
    expectedType: "FAQPage",
  },
  {
    path: "/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide",
    expectedTitlePart: "What size dumpster",
    expectedType: "FAQPage",
  },
];

test.describe("SEO / AEO / SERP metadata validation", () => {
  test("core SEO pages render unique title, description, canonical and OG image", async ({ page }) => {
    const seenTitles = new Set<string>();
    const seenCanonicals = new Set<string>();

    for (const entry of pages) {
      await page.goto(entry.path);
      await expect(page).toHaveTitle(new RegExp(entry.expectedTitlePart, "i"));

      const title = await page.title();
      const canonical = await page.locator("link[rel='canonical']").getAttribute("href");
      const description = await page.locator("meta[name='description']").getAttribute("content");
      const ogImage = await page.locator("meta[property='og:image']").getAttribute("content");
      const twitterCard = await page.locator("meta[name='twitter:card']").getAttribute("content");

      expect(title).not.toBe("");
      expect(description).toBeTruthy();
      expect((description ?? "").length).toBeGreaterThan(80);
      expect(canonical).toMatch(/^http:\/\/127\.0\.0\.1:4173\//);
      expect(ogImage).toMatch(/^http:\/\/127\.0\.0\.1:4173\/og-image\.png$/);
      expect(twitterCard).toBe("summary_large_image");

      expect(seenTitles.has(title)).toBeFalsy();
      expect(seenCanonicals.has(canonical!)).toBeFalsy();
      seenTitles.add(title);
      seenCanonicals.add(canonical!);
    }
  });

  test("JSON-LD schema blocks include expected entity types per page", async ({ page }) => {
    for (const entry of pages) {
      await page.goto(entry.path);
      const jsonLdBlocks = await page.locator("script[type='application/ld+json']").allTextContents();
      expect(jsonLdBlocks.length).toBeGreaterThan(0);
      const types = jsonLdTypes(jsonLdBlocks);
      expect(types).toContain(entry.expectedType);
    }
  });

  test("breadcrumb and FAQ schema are present on hub and intent pages", async ({ page }) => {
    const targets = [
      "/dumpster/material-guides",
      "/dumpster/project-guides",
      "/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide",
    ];

    for (const path of targets) {
      await page.goto(path);
      const blocks = await page.locator("script[type='application/ld+json']").allTextContents();
      const types = jsonLdTypes(blocks);
      expect(types).toContain("BreadcrumbList");
      expect(types).toContain("FAQPage");
    }
  });

  test("robots and sitemap expose crawlable phase-one assets and block combinatorial intent pages", async ({ request }) => {
    const robots = await request.get("/robots.txt");
    expect(robots.ok()).toBeTruthy();
    const robotsTxt = await robots.text();
    expect(robotsTxt).toContain("Disallow: /dumpster/answers/");
    expect(robotsTxt).toContain("Allow: /about/");
    expect(robotsTxt).toContain("Disallow: /api/");
    expect(robotsTxt).toContain("Sitemap:");

    const sitemap = await request.get("/sitemap.xml");
    expect(sitemap.ok()).toBeTruthy();
    const xml = await sitemap.text();
    expect(xml).toContain("/about/methodology");
    expect(xml).toContain("/dumpster/10-yard-dumpster-weight-limit-overage");
    expect(xml).toContain("/dumpster/weight/concrete");
    expect(xml).not.toContain("/dumpster/material-guides");
    expect(xml).not.toContain("/dumpster/project-guides");
    expect(xml).not.toContain("/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide");
    expect(xml).not.toContain("/dumpster/answers/concrete_removal/concrete/overage-risk");
  });

  test("intent page includes direct answer block, table, checklist and related links", async ({ page }) => {
    await page.goto("/dumpster/answers/roof_tearoff/asphalt_shingles/size-guide");
    await expect(page.locator(".answer-first")).toContainText("Direct answer:");
    await expect(page.getByRole("heading", { name: "Size-by-size load comparison" })).toBeVisible();
    await expect(page.locator("table.data-table tbody tr")).toHaveCount(5);
    await expect(page.getByRole("heading", { name: "Decision checklist" })).toBeVisible();
    await expect(page.getByRole("heading", { name: "Related intent guides" })).toBeVisible();
  });
});

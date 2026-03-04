import { expect, test } from "@playwright/test";
import { captureEvents, jsonLdTypes } from "./helpers";

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
    path: "/dumpster/answers/roof-tear-off/shingles/size-guide",
    expectedTitlePart: "Best dumpster size",
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
      "/dumpster/answers/roof-tear-off/shingles/size-guide",
    ];

    for (const path of targets) {
      await page.goto(path);
      const blocks = await page.locator("script[type='application/ld+json']").allTextContents();
      const types = jsonLdTypes(blocks);
      expect(types).toContain("BreadcrumbList");
      expect(types).toContain("FAQPage");
    }
  });

  test("robots and split sitemaps expose money assets with answer allowlist", async ({ request }) => {
    const robots = await request.get("/robots.txt");
    expect(robots.ok()).toBeTruthy();
    const robotsTxt = await robots.text();
    expect(robotsTxt).toContain("Allow: /dumpster/answers/");
    expect(robotsTxt).not.toContain("Disallow: /dumpster/answers/");
    expect(robotsTxt).toContain("Allow: /about/");
    expect(robotsTxt).toContain("Disallow: /api/");
    expect(robotsTxt).toContain("Sitemap: http://127.0.0.1:4173/sitemap.xml");
    expect(robotsTxt).toContain("Sitemap: http://127.0.0.1:4173/sitemap-core.xml");
    expect(robotsTxt).toContain("Sitemap: http://127.0.0.1:4173/sitemap-money.xml");
    expect(robotsTxt).toContain("Sitemap: http://127.0.0.1:4173/sitemap-experiments.xml");

    const sitemapIndex = await request.get("/sitemap.xml");
    expect(sitemapIndex.ok()).toBeTruthy();
    const indexXml = await sitemapIndex.text();
    expect(indexXml).toContain("/sitemap-core.xml");
    expect(indexXml).toContain("/sitemap-money.xml");
    expect(indexXml).toContain("/sitemap-experiments.xml");

    const coreSitemap = await request.get("/sitemap-core.xml");
    expect(coreSitemap.ok()).toBeTruthy();
    const coreXml = await coreSitemap.text();
    expect(coreXml).toContain("/about/methodology");
    expect(coreXml).toContain("/dumpster/size-weight-calculator");
    expect(coreXml).not.toContain("/dumpster/material-guides");

    const moneySitemap = await request.get("/sitemap-money.xml");
    expect(moneySitemap.ok()).toBeTruthy();
    const moneyXml = await moneySitemap.text();
    expect(moneyXml).toContain("/dumpster/10-yard-dumpster-weight-limit-overage");
    expect(moneyXml).toContain("/dumpster/weight/concrete");
    expect(moneyXml).toContain("/dumpster/answers/roof-tear-off/shingles/overage-risk");
    expect(moneyXml).toContain("/dumpster/answers/concrete-removal/concrete/size-guide");
    expect(moneyXml).not.toContain("/dumpster/answers/roof-tear-off/tile-ceramic/size-guide");

    const experimentsSitemap = await request.get("/sitemap-experiments.xml");
    expect(experimentsSitemap.ok()).toBeTruthy();
    const experimentsXml = await experimentsSitemap.text();
    expect(experimentsXml).toContain("/dumpster/material-guides");
    expect(experimentsXml).toContain("/dumpster/project-guides");
  });

  test("intent page includes decision blocks, table/checklist skeleton and next-step links", async ({ page }) => {
    await page.goto("/dumpster/answers/roof-tear-off/shingles/size-guide");
    await expect(page.locator(".answer-first")).toContainText("Direct answer:");
    await expect(page.getByRole("heading", { name: "Size-by-size load comparison" })).toBeVisible();
    await expect(page.locator("table.data-table tbody tr")).toHaveCount(5);
    await expect(page.getByRole("heading", { name: "Decision checklist" })).toBeVisible();
    await expect(page.getByRole("heading", { name: "Homeowner decision blocks" })).toBeVisible();
    await expect(page.locator(".homeowner-decision-blocks .decision-tile")).toHaveCount(8);
    await expect(page.getByRole("heading", { name: "Next decision steps" })).toBeVisible();
    await expect(page.getByRole("link", { name: /Compare dumpster vs junk/i })).toBeVisible();
    await expect(page.getByRole("heading", { name: "Related intent guides" })).toBeVisible();
  });

  test("material and project pages expose decision-stage next-step links", async ({ page }) => {
    await page.goto("/dumpster/weight/shingles");
    await expect(page.getByRole("heading", { name: "Next decision steps" })).toBeVisible();
    await expect(page.getByRole("link", { name: /Check overage-risk answer/i })).toBeVisible();
    await expect(page.getByRole("link", { name: /Compare dumpster vs junk/i })).toBeVisible();

    await page.goto("/dumpster/size/roof-tear-off");
    await expect(page.getByRole("heading", { name: "Next decision steps" })).toBeVisible();
    await expect(page.getByRole("link", { name: /Check overage-risk answer/i })).toBeVisible();
    await expect(page.getByRole("link", { name: /Check heavy-load rules/i })).toBeVisible();
  });

  test("guide hubs and heavy rules expose comparison hub as a first-class route", async ({ page }) => {
    await page.goto("/dumpster/material-guides");
    await expect(page.getByRole("link", { name: /Compare dumpster vs junk/i }).first()).toBeVisible();

    await page.goto("/dumpster/project-guides");
    await expect(page.getByRole("link", { name: /Compare dumpster vs junk/i }).first()).toBeVisible();

    await page.goto("/dumpster/heavy-debris-rules");
    await expect(page.getByRole("link", { name: /Compare dumpster vs junk/i })).toBeVisible();
  });

  test("decision-stage and comparison entry links emit analytics events", async ({ page }) => {
    const events = await captureEvents(page);

    await page.goto("/dumpster/weight/shingles");
    await page.getByRole("link", { name: "Check overage-risk answer" }).click();
    await expect(page).toHaveURL(/\/dumpster\/answers\/roof-tear-off\/shingles\/overage-risk/);

    await page.goto("/dumpster/project-guides");
    await page.getByRole("link", { name: /Compare dumpster vs junk/i }).first().click();
    await expect(page).toHaveURL(/\/dumpster\/dumpster-vs-junk-removal-which-is-cheaper/);

    await expect
      .poll(
        () =>
          events.some(
            (event) =>
              event.eventName === "decision_stage_link_click" &&
              String((event.payload as Record<string, unknown>).source || "") === "material_page"
          ),
        { timeout: 10_000 }
      )
      .toBeTruthy();

    await expect
      .poll(
        () =>
          events.some(
            (event) =>
              event.eventName === "comparison_hub_entry_click" &&
              String((event.payload as Record<string, unknown>).source || "") === "project_guides_hub"
          ),
        { timeout: 10_000 }
      )
      .toBeTruthy();
  });
});

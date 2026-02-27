import { devices, expect, test, type Page } from "@playwright/test";

async function waitForLiveEstimate(page: Page) {
  const resultPanel = page.locator("#result-panel");
  const shareLink = page.locator("#share-link");
  await expect(resultPanel).toBeVisible({ timeout: 15000 });
  await expect(shareLink).toHaveAttribute("href", /\/dumpster\/estimate\/[a-zA-Z0-9-]+/, {
    timeout: 15000,
  });
}

async function currentSharePath(page: Page): Promise<string> {
  const href = (await page.locator("#share-link").getAttribute("href")) || "";
  return /\/dumpster\/estimate\/[a-zA-Z0-9-]+/.test(href) ? href : "";
}

test.describe("Launch smoke - infrastructure and calculator", () => {
  test("health, robots, and sitemap endpoints are production-safe", async ({ request }) => {
    const healthResponse = await request.get("/api/health");
    expect(healthResponse.ok()).toBeTruthy();
    const healthBody = await healthResponse.json();
    expect(healthBody.status).toBe("ok");

    const robotsResponse = await request.get("/robots.txt");
    expect(robotsResponse.ok()).toBeTruthy();
    const robotsTxt = await robotsResponse.text();
    expect(robotsTxt).toContain("Allow: /dumpster/size-weight-calculator");
    expect(robotsTxt).toContain("Disallow: /dumpster/estimate/");
    expect(robotsTxt).toContain("Sitemap:");

    const sitemapResponse = await request.get("/sitemap.xml");
    expect(sitemapResponse.ok()).toBeTruthy();
    const sitemapXml = await sitemapResponse.text();
    expect(sitemapXml).toContain("/dumpster/material-guides");
    expect(sitemapXml).toContain("/dumpster/project-guides");
  });

  test("calculator live mode renders result, gauges, and no server errors", async ({ page }) => {
    const serverErrors: string[] = [];
    page.on("response", (response) => {
      if (response.status() >= 500) {
        serverErrors.push(`${response.status()} ${response.url()}`);
      }
    });

    await page.goto("/dumpster/size-weight-calculator");
    await waitForLiveEstimate(page);

    await expect(page.locator("#gauge-weight")).toHaveCSS("width", /.+/);
    await expect(page.locator("#gauge-volume")).toHaveCSS("width", /.+/);
    await expect(page.locator("#gauge-risk")).toHaveCSS("width", /.+/);
    await expect(page.locator("#live-status")).toContainText("Updated:");
    await expect(page.locator("#result-panel")).toContainText("Risk:");
    await expect(page.locator("#result-panel")).toContainText("Feasibility:");
    expect(serverErrors).toEqual([]);
  });

  test("unit compatibility toggles roof square availability by material", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    const roofSquareChip = page.locator("[data-choice-target='unit-id'] [data-choice-value='roof_square']");

    await page.locator("[data-choice-target='material-id'] [data-choice-value='concrete']").click();
    await expect(roofSquareChip).toBeDisabled();
    await expect(page.locator("#unit-id")).toHaveValue("pickup_load");

    await page.locator("[data-choice-target='material-id'] [data-choice-value='asphalt_shingles']").click();
    await expect(roofSquareChip).toBeEnabled();
    await roofSquareChip.click();
    await expect(page.locator("#unit-id")).toHaveValue("roof_square");
  });

  test("quantity stepper updates estimate id in live mode", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    await waitForLiveEstimate(page);

    const firstSharePath = await currentSharePath(page);
    await page.locator("#quantity-inc").click();

    await expect.poll(async () => currentSharePath(page), {
      timeout: 15000,
      message: "Expected a new estimate id after quantity change",
    }).not.toBe(firstSharePath);
  });

  test("share route and legal pages keep compliance metadata", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    await waitForLiveEstimate(page);
    const sharePath = await currentSharePath(page);
    expect(sharePath).toMatch(/\/dumpster\/estimate\/[a-zA-Z0-9-]+/);

    await page.goto(sharePath);
    await expect(page.locator("meta[name='robots']")).toHaveAttribute("content", "noindex,follow");
    await expect(page.locator("link[rel='canonical']")).toHaveAttribute(
      "href",
      "/dumpster/size-weight-calculator"
    );
    await expect(page.locator("footer.site-footer")).toContainText("Planning estimate only");

    await page.goto("/legal/terms.html");
    await expect(page.getByRole("heading", { name: "Terms of use" })).toBeVisible();
    await page.goto("/legal/privacy.html");
    await expect(page.getByRole("heading", { name: "Privacy notice" })).toBeVisible();
  });
});

test.describe("Launch smoke - SEO intent capture", () => {
  test("material guide widget forwards material and quantity into calculator", async ({ page }) => {
    await page.goto("/dumpster/weight/asphalt_shingles");
    await page.fill("#intent-qty-material", "9");
    await page.selectOption("#intent-unit-material", "pickup_load");
    await page.getByRole("button", { name: "Run live estimate" }).click();

    await expect(page).toHaveURL(/\/dumpster\/size-weight-calculator\?/);
    await expect(page.locator("#material-id")).toHaveValue("asphalt_shingles");
    await expect(page.locator("#unit-id")).toHaveValue("pickup_load");
    await expect(page.locator("#quantity")).toHaveValue("9");
    await waitForLiveEstimate(page);
  });

  test("project guide widget forwards persona and project preset", async ({ page }) => {
    await page.goto("/dumpster/size/roof_tearoff");
    await page.fill("#intent-qty-project", "5");
    await page.selectOption("#intent-persona-project", "property_manager");
    await page.getByRole("button", { name: "Launch live simulator" }).click();

    await expect(page).toHaveURL(/project=roof_tearoff/);
    await expect(page.locator("#project-id")).toHaveValue("roof_tearoff");
    await expect(page.locator("#material-id")).toHaveValue("asphalt_shingles");
    await expect(page.locator("#persona")).toHaveValue("property_manager");
    await expect(page.locator("#quantity")).toHaveValue("5");
    await waitForLiveEstimate(page);
  });

  test("heavy rules widget forwards heavy presets and keeps intent", async ({ page }) => {
    await page.goto("/dumpster/heavy-debris-rules");
    await page.fill("#intent-qty-heavy", "7");
    await page.selectOption("#intent-unit-heavy", "sqft_4in");
    await page.getByRole("button", { name: "Open live estimate" }).click();

    await expect(page).toHaveURL(/project=concrete_removal/);
    await expect(page).toHaveURL(/material=concrete/);
    await expect(page.locator("#project-id")).toHaveValue("concrete_removal");
    await expect(page.locator("#material-id")).toHaveValue("concrete");
    await expect(page.locator("#unit-id")).toHaveValue("sqft_4in");
    await expect(page.locator("#quantity")).toHaveValue("7");
    await waitForLiveEstimate(page);
  });

  test("SEO pages preserve header, footer, and JSON-LD blocks", async ({ page }) => {
    const seoPaths = [
      "/dumpster/material-guides",
      "/dumpster/project-guides",
      "/dumpster/weight/asphalt_shingles",
      "/dumpster/size/roof_tearoff",
      "/dumpster/heavy-debris-rules",
    ];

    for (const path of seoPaths) {
      await page.goto(path);
      await expect(page.locator("header.site-header")).toBeVisible();
      await expect(page.locator("footer.site-footer")).toBeVisible();
      const jsonLdCount = await page.locator("script[type='application/ld+json']").count();
      expect(jsonLdCount).toBeGreaterThan(0);
    }
  });
});

test.describe("Launch smoke - mobile viewport", () => {
  const iphone13 = devices["iPhone 13"];
  test.use({
    viewport: iphone13.viewport,
    userAgent: iphone13.userAgent,
    deviceScaleFactor: iphone13.deviceScaleFactor,
    isMobile: iphone13.isMobile,
    hasTouch: iphone13.hasTouch,
  });

  test("mobile shell keeps nav usability and floating CTA behavior", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    await expect(page.locator("header.site-header")).toBeVisible();
    await expect(page.locator("footer.site-footer")).toBeVisible();

    await waitForLiveEstimate(page);
    await expect(page.locator("#floating-cta")).toBeVisible();
    await expect(page.locator("#floating-call")).toBeVisible();
    await expect(page.locator("#floating-quote")).toBeVisible();

    const navOverflowX = await page.locator(".site-nav").evaluate((el) => getComputedStyle(el).overflowX);
    expect(navOverflowX === "auto" || navOverflowX === "scroll").toBeTruthy();
  });
});

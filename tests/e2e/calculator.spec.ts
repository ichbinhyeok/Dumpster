import { expect, test } from "@playwright/test";

test.describe("Dumpster calculator local e2e", () => {
  test("calculator shell renders header, footer, and live update", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");

    await expect(page.locator("header.site-header")).toBeVisible();
    await expect(page.locator("footer.site-footer")).toBeVisible();
    await expect(page.locator("text=Data updated:")).toHaveCount(0);

    const liveStatus = page.locator("#live-status");
    await expect(liveStatus).toContainText("Updated:", { timeout: 10000 });
  });

  test("quick input renders a decision result and share link", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");

    await expect(page.getByRole("heading", { name: "Dumpster Size & Weight Calculator" })).toBeVisible();
    await page.getByRole("button", { name: "Calculate" }).click();

    const resultPanel = page.locator("#result-panel");
    await expect(resultPanel).toBeVisible();
    await expect(resultPanel).toContainText("Risk:");
    await expect(resultPanel).toContainText("Feasibility:");

    const shareLink = page.locator("#share-link");
    await expect(shareLink).toHaveAttribute("href", /\/dumpster\/estimate\/[a-zA-Z0-9-]+/);
  });

  test("share estimate page is noindex with canonical to calculator", async ({ page }) => {
    await page.goto("/dumpster/size-weight-calculator");
    await page.getByRole("button", { name: "Calculate" }).click();
    const sharePath = await page.locator("#share-link").getAttribute("href");
    expect(sharePath).toBeTruthy();

    const response = await page.goto(sharePath!);
    expect(response).toBeTruthy();

    await expect(page.locator("meta[name='robots']")).toHaveAttribute("content", "noindex,follow");
    await expect(page.locator("link[rel='canonical']")).toHaveAttribute(
      "href",
      "/dumpster/size-weight-calculator"
    );
  });

  test("split sitemap endpoints expose core and money pages", async ({ request }) => {
    const indexResponse = await request.get("/sitemap.xml");
    expect(indexResponse.ok()).toBeTruthy();
    const indexXml = await indexResponse.text();
    expect(indexXml).toContain("/sitemap-core.xml");
    expect(indexXml).toContain("/sitemap-money.xml");
    expect(indexXml).toContain("/sitemap-experiments.xml");

    const coreResponse = await request.get("/sitemap-core.xml");
    expect(coreResponse.ok()).toBeTruthy();
    const coreXml = await coreResponse.text();
    expect(coreXml).toContain("/dumpster/size-weight-calculator");
    expect(coreXml).toContain("/dumpster/heavy-debris-rules");
    expect(coreXml).toContain("/about/methodology");

    const moneyResponse = await request.get("/sitemap-money.xml");
    expect(moneyResponse.ok()).toBeTruthy();
    const moneyXml = await moneyResponse.text();
    expect(moneyXml).toContain("/dumpster/weight/shingles");
    expect(moneyXml).toContain("/dumpster/10-yard-dumpster-weight-limit-overage");
    expect(moneyXml).toContain("/dumpster/dumpster-vs-junk-removal-which-is-cheaper");
    expect(moneyXml).toContain("/dumpster/answers/roof-tear-off/shingles/overage-risk");
  });
});

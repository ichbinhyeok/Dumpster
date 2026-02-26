import { expect, test } from "@playwright/test";

test.describe("Dumpster calculator local e2e", () => {
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
    const headers = response!.headers();
    expect(headers["x-robots-tag"]).toContain("noindex");

    await expect(page.locator("meta[name='robots']")).toHaveAttribute("content", "noindex,follow");
    await expect(page.locator("link[rel='canonical']")).toHaveAttribute(
      "href",
      "/dumpster/size-weight-calculator"
    );
  });

  test("sitemap exposes calculator and guide hubs", async ({ request }) => {
    const response = await request.get("/sitemap.xml");
    expect(response.ok()).toBeTruthy();
    const xml = await response.text();
    expect(xml).toContain("/dumpster/size-weight-calculator");
    expect(xml).toContain("/dumpster/material-guides");
    expect(xml).toContain("/dumpster/project-guides");
    expect(xml).toContain("/dumpster/weight/asphalt_shingles");
    expect(xml).toContain("/dumpster/size/light_commercial_fitout");
  });
});

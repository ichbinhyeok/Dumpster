import { expect, test } from "@playwright/test";
import { waitForLiveEstimate } from "./helpers";

test.describe("Post-deploy critical smoke suite", () => {
  test("critical endpoints are up and return expected status", async ({ request }) => {
    const health = await request.get("/api/health");
    expect(health.ok()).toBeTruthy();

    const home = await request.get("/");
    expect(home.ok()).toBeTruthy();

    const calculator = await request.get("/dumpster/size-weight-calculator");
    expect(calculator.ok()).toBeTruthy();

    const materialHub = await request.get("/dumpster/material-guides");
    expect(materialHub.ok()).toBeTruthy();
  });

  test("calculator loads with functional primary actions and no 500 responses", async ({ page }) => {
    const serverErrors: string[] = [];
    page.on("response", (response) => {
      if (response.status() >= 500) {
        serverErrors.push(`${response.status()} ${response.url()}`);
      }
    });

    await page.goto("/dumpster/size-weight-calculator");
    await expect(page.getByRole("button", { name: "Calculate" })).toBeVisible();
    await expect(page.locator("header.site-header").getByRole("link", { name: "Run live estimate" })).toBeVisible();
    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);

    await expect(page.locator("#result-actions")).toContainText("Get dumpster quotes");
    await expect(page.locator("#result-actions")).toContainText("Run the live estimate");
    await expect(page.locator("#result-actions")).toContainText("Compare junk removal");
    await expect(page.locator("#result-actions")).toContainText("Check heavy-load rules first");
    await expect(page.locator("#result-summary")).toContainText("Decision scorecard");
    await expect(page.locator("#result-summary .decision-score-row")).toHaveCount(4);
    expect(serverErrors).toEqual([]);
  });

  test("SEO entry pages render without error and keep header/footer shells", async ({ page }) => {
    const paths = [
      "/dumpster/material-guides",
      "/dumpster/project-guides",
      "/dumpster/weight/shingles",
      "/dumpster/size/roof-tear-off",
      "/dumpster/heavy-debris-rules",
      "/dumpster/dumpster-vs-junk-removal-which-is-cheaper",
    ];

    for (const path of paths) {
      await page.goto(path);
      await expect(page.locator("header.site-header")).toBeVisible();
      await expect(page.locator("footer.site-footer")).toBeVisible();
      await expect(page.locator("h1")).toBeVisible();
    }
  });

  test("comparison hub priority toggles update scenario ordering", async ({ page }) => {
    await page.goto("/dumpster/dumpster-vs-junk-removal-which-is-cheaper");
    const topPlanTitle = page.locator("#comparison-plan-grid .comparison-plan-card.is-priority-top h3");

    await expect(topPlanTitle).toContainText("Remodel Standard");

    await page.getByRole("button", { name: "Fastest completion" }).click();
    await expect(topPlanTitle).toContainText("Starter Cleanout");

    await page.getByRole("button", { name: "Heavy-load safety" }).click();
    await expect(topPlanTitle).toContainText("Heavy High-Risk");
  });
});

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
    await expect(page.getByRole("link", { name: "Request quote" })).toBeVisible();
    await page.getByRole("button", { name: "Calculate" }).click();
    await waitForLiveEstimate(page);

    await expect(page.locator("#result-actions")).toContainText("Contact for quote");
    await expect(page.locator("#result-actions")).toContainText("Request online quote");
    await expect(page.locator("#result-actions")).toContainText("Compare junk removal");
    expect(serverErrors).toEqual([]);
  });

  test("SEO entry pages render without error and keep header/footer shells", async ({ page }) => {
    const paths = [
      "/dumpster/material-guides",
      "/dumpster/project-guides",
      "/dumpster/weight/shingles",
      "/dumpster/size/roof-tear-off",
      "/dumpster/heavy-debris-rules",
      "/dumpster/dumpster-vs-junk-removal",
    ];

    for (const path of paths) {
      await page.goto(path);
      await expect(page.locator("header.site-header")).toBeVisible();
      await expect(page.locator("footer.site-footer")).toBeVisible();
      await expect(page.locator("h1")).toBeVisible();
    }
  });
});

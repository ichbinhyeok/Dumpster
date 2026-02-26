import { defineConfig, devices } from "@playwright/test";

export default defineConfig({
  testDir: "./tests/e2e",
  timeout: 45_000,
  fullyParallel: false,
  retries: 0,
  workers: 1,
  reporter: [["list"], ["html", { open: "never" }]],
  use: {
    baseURL: "http://127.0.0.1:4173",
    trace: "retain-on-failure",
    screenshot: "only-on-failure",
    video: "retain-on-failure",
  },
  webServer: {
    command: "gradlew.bat bootRun --args=\"--server.port=4173 --app.base-url=http://127.0.0.1:4173\"",
    url: "http://127.0.0.1:4173/api/health",
    timeout: 120_000,
    reuseExistingServer: true,
    env: {
      SPRING_DATASOURCE_URL:
        "jdbc:h2:file:./data/dumpster_playwright;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
    },
  },
  projects: [
    {
      name: "chromium",
      use: { ...devices["Desktop Chrome"] },
    },
  ],
});

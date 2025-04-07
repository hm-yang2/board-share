import { test, expect } from "@playwright/test";

test.describe.serial("homepage-content-tests", () => {
  test("hompage-content-tests-setup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("button", { name: "Create channel" }).click();
    await page.getByRole("textbox", { name: "Name" }).click();
    await page.getByRole("textbox", { name: "Name" }).fill("Test Channel 7");
    await page.getByRole("button", { name: "Create Channel" }).click();
    await page
      .getByRole("list")
      .getByRole("button", { name: "Add Post" })
      .click();
    await page.getByRole("textbox", { name: "Enter title here..." }).click();
    await page
      .getByRole("textbox", { name: "Enter title here..." })
      .fill("Test Post");
    await page.getByRole("textbox", { name: "Enter link here..." }).click();
    await page
      .getByRole("textbox", { name: "Enter link here..." })
      .fill("https://www.example.com");
    await page.getByRole("button", { name: "Post", exact: true }).click();
  });

  test("homepage-content-visible", async ({ page }) => {
    await page.goto("http://localhost:5173/login");
    await page.waitForTimeout(10000);
    await expect(
      page.getByRole("heading", { name: "Global OPS Dashboard" }),
    ).toBeVisible();
    await expect(page.getByRole("button", { name: "Add Post" })).toBeVisible();
    await expect(page.getByRole("button", { name: "Profile" })).toBeVisible();
    await expect(
      page.getByRole("textbox", { name: "Search channels" }),
    ).toBeVisible();
    await expect(
      page.locator("div").filter({ hasText: "Test Channel 7" }).first(),
    ).toBeVisible();
  });

  test("homepage-search-shows-test-channel", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.getByRole("textbox", { name: "Search channels" }).fill("test");
    await expect(page.locator("#root")).toMatchAriaSnapshot(`
    - heading "Explore Channels" [level=3]
    - button "Create channel":
      - paragraph: Create channel
    - text: "/Test Channel 7/"
    - paragraph: No channel description
    `);
  });

  test("homepage-search-shows-no-channel-then-all-channels", async ({
    page,
  }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page
      .getByRole("textbox", { name: "Search channels" })
      .fill("ihwefoh");
    await expect(page.locator("#root")).toMatchAriaSnapshot(`
    - heading "Explore Channels" [level=3]
    - button "Create channel":
      - paragraph: Create channel
    `);
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.getByRole("textbox", { name: "Search channels" }).fill("");
    await expect(
      page.locator("div").filter({ hasText: "Test Channel 7" }).first(),
    ).toBeVisible();
  });

  test("homepage-tests-cleanup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.locator("a").filter({ hasText: "Test Channel 7" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await page.getByRole("button", { name: "Delete Channel" }).click();
  });
});

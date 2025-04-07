import { test, expect } from "@playwright/test";

test.describe.serial("channel-settings-tests", () => {
  test("channel-settings-tests-setup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("button", { name: "Create channel" }).click();
    await page.getByRole("textbox", { name: "Name" }).click();
    await page.getByRole("textbox", { name: "Name" }).fill("Test Channel 2");
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

  test("channel-settings-page-content-present", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.waitForTimeout(10000);
    await page.locator("a").filter({ hasText: "Test Channel 2" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await expect(
      page.getByRole("heading", { name: "Channel Settings" }),
    ).toBeVisible();
    await expect(page.locator("#root input")).toBeVisible();
    await expect(
      page
        .locator("div")
        .filter({ hasText: /^Channel Description$/ })
        .getByRole("textbox"),
    ).toBeVisible();
    await expect(page.getByText("VisibilityMake PublicMake")).toBeVisible();
    await expect(
      page.getByRole("button", { name: "Delete Channel" }),
    ).toBeVisible();
  });

  test("channel-settings-edit-worked", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.locator("a").filter({ hasText: "Test Channel 2" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await page.locator("#root input").click();
    await page.locator("#root input").fill("Edited Test Channel 2");
    await page.getByRole("button", { name: "Make Private" }).click();
    await expect(page.getByText("ConfirmCancel")).toBeVisible();
    await page.getByRole("button", { name: "Confirm" }).click();
    await expect(page.getByText("PRIVATE", { exact: true })).toBeVisible();
    await expect(page.getByText("Edited Test Channel 2")).toBeVisible();
  });

  test("channel-settings-tests-cleanup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.locator("a").filter({ hasText: "Test Channel 2" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await page.getByRole("button", { name: "Delete Channel" }).click();
  });
});

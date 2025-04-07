import { test, expect } from "@playwright/test";

test.describe.serial("channel-sidebar-tests", () => {
  test("channel-sidebar-tests-setup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("button", { name: "Create channel" }).click();
    await page.getByRole("textbox", { name: "Name" }).click();
    await page.getByRole("textbox", { name: "Name" }).fill("Test Channel 3");
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

  test("channel-sidebar-works", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 3" }).click();
    await expect(page.getByRole("button", { name: "Home" })).toBeVisible();
    await expect(page.getByRole("button", { name: "Posts" })).toBeVisible();
    await expect(
      page.getByRole("list").getByRole("button", { name: "Add Post" }),
    ).toBeVisible();
    await expect(page.getByRole("button", { name: "Users" })).toBeVisible();
    await expect(page.getByRole("button", { name: "Settings" })).toBeVisible();
    await page.getByRole("button", { name: "Home" }).click();
    await page.getByRole("button", { name: "Users" }).click();
    await expect(
      page.getByRole("heading", { name: "Channel Users" }),
    ).toBeVisible();
    await page.getByRole("button", { name: "Settings" }).click();
    await expect(
      page.getByRole("heading", { name: "Channel Settings" }),
    ).toBeVisible();
    await page.getByRole("button", { name: "Posts" }).click();
    await expect(page.getByRole("heading", { name: "Posts" })).toBeVisible();
    await page.locator(".MuiDrawer-backdrop").click();
    await page
      .getByRole("list")
      .getByRole("button", { name: "Add Post" })
      .click();
    await expect(
      page.getByRole("heading", { name: "Create post" }),
    ).toBeVisible();
    await expect(
      page.getByRole("button", { name: "Selected Channel: Test Channel 3" }),
    ).toBeVisible();
  });

  test("channel-sidebar-tests-cleanup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.waitForTimeout(10000);
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.locator("a").filter({ hasText: "Test Channel 3" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await page.getByRole("button", { name: "Delete Channel" }).click();
  });
});

import { test, expect } from "@playwright/test";

test.describe.serial("channel-homepage-tests", () => {
  test("channel-homepage-tests-setup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("button", { name: "Create channel" }).click();
    await page.getByRole("textbox", { name: "Name" }).click();
    await page.getByRole("textbox", { name: "Name" }).fill("Test Channel 1");
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

  test("channel-homepage-items-present", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 1" }).click();
    await expect(
      page.getByRole("heading", { name: "Test Channel 1" }),
    ).toBeVisible();
    await expect(
      page.locator("div:nth-child(2) > div > div:nth-child(2) > div").first(),
    ).toBeVisible();
    await expect(
      page.getByRole("link").filter({ hasText: "Test Post" }),
    ).toBeVisible();
    await expect(
      page.getByText("Test Channel 1PUBLICHomePostsAdd PostUsersSettings"),
    ).toBeVisible();
  });

  test("channel-homepage-change-view", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 1" }).click();
    await expect(
      page.getByRole("link").filter({ hasText: "Test post" }),
    ).toBeVisible();
    await expect(
      page.getByRole("button").filter({ hasText: /^$/ }).first(),
    ).toBeVisible();
    await page.getByRole("button").filter({ hasText: /^$/ }).first().click();
    await page.getByText("Compact View").click();
    await expect(page.getByRole("link", { name: "Test post" })).toBeVisible();
    await expect(page.locator("button").nth(3)).toBeVisible();
    await page.locator("button").nth(2).click();
    await page.getByText("Card View").click();
    await expect(
      page.getByRole("link").filter({ hasText: "Test post" }),
    ).toBeVisible();
  });

  test("channel-homepage-searchbar", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 1" }).click();
    await expect(
      page.locator("div:nth-child(2) > div > div:nth-child(2)").first(),
    ).toBeVisible();
    await page.getByRole("textbox", { name: "Search posts..." }).click();
    await page
      .getByRole("textbox", { name: "Search posts..." })
      .fill("fdhasio");
    await expect(page.getByText("NO POSTS")).toBeVisible();
  });

  test("homepage-cards-clickable", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 1" }).click();
    await page
      .getByRole("link")
      .filter({ hasText: "Test Post" })
      .getByRole("button")
      .nth(1)
      .click();
    await expect(
      page.locator("div").filter({ hasText: "Test post" }).nth(3),
    ).toBeVisible();
  });

  test("channel-homepage-tests-cleanup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.locator("a").filter({ hasText: "Test Channel 1" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await page.getByRole("button", { name: "Delete Channel" }).click();
  });
});

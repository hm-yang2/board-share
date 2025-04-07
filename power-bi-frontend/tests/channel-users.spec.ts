import { test, expect } from "@playwright/test";
test.describe.serial("channel-users-tests", () => {
  test("channel-users-tests-setup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("button", { name: "Create channel" }).click();
    await page.getByRole("textbox", { name: "Name" }).click();
    await page.getByRole("textbox", { name: "Name" }).fill("Test Channel 4");
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
  test("channel-users-page-content-present", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 4" }).click();
    await page.getByRole("button", { name: "Users" }).click();
    await expect(
      page.getByRole("heading", { name: "Channel Users" }),
    ).toBeVisible();
    await expect(page.getByRole("button", { name: "Add User" })).toBeVisible();
    await expect(page.getByRole("heading", { name: "Owners" })).toBeVisible();
    await expect(page.getByRole("heading", { name: "Admins" })).toBeVisible();
    await expect(page.getByRole("heading", { name: "Members" })).toBeVisible();
  });

  test("add-and-delete-user-works", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 4" }).click();
    await page.getByRole("button", { name: "Users" }).click();
    await page.getByRole("button", { name: "Add User" }).click();
    await expect(page.getByRole("dialog")).toBeVisible();
    await expect(
      page.getByRole("heading", { name: "Add User to Channel" }),
    ).toBeVisible();
    await expect(
      page.getByRole("textbox", { name: "Search users..." }),
    ).toBeVisible();
    await page
      .locator("div")
      .filter({ hasText: /^bob@gmail\.com$/ })
      .click();
    await expect(
      page.getByRole("heading", { name: "Assign Role to bob@gmail.com" }),
    ).toBeVisible();
    await expect(page.getByText("MemberAdminOwner")).toBeVisible();
    await page.getByRole("button", { name: "Member" }).click();
    await expect(
      page
        .locator("div")
        .filter({ hasText: /^bob@gmail\.com/ })
        .first(),
    ).toBeVisible();
    await page
      .locator("div")
      .filter({ hasText: /^Membersbob@gmail\.comJoined on 4\/4\/2025MEMBER$/ })
      .getByRole("button")
      .click();
    await expect(page.getByRole("heading", { name: "Members" })).toBeVisible();
  });

  test("channel-users-tests-cleanup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.locator("a").filter({ hasText: "Test Channel 4" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await page.getByRole("button", { name: "Delete Channel" }).click();
  });
});

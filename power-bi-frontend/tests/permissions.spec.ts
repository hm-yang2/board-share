import { test, expect } from "@playwright/test";

test.describe.serial("Permission Flow", () => {
  test("owner-channel-view-users-and-settings", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Owner Channel" }).click();
    await expect(page.getByRole("button", { name: "Users" })).toBeVisible();
    await expect(page.getByRole("button", { name: "Settings" })).toBeVisible();
    await page.getByRole("button", { name: "Users" }).click();
    await expect(page.getByRole("heading", { name: "Owners" })).toBeVisible();
    await page.getByRole("button", { name: "Settings" }).click();
    await expect(
      page.getByRole("heading", { name: "Channel Settings" }),
    ).toBeVisible();
  });

  test("admin-channel-no-settings", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Admin Channel" }).click();
    await expect(page.getByRole("button", { name: "Users" })).toBeVisible();
    await page.getByRole("button", { name: "Users" }).click();
    await expect(
      page.getByRole("heading", { name: "Channel Users" }),
    ).toBeVisible();
    await expect(page.getByRole("heading", { name: "Admins" })).toBeVisible();
    await expect(page.getByRole("button", { name: "Settings" })).toBeHidden();
  });

  test("member-channel-no-users", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Member Channel" }).click();
    await expect(page.getByRole("button", { name: "Users" })).toBeHidden();
    await expect(page.getByRole("button", { name: "Settings" })).toBeHidden();
  });
});

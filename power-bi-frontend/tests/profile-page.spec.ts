import { test, expect } from "@playwright/test";

test("profile-page-content-present", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.waitForTimeout(10000);
  await page.getByRole("button", { name: "Profile" }).click();
  await expect(page.getByRole("tab", { name: "Posts" })).toBeVisible();
  await expect(page.getByRole("tab", { name: "Channels" })).toBeVisible();
  await page.getByRole("tab", { name: "Channels" }).click();
  await page.getByRole("tab", { name: "Posts" }).click();
});

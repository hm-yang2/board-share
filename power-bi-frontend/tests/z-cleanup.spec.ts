import { test, expect } from "@playwright/test";

// test("Delete posts", async ({ page }) => {
//   test.slow();
//   await page.goto("http://localhost:5173/");
//   await page.waitForTimeout(10000);
//   await page.goto("http://localhost:5173/profile");
//   const posts = await page
//     .locator("div")
//     .filter({ hasText: "Test Post" })
//     .all();

//   for (let i = 0; i < posts.length; i++) {
//     const deleteButton = page.locator(".MuiIconButton-root").first(); // Find the button near the post link
//     await deleteButton.click();
//     await page.getByRole("menuitem", { name: "Delete" }).click();
//     await page.getByRole("button", { name: "Delete" }).click();
//     setTimeout(() => {}, 3000); // Wait for the post to be deleted
//   }
// });

import { test, expect } from "@playwright/test";

test("open Create Channel dialog and verify inputs", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.waitForTimeout(20000);
  await expect(
    page.getByRole("button", { name: "Create channel" }),
  ).toBeVisible();
  await page.getByRole("button", { name: "Create channel" }).click();
  await expect(
    page.getByRole("dialog", { name: "Create new Channel" }),
  ).toBeVisible();
  await expect(page.getByRole("textbox", { name: "Name" })).toBeVisible();
  await expect(
    page.getByRole("button", { name: "Channel visible to all users" }),
  ).toBeVisible();
  await expect(
    page.getByRole("button", { name: "Channel only visible to" }),
  ).toBeVisible();
  await expect(
    page.getByRole("textbox", { name: "Description" }),
  ).toBeVisible();
  await expect(
    page.getByRole("button", { name: "Create Channel" }),
  ).toBeVisible();
});

test.describe.serial("Create Channel Flow", () => {
  test("Create Channel works", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("textbox", { name: "Search channels" }).click();
    await page.getByRole("button", { name: "Create channel" }).click();
    await page.getByRole("textbox", { name: "Name" }).click();
    await page.getByRole("textbox", { name: "Name" }).fill("Test Channel 5");
    await page
      .getByRole("button", { name: "Channel visible to all users" })
      .click();
    await page.getByRole("textbox", { name: "Description" }).click();
    await page
      .getByRole("textbox", { name: "Description" })
      .fill("Description");
    await page.getByRole("button", { name: "Create Channel" }).click();
    await expect(
      page.getByRole("heading", { name: "Test Channel 5" }),
    ).toBeVisible();
  });

  test("clean up channel", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 5" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await page.getByRole("button", { name: "Delete Channel" }).click();
  });
});

test("Create channel fails with lack of name", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Create channel" }).click();
  await page.getByRole("dialog", { name: "Create new Channel" }).click();
  await page.getByRole("button", { name: "Create Channel" }).click();
  await expect(page.getByRole("textbox", { name: "Name" })).toBeEmpty();
  await expect(page.getByLabel("Create new Channel")).toMatchAriaSnapshot(`
    - dialog "Create new Channel":
      - button
      - heading "Create new Channel" [level=2]
      - text: A name and description help people understand what your channel is all about.
      - textbox "Name"
      - group:
        - button "Channel visible to all users"
        - button "Channel only visible to members"
      - textbox "Description"
      - button "Create Channel"
    `);
});

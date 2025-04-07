import { test, expect } from "@playwright/test";

test("add-post-button-works", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.waitForTimeout(10000);
  await expect(
    page.getByRole("heading", { name: "Explore Channels" }),
  ).toBeVisible();
  await expect(page.getByRole("button", { name: "Add Post" })).toBeVisible();
  await page.getByRole("button", { name: "Add Post" }).click();
  await expect(
    page.getByRole("heading", { name: "Create post" }),
  ).toBeVisible();
  await expect(
    page.getByRole("button", { name: "Select Channel" }),
  ).toBeVisible();
  await expect(
    page.getByRole("textbox", { name: "Enter title here..." }),
  ).toBeVisible();
  await expect(
    page.getByRole("textbox", { name: "Enter link here..." }),
  ).toBeVisible();
  await expect(
    page.getByRole("button", { name: "Post", exact: true }),
  ).toBeVisible();
  await expect(page.getByTestId("HelpOutlineOutlinedIcon")).toBeVisible();
  await expect(page.getByTestId("InfoOutlinedIcon")).toBeVisible();
});

test.describe.serial("successful-add-post-with-channel", () => {
  test("setup-channel", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.getByRole("button", { name: "Create channel" }).click();
    await page.getByRole("textbox", { name: "Name" }).click();
    await page.getByRole("textbox", { name: "Name" }).fill("Test Channel 6");
    await page.getByRole("button", { name: "Create Channel" }).click();
  });

  test("add-post-with-channel", async ({ page }) => {
    await page.goto("http://localhost:5173/login");
    await page.waitForTimeout(10000);
    await expect(
      page.getByRole("heading", { name: "Explore Channels" }),
    ).toBeVisible();
    await page.getByRole("button", { name: "Add Post" }).click();
    await page.getByRole("button", { name: "Select Channel" }).click();
    await page.getByRole("button", { name: "Test Channel 6" }).click();
    await expect(
      page.getByRole("button", { name: "Selected Channel: Test Channel 6" }),
    ).toBeVisible();
    await page.getByRole("textbox", { name: "Enter title here..." }).click();
    await page
      .getByRole("textbox", { name: "Enter title here..." })
      .fill("Test post");
    await page.getByRole("textbox", { name: "Enter link here..." }).click();
    await page
      .getByRole("textbox", { name: "Enter link here..." })
      .fill("https://www.example.com");
    await page.getByRole("button", { name: "Post", exact: true }).click();
    await expect(
      page
        .locator('iframe[title="Test post"]')
        .contentFrame()
        .getByRole("heading", { name: "Example Domain" }),
    ).toBeVisible();
    await expect(
      page.getByRole("heading", { name: "Test post" }),
    ).toBeVisible();
    await expect(page.getByText("Test Channel 6")).toBeVisible();
  });

  test("add-post-with-channel-cleanup", async ({ page }) => {
    await page.goto("http://localhost:5173/");
    await page.locator("a").filter({ hasText: "Test Channel 6" }).click();
    await page.getByRole("button", { name: "Settings" }).click();
    await page.getByRole("button", { name: "Delete Channel" }).click();
  });
});

test("add-post-no-channel", async ({ page }) => {
  await page.goto("http://localhost:5173/login");
  await page.waitForTimeout(10000);
  await page.goto("http://localhost:5173/create-post");
  await page.getByRole("textbox", { name: "Enter title here..." }).click();
  await page
    .getByRole("textbox", { name: "Enter title here..." })
    .fill("Test post");
  await page.getByRole("textbox", { name: "Enter link here..." }).click();
  await page
    .getByRole("textbox", { name: "Enter link here..." })
    .fill("https://www.example.com");
  await page.getByRole("button", { name: "Post", exact: true }).click();
  await expect(
    page
      .locator('iframe[title="Test post"]')
      .contentFrame()
      .getByRole("heading", { name: "Example Domain" }),
  ).toBeVisible();
  await expect(page.getByRole("heading", { name: "Test post" })).toBeVisible();
});

test("fail-add-post-no-link", async ({ page }) => {
  await page.goto("http://localhost:5173/login");
  await page.waitForTimeout(10000);
  await page.goto("http://localhost:5173/create-post");
  await page.getByRole("button", { name: "Post", exact: true }).click();
  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Unable to create post\. Invalid link$/ }),
  ).toBeVisible();
});

test("fail-add-post-invalid-url", async ({ page }) => {
  await page.goto("http://localhost:5173/login");
  await page.waitForTimeout(10000);
  await page.goto("http://localhost:5173/create-post");
  await page.getByRole("textbox", { name: "Enter title here..." }).click();
  await page
    .getByRole("textbox", { name: "Enter title here..." })
    .fill("test post");
  await page.getByRole("textbox", { name: "Enter link here..." }).click();
  await page
    .getByRole("textbox", { name: "Enter link here..." })
    .fill("www.example.com");
  await page.getByRole("button", { name: "Post", exact: true }).click();
  await expect(page.getByText("Unable to create post.")).toBeVisible();
});

import { axiosInstance } from "./common";

/**
 * Redirects the user to the Azure login page.
 * @returns A promise that resolves to false if the redirection fails.
 */
export async function AzureLogin() {
  try {
    const response = await axiosInstance.get("/api/auth/login");
    const { url } = response.data;
    window.location.href = url;
  } catch (error) {
    console.error(error);
    return false;
  }
}

/**
 * Exchanges an authorization code for JWT tokens.
 * @param code The authorization code received from Azure.
 * @returns A promise that resolves to true if the tokens are successfully obtained, or false otherwise.
 */
export async function GetJWTTokens(code: string) {
  try {
    await axiosInstance.post("/api/auth/login", { code: code });
    return true;
  } catch (error) {
    console.error(error);
    return false;
  }
}

/**
 * Checks if the user is authenticated.
 * @returns A promise that resolves to true if the user is authenticated, or false otherwise.
 */
export async function CheckAuthentication() {
  try {
    await axiosInstance.get("/api/auth/check");
    return true;
  } catch (error) {
    console.error(error);
    return false;
  }
}

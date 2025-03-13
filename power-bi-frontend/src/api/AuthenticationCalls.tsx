import { axiosInstance } from "./common";

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

export async function GetJWTTokens(code: string) {
  try {
    await axiosInstance.post("/api/auth/login", { code: code });
    return true;
  } catch (error) {
    console.error(error);
    return false;
  }
}

export async function CheckAuthentication() {
  try {
    await axiosInstance.get("/api/auth/check");
    return true;
  } catch (error) {
    console.error(error);
    return false;
  }
}

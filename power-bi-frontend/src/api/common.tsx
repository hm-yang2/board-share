import axios from "axios";

/**
 * API Constants
 */
export const BASE_API_ENDPOINT = import.meta.env.VITE_BASE_API_URL;

axios.defaults.withCredentials = true;
export const axiosInstance = axios.create({
  baseURL: BASE_API_ENDPOINT,
});

import axios from "axios";
import {
  clearAuthData,
  getToken,
} from "../utils/storage";

/*
 * Axios instance-ka isku xiraya
 * React frontend-ka iyo Spring Boot backend-ka.
 *
 * Backend URL-ka waxaa laga akhrinayaa .env.
 */
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,

  headers: {
    "Content-Type": "application/json",
  },
});

/*
 * Request kasta JWT token-ka ku dar
 * marka user-ku login sameeyay.
 */
apiClient.interceptors.request.use(
  (config) => {
    const token = getToken();

    if (token) {
      config.headers.Authorization =
        `Bearer ${token}`;
    }

    return config;
  },

  (error) => {
    return Promise.reject(error);
  }
);

/*
 * Maaree errors-ka kasoo baxa backend-ka.
 */
apiClient.interceptors.response.use(
  (response) => response,

  (error) => {
    const status = error.response?.status;
    const data = error.response?.data;

    let message =
      "Something went wrong. Please try again.";

    /*
     * Backend-ka lama gaari karo.
     */
    if (!error.response) {
      message =
        "Cannot connect to the backend server.";

      return Promise.reject({
        message,
        status: 0,
      });
    }

    /*
     * Backend-ku haddii message soo celiyo.
     */
    if (data?.message) {
      message = data.message;
    } else if (data?.errors) {
      message = Object.values(
        data.errors
      ).join(" ");
    } else if (typeof data === "string") {
      message = data;
    }

    /*
     * Form validation error.
     */
    if (
      status === 400 &&
      !data?.message &&
      !data?.errors
    ) {
      message =
        "Please check the form and try again.";
    }

    /*
     * Token maqan ama dhacay.
     */
    if (status === 401) {
      clearAuthData();

      message =
        "Your session has expired. Please login again.";
    }

    /*
     * User-ku permission ma haysto.
     */
    if (status === 403) {
      message =
        "You do not have permission to perform this action.";
    }

    /*
     * Record lama helin.
     */
    if (status === 404) {
      message =
        "The requested record was not found.";
    }

    /*
     * Backend server error.
     */
    if (status >= 500) {
      message =
        "The server had a problem. Please try again later.";
    }

    return Promise.reject({
      message,
      status,
    });
  }
);

export default apiClient;
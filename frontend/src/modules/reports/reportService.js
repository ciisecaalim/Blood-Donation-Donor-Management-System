import apiClient from "../../api/apiClient";

export function getCompleteSystemReport() {
  return apiClient.get("/reports/system").then((response) => response.data);
}

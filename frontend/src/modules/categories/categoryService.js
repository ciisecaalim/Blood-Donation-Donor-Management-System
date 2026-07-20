import apiClient from "../../api/apiClient";

export function getAllCategories() {
  return apiClient.get("/categories").then((response) => response.data);
}

export function getActiveCategories() {
  return apiClient.get("/categories/active").then((response) => response.data);
}

export function createCategory(data) {
  return apiClient.post("/categories", data).then((response) => response.data);
}

export function updateCategory(id, data) {
  return apiClient.put(`/categories/${id}`, data).then((response) => response.data);
}

export function updateCategoryStatus(id, status) {
  return apiClient.patch(`/categories/${id}/status`, status).then((response) => response.data);
}

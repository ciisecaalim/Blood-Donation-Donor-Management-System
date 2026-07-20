import apiClient from "../../api/apiClient";

export function getInventory() {
  return apiClient.get("/inventory").then((response) => response.data);
}

export function getInventoryById(id) {
  return apiClient.get(`/inventory/${id}`).then((response) => response.data);
}

export function createInventory(data) {
  return apiClient.post("/inventory", data).then((response) => response.data);
}

export function updateInventory(id, data) {
  return apiClient.put(`/inventory/${id}`, data).then((response) => response.data);
}

export function deleteInventory(id) {
  return apiClient.delete(`/inventory/${id}`).then((response) => response.data);
}

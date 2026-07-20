export function saveAuthData(authData) {
  localStorage.setItem("token", authData.token || "");
  localStorage.setItem("role", authData.role || "");
  localStorage.setItem(
    "user",
    JSON.stringify({
      id: authData.userId,
      fullName: authData.fullName,
      email: authData.email,
      role: authData.role,
      status: authData.status,
    })
  );
}

export function getToken() {
  return localStorage.getItem("token");
}

export function getRole() {
  return localStorage.getItem("role");
}

export function getUser() {
  const user = localStorage.getItem("user");
  return user ? JSON.parse(user) : null;
}

export function clearAuthData() {
  localStorage.removeItem("token");
  localStorage.removeItem("role");
  localStorage.removeItem("user");
}

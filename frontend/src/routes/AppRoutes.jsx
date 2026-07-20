import { Navigate, Route, Routes } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import UnauthorizedPage from "../pages/UnauthorizedPage";
import NotFoundPage from "../pages/NotFoundPage";
import LoginPage from "../modules/auth/LoginPage";
import RegisterPage from "../modules/auth/RegisterPage";
import DashboardPage from "../modules/dashboard/DashboardPage";
import UsersPage from "../modules/users/UsersPage";
import CategoriesPage from "../modules/categories/CategoriesPage";
import DonorsPage from "../modules/donors/DonorsPage";
import DonationsPage from "../modules/donations/DonationsPage";
import InventoryPage from "../modules/inventory/InventoryPage";
import AnnouncementsPage from "../modules/announcements/AnnouncementsPage";
import ReportsPage from "../modules/reports/ReportsPage";
import ProfilePage from "../pages/ProfilePage";
import ProtectedRoute from "./ProtectedRoute";

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/unauthorized" element={<UnauthorizedPage />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/donors" element={<DonorsPage />} />
          <Route path="/donations" element={<DonationsPage />} />
          <Route path="/inventory" element={<InventoryPage />} />
          <Route path="/announcements" element={<AnnouncementsPage />} />
          <Route path="/profile" element={<ProfilePage />} />
        </Route>
      </Route>
      <Route element={<ProtectedRoute allowedRoles={["ROLE_ADMIN", "ROLE_STAFF"]} />}>
        <Route element={<AppLayout />}>
          <Route path="/categories" element={<CategoriesPage />} />
        </Route>
      </Route>
      <Route element={<ProtectedRoute allowedRoles={["ROLE_ADMIN"]} />}>
        <Route element={<AppLayout />}>
          <Route path="/users" element={<UsersPage />} />
          <Route path="/reports" element={<ReportsPage />} />
        </Route>
      </Route>
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}

export default AppRoutes;

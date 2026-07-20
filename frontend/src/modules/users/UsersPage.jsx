import { useEffect, useReducer, useState } from "react";
import StandardModulePage from "../../shared/StandardModulePage";
import { apiReducer, initialApiState } from "../../reducers/apiReducer";
import { includesText, matchesValue } from "../../utils/filterHelpers";
import { getAllUsers } from "./userService";

const emptyFilters = { fullName: "", email: "", role: "", status: "" };

function UsersPage() {
  const [state, dispatch] = useReducer(apiReducer, initialApiState);
  const [filters, setFilters] = useState(emptyFilters);

  async function loadUsers() {
    dispatch({ type: "FETCH_START" });
    try {
      const data = await getAllUsers();
      dispatch({ type: "FETCH_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  useEffect(() => {
    loadUsers();
  }, []);

  function handleFilterChange(name, value) {
    setFilters({ ...filters, [name]: value });
  }

  const filteredUsers = state.data.filter((user) =>
    includesText(user.fullName, filters.fullName) &&
    includesText(user.email, filters.email) &&
    matchesValue(user.role, filters.role) &&
    matchesValue(user.status, filters.status)
  );

  const summaryItems = [
    { title: "Total Users", value: state.data.length },
    { title: "Active Users", value: state.data.filter((item) => item.status === "ACTIVE").length },
    { title: "Inactive Users", value: state.data.filter((item) => item.status === "INACTIVE").length },
    { title: "Blocked Users", value: state.data.filter((item) => item.status === "BLOCKED").length },
  ];

  return (
    <StandardModulePage
      title="Users"
      description="View registered system users and account status."
      summaryItems={summaryItems}
      filters={[
        { name: "fullName", label: "Search by full name" },
        { name: "email", label: "Search by email" },
        { name: "role", label: "Role", type: "select", options: ["ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER"] },
        { name: "status", label: "User status", type: "select", options: ["ACTIVE", "INACTIVE", "BLOCKED"] },
      ]}
      filterValues={filters}
      onFilterChange={handleFilterChange}
      onClearFilters={() => setFilters(emptyFilters)}
      columns={[
        { key: "fullName", label: "Full Name" },
        { key: "email", label: "Email" },
        { key: "role", label: "Role", type: "status" },
        { key: "status", label: "Status", type: "status" },
      ]}
      records={filteredUsers}
      loading={state.loading}
      error={state.error}
      emptyMessage="No users match the selected filters."
    />
  );
}

export default UsersPage;

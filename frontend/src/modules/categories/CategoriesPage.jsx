import { useEffect, useReducer, useState } from "react";
import PageHeader from "../../components/common/PageHeader";
import SummaryGrid from "../../components/common/SummaryGrid";
import FilterBar from "../../components/common/FilterBar";
import ErrorMessage from "../../components/common/ErrorMessage";
import FormModal from "../../components/common/FormModal";
import LoadingState from "../../components/common/LoadingState";
import DataTable from "../../components/common/DataTable";
import { apiReducer, initialApiState } from "../../reducers/apiReducer";
import { getRole } from "../../utils/storage";
import { includesText, matchesValue, formatDate } from "../../utils/filterHelpers";
import { createCategory, getAllCategories, updateCategory, updateCategoryStatus } from "./categoryService";

const emptyFilters = { bloodGroup: "", status: "" };
const emptyForm = { bloodGroup: "", description: "" };
const bloodGroups = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];

function CategoriesPage() {
  const role = getRole();
  const [state, dispatch] = useReducer(apiReducer, initialApiState);
  const [filters, setFilters] = useState(emptyFilters);
  const [formData, setFormData] = useState(emptyForm);
  const [selected, setSelected] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState("");

  async function loadCategories() {
    dispatch({ type: "FETCH_START" });
    try {
      const data = await getAllCategories();
      dispatch({ type: "FETCH_SUCCESS", payload: data });
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  useEffect(() => {
    loadCategories();
  }, []);

  function openCreate() {
    setSelected(null);
    setFormData(emptyForm);
    setFormError("");
    setModalOpen(true);
  }

  function openEdit(record) {
    setSelected(record);
    setFormData({ bloodGroup: record.bloodGroup || "", description: record.description || "" });
    setFormError("");
    setModalOpen(true);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setFormError("");

    try {
      if (selected) {
        await updateCategory(selected.id, formData);
      } else {
        await createCategory(formData);
      }
      setModalOpen(false);
      loadCategories();
    } catch (error) {
      setFormError(error.message);
    } finally {
      setSaving(false);
    }
  }

  async function handleStatus(record) {
    const nextStatus = record.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    try {
      await updateCategoryStatus(record.id, nextStatus);
      loadCategories();
    } catch (error) {
      dispatch({ type: "FETCH_ERROR", payload: error.message });
    }
  }

  const filteredCategories = state.data.filter(
    (item) => includesText(item.bloodGroup, filters.bloodGroup) && matchesValue(item.status, filters.status)
  );

  const summaryItems = [
    { title: "Total Categories", value: state.data.length },
    { title: "Active Categories", value: state.data.filter((item) => item.status === "ACTIVE").length },
    { title: "Inactive Categories", value: state.data.filter((item) => item.status === "INACTIVE").length },
    { title: "Total Blood Groups", value: bloodGroups.length },
  ];

  if (state.loading) {
    return <LoadingState message="Loading blood categories..." />;
  }

  return (
    <div className="space-y-6">
      <PageHeader
        title="Blood Categories"
        description="Manage blood groups used across donors, inventory, and announcements."
        action={
          role === "ROLE_ADMIN" ? (
            <button className="btn-primary" type="button" onClick={openCreate}>
              Add Category
            </button>
          ) : null
        }
      />

      <SummaryGrid items={summaryItems} />

      <FilterBar
        filters={[
          { name: "bloodGroup", label: "Search by blood group" },
          { name: "status", label: "Category status", type: "select", options: ["ACTIVE", "INACTIVE"] },
        ]}
        values={filters}
        onChange={(name, value) => setFilters({ ...filters, [name]: value })}
        onClear={() => setFilters(emptyFilters)}
        resultsCount={filteredCategories.length}
      />

      <ErrorMessage message={state.error} />

      <DataTable
        columns={[
          { key: "bloodGroup", label: "Blood Group" },
          { key: "description", label: "Description" },
          { key: "status", label: "Status", type: "status" },
          { key: "createdAt", label: "Created", render: (row) => formatDate(row.createdAt) },
        ]}
        rows={filteredCategories}
        loading={false}
        emptyMessage="No blood categories match the selected filters."
        rowActions={
          role === "ROLE_ADMIN"
            ? [
                { label: "Edit", onClick: openEdit },
                { label: "Change Status", onClick: handleStatus },
              ]
            : []
        }
      />

      <p className="text-sm text-gray-500 font-medium">Showing {filteredCategories.length} category(ies)</p>

      {modalOpen && (
        <FormModal
          title={selected ? "Edit Category" : "Add Category"}
          open={modalOpen}
          error={formError}
          saving={saving}
          submitLabel={selected ? "Update Category" : "Create Category"}
          onSubmit={handleSubmit}
          onClose={() => setModalOpen(false)}
        >
          <label className="block text-sm font-medium text-gray-600">
            Blood group
            <select
              className="form-input mt-1"
              value={formData.bloodGroup}
              onChange={(event) => setFormData({ ...formData, bloodGroup: event.target.value })}
              required
            >
              <option value="">Select group</option>
              {bloodGroups.map((item) => (
                <option key={item} value={item}>
                  {item}
                </option>
              ))}
            </select>
          </label>
          <label className="block text-sm font-medium text-gray-600 mt-4">
            Description
            <textarea
              className="form-input mt-1"
              value={formData.description}
              onChange={(event) => setFormData({ ...formData, description: event.target.value })}
              rows="3"
              placeholder="Provide a brief description of the use cases and significance of this blood group."
            />
          </label>
        </FormModal>
      )}
    </div>
  );
}

export default CategoriesPage;

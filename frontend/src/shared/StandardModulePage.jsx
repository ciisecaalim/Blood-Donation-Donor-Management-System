import ConfirmModal from "../components/common/ConfirmModal";
import DataTable from "../components/common/DataTable";
import ErrorMessage from "../components/common/ErrorMessage";
import FilterBar from "../components/common/FilterBar";
import FormModal from "../components/common/FormModal";
import PageHeader from "../components/common/PageHeader";
import SummaryGrid from "../components/common/SummaryGrid";

function StandardModulePage({
  title,
  description,
  summaryItems,
  filters,
  filterValues,
  onFilterChange,
  onClearFilters,
  columns,
  records,
  loading,
  error,
  primaryAction,
  rowActions,
  emptyMessage,
  modal,
  deleteModal,
}) {
  return (
    <div className="space-y-6">
      <PageHeader
        title={title}
        description={description}
        action={primaryAction ? <button className="btn-primary" type="button" onClick={primaryAction.onClick}>{primaryAction.label}</button> : null}
      />
      <SummaryGrid items={summaryItems} />
      <FilterBar filters={filters} values={filterValues} onChange={onFilterChange} onClear={onClearFilters} resultsCount={records.length} />
      <ErrorMessage message={error} />
      <DataTable columns={columns} rows={records} loading={loading} emptyMessage={emptyMessage} rowActions={rowActions} />
      <p className="text-sm text-gray-500">Showing {records.length} result(s)</p>
      {modal && (
        <FormModal
          title={modal.title}
          open={modal.open}
          error={modal.error}
          saving={modal.saving}
          submitLabel={modal.submitLabel}
          onSubmit={modal.onSubmit}
          onClose={modal.onClose}
        >
          {modal.children}
        </FormModal>
      )}
      {deleteModal && (
        <ConfirmModal
          open={deleteModal.open}
          title={deleteModal.title}
          message={deleteModal.message}
          loading={deleteModal.loading}
          onCancel={deleteModal.onCancel}
          onConfirm={deleteModal.onConfirm}
        />
      )}
    </div>
  );
}

export default StandardModulePage;

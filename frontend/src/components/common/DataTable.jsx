import EmptyState from "./EmptyState";
import LoadingState from "./LoadingState";
import StatusBadge from "./StatusBadge";

function DataTable({ columns, rows, loading, emptyMessage, rowActions }) {
  if (loading) {
    return <LoadingState />;
  }

  if (!rows.length) {
    return <EmptyState message={emptyMessage} />;
  }

  return (
    <div className="overflow-hidden rounded-lg border border-gray-200 bg-white shadow-sm">
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200 text-left text-sm">
          <thead className="bg-gray-50">
            <tr>
              {columns.map((column) => (
                <th key={column.key} className="whitespace-nowrap px-4 py-3 font-semibold text-gray-600">
                  {column.label}
                </th>
              ))}
              {rowActions?.length > 0 && (
                <th className="whitespace-nowrap px-4 py-3 font-semibold text-gray-600">Actions</th>
              )}
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {rows.map((row) => (
              <tr key={row.id} className="transition-all duration-200 hover:bg-red-50/40">
                {columns.map((column) => (
                  <td key={column.key} className="whitespace-nowrap px-4 py-3 text-gray-700">
                    {column.type === "status" ? <StatusBadge value={row[column.key]} /> : column.render ? column.render(row) : row[column.key] || "-"}
                  </td>
                ))}
                {rowActions?.length > 0 && (
                  <td className="whitespace-nowrap px-4 py-3">
                    <div className="flex gap-2">
                      {rowActions.map((action) =>
                        action.show && !action.show(row) ? null : (
                          <button key={action.label} className="table-action" type="button" onClick={() => action.onClick(row)}>
                            {action.label}
                          </button>
                        )
                      )}
                    </div>
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default DataTable;

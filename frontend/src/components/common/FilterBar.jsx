function FilterBar({ filters, values, onChange, onClear, resultsCount }) {
  return (
    <div className="content-card">
      <div className="grid grid-cols-1 gap-3 md:grid-cols-2 xl:grid-cols-4">
        {filters.map((filter) => (
          <label key={filter.name} className="text-sm font-medium text-gray-600">
            {filter.label}
            {filter.type === "select" ? (
              <select
                className="form-input mt-1"
                value={values[filter.name] || ""}
                onChange={(event) => onChange(filter.name, event.target.value)}
              >
                <option value="">All</option>
                {filter.options.map((option) => (
                  <option key={option} value={option}>
                    {option.replaceAll("_", " ")}
                  </option>
                ))}
              </select>
            ) : (
              <input
                className="form-input mt-1"
                type={filter.type || "text"}
                value={values[filter.name] || ""}
                placeholder={filter.placeholder || ""}
                onChange={(event) => onChange(filter.name, event.target.value)}
              />
            )}
          </label>
        ))}
      </div>
      <div className="mt-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <p className="text-sm text-gray-500">Current results: {resultsCount}</p>
        <button className="btn-secondary" type="button" onClick={onClear}>
          Clear Filters
        </button>
      </div>
    </div>
  );
}

export default FilterBar;

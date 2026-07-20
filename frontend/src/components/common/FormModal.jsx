import ErrorMessage from "./ErrorMessage";

function FormModal({ title, open, children, error, saving, submitLabel, onSubmit, onClose }) {
  if (!open) {
    return null;
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-ink/60 p-4">
      <div className="w-full max-w-2xl rounded-lg bg-white p-5 shadow-xl transition-all duration-200">
        <div className="mb-4 flex items-center justify-between gap-4">
          <h2 className="text-lg font-bold text-ink">{title}</h2>
          <button className="btn-secondary px-3 py-1" type="button" onClick={onClose}>
            Close
          </button>
        </div>
        <ErrorMessage message={error} />
        <form className="mt-4 space-y-4" onSubmit={onSubmit}>
          {children}
          <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <button className="btn-secondary" type="button" onClick={onClose} disabled={saving}>
              Cancel
            </button>
            <button className="btn-primary" type="submit" disabled={saving}>
              {saving ? "Saving..." : submitLabel}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default FormModal;

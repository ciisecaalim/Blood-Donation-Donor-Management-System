function EmptyState({ message = "No records found." }) {
  return (
    <div className="rounded-lg border border-dashed border-gray-300 bg-white p-8 text-center text-sm text-gray-500">
      {message}
    </div>
  );
}

export default EmptyState;

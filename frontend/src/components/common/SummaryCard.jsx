function SummaryCard({ title, value, note }) {
  return (
    <div className="content-card transition-all duration-200 hover:-translate-y-0.5 hover:shadow-md">
      <p className="text-sm font-medium text-gray-500">{title}</p>
      <p className="mt-3 text-3xl font-bold text-ink">{value}</p>
      {note && <p className="mt-2 text-xs text-gray-500">{note}</p>}
    </div>
  );
}

export default SummaryCard;

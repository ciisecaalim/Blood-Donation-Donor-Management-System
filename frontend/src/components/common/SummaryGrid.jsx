import SummaryCard from "./SummaryCard";

function SummaryGrid({ items }) {
  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
      {items.map((item) => (
        <SummaryCard key={item.title} title={item.title} value={item.value} note={item.note} />
      ))}
    </div>
  );
}

export default SummaryGrid;

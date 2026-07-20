function LoadingState({ message = "Loading data..." }) {
  return (
    <div className="content-card text-center text-sm text-gray-500">
      {message}
    </div>
  );
}

export default LoadingState;

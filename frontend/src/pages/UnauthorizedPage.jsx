import { Link } from "react-router-dom";

function UnauthorizedPage() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-gray-100 px-4">
      <section className="content-card max-w-md text-center">
        <h1 className="text-2xl font-bold text-ink">Unauthorized</h1>
        <p className="mt-2 text-sm text-gray-500">You do not have permission to open this page.</p>
        <Link className="btn-primary mt-5 inline-flex" to="/dashboard">Go to Dashboard</Link>
      </section>
    </main>
  );
}

export default UnauthorizedPage;

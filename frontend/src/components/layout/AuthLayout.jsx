function AuthLayout({ title, subtitle, children }) {
  return (
    <main className="flex min-h-screen items-center justify-center bg-gray-100 px-4 py-8">
      <section className="w-full max-w-md rounded-lg border border-gray-200 bg-white p-6 shadow-sm">
        <div className="mb-6 text-center">
          <p className="text-sm font-bold uppercase tracking-wide text-blood">Blood Donation System</p>
          <h1 className="mt-2 text-2xl font-bold text-ink">{title}</h1>
          <p className="mt-2 text-sm text-gray-500">{subtitle}</p>
        </div>
        {children}
      </section>
    </main>
  );
}

export default AuthLayout;

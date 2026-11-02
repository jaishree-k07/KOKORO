const API = {
  token: () => localStorage.getItem("kokoroToken"),
  async call(method, url, body) {
    const headers = { "Content-Type": "application/json" };
    if (API.token()) headers["Authorization"] = "Bearer " + API.token();
    const res = await fetch(url, { method, headers, body: body ? JSON.stringify(body) : undefined });
    if (res.status === 401 && !url.startsWith("/api/auth/")) { logout(); throw new Error("Please log in"); }
    const data = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(data.error || "Something went wrong");
    return data;
  },
  get: (u) => API.call("GET", u),
  post: (u, b) => API.call("POST", u, b),
  put: (u, b) => API.call("PUT", u, b),
  del: (u) => API.call("DELETE", u)
};
function requireLogin() { if (!API.token()) location.href = "login.html"; }
function logout() { localStorage.removeItem("kokoroToken"); location.href = "login.html"; }

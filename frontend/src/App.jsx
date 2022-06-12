import "./App.css";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import { Routes, Route, Link } from "react-router-dom";
import HelloWorld from "./pages/HelloWorld";
import { useState } from "react";

function App() {
  const [user, setUser] = useState(null);

  return (
    <div>
      <Routes>
        <Route path="/" element={<Login user={user} setUser={setUser} />} />
        <Route
          path="/login"
          element={<Login user={user} setUser={setUser} />}
        />
        <Route
          path="/dashboard"
          element={<Dashboard user={user} setUser={setUser} />}
        />
      </Routes>
    </div>
  );
}

export default App;

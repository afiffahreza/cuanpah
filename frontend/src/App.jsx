import "./App.css";
import Login from "./pages/Login";
import { Routes, Route, Link } from "react-router-dom";
import HelloWorld from "./pages/HelloWorld";
import { useState } from "react";

function App() {
  const [user, setUser] = useState(null);
  const test = () => {
    console.log("click");
  };

  return (
    <div>
      <Routes>
        <Route path="/" element={<HelloWorld />} />
        <Route
          path="/login"
          element={<Login user={user} setUser={setUser} test={test} />}
        />
      </Routes>
    </div>
  );
}

export default App;

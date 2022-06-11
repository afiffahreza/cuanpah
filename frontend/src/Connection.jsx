import axios from "axios";

export default axios.create({
  baseURL: "https://cuanpah-backend-5xzqacjzkq-et.a.run.app",
  headers: {
    "Content-type": "application/json",
  },
});

const user = require("./services/users");

module.exports = function (app) {
  app.get("/", (req, res) => res.send("Try: /status"));
  app.get("/status", (req, res) => res.send("Success."));
  app.post("/register", user.register);
};

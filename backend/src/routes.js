const user = require("./services/users");
const auth = require("./middleware/auth");

module.exports = function (app) {
  app.get("/", (req, res) => res.send("Try: /status"));
  app.get("/status", (req, res) => res.send("Success."));
  app.post("/register", user.register);
  app.post("/login", user.login);

  app.get("/welcome", auth, (req, res) => {
    res.status(200).send("Welcome 🙌");
  });

  app.use("*", (req, res) => {
    res.status(404).json({
      success: "false",
      message: "Page not found",
      error: {
        statusCode: 404,
        message: "You reached a route that is not defined on this server",
      },
    });
  });
};

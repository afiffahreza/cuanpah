const user = require("./services/users");
const request = require("./services/requests");
const driver = require("./services/drivers");
const auth = require("./middleware/auth");
const voucher = require("./services/vouchers");
const userPoints = require("./services/userPoints");
const userVouchers = require("./services/userVouchers");

module.exports = function (app) {
  app.get("/", (req, res) => res.send("Try: /status"));
  app.get("/status", (req, res) => res.send("Success."));
  app.post("/register", user.register);
  app.post("/login", user.login);

  app.post("/requests", request.createNewRequest);
  app.get("/requests", request.getAllRequests);
  app.get("/requests/:driverId", request.getRequestbyDriver);
  app.put("/requests", request.updateRequestStatus);

  app.post("/userpoints", userPoints.createUserPoint);
  app.get("/userpoints/:userId", userPoints.getUserPoint);
  app.delete("/userpoints", userPoints.deleteUserPoint);
  app.put("/userpoints", userPoints.updateUserPoint);

  app.post("/uservouchers", userVouchers.createUserVoucher);
  app.get("/uservouchers/:userId", userVouchers.getUserVoucher);
  app.put("/uservouchers", userVouchers.updateUserVoucher);
  app.delete("/uservouchers", userVouchers.deleteUserVoucher);

  app.post("/vouchers", voucher.createVoucher);
  app.get("/vouchers", voucher.getVoucher);
  app.get("/allVouchers", voucher.getAllVouchers);

  app.post("/drivers", driver.addDriver);
  app.get("/drivers/:id", driver.getDriverbyId);

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

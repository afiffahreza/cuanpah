const bodyParser = require("body-parser");
const connection = require("../config/db");

const createUserPoint = async (req, res) => {
  try {
    const { userId, points } = req.body;
    const sql = `INSERT INTO user_points (userId, points) VALUES ('${userId}', '${points}')`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("1 record inserted");
    });

    res.status(201).json({
      message: "User Point Created Successfully",
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

const getUserPoint = async (req, res) => {
  try {
    const { userId } = req.params;
    const sql = `SELECT * FROM user_points WHERE userId = '${userId}'`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("User's point found");
      res.status(200).json({
        message: "User's point found",
        data: result,
      });
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

const deleteUserPoint = async (req, res) => {
  try {
    const { userId } = req.body;
    const sql = `DELETE FROM user_points WHERE userId = '${userId}'`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("User's point deleted");
      res.status(200).json({
        message: "User's point deleted",
        data: result,
      });
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

const updateUserPoint = async (req, res) => {
  try {
    const { userId, points } = req.body;
    const sql = `UPDATE user_points SET points = '${points}' WHERE userId = '${userId}'`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("User's point updated");
      res.status(200).json({
        message: "User's point updated",
        data: result,
      });
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

module.exports = {
  createUserPoint,
  getUserPoint,
  deleteUserPoint,
  updateUserPoint,
};

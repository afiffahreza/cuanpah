const bodyParser = require("body-parser");
const connection = require("../config/db");

const createUserVoucher = async (req, res) => {
  try {
    const { userId, voucherId } = req.body;
    const sql = `INSERT INTO user_vouchers (userId, voucherId) VALUES ('${userId}', '${voucherId}')`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("1 record inserted");
      res.status(201).json({
        message: "User Voucher Created Successfully",
        data: result,
      });
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

const getUserVoucher = async (req, res) => {
  try {
    const { userId } = req.params;
    const sql = `SELECT * FROM user_vouchers WHERE userId = '${userId}'`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("User's voucher found");
      res.status(200).json({
        message: "User's voucher found",
        data: result,
      });
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

const updateUserVoucher = async (req, res) => {
  try {
    const { userId, voucherId } = req.body;
    const sql = `UPDATE user_vouchers SET userId = '${userId}', voucherId = '${voucherId}' WHERE userId = '${userId}'`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("User's voucher updated");
      res.status(200).json({
        message: "User's voucher updated",
        data: result,
      });
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

const deleteUserVoucher = async (req, res) => {
  try {
    const { userId, voucherId } = req.body;
    const sql = `DELETE FROM user_vouchers WHERE userId = '${userId}' AND voucherId = '${voucherId}'`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("User's voucher deleted");
      res.status(200).json({
        message: "User's voucher deleted",
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
  createUserVoucher,
  getUserVoucher,
  updateUserVoucher,
  deleteUserVoucher,
};

const bodyParser = require("body-parser");
const connection = require("../config/db");

const createVoucher = async (req, res) => {
  try {
    const { voucherName, voucherDescription, voucherPoints } = req.body;
    const sql = `INSERT INTO vouchers (voucherName, voucherDescription, voucherPoints) VALUES ('${voucherName}', '${voucherDescription}', ${voucherPoints})`;

    onnection.query(sql, function (err, result) {
      if (err) throw err;
      console.log("1 record inserted");
      res.status(201).json({
        message: "Voucher Created Successfully",
        data: result,
      });
    });

    return;
  } catch (err) {
    console.log(err);
    res.status(500).json({
      message: "Something went wrong",
      error: err,
    });
  }
};

const getVoucher = async (req, res) => {
  try {
    const { voucherId } = req.body;
    const sql = `SELECT * FROM vouchers WHERE voucherId = '${voucherId}'`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      const response = {
        message: "Voucher found",
        data: result,
      };
      res.status(201).json(response);
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

const getAllVouchers = async (req, res) => {
  try {
    const sql = `SELECT * FROM vouchers`;
    connection.query(sql, function (err, result) {
      if (err) throw err;
      const response = {
        message: "Vouchers found",
        data: result,
      };
      res.status(201).json(response);
    });
  } catch (err) {
    res.status(500).json({
      message: "Something went wrong",
    });
  }
};

module.exports = { createVoucher, getVoucher, getAllVouchers };

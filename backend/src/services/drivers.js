const bodyParser = require("body-parser");
const connection = require("../config/db");

const addDriver = (req, res) => {
  const { body } = req;

  if (!body.name || !body.plate) {
    res.status(400).send({
      status: "FAILED",
      data: {
        error:
          "One of the following keys is missing or is empty in request body: 'name', 'plate'",
      },
    });
    return;
  }

  const newDriver = {
    name: body.name,
    plate: body.plate,
  };

  let query = `SELECT * FROM drivers WHERE name= ${newDriver.name} `;
  connection.query(query, (results, error) => {
    if (results.length > 0) {
      return res.status(409).send("Driver Already Exist");
    } else {
      let sql = `INSERT INTO cuanpah.drivers (name, plate) VALUES (\'${newDriver.name}\', \'${newDriver.plate}\')`;
      connection.query(sql, (err) => {
        if (err) throw err;
        console.log("1 record inserted");
      });
    }
  });

  res.status(201).send({ status: "OK", data: { newDriver } });
};

const getDriverbyId = (req, res) => {
  const { id } = req.params;
  let sql = `SELECT * FROM drivers WHERE id=${id}`;
  connection.query(sql, (err, results) => {
    if (err) throw err;
    res.status(200).send({ status: "OK", data: results });
  });
};

module.exports = { addDriver, getDriverbyId };

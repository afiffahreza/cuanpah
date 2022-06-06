require("dotenv").config();

const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const connection = require("./db");

app.get("/", (req, res) =>
  res.send("Try: /status, /warehouses, or /warehouses/2")
);

app.get("/status", (req, res) => res.send("Success."));

// app.get('/warehouses', (req, res) => {
//   connection.query(
//     "SELECT * FROM `acme`.`warehouses`",
//     (error, results, fields) => {
//       if(error) throw error;
//       res.json(results);
//     }
//   );
// });

// app.route('/warehouses/:id')
//   .get( (req, res, next) => {
//     connection.query(
//       "SELECT * FROM `acme`.`warehouses` WHERE id = ?", req.params.id,
//       (error, results, fields) => {
//         if(error) throw error;
//         res.json(results);
//       }
//     );
//   });

// Use port 8080 by default, unless configured differently in Google Cloud
const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`App is running at: http://localhost:${port}`);
});

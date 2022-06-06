require("dotenv").config();

const express = require("express");
const app = express();
const cors = require("cors");
const bodyParser = require("body-parser");
const connection = require("./src/config/db");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const auth = require("./src/middleware/auth");

app.use(express.json());
app.use(cors());

app.get("/", (req, res) => res.send("Try: /status"));

app.get("/status", (req, res) => res.send("Success."));

app.post("/register", async (req, res) => {
  // Our register logic starts here
  try {
    // Get user input
    const { email, password, name } = req.body;
    const encryptedPassword = await bcrypt.hash(password, 10);

    // Validate user input
    if (!(email && password && name)) {
      res.status(400).send("All input is required");
    }

    // check if user already exist
    // Validate if user exist in our database
    let query = "SELECT * FROM users WHERE email='" + email + "'";
    connection.query(query, (error, results, fields) => {
      if (error) throw error;
      console.log(results);
      if (results.length > 0) {
        console.log("User already exist");
        return res.status(409).send("User Already Exist. Please Login");
      } else {
        //Encrypt user password

        // Create user in our database
        let sql =
          "INSERT INTO cuanpah.users (name, email, password) VALUES ('" +
          name +
          "','" +
          email +
          "','" +
          encryptedPassword +
          "')";
        connection.query(sql, function (err, result) {
          if (err) throw err;
          console.log("1 record inserted");
        });

        // Create token
        const token = jwt.sign({ email }, process.env.TOKEN_SECRET, {
          expiresIn: "30d",
        });
        // save user token
        const user = {
          message: "User Created Successfully",
          email,
          name,
          token,
        };
        user.token = token;

        // return new user
        res.status(201).json(user);
      }
    });
  } catch (err) {
    console.log(err);
  }
  // Our register logic ends here
});

// Use port 8080 by default, unless configured differently in Google Cloud
const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`App is running at: http://localhost:${port}`);
});

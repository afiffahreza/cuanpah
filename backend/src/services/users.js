const bodyParser = require("body-parser");
const connection = require("../config/db");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const auth = require("../middleware/auth");
require("dotenv").config();

exports.register = async (req, res) => {
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
};

exports.login = async (req, res) => {
  try {
    // Get user input
    const { email, password } = req.body;

    // Validate user input
    if (!(email && password)) {
      res.status(400).send("All input is required");
    }
    // Validate if user exist in our database
    let query = "SELECT * FROM users WHERE email='" + email + "'";
    let user;
    connection.query(query, (error, results, fields) => {
      if (error) throw error;
      // console.log(results);
      if (results.length > 0) {
        user = results[0];
        // console.log("User exist");

        bcrypt.compare(password, user.password, (err, result) => {
          if (result) {
            // Create token
            const token = jwt.sign({ email }, process.env.TOKEN_SECRET, {
              expiresIn: "30d",
            });

            // save user token
            const response = {
              message: "User Logged In Successfully",
              id: user.id,
              email,
              name: user.name,
              token,
            };

            // user
            res.status(200).json(response);
          } else res.status(400).send("Invalid Credentials");
        });
      } else {
        console.log("User does not exist");
        res.status(400).send("Email is not registered");
      }
    });
  } catch (err) {
    console.log(err);
  }
};

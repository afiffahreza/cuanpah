require("dotenv").config();
const express = require("express");
const app = express();
const cors = require("cors");

app.use(express.json());
app.use(cors());

require("./src/routes")(app);

// Use port 8080 by default, unless configured differently in Google Cloud
const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`App is running at: http://localhost:${port}`);
});

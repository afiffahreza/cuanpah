import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import Alert from "@mui/material/Alert";
import Services from "../Services";
import { useNavigate } from "react-router-dom";

const theme = createTheme();

export default function Login({ user, setUser }) {
  const [wrongUser, setWrongUser] = React.useState(false);
  let navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    Services.login(data.get("driverId")).then((res) => {
      if (res.data.data.length === 0) {
        setWrongUser(true);
      } else {
        if (res.data.data[0].plate === data.get("plate")) {
          setUser(res.data.data[0]);
          sessionStorage.setItem(
            "cuanpahDriver",
            JSON.stringify(res.data.data[0])
          );
          navigate("../dashboard");
        } else {
          setWrongUser(true);
        }
      }
    });
  };

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <img
            src="https://res.cloudinary.com/cloudinary-afif/image/upload/v1655084379/Logo_sbcnyb.png"
            style={{ maxWidth: "200px" }}
          />
          <Typography component="h1" variant="h5">
            Cuanpah Driver Login
          </Typography>
          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <TextField
              margin="normal"
              required
              fullWidth
              id="driverId"
              label="Driver ID"
              name="driverId"
              autoComplete="driverId"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="plate"
              label="Plate"
              id="plate"
              autoComplete="plate"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{
                mt: 3,
                mb: 2,
                bgcolor: "#B6DB61",
                ":hover": { bgcolor: "#FFDB61" },
              }}
            >
              Sign In
            </Button>
          </Box>
          {wrongUser && (
            <Alert
              onClose={() => {
                setWrongUser(false);
              }}
              severity="error"
            >
              Wrong Driver ID or Plate
            </Alert>
          )}
        </Box>
      </Container>
    </ThemeProvider>
  );
}

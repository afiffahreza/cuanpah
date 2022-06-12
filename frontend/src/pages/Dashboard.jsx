import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Services from "../Services";
import { Button } from "@mui/material";

const Dashboard = ({ user, setUser }) => {
  const [requests, setRequests] = useState([]);

  const updateStatus = (id, status) => {
    Services.updateStatus({ requestId: id, status }).then((res) => {
      console.log(res);
    });
    location.reload();
  };

  useEffect(() => {
    Services.getRequestsByDriver(user.id).then((res) => {
      setRequests(res.data.data);
      console.log(res.data.data);
    });
  }, []);

  return (
    <>
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
          <img src="../assets/logo.png" style={{ maxWidth: "200px" }} />
          <Typography component="h1" variant="h5">
            Welcome {user.name}!
          </Typography>
          <Typography component="h1" variant="h5" marginY={1}>
            Pick Up Requests Dashboard
          </Typography>
          {requests.map((request) => (
            <Box
              sx={{
                display: "flex",
                flexDirection: "row",
                alignItems: "center",
                margin: 1,
                border: "1px solid black",
                padding: 1,
                width: "500px",
              }}
            >
              <Box
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "left",
                  justifyContent: "center",
                  margin: 1,
                  width: "300px",
                }}
              >
                <Typography variant="h6">
                  Request ID: {request.requestId}
                </Typography>
                <Typography>
                  Location: {request.lat.toString().slice(0, 5)}{" "}
                  {request.lon.toString().slice(0, 5)}
                </Typography>
                <Typography>
                  Waste: {request.wasteType} {request.wasteWeight}kg
                </Typography>
                <Typography>Status: {request.status}</Typography>
              </Box>
              <Box
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  verticalAlign: "top",
                  margin: 1,
                  width: "200px",
                }}
              >
                {request.status === "pending" ||
                requests.status === "Ongoing" ||
                requests.status === "ongoing" ? (
                  <>
                    <Typography>Change Status</Typography>
                    <Button
                      variant="contained"
                      style={{ backgroundColor: "#B6DB61", color: "white" }}
                      onClick={() =>
                        updateStatus(request.requestId, "completed")
                      }
                    >
                      Completed
                    </Button>
                  </>
                ) : (
                  <>
                    <Typography>Completed at</Typography>
                    <Typography>{request.pickupTime.split("T")[0]}</Typography>
                    <Typography>{request.pickupTime.split("T")[1]}</Typography>
                  </>
                )}
              </Box>
            </Box>
          ))}
        </Box>
      </Container>
    </>
  );
};

export default Dashboard;

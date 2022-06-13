import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Services from "../Services";
import { Button, Link } from "@mui/material";

const Dashboard = ({ user, setUser }) => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [driver, setDriver] = useState(null);

  const updateStatus = (id, status, userId) => {
    Services.updateStatus({ requestId: id, status }).then((res) => {
      console.log(res);
    });
    Services.getPoints(userId).then((res) => {
      let initialPoint = res.data.data[0].points;
      Services.updatePoints({ userId, points: initialPoint + 100 });
    });
    setTimeout(() => location.reload(), 2000);
  };

  const getMapsLink = (lat, lng) => {
    return `http://www.google.com/maps/place/${lat},${lng}`;
  };

  useEffect(() => {
    const cuanpahDriver = JSON.parse(sessionStorage.getItem("cuanpahDriver"));
    setDriver(cuanpahDriver);
    Services.getRequestsByDriver(cuanpahDriver.id).then((res) => {
      setRequests(res.data.data);
      console.log(res.data.data);
      setLoading(false);
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
          <img
            src="https://res.cloudinary.com/cloudinary-afif/image/upload/v1655084379/Logo_sbcnyb.png"
            style={{ maxWidth: "200px" }}
          />
          {loading ? (
            <Typography component="h1" variant="h5">
              Loading...
            </Typography>
          ) : (
            <>
              <Typography component="h1" variant="h5">
                Welcome {driver.name}!
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
                    margin: 2,
                    border: "1px solid black",
                    borderRadius: "15px",
                    boxShadow: "0px 2px 5px #B6DB61",
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
                      <Link href={getMapsLink(request.lat, request.lon)}>
                        Location:
                      </Link>{" "}
                      {request.lat.toString().slice(0, 5)}{" "}
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
                    request.status === "Ongoing" ||
                    request.status === "ongoing" ? (
                      <>
                        <Typography>Change Status</Typography>
                        <Button
                          variant="contained"
                          style={{ backgroundColor: "#B6DB61", color: "white" }}
                          onClick={() =>
                            updateStatus(
                              request.requestId,
                              "completed",
                              request.userId
                            )
                          }
                        >
                          Completed
                        </Button>
                      </>
                    ) : (
                      <>
                        <Typography>Completed at</Typography>
                        <Typography>
                          {request.pickupTime.split("T")[0]}
                        </Typography>
                        <Typography>
                          {request.pickupTime.split("T")[1]}
                        </Typography>
                      </>
                    )}
                  </Box>
                </Box>
              ))}
            </>
          )}
        </Box>
      </Container>
    </>
  );
};

export default Dashboard;

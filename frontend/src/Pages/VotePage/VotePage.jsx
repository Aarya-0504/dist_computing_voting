import React from "react";
import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Lottie from 'react-lottie';
import liveSession from '../../assets/live.json' 

const ElectionDetails = () => {
  const electionDetails = {
    date: "April 15, 2024",
    status: "Upcoming",
    electionInfo: "General Election 2024",
  };

  const handleAction1 = () => {
    // Handle action 1
  };

  const handleAction2 = () => {
    // Handle action 2
  };

  return (
   
    <Box
    sx={{
        padding: '0px',
        borderRadius: 2,
        width: "100%", // Adjusted to 100% for responsiveness
        maxWidth: "500px", // Limit maximum width to 500px
        boxShadow: "0 0 10px rgba(0, 0, 0, 0.1), 0 0 15px rgba(0, 0, 0, 0.2)",
        backgroundColor: "#f0f0f0",
        margin: '10px',
        
      }}
    >
    
    <Grid container spacing={2} alignItems="center" sx={{width:'500px',
    padding:'0px',
    margin:'0px',
    overflow:'hidden',
  
  }}>
      <Grid item xs={6} sx={{ textAlign: "center"}}>
        <Box>
          <Typography variant="body1" sx={{ fontSize: "1.5rem" }}>
            {electionDetails.date}
          </Typography>
        </Box>
      </Grid>
      <Grid item xs={6} sx={{ textAlign: "center" }}>
        <Box
        sx={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100%',
            width: '100%',
            padding: '0px',
            margin: '0px',
            overflow: 'hidden',
        }}
        >
        <Lottie 
        options={{
            loop: true,
            autoplay: true,
            animationData: liveSession,
            rendererSettings: {
              preserveAspectRatio: 'xMidYMid slice'
            }
        }}
        width={70}
      />
        </Box>
      </Grid>
    </Grid>

    <Typography variant="h6" gutterBottom sx={{ marginTop: 2, textAlign: "center" }}>
      Election Details
    </Typography>
    <Typography variant="body1" sx={{ textAlign: "center" }}>
      {electionDetails.electionInfo}
    </Typography>

    <Box sx={{ marginTop: 2, display: "flex", justifyContent: "center" }}>
      <Button variant="contained" color="primary" onClick={handleAction1} sx={{ marginRight: 1 }}>
        Action 1
      </Button>
      <Button variant="contained" color="secondary" onClick={handleAction2}>
        Action 2
      </Button>
    </Box>
    </Box>
  );
};

export default ElectionDetails;

const express = require('express');
const bcryptjs = require('bcryptjs');
const jwt = require('jsonwebtoken');
const cors = require("cors");
const corsConfigs = require("./config/corsConfigs");
require('./db/connection');
require("dotenv").config();
const PORT=8000
const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cors());
app.use(cors(corsConfigs));











app.listen(PORT,async ()=>{
    
    console.log(`Listening on port:${PORT}`);
})
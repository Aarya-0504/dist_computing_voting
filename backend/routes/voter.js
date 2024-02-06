const mongoose = require("mongoose");
const express = require("express");
const router = express.Router();
const axios = require("axios");

require("../model/Party");
require("../model/Voter");

const Voter = mongoose.model("Voter");
const Party = mongoose.model("Party");

router.post('/api/vote',async(req,res)=>{
    try{
        const newVoter=Voter(req.body);

        try{
            const savedVoter=await newVoter.save();
        res.status(201).json(savedVoter);
        }catch(err){
            res.status(500).json(err);
        }

    }catch(e){
        console.log(e);
    }
})
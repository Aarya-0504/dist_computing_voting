const express = require('express');
// const bcryptjs = require('bcryptjs');
// const jwt = require('jsonwebtoken');
const cors = require("cors");
//const corsConfigs = require("./config/corsConfigs");
require('./db/connection');
require("dotenv").config();
const mongoose = require('mongoose');
const PORT=8000
const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cors());
//app.use(cors(corsConfigs));
require("./model/Party");
require("./model/Voter");

const Voter = mongoose.model("Voter");
const Party = mongoose.model("Party");



app.get('/api/allparty', async(req, res) => {
    const Party1 = await Party.find({});
    //console.log("Parties>>>>>>>>" ,places);
        
    res.send({Party1});
})

app.get('/api/allvoter', async(req, res) => {
    const voters = await Voter.find({});
    //console.log("Parties>>>>>>>>" ,places);
        
    res.send({voters});
})

// app.post('/api/vote',async(req,res)=>{
//     try{
//         const newVoter=Voter(req.body);

//         try{
//             const savedVoter=await newVoter.save();
//             const partyId = req.body.PartyId;
//             await Party.findByIdAndUpdate(
//                 partyId,
//                 { $push: { voters: savedVoter._id } },
//                 { new: true }
//             );

//         res.status(201).json(savedVoter);
//         }catch(err){
//             res.status(500).json(err);
//         }

//     }catch(e){
//         console.log(e);
//     }
// })

app.post('/api/vote', async (req, res) => {
    try {
        const newVoter = new Voter(req.body);

        try {
            const savedVoter = await newVoter.save();
            console.log('Saved Voter ID:', savedVoter._id);

            const partyId = req.body.PartyId;
            //console.log()
            console.log('Party ID:', partyId);

            const updatedParty = await Party.findByIdAndUpdate(
                partyId,
                { $push: { voters: savedVoter._id } },
                { new: true }
            );

            console.log('Updated Party:', updatedParty);

            res.status(201).json(savedVoter);
        } catch (err) {
            console.error('Error updating party:', err);
            res.status(500).json(err);
        }
    } catch (e) {
        console.error('Error saving voter:', e);
        console.log(e);
    }
});


app.post('/api/party',async(req,res)=>{
    try{
        const newParty=Party(req.body);

        try{
            const savedParty=await newParty.save();
        res.status(201).json(savedParty);
        }catch(err){
            res.status(500).json(err);
        }

    }catch(e){
        console.log(e);
    }
})




app.listen(PORT,async ()=>{
    
    console.log(`Listening on port:${PORT}`);
})
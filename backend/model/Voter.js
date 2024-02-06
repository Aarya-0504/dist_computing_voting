const mongoose = require('mongoose');

const voterSchema = mongoose.Schema({
    Name: {
        type: String,
        required: true,
    },
    Age: {
        type: Number,
        required: true,
        
    },
    PartyId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "Candidate",
      },
   
});

const Voter = global.Voter || mongoose.model("Voter", voterSchema);
module.exports = Voter;
const mongoose = require("mongoose");
const { Schema } = require("mongoose");

const partySchema = new Schema({
    partyName: {
      type: String,
    },
    partyLeaderName: {
        type: String,
      },
      voters: [
        {
          type: mongoose.Schema.Types.ObjectId,
          ref: "Voter",
        },
      ],
});

const Party = global.Party || mongoose.model("Party", partySchema);

module.exports = Party;
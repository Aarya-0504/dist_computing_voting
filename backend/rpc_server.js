const protoLoader = require("./node_modules/@grpc/proto-loader");
const grpc = require("./node_modules/@grpc/grpc-js");

const PROTO_PATH = "./voting.proto";
const mongoose = require('mongoose');
const Voter = require('./model/Voter');
const Party = require('./model/Party');

// connecting to database
require('./db/connection');

const packageDefinition = protoLoader.loadSync(PROTO_PATH);
const { VotingService } = grpc.loadPackageDefinition(packageDefinition);
const server = new grpc.Server();




// // trying to send dummy data right now
// const news = [
//     { id: "1", title: "Note 1", body: "Content 1", postImage: "Post image 1" },
//     { id: "2", title: "Note 2", body: "Content 2", postImage: "Post image 2" },
// ];


server.addService(VotingService.service, {

    GetVoterList:async (_, callback) => {
            await Voter.find({})
            .then(voters => {
                const serializedResponse=JSON.stringify(voters)
                
                const voterListResponse = { 
                    voters: [serializedResponse]
                };

                callback(null, voterListResponse);
            })
            .catch(err => {
              console.error('Error fetching voter list:', err);
            });
    },

    GetPartyList:async (_, callback) => {
        await Party.find({})
        .then(parties => {
            const serializedResponse=JSON.stringify(parties)
            
            const voterListResponse = { 
                parties: [serializedResponse]
            };

            callback(null, voterListResponse);
        })
        .catch(err => {
          console.error('Error fetching voter list:', err);
        });
},

});


server.bindAsync('127.0.0.1:3030', grpc.ServerCredentials.createInsecure(), (err, port) => {
    if (err) {
        console.error('Error starting server:', err);
    } else {
        console.log(`Server running at 127.0.0.1:${port}`);
        server.start();
    }
});
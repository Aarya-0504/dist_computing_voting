const protoLoader = require("./node_modules/@grpc/proto-loader");
const grpc = require("./node_modules/@grpc/grpc-js");

// const PROTO_PATH = "./voting.proto";
// var protoLoader = require("@grpc/proto-loader");

const mongoose = require('mongoose');

const Voter = require('./model/Voter');
const Party = require('./model/Party');

require('./db/connection');

const PROTO_PATH = './voting.proto';

const packageDefinition = protoLoader.loadSync(PROTO_PATH);
const { VotingService } = grpc.loadPackageDefinition(packageDefinition);

const server = new grpc.Server();

server.addService(VotingService.service, {
    GetVoterList: (_, callback) => {
        Voter.find({}, 'name', (err, voters) => {
            if (err) {
                console.error('Error fetching voter list:', err);
                callback(err, null);
            } else {
                const voterNames = voters.map(voter => voter.name);
                callback(null, { voters: voterNames });
            }
        });
    },

    GetPartyList: (_, callback) => {
        Party.find({}, 'name', (err, parties) => {
            if (err) {
                console.error('Error fetching party list:', err);
                callback(err, null);
            } else {
                const partyNames = parties.map(party => party.name);
                callback(null, { parties: partyNames });
            }
        });
    }
});

server.bindAsync('127.0.0.1:3035', grpc.ServerCredentials.createInsecure(), (err, port) => {
    if (err) {
        console.error('Error starting server:', err);
    } else {
        console.log('Server running at 127.0.0.1:3035');
        server.start();
    }
});

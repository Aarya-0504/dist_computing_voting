const protoLoader = require("./node_modules/@grpc/proto-loader");
const grpc = require("./node_modules/@grpc/grpc-js");

const PROTO_PATH = './voting.proto';

const protoDefinition = protoLoader.loadSync(PROTO_PATH);
const grpcObject = grpc.loadPackageDefinition(protoDefinition);
const { VotingService } = grpcObject;


const client = new VotingService('localhost:3030', grpc.credentials.createInsecure());

  client.GetVoterList({}, (err, response) => {
        if (err) {
            console.error('Error fetching voter list:', err);
        } else {
            console.log(JSON.parse(response.voters))
            
        }
    });
    
  client.GetPartyList({}, (err, response) => {
        if (err) {
            console.error('Error fetching voter list:', err);
        } else {
            console.log(JSON.parse(response.parties))
            
        }
    });
    


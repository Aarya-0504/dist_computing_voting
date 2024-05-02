// package LOAD_BALANCER_SERVER_CLIENT;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.*;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.logging.*;

class VotingSystem implements VotingInterface 
{
    private Set<String> voters;
    private Map<String, Integer> parties;
    private MongoCollection<Document> votersCollection;
    private MongoCollection<Document> partiesCollection;
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> electionCollection;

    public VotingSystem() {
        this.voters = new HashSet<>();
        this.parties = new HashMap<>();
        String connectionString = "mongodb+srv://nikhilprajapati2:AT6QAz2cCfKKCOOI@cluster0.vzfozkt.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0&tls=true";
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);

            // Connect to MongoDB Atlas
            MongoClient mongoClient = MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase("DC_MINI_PROJECT");
    
            // Initialize collections
            this.votersCollection = database.getCollection("registered_voters");
            this.partiesCollection = database.getCollection("registered_parties");
            this.usersCollection = database.getCollection("users");
            this.electionCollection = database.getCollection("elections");
        }
    

    public String register_voter(String voterId) throws RemoteException {
 
        // mongodb table addition
        Document voter = votersCollection.find(new Document("voterId", voterId)).first();
        if (voter != null) {
            return "VoterId " + voterId + " exists, registration unsuccessful";
        }
        votersCollection.insertOne(new Document("voterId", voterId));
        // mongodb table addition ends

        if (voters.contains(voterId)) {
            return "VoterId " + voterId + " exist, registration unsuccessful";
        }
        voters.add(voterId);
        return "Voter " + voterId + " registered successfully";
    }


    public String register_party(String partyName) throws RemoteException {

        //mongodb party name registeraton start
        Document party = partiesCollection.find(new Document("partyName", partyName)).first();
        if (party != null) {
            return "party :  " + partyName + " already registered";
        }
        
        partiesCollection.insertOne(new Document("partyName", partyName));
        //mongodb party name registeraton end

        if (parties.containsKey(partyName)) {
            return "Party " + partyName + " already registered";
        }
        parties.put(partyName, 0);
        return "Party " + partyName + " registered successfully";
    }

 
 public String vote(String voterId, String partyName, String electionId) throws RemoteException {
    
    Document electionFilter = new Document("electionId", electionId);
    Document existingElection = electionCollection.find(electionFilter).first();
    if (existingElection == null) {
        System.out.println("No election found with ID: " + electionId);
        return "No election found with ID: " + electionId;
    }

    // Update the 'votersVoted' array in the existing election document
    List<String> votersVoted = existingElection.getList("votersVoted", String.class);
    if (votersVoted == null) {
        votersVoted = new ArrayList<>();
    }
    if (!votersVoted.contains(voterId)) {
        votersVoted.add(voterId);
        electionCollection.updateOne(electionFilter, new Document("$set", new Document("votersVoted", votersVoted)));
    } else {
        return "Voter " + voterId + " has already voted in election " + electionId;
    }

    // Update the 'partyVotes' count in the existing election document
    String partyVotesKey = "partyVotes." + partyName;
    Integer partyVotesCount = existingElection.getInteger(partyVotesKey, 0);
    partyVotesCount++;
    electionCollection.updateOne(electionFilter, new Document("$set", new Document(partyVotesKey, partyVotesCount)));

    // System.out.println("Vote successfully cast for " + partyName + " in election " + electionId);
    return "Vote successfully cast for " + partyName + " in election " + electionId;
}

    public Map<String, Integer> tally_votes() throws RemoteException {
        return parties;
    }

    public Map<String, Integer> tally_votes(String electionId) throws RemoteException {
        
        Map<String, Integer> voteCounts = new HashMap<>();

        
        Document electionDoc = electionCollection.find(new Document("electionId", electionId)).first();

        if (electionDoc == null) {
            
            return null;
        }

        // Get the partyVotes field from the document
        Document partyVotes = electionDoc.get("partyVotes", Document.class);

        // Iterate through the partyVotes and add the counts to the voteCounts map
        for (String partyName : partyVotes.keySet()) {
            int count = partyVotes.getInteger(partyName);
            voteCounts.put(partyName, count);
        }

        return voteCounts;
    }


    public Instant getServerTime() throws RemoteException {
        return Instant.now();
    }

    public String signup(String email, String username, String pass) throws RemoteException {
        Document user = usersCollection.find(new Document("email", email)).first();
        if (user != null) {
            return "false "+"User Already Exists"; // User already exists
        }
        usersCollection.insertOne(new Document("email", email).append("username", username).append("password", pass));
        return "true "+"User Registered Successfully"; // Signup successful
    }

    public String login(String email, String pass) throws RemoteException {
        Document user = usersCollection.find(new Document("email", email).append("password", pass)).first();

        if (user != null) {
            ObjectId userId = user.getObjectId("_id");
            return userId.toHexString(); // Return _id as a String
        }
    
        return null; // Return null if user is not found
    }

    public Map<String, Object> fetchElectionDetail() throws RemoteException {
        // Query MongoDB collection for the latest active election
        Document election = electionCollection.find(
            Filters.eq("election_Status", true)
        ).sort(Sorts.descending("timestamp")).first();
        
        if (election != null) {
            // Convert the Document to a Map<String, Object>
            Map<String, Object> electionDetailMap = new HashMap<>();
            electionDetailMap.put("electionId", election.getString("electionId"));
            electionDetailMap.put("registeredParties", election.get("registeredParties", List.class));
            electionDetailMap.put("election_Status", election.getBoolean("election_Status"));

            return electionDetailMap;
        } else {
            // No active election found
            return null;
        }
    }

}




public class Server {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Server <port>");
            System.exit(1);
        }
        
        int port = Integer.parseInt(args[0]);

        try {
            VotingSystem serverInstance = new VotingSystem();
            VotingInterface serverStub = (VotingInterface) UnicastRemoteObject.exportObject(serverInstance, 0);
            Registry serverRegistry = LocateRegistry.createRegistry(port);
            serverRegistry.bind("Server " + port, serverStub);

            System.err.println("Voting Server on port " + port + " ready.");

            // Update load balancer with new server details
            updateLoadBalancer(port, true);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void updateLoadBalancer(int port, boolean addServer) {
        try {
            // Locate the load balancer registry
            Registry registry = LocateRegistry.getRegistry("localhost", 3000);

            // Look up the load balancer
            LoadBalancerInterface loadBalancer = (LoadBalancerInterface) registry.lookup("LoadBalancer");
            
            // Add or remove the server from the load balancer
            if (addServer) {
                loadBalancer.addServer("Server " + port);
                System.out.println("Load Balancer updated with new server: Server" + port);
            } else {
                // System.out.println()
                loadBalancer.removeServer("Server " + port);
                System.out.println("Load Balancer removed server: Server" + port);
            }
        } catch (Exception e) {
            System.err.println("Error updating load balancer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add a shutdown hook to notify the load balancer when the server is shut down
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // System.out.println("sun.java.command: " + System.getProperty("sun.java.command"));
            
            String[] commandParts = System.getProperty("sun.java.command").split(" ");
            int port=Integer.parseInt(commandParts[1]);
            // for( String  str: commandParts)
            //     System.out.println(str);

            System.out.println("Closing server at port : " + port);

            updateLoadBalancer(port, false);
        }));
    }
}

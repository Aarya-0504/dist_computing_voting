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
import com.mongodb.client.MongoCollection;
import org.bson.Document;

class VotingSystem implements VotingInterface {
    private Set<String> voters;
    private Map<String, Integer> parties;
    private MongoCollection<Document> votersCollection;
    private MongoCollection<Document> partiesCollection;


    public VotingSystem() {
        this.voters = new HashSet<>();
        this.parties = new HashMap<>();
        String pass = System.getenv("CLUSTER_PASSOWRD");

            // Connect to MongoDB Atlas
            String connectionString = "mongodb+srv://nikhilprajapati2:"+pass+"@cluster0.vzfozkt.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0&tls=true";
            MongoClient mongoClient = MongoClients.create(connectionString);
            MongoDatabase database = mongoClient.getDatabase("DC_MINI_PROJECT");
    
            // Initialize collections
            this.votersCollection = database.getCollection("registered_voters");
            // this.partiesCollection = database.getCollection("parties");
        }
    

    public String register_voter(String voterId) throws RemoteException {
 
        Document voter = votersCollection.find(new Document("voterId", voterId)).first();
        if (voter != null) {
            return "VoterId " + voterId + " exists, registration unsuccessful";
        }
        votersCollection.insertOne(new Document("voterId", voterId));

        if (voters.contains(voterId)) {
            return "VoterId " + voterId + " exist, registration unsuccessful";
        }
        voters.add(voterId);
        return "Voter " + voterId + " registered successfully";
    }

    public String register_party(String partyName) throws RemoteException {
        if (parties.containsKey(partyName)) {
            return "Party " + partyName + " already registered";
        }
        parties.put(partyName, 0);
        return "Party " + partyName + " registered successfully";
    }

    public String vote(String voterId, String partyName) throws RemoteException {
        if (!voters.contains(voterId)) {
            return "Voter not registered";
        }
        if (!parties.containsKey(partyName)) {
            return "Party not registered";
        }
        parties.put(partyName, parties.get(partyName) + 1);
        voters.remove(voterId);
        return "Vote cast for " + partyName + " successfully";
    }

    public Map<String, Integer> tally_votes() throws RemoteException {
        return parties;
    }

    public Instant getServerTime() throws RemoteException {
        return Instant.now();
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

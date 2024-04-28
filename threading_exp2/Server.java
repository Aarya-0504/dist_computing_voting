import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.*;

interface Hello extends java.rmi.Remote {
    String register_voter(String voterId) throws RemoteException;
    String register_party(String partyName) throws RemoteException;
    String vote(String voterId, String partyName) throws RemoteException;
    Map<String, Integer> tally_votes() throws RemoteException;
}

class VotingSystem implements Hello {
    private Set<String> voters;
    private Map<String, Integer> parties;
    private final Object votersLock = new Object();
    private final Object partiesLock = new Object();

    public VotingSystem() {
        this.voters = new HashSet<>();
        this.parties = new HashMap<>();
    }

    public String register_voter(String voterId) throws RemoteException {
        synchronized (votersLock) {
            voters.add(voterId);
        }
        return "Voter " + voterId + " registered successfully";
    }

    public String register_party(String partyName) throws RemoteException {
        synchronized (partiesLock) {
            parties.put(partyName, 0);
        }
        return "Party " + partyName + " registered successfully";
    }

    public String vote(String voterId, String partyName) throws RemoteException {
        synchronized (votersLock) {
            if (!voters.contains(voterId)) {
                return "Voter not registered";
            }
        }

        synchronized (partiesLock) {
            if (!parties.containsKey(partyName)) {
                return "Party not registered";
            }
            parties.put(partyName, parties.get(partyName) + 1);
            // return tally_votes();
        }
        
        System.out.println("Vote cast for " + partyName + " successfully"+" from "+voterId);
        return "Vote cast for " + partyName + " successfully"+" from "+voterId;
    }

    public Map<String, Integer> tally_votes() throws RemoteException {
        return parties;
    }
}


class RegisterVoterHandler implements HttpHandler {
    private VotingSystem votingSystem;

    public RegisterVoterHandler(VotingSystem votingSystem) {
        this.votingSystem = votingSystem;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            // Response message
            String response = "hello";

            // Send response headers
            exchange.sendResponseHeaders(200, response.length());

            // Get the response body's output stream
            OutputStream os = exchange.getResponseBody();

            // Write the response to the output stream
            os.write(response.getBytes());

            // Close the output stream
            os.close();
        } 
        
        else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            // Response message
            String response = "hello";

            // Send response headers
            exchange.sendResponseHeaders(200, response.length());

            // Get the response body's output stream
            OutputStream os = exchange.getResponseBody();

            // Write the response to the output stream
            os.write(response.getBytes());

            // Close the output stream
            os.close();
        } 
        
        else {
            // Handle method not allowed
            exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            exchange.close();
        }


    }

}


public class Server {

    public static void main(String args[]) {
        try {
            VotingSystem obj = new VotingSystem();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Hello", stub);

            System.err.println("Server ready");

            // Start HTTP Server
            startHTTPServer(obj);

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void startHTTPServer(VotingSystem votingSystem) throws Exception {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Registering different HTTP handler classes for different endpoints
        server.createContext("/register_voter", new RegisterVoterHandler(votingSystem));
        // server.createContext("/register_party", new RegisterPartyHandler(votingSystem));
        // server.createContext("/vote", new VoteHandler(votingSystem));
        // server.createContext("/tally_votes", new TallyVotesHandler(votingSystem));
        
        server.setExecutor(null);
        server.start();
        System.out.println("HTTP Server started on port " + port);
    }
}

// Similarly, create handler classes for other endpoints like RegisterPartyHandler, VoteHandler, and TallyVotesHandler

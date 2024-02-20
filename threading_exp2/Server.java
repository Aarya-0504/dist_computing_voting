import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.*;



class VotingSystem implements Hello {
    private Set<String> voters;
    private Map<String, Integer> parties;
    private final Object votersLock = new Object();
    private final Object partiesLock = new Object();

    public VotingSystem() {
        this.voters = new HashSet<>();
        this.parties = new HashMap<>();
    }

    public String register_voter(String voterId) {
        synchronized (votersLock) {
            voters.add(voterId);
        }
        return "Voter " + voterId + " registered successfully";
    }

    public String register_party(String partyName) {
        synchronized (partiesLock) {
            parties.put(partyName, 0);
        }
        return "Party " + partyName + " registered successfully";
    }

    public String vote(String voterId, String partyName) {
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

    public Map<String, Integer> tally_votes() {
        return parties;
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

            // Create a thread pool to handle multiple client requests concurrently
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true) {
                // Accept client connections and execute tasks concurrently
                executorService.execute(() -> {
                    // Handle client requests here
                });
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

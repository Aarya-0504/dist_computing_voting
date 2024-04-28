// package LOAD_BALANCER_SERVER_CLIENT;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.*;


interface VotingInterface extends java.rmi.Remote {
    String register_voter(String voterId) throws RemoteException;
    String register_party(String partyName) throws RemoteException;
    String vote(String voterId, String partyName) throws RemoteException;
    Map<String, Integer> tally_votes() throws RemoteException;
    Instant getServerTime() throws RemoteException;
}

class VotingSystem implements VotingInterface {
    private Set<String> voters;
    private Map<String, Integer> parties;

    public VotingSystem() {
        this.voters = new HashSet<>();
        this.parties = new HashMap<>();
    }

    public String register_voter(String voterId) throws RemoteException {
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
            try {
                // Start multiple server instances
                for (int i = 0; i < 3; i++) {
                    System.out.println("hello");
                    VotingSystem serverInstance = new VotingSystem();
                    VotingInterface serverStub = (VotingInterface) UnicastRemoteObject.exportObject(serverInstance, 0);
                    Registry serverRegistry = LocateRegistry.createRegistry(1100 + i);  // Different ports for each server instance 
                    serverRegistry.bind("Server" + i, serverStub);
                }
    
                System.err.println("Voting Servers ready.");
            } catch (Exception e) {
                System.err.println("Server exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

 class VotingSystem implements Hello{
    private Set<String> voters;
    private Map<String, Integer> parties;

    public VotingSystem() {
        this.voters = new HashSet<>();
        this.parties = new HashMap<>();
    }

    public String register_voter(String voterId) {
        voters.add(voterId);
        return "Voter " + voterId + " registered successfully";
    }
    
    public String register_party(String partyName) {
        parties.put(partyName, 0);
        return "Party " + partyName + " registered successfully";
    }

    public String vote(String voterId, String partyName) {
        if (!voters.contains(voterId)) {
            return "Voter not registered";
        }
        if (!parties.containsKey(partyName)) {
            return "Party not registered";
        }
        parties.put(partyName, parties.get(partyName) + 1);
        return "Vote cast for " + partyName + " successfully";
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
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

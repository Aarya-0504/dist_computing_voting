import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface VotingInterface extends Remote {
    
    String register_party(String party_name) throws RemoteException;
    String register_voter(String voterId) throws RemoteException;
    public String vote(String voterId, String partyName) throws RemoteException;
    Map<String, Integer> tally_votes() throws RemoteException;
    
}

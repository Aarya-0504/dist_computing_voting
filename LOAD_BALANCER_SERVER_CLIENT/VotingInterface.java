import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.time.Instant;

public interface VotingInterface extends Remote {
    String register_voter(String voterId) throws RemoteException;
    String register_party(String partyName) throws RemoteException;
    
    String  signup(String email,String username,String pass) throws RemoteException;
    String  login(String email,String pass) throws RemoteException;
    Map<String, Integer> tally_votes() throws RemoteException;
    Instant getServerTime() throws RemoteException;
    Map<String, Object> fetchElectionDetail() throws RemoteException;
    String vote(String voterId, String partyName,String electionId) throws RemoteException;
    Map<String, Integer> tally_votes(String electionId) throws RemoteException;
}

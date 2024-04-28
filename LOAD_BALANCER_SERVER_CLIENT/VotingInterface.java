// package LOAD_BALANCER_SERVER_CLIENT;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.time.Instant;

public interface VotingInterface extends java.rmi.Remote {
    String register_voter(String voterId) throws RemoteException;
    String register_party(String partyName) throws RemoteException;
    String vote(String voterId, String partyName) throws RemoteException;
    Map<String, Integer> tally_votes() throws RemoteException;
    Instant getServerTime() throws RemoteException;

}
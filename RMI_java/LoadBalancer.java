import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.*;

interface LoadBalancerInterface extends Remote {
    String handleRequest(String request) throws RemoteException;
}

public class LoadBalancer extends UnicastRemoteObject implements LoadBalancerInterface {
    private List<VotingInterface> servers;
    private int currentServerIndex;

    public LoadBalancer() throws RemoteException {
        super();
        servers = new ArrayList<>();
        currentServerIndex = 0;
    }

    public void addServer(VotingInterface server) {
        servers.add(server);
    }

    public String handleRequest(String request) throws RemoteException {

        VotingInterface server = servers.get(currentServerIndex);
        currentServerIndex = (currentServerIndex + 1) % servers.size();
        return ((LoadBalancer) server).handleRequest(request);


    }
}

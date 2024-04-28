// package LOAD_BALANCER_SERVER_CLIENT;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoadBalancerInterface extends Remote {
    String getServerName() throws RemoteException;
    void addServer(String serverName) throws RemoteException;
    void removeServer(String serverName) throws RemoteException;
    boolean isServerActive(String serverName) throws RemoteException;
}


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoadBalancerInterface extends Remote {
    String handleRequest(String request) throws RemoteException;
}

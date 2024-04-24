import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LoadBalancerServer {
    public static void main(String[] args) {
        try {
            LoadBalancer loadBalancer = new LoadBalancer();
            LoadBalancerInterface loadBalancerStub = (LoadBalancerInterface) UnicastRemoteObject.exportObject(loadBalancer, 0);

            // Bind the load balancer stub in the registry
            Registry registry = LocateRegistry.createRegistry(1099); // Port for load balancer server
            registry.bind("LoadBalancer", loadBalancerStub);

            System.err.println("Load Balancer Server ready.");
        } catch (Exception e) {
            System.err.println("Load Balancer Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

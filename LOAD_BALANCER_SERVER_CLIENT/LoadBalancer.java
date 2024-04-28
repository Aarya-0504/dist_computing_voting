import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;



public class LoadBalancer implements LoadBalancerInterface {
    private String[] serverNames = {"Server0", "Server1", "Server2"}; // Names of the servers
    private Random random = new Random();

    public LoadBalancer() {
        // Export load balancer object
        try {
            LoadBalancerInterface loadBalancerStub = (LoadBalancerInterface) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(3000); // Port for load balancer
            registry.bind("LoadBalancer", loadBalancerStub);
        } catch (Exception e) {
            System.err.println("Load balancer exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public String getServerName() {
        // Randomly select a server
        return serverNames[random.nextInt(serverNames.length)];
    }

    public static void main(String[] args) {
        new LoadBalancer();
        System.err.println("Load Balancer ready.");
    }
}

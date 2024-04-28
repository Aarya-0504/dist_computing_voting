import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class LoadBalancer implements LoadBalancerInterface {
    private List<String> serverNames; // Names of the servers
    private int currentIndex; // Index of the last selected server

    public LoadBalancer() {
        this.serverNames = new ArrayList<>();
        this.currentIndex = 0;

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
        // No servers available
        if (serverNames.isEmpty()) {
            return null;
        }
        // Increment the index and wrap around if needed
        currentIndex = (currentIndex + 1) % serverNames.size();
        return serverNames.get(currentIndex);
    }

    @Override
    public void addServer(String serverName) {
        if (!serverNames.contains(serverName)) {
            serverNames.add(serverName);
            System.out.println("Server added: " + serverName);
        }
    }

    @Override
    public void removeServer(String serverName) {
        if (serverNames.contains(serverName)) {
            serverNames.remove(serverName);
            System.out.println("Server removed: " + serverName);
        }
    }
    @Override
    public boolean isServerActive(String serverName){
        if (serverNames.contains(serverName)) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        new LoadBalancer();
        System.err.println("Load Balancer ready.");
    }
}

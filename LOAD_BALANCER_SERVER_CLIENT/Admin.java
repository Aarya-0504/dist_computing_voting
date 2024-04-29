import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Admin {
    private static LoadBalancerInterface loadBalancer;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 3000);
            loadBalancer = (LoadBalancerInterface) registry.lookup("LoadBalancer");

            // Get the server name and port from the load balancer
            String serverName = loadBalancer.getServerName();
            String[] parts = serverName.split(" ");
            int port = Integer.parseInt(parts[1]);

            System.out.println("Connected to server: " + serverName);

            // Connect to the server
            Registry serverRegistry = LocateRegistry.getRegistry("localhost", port);
            VotingInterface stub = (VotingInterface) serverRegistry.lookup(serverName);

            // Add voting details by admin
            addVotingDetails(stub);

        } catch (Exception e) {
            System.err.println("Admin client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void addVotingDetails(VotingInterface stub) throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter voting details:");
        System.out.print("Voting ID: ");
        String votingId = scanner.nextLine();
        System.out.print("Voting Name: ");
        String votingName = scanner.nextLine();
        System.out.print("Voting Description: ");
        String votingDescription = scanner.nextLine();

        // Call the method on the server to add voting details
        String response = stub.addVotingDetails(votingId, votingName, votingDescription);
        System.out.println("Response from server: " + response);
    }
}

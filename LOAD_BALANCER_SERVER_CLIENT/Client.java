import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Instant;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private static final int CHECK_INTERVAL_SECONDS = 2; // Adjust the interval as needed
    private static String serverName = null; // Adjust the interval as needed
    private static  int port = -1; // Adjust the interval as needed
    private static Registry serverRegistry;
    private static VotingInterface stub;
    private static LoadBalancerInterface loadBalancer;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 3000); // Connect to load balancer
            loadBalancer = (LoadBalancerInterface) registry.lookup("LoadBalancer");

            serverName = loadBalancer.getServerName();
            String[] parts = serverName.split(" ");
            port = Integer.parseInt(parts[1]);
            System.out.println("Port number: " + port);

            serverRegistry = LocateRegistry.getRegistry("localhost", port); // Connect to selected server
            stub = (VotingInterface) serverRegistry.lookup(serverName);

            // Start the server check thread
            Thread serverCheckThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(CHECK_INTERVAL_SECONDS * 1000); 
                        if (!loadBalancer.isServerActive(serverName)) 
                        {
                            System.out.println("Ooops !! lost connection... Requesting another server...");
                            
                            String newServerName = loadBalancer.getServerName();
                            // Handle the case when no server is available
                            if (newServerName == null) {
                                serverName=newServerName;
                                System.out.println("No servers available Right Try again later.");
                                continue;
                            }

                            
                            // Update serverName and port based on the new server
                            String[] part = newServerName.split(" ");

                           int newPort = Integer.parseInt(part[1]);
                           
                            System.out.println("New server selected: " + newServerName);
                            // Reconnect to the new server
                            serverRegistry = LocateRegistry.getRegistry("localhost", newPort);
                            stub = (VotingInterface) serverRegistry.lookup(newServerName);

                            serverName=newServerName;
                            port=newPort;
                            // serverAvailable = true;
                            // break;
                        }

                    } catch (Exception e) {
                        // serverAvailable = false;
                        System.err.println("Error checking server availability: " + e);
                    }
                }
            });
            
            serverCheckThread.start();

            // Remaining client code remains the same...
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n-------------------------------");
                System.out.println("Voting Machine");
                System.out.println("-------------------------------");
                System.out.println("1. Register Voter");
                System.out.println("2. Register Party");
                System.out.println("3. Cast Vote");
                System.out.println("4. Tally Votes");
                System.out.println("0. Exit");
                System.out.println("-------------------------------");

                System.out.print("\nEnter your choice: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        System.out.print("Enter Voter ID: ");
                        String voterId = scanner.nextLine();
                        String response = stub.register_voter(voterId);
                        System.out.println(response);
                        break;

                    case "2":
                        System.out.print("Enter Party Name: ");
                        String partyName = scanner.nextLine();
                        response = stub.register_party(partyName);
                        System.out.println(response);
                        break;

                    case "3":
                        System.out.print("Enter Voter ID: ");
                        String voterIdVote = scanner.nextLine();
                        System.out.print("Enter Party Name: ");
                        String partyNameVote = scanner.nextLine();
                        response = stub.vote(voterIdVote, partyNameVote);
                        System.out.println(response);
                        break;

                    case "4":
                        Map<String, Integer> voteTally = stub.tally_votes();
                        for (Map.Entry<String, Integer> entry : voteTally.entrySet()) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        }
                        break;

                    case "0":
                        System.out.println("Exiting...\n");
                        return;

                    default:
                        System.out.println("Invalid choice! Please try again!");
                        break;
                }


                if (serverName == null) {
                    System.out.println("No servers available. Exiting...");
                    break;
                }
            
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        
    }
}

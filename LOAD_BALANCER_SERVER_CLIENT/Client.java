import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Instant;
import java.util.Map;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 3000); // Connect to load balancer
            LoadBalancerInterface loadBalancer = (LoadBalancerInterface) registry.lookup("LoadBalancer");
            
            String serverName = loadBalancer.getServerName();
            
            String[] parts = serverName.split(" ");
            int port = Integer.parseInt(parts[1]);
            System.out.println("Port number: " + port);

            // int port=(int)(serverName.charAt(serverName.length()-1)-'0')+1100;
            
            Registry serverRegistry = LocateRegistry.getRegistry("localhost", port); // Connect to selected server
            VotingInterface stub = (VotingInterface) serverRegistry.lookup(serverName);

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
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

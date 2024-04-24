import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Instant;
import java.util.*;

public class Client {

    public static void main(String[] args) {

        // Specify Ngrok forwarding URL and port
        String ngrokHost = "0.tcp.ap.ngrok.io:17648"; // Update with your Ngrok hostname or IP
        int ngrokPort = 1099;  // Update with your Ngrok port

        try {
            // Connect to the Ngrok-hosted RMI server
            Registry registry = LocateRegistry.getRegistry(ngrokHost, ngrokPort);
            VotingInterface stub = (VotingInterface) registry.lookup("Hello");

            // Get the server time
            Instant serverTime = stub.getServerTime();
            Instant clientTimeBeforeSync = Instant.now();
            System.out.print("Client start at time: " + clientTimeBeforeSync);

            // Calculate time difference
            long timeDifferenceMillis = serverTime.toEpochMilli() - clientTimeBeforeSync.toEpochMilli();
            System.out.print(" Time diff: " + timeDifferenceMillis);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Synchronize client time after each iteration
                Instant clientTime = Instant.now().plusMillis(timeDifferenceMillis);
                System.out.println(" Client time adjusted to: " + clientTime);
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

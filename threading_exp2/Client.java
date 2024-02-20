import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
// import java.util.HashMap;
// import java.util.Map;
import java.util.*;

public class Client {
    static String vId="";
    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        // System.out.println(host);
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Hello stub = (Hello) registry.lookup("Hello");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n-------------------------------");
                System.out.println("Voting Machine");
                System.out.println("-------------------------------");
                System.out.println("1. Register Voter");
                System.out.println("2. Register Party");
                System.out.println("3. Cast Vote");
                System.out.println("4. Tally Votes");
                System.out.println("5. Run continuously");
                System.out.println("0. Exit");
                System.out.println("-------------------------------");

                System.out.print("\nEnter your choice: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        System.out.print("Enter Voter ID: ");
                        String voterId = scanner.nextLine();
                        String response=stub.register_voter(voterId);
                        vId=voterId;
                        System.out.println(response);
                        // stub("register_voter", Map.of("voter_id", voterId));
                        break;

                    case "2":
                        System.out.print("Enter Party Name: ");
                        String partyName = scanner.nextLine();
                        response=stub.register_party(partyName);
                        System.out.println(response);
                        break;

                    case "3":
                        System.out.print("Enter Voter ID: ");
                        String voterIdVote = scanner.nextLine();
                        System.out.print("Enter Party Name: ");
                        String partyNameVote = scanner.nextLine();
                        response=stub.vote(voterIdVote,partyNameVote);
                        System.out.println(response);

                        break;
                    case "4":
                        Map<String, Integer> vote_tally =stub.tally_votes();
                        for(Map.Entry<String, Integer> entry : vote_tally.entrySet()){
                        System.out.println(entry.getKey()+" : "+entry.getValue());
                        }
                        
                        break;
                    case "5":
                        int i=0;
                        String pName=scanner.nextLine();

                        while(i<500){
                            response=stub.vote(vId,pName);
                            System.out.println(response);
                            i++;

                            try {
                                Thread.sleep(100); // Sleep for 1 second (1000 milliseconds)
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    
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

            // while(rue)            // String response = stub.sayHello();
            // System.out.println("response: ");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
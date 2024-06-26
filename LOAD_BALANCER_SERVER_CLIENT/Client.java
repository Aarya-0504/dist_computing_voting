// add package
// package LOAD_BALANCER_SERVER_CLIENT;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Instant;
import java.util.Map;
import java.util.Scanner;



public class Client {
    private static final int CHECK_INTERVAL_SECONDS = 10; // Adjust the interval as needed
    private static String serverName = null; // Adjust the interval as needed
    private static  int port = -1; // Adjust the interval as needed
    private static Registry serverRegistry;
    private static VotingInterface stub;
    private static LoadBalancerInterface loadBalancer;
    private static boolean isLoggedIn = false;
    private static String loggedInUsername;
    private static String passwordString;
    private static String voterID;


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

            
            Scanner scanner = new Scanner(System.in);

            while (true) {
                
                if (!isLoggedIn) {
                    System.out.println("-------------------------------");
                    System.out.println("Voting System");
                    System.out.println("-------------------------------");
                    System.out.println("1. Signup");
                    System.out.println("2. Login");
                    System.out.println("0. Exit");
                    System.out.println("-------------------------------");

                    System.out.print("\nEnter your choice: ");
                    String authChoice = scanner.nextLine();

                    switch (authChoice) {
                        case "1":
                            performSignup(scanner,stub);
                            break;
                        case "2":
                            System.out.println("called login");
                            String response = performLogin(scanner, stub);
                            System.out.println(response);
                            break;
                        case "0":
                            System.out.println("Exiting...\n");
                            System.exit(0);
                            // return;
                        default:
                            System.out.println("Invalid choice! Please try again!");
                            break;
                    }
                } 
                 
                else{

                System.out.println("\n-------------------------------");
                System.out.println("Voting Machine");
                System.out.println("-------------------------------");
                // System.out.println("1. Register Voter");
                System.out.println("1. Cast Vote");
                System.out.println("2. Tally Votes");
                System.out.println("3. Register Party");
                System.out.println("4. Exit");
                System.out.println("-------------------------------");

                System.out.print("\nEnter your choice: ");
                String choice = scanner.nextLine();

                String response=null;
                switch (choice) {
                    
                    case "1":

                    String electionId = null;

                    Map<String, Object> electionDetails = stub.fetchElectionDetail(); 
                    
                    if (electionDetails != null && !electionDetails.isEmpty()) {
                        for (Map.Entry<String, Object> entry : electionDetails.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                    
                            if (key.equals("electionId")) {
                                electionId = (String) value;
                            }
                    
                            System.out.println(key + ": " + value);
                        }

                        System.out.print("Enter Party Name: ");
                        String partyNameVote = scanner.nextLine();
                        
                        // Cast the vote
                        response = stub.vote(voterID, partyNameVote,electionId);
                        System.out.println(response);

                    } else {
                        System.out.println("No active Election Going On Right now !!");
                    }
                    

                        break;
                    
                    case "2":
                    System.out.println("Enter Election ID: ");
                    electionId = scanner.nextLine();

                    Map<String, Integer> voteTally = stub.tally_votes(electionId);
                    System.out.println("Vote Tally: ");
                    for (Map.Entry<String, Integer> entry : voteTally.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
                    break;

                    case "3":
                        System.out.print("Enter Party Name: ");
                        String partyName = scanner.nextLine();
                        response = stub.register_party(partyName);
                        System.out.println(response);
                        break;

                    case "0":
                        System.out.println("Exiting...\n");
                        System.exit(0);
                        // return;

                    default:
                        System.out.println("Invalid choice! Please try again!");
                        break;
                }
                
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

    private static void performSignup(Scanner scanner,VotingInterface stub) {
        System.out.println("-------------------------------");
        System.out.println("Welcome to Voting System Signup");
        System.out.println("-------------------------------");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = readPasswordFromConsole();
        passwordString=password;
        
        try{
            String ans=stub.signup(email,username,password);
            if(ans.split(" ")[0].equals("true"))
                System.out.println("Signup successful! Please login to proceed.");
            else
            System.out.println("User already Exists");
        }

        catch (Exception e) {
            System.err.println("RemoteException occurred: " + e.getMessage());
        }
    }

    private static String performLogin(Scanner scanner,VotingInterface stub) {
        System.out.println("-------------------------------");
        System.out.println("Login");
        System.out.println("-------------------------------");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = readPasswordFromConsole(); 

        try{
            String response=stub.login(email,password);

            if (response!=null) {
    
                System.out.println("Login successful! Welcome");
                voterID=response;
                isLoggedIn=true;
                return response;
            } else {
                System.out.println("Invalid email or password. Please try again!");
                return null;
            }
        }
        catch(Exception e){
            System.err.println("RemoteException occurred: " + e.getMessage());
        }

    return null;
    }


    private static String readPasswordFromConsole() {
        if (System.console() != null) {
        
            return new String(System.console().readPassword());
        } else {
        
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        }
    }



}

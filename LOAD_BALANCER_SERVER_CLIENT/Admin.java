import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.logging.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Admin {
    
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    public static void main(String[] args) {
        // Connect to MongoDB Atlas

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        // Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        String connectionString = "mongodb+srv://nikhilprajapati2:AT6QAz2cCfKKCOOI@cluster0.vzfozkt.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0&tls=true";
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase("DC_MINI_PROJECT");
        collection = database.getCollection("elections");

        Scanner scanner = new Scanner(System.in);

        for (int k = 0; k < 3; k++)
            System.out.println();

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("------------------Welcome to the Election Management System----------------------");
        System.out.println("--------------------------------------------------------------------------------");
        for (int k = 0; k < 3; k++)
            System.out.println();

        // Display menu options
        System.out.println("Select an option:");
        System.out.println("1. Register a new election");
        System.out.println("2. Update election status");
        System.out.println("3. Exit");

        // Input choice
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                addElection(scanner);
                break;
            case 2:
                updateElectionStatus(scanner);
                break;
            case 3:
                // Close the scanner and MongoDB client
                scanner.close();
                mongoClient.close();
                System.out.println("Exiting...");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    // Function to add election details
    private static void addElection(Scanner scanner) {
        System.out.println("Please enter the following details to register an election:");
        System.out.print("Enter Election ID: ");
        String electionId = scanner.nextLine();

        List<String> registeredParties = new ArrayList<>();
        while (true) {
            System.out.print("Enter Party Name (or type 'done' to finish): ");
            String partyName = scanner.nextLine();
            if (partyName.equalsIgnoreCase("done")) {
                break;
            }
            registeredParties.add(partyName);
        }

        // Add timestamp
        Date timestamp = new Date();

        // Initialize votersVoted as an empty array
        List<String> votersVoted = new ArrayList<>();

        // Initialize partyVotes with initial count of 0 for each party
        Map<String, Integer> partyVotes = new HashMap<>();
        for (String party : registeredParties) {
            partyVotes.put(party, 0);
        }

        Document electionDocument = new Document("electionId", electionId)
                .append("registeredParties", registeredParties)
                .append("election_Status", true)
                .append("timestamp", timestamp)
                .append("votersVoted", votersVoted)
                .append("partyVotes", partyVotes);

        collection.insertOne(electionDocument);
        System.out.println("Election registered successfully!");

    }

    // Function to update election status
    private static void updateElectionStatus(Scanner scanner) {
        System.out.print("Enter Election ID to update status: ");
        String electionId = scanner.nextLine();
        System.out.print("Enter new status (true/false): ");
        boolean status = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline character

        Document filter = new Document("electionId", electionId);
        Document update = new Document("$set", new Document("election_Status", status));
        collection.updateOne(filter, update);

        System.out.println("Election status updated successfully!");
    }
}

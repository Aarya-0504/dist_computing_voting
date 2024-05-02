import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin {
    String connectionString = "mongodb+srv://nikhilprajapati2:AT6QAz2cCfKKCOOI@cluster0.vzfozkt.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0&tls=true";
    MongoClient mongoClient = MongoClients.create(connectionString);
    private static final String DATABASE_NAME = "DC_MINI_PROJECT";
    private static final String COLLECTION_NAME = "elections";

    public static void main(String[] args) {
        // Create a MongoDB client
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        Scanner scanner = new Scanner(System.in);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("------------------Welcome to the Election Management System----------------------");
        System.out.println("--------------------------------------------------------------------------------");

        // Add election details
        addElectionDetails(collection, scanner);

        // Update election status
        updateElectionStatus(collection, scanner);

        // Close the scanner and MongoDB client
        scanner.close();
        mongoClient.close();
    }

    private static void addElectionDetails(MongoCollection<Document> collection, Scanner scanner) {
        System.out.println("Please enter the following details to register an election:");
        // Input election ID
        System.out.print("Enter Election ID: ");
        String electionId = scanner.nextLine();

        // Input parties
        List<String> registeredParties = new ArrayList<>();
        while (true) {
            System.out.print("Enter Party Name (or type 'done' to finish): ");
            String partyName = scanner.nextLine();
            if (partyName.equalsIgnoreCase("done")) {
                break;
            }
            registeredParties.add(partyName);
        }

        // Create a document to store election data
        Document electionDocument = new Document("electionId", electionId)
                .append("registeredParties", registeredParties)
                .append("election_Status", true); // Assuming true means active

        // Insert the document into the collection
        collection.insertOne(electionDocument);
    }

    private static void updateElectionStatus(MongoCollection<Document> collection, Scanner scanner) {
        System.out.println("Please enter the Election ID to update its status:");
        // Input election ID for status update
        System.out.print("Enter Election ID: ");
        String electionIdToUpdate = scanner.nextLine();

        // Check if the election ID exists in the collection
        Document query = new Document("electionId", electionIdToUpdate);
        Document election = collection.find(query).first();
        if (election != null) {
            // Input new status
            System.out.print("Enter New Status (true/false): ");
            boolean newStatus = scanner.nextBoolean();

            // Update the election status in the collection
            collection.updateOne(query, new Document("$set", new Document("election_Status", newStatus)));
            System.out.println("Election status updated successfully!");
        } else {
            System.out.println("Election ID not found!");
        }
        scanner.nextLine(); // Consume newline character after nextBoolean()
    }
}

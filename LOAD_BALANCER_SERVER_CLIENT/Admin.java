import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin {

    public static void main(String[] args) {
        // Create a MongoDB client
        String pass = System.getenv("CLUSTER_PASSOWRD");

        // Connect to MongoDB Atlas
        String connectionString = "mongodb+srv://nikhilprajapati2:AT6QAz2cCfKKCOOI@cluster0.vzfozkt.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0&tls=true";
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("DC_MINI_PROJECT");
        MongoCollection<Document> collection = database.getCollection("elections");
        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

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

        // Input party status
        List<Boolean> partyStatus = new ArrayList<>();
        for (String party : registeredParties) {
            System.out.print("Is Party " + party + " active? (true/false): ");
            boolean isActive = scanner.nextBoolean();
            partyStatus.add(isActive);
        }

        // Create a document to store election data
        Document electionDocument = new Document("electionId", electionId)
                .append("registeredParties", registeredParties)
                .append("partyStatus", partyStatus);

        // Insert the document into the collection
        collection.insertOne(electionDocument);

        // Close the scanner and MongoDB client
        scanner.close();
        mongoClient.close();
    }
}

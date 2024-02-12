// import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;

// public class Client {
//     public static void main(String[] args) {
//         try {
//             // Get the registry
//             Registry registry = LocateRegistry.getRegistry(null);

//             // Look up the remote object
//             Hello stub = (Hello) registry.lookup("Hello");

//             // Call the remote method
//             String response = stub.sayHello();
//             System.out.println("Response from server: " + response);
//         } catch (Exception e) {
//             System.err.println("Client exception: " + e.toString());
//             e.printStackTrace();
//         }
//     }
// }

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
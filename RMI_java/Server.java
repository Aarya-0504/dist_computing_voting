// import java.rmi.registry.LocateRegistry;
// import java.rmi.registry.Registry;

// public class Server {
//     public static void main(String[] args) {
//         try {
//             // Create and export the remote object
//             HelloImpl obj = new HelloImpl();

//             // Create RMI registry on port 3000
//             Registry registry = LocateRegistry.createRegistry(3000);

//             // Bind the remote object's stub in the registry
//             registry.rebind("Hello", obj);

//             System.out.println("RMI Server running on port 3000");
//         } catch (Exception e) {
//             System.err.println("Server exception: " + e.toString());
//             e.printStackTrace();
//         }
//     }
// }


import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
        
public class Server implements Hello {
        
    public Server() {}

    public String sayHello() {
        return "Hello, world!";
    }
        
    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Hello", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

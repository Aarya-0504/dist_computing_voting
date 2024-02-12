// import java.rmi.Remote;
// import java.rmi.RemoteException;

// // Remote interface
// public interface Hello extends Remote {
//     // Remote method declaration
//     String sayHello() throws RemoteException;
// }


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {
    String sayHello() throws RemoteException;
}

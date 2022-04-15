package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionCommunication extends Remote {
    String uniqueReverse(String input) throws RemoteException;
}

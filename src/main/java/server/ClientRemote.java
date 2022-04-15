package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemote extends Remote {
    public String notify(String message) throws RemoteException;

}

package at.aau.se2.gamma.core.network;
import at.aau.se2.gamma.core.models.impl.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionCommunicationInterface extends Remote {
    GameState getGameObject(Session session) throws RemoteException;
}

package server;

import models.impl.GameState;
import models.impl.Session;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionCommunicationInterface extends Remote {
    GameState getGameObject(Session session) throws RemoteException;
}

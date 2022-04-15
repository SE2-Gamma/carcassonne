package server;

import models.impl.GameState;
import models.impl.Session;

import java.rmi.RemoteException;

public class SessionCommunication implements SessionCommunicationInterface{
    @Override
    public GameState getGameObject(Session session) throws RemoteException {
        return new GameState();
    }
}

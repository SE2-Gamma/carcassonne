package at.aau.se2.gamma.core.network;
import at.aau.se2.gamma.core.models.impl.*;
import java.rmi.RemoteException;



public class SessionCommunication implements SessionCommunicationInterface{
    @Override
    public GameState getGameObject(Session session) throws RemoteException {
        return new GameState();
    }


}

package at.aau.se2.gamma.server.models;

import at.aau.se2.gamma.core.models.interfaces.PlayerInterface;
import at.aau.se2.gamma.server.ClientThread;

public class Player implements PlayerInterface {
    private ClientThread clientThread;
    private String id;
    private String name;

    public ClientThread getClientThread() {
        return clientThread;
    }

    public void setClientThread(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

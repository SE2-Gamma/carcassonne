package at.aau.se2.gamma.core.network;

import java.io.Serializable;

public class ClientRemoteImpl implements ClientRemote, Serializable {
    @Override
    public String notify(String message) {
        System.out.println(message);
        return message;
    }
}
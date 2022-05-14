import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.commands.DisconnectCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestSocket {
    public TestSocket(Socket socket, SecureObjectInputStream secureObjectInputStream, ObjectOutputStream objectOutputStream) {
        this.socket = socket;
        this.secureObjectInputStream = secureObjectInputStream;
        this.objectOutputStream = objectOutputStream;
    }
    public void disconnect(){
        try {
           objectOutputStream.writeObject(new DisconnectCommand(null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket socket;
    SecureObjectInputStream secureObjectInputStream;
    ObjectOutputStream objectOutputStream;

}

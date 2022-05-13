import at.aau.se2.gamma.core.SecureObjectInputStream;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestSocket {
    public TestSocket(Socket socket, SecureObjectInputStream secureObjectInputStream, ObjectOutputStream objectOutputStream) {
        this.socket = socket;
        this.secureObjectInputStream = secureObjectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    Socket socket;
    SecureObjectInputStream secureObjectInputStream;
    ObjectOutputStream objectOutputStream;

}

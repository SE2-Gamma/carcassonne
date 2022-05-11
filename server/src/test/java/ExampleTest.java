
import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.commands.DisconnectCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.utils.GlobalVariables;
import at.aau.se2.gamma.server.ClientThread;
import at.aau.se2.gamma.server.Server;
import at.aau.se2.gamma.server.models.ServerPlayer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import at.aau.se2.gamma.core.models.impl.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ExampleTest {
    // this is for the git demo
    private ObjectOutputStream objectOutputStream;
    private SecureObjectInputStream objectInputStream;

    @BeforeAll
    public static void setup()  {


        InetAddress ip= null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String hostname= ip.getHostAddress();

        System.out.println(hostname);
        GlobalVariables.setAdress(hostname);
        Server.startServer();

    }
    @AfterAll
    public static void stopserver(){
        Server.closeServer();
    }
    @Test
    public void failure() {
        assertEquals(4, 2+2);
    }

    @Test
    public void baseTest() {
        try {
            Socket socket= new Socket(GlobalVariables.getAdress(),GlobalVariables.getPort());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new SecureObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(new InitialSetNameCommand("Bodo"));
            objectOutputStream.writeObject(new DisconnectCommand(null));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

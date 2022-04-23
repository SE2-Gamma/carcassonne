package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.utils.globalVariables;
import org.junit.jupiter.api.BeforeEach;

import java.net.Socket;

public class ServerTests {
Socket socket=null;
    @BeforeEach
    public void buildConnection(){
        socket=new Socket(globalVariables.getAdress(),globalVariables.getPort());
    }


}

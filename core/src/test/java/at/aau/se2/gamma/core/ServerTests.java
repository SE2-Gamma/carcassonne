package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.utils.globalVariables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;


import static org.junit.jupiter.api.Assertions.*;


public class ServerTests {
Socket socket=null;
ObjectInputStream objectInputStream=null;
ObjectOutputStream objectOutputStream=null;

    @BeforeEach
    public void buildConnection(){
        try {
            socket=new Socket(globalVariables.getAdress(),globalVariables.getPort());
            objectInputStream=new ObjectInputStream(socket.getInputStream());
            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testInitialSetName() {
        String ID = null;
        try {
            objectOutputStream.writeObject("myName");
            ID = (String) objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(ID, "0");

    }

}

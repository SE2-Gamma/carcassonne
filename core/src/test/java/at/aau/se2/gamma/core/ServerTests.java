package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.commands.DisconnectCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.commands.PayloadResponseCommand;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;
import at.aau.se2.gamma.core.utils.ServerResponseDecrypter;
import at.aau.se2.gamma.core.utils.globalVariables;

import org.junit.jupiter.api.AfterEach;
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

            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectInputStream=new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @AfterEach
    public void destroyConnection(){
        try {
            objectOutputStream.writeObject(new DisconnectCommand(null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInitialSetName() {
        String ID = null;
        try {
            objectOutputStream.writeObject(new InitialSetNameCommand("bodo"));
            ID= (String) ServerResponseDecrypter.payloadRetriever(objectInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(ID, "0");

    }


}

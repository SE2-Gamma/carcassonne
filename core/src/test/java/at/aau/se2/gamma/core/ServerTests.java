package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.commands.CreateGameCommand;
import at.aau.se2.gamma.core.commands.DisconnectCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.commands.RequestUserListCommand;
import at.aau.se2.gamma.core.models.impl.Session;
import at.aau.se2.gamma.core.utils.ServerResponseDecrypter;
import at.aau.se2.gamma.core.utils.globalVariables;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;


import static org.junit.jupiter.api.Assertions.*;


public class ServerTests {
Socket socket=null;
ObjectInputStream in =null;
ObjectOutputStream out =null;

    @BeforeEach
    public void buildConnection(){
        try {
            socket=new Socket(globalVariables.getAdress(),globalVariables.getPort());
            out =new ObjectOutputStream(socket.getOutputStream());
            in =new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @AfterEach
    public void destroyConnection(){
        try {
            out.writeObject(new DisconnectCommand(null));
            ServerResponseDecrypter.payloadRetriever(in);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testInitialSetName() {
        String ID = null;

        try {
            out.writeObject(new InitialSetNameCommand("test"));
            ID= (String) ServerResponseDecrypter.payloadRetriever(in);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(ID, "0");


    }

    @Test
    void TestCreateLobby() {
        try {
            out.writeObject(new InitialSetNameCommand("test"));
            ServerResponseDecrypter.payloadRetriever(in);
            out.writeObject(new CreateGameCommand("testGame"));
            Session session=(Session)ServerResponseDecrypter.payloadRetriever(in);
            assertEquals(session.getId(),"testGame");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    void TestRequestUserList() {
        LinkedList<String> list = null;
        try {
            out.writeObject(new InitialSetNameCommand("test"));
            ServerResponseDecrypter.payloadRetriever(in);
            out.writeObject(new CreateGameCommand("testgame"));
            ServerResponseDecrypter.payloadRetriever(in);
            out.writeObject(new RequestUserListCommand(null));
            list = (LinkedList<String>) ServerResponseDecrypter.payloadRetriever(in);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(list.pop(),"test");
    }
    public class secondSocket extends Thread{
       public ObjectInputStream in2=null;
      public   ObjectOutputStream out2=null;
      boolean running=false;
        @Override
        public void run() {
running=true;
            try {
                Socket socket2=new Socket(globalVariables.getAdress(),globalVariables.getPort());

                ObjectInputStream in2 =new ObjectInputStream(socket2.getInputStream());
                ObjectOutputStream out2 =new ObjectOutputStream(socket2.getOutputStream());
        while(running){

        }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   /* @Test
    void testJoinGameCommand(){
        try {
            secondSocket socket2=new secondSocket();
            socket2.start();

            System.err.println("here");


            socket2.out2.writeObject(new InitialSetNameCommand("test2"));
            ServerResponseDecrypter.payloadRetriever(socket2.in2);

            socket2.out2.writeObject(new CreateGameCommand("myGame"));


            ServerResponseDecrypter.payloadRetriever(socket2.in2);

            out.writeObject(new InitialSetNameCommand("test1"));

            ServerResponseDecrypter.payloadRetriever(in);

            out.writeObject(new InitialJoinCommand("myGame"));


            ServerResponseDecrypter.payloadRetriever(socket2.in2);

            out.writeObject(new RequestUserListCommand(null));

            socket2.out2.writeObject(new RequestUserListCommand(null));

            LinkedList list1= (LinkedList)ServerResponseDecrypter.payloadRetriever(in);

            LinkedList list2= (LinkedList)ServerResponseDecrypter.payloadRetriever(socket2.in2);

            assertEquals(list1.pop(),list2.pop());
            assertEquals(list1.pop(),list2.pop());
            socket2.running=false;



        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

}

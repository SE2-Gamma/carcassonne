import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.commands.BroadcastCommands.PlayerJoinedBroadcastCommand;
import at.aau.se2.gamma.core.commands.CreateGameCommand;
import at.aau.se2.gamma.core.commands.*;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.core.utils.GlobalVariables;
import at.aau.se2.gamma.core.utils.ServerResponseDecrypter;
import at.aau.se2.gamma.server.ClientThread;
import at.aau.se2.gamma.server.Server;
import at.aau.se2.gamma.server.models.ServerPlayer;
import at.aau.se2.gamma.server.models.Session;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import at.aau.se2.gamma.core.models.impl.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

public class ExampleTest {
    // this is for the git demo
    private ObjectOutputStream objectOutputStream;
    private SecureObjectInputStream objectInputStream;
    Socket socket;


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
    @BeforeEach
    public void createTestPlayer(){
        System.err.println("-------------------------------------------------------------------testing----------------------------");

        try {
            System.out.println("TEST: creating a testplayer");
            socket= new Socket(GlobalVariables.getAdress(),GlobalVariables.getPort());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new SecureObjectInputStream(socket.getInputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public TestSocket createanotherSocket(String name){
        try {
            Socket anothersocket= new Socket(GlobalVariables.getAdress(),GlobalVariables.getPort());
            ObjectOutputStream anotheroutput = new ObjectOutputStream(anothersocket.getOutputStream());
            SecureObjectInputStream anotherinput = new SecureObjectInputStream(anothersocket.getInputStream());
            anotheroutput.writeObject(new InitialSetNameCommand(name));
            ServerResponseDecrypter.payloadRetriever(anotherinput);
            return new TestSocket(anothersocket,anotherinput,anotheroutput);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;


    }


    public void sendName(String name){
        try {
            objectOutputStream.writeObject(new InitialSetNameCommand(name));
            ServerResponseDecrypter.payloadRetriever(objectInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    void TestCreateLobby() {

        try {
            sendName("testcreatelobby");
            objectOutputStream.writeObject(new CreateGameCommand("testcreatelobby"));
            String session=(String)ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("Game Created",session);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @Test
    void testRequestUserList() {

        try {
            sendName("testrequestuserlist");
            TestSocket testSocket=createanotherSocket("testrequestuserlist2");
            objectOutputStream.writeObject(new CreateGameCommand("testrequestuserlist"));
            ServerResponseDecrypter.payloadRetriever(objectInputStream);

            testSocket.objectOutputStream.writeObject(new InitialJoinCommand("testrequestuserlist"));
            ServerResponseDecrypter.payloadRetriever(testSocket.secureObjectInputStream);


            objectOutputStream.writeObject(new RequestUserListCommand(null));
            String broadcast= (String) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("testrequestuserlist2",broadcast);
            LinkedList<String>userlist= (LinkedList<String>) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("testrequestuserlist",userlist.pop());
            assertEquals("testrequestuserlist2",userlist.pop());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @Test
    void testInitialJoin(){
        try {
            sendName("testinitialjoin");
            TestSocket anothersocket=createanotherSocket("testinitialjoin2");
            objectOutputStream.writeObject(new CreateGameCommand("testinitialjoin"));
            ServerResponseDecrypter.payloadRetriever(objectInputStream);

            anothersocket.objectOutputStream.writeObject(new InitialJoinCommand("testinitialjoin"));
            String broadcast1= (String) ServerResponseDecrypter.payloadRetriever(anothersocket.secureObjectInputStream);
            assertEquals("testinitialjoin2",broadcast1);
            String anotherResponse=(String)ServerResponseDecrypter.payloadRetriever(anothersocket.secureObjectInputStream);
            assertEquals("successfully joined",anotherResponse);

            String broadcast2= (String) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("testinitialjoin2",broadcast2);


            objectOutputStream.writeObject(new GetClientStateCommand(null));
            ClientState state=(ClientState) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals(ClientState.LOBBY,state);

            anothersocket.objectOutputStream.writeObject(new GetClientStateCommand(null));
            ClientState anotherstate=(ClientState) ServerResponseDecrypter.payloadRetriever(anothersocket.secureObjectInputStream);
            assertEquals(ClientState.LOBBY,state);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @Test
    void testkickplayer(){
        sendName("testkick1");
        TestSocket socket1=createanotherSocket("testkick2");
        TestSocket socket2=createanotherSocket("testkick3");
        TestSocket socket3=createanotherSocket("testkick4");
        TestSocket socket4=createanotherSocket("testkick5");

        try {
            objectOutputStream.writeObject(new CreateGameCommand("testkick"));
            ServerResponseDecrypter.payloadRetriever(objectInputStream);


            socket1.objectOutputStream.writeObject(new InitialJoinCommand("testkick"));
            ServerResponseDecrypter.payloadRetriever(socket1.secureObjectInputStream);
            ServerResponseDecrypter.payloadRetriever(socket1.secureObjectInputStream);


            socket2.objectOutputStream.writeObject(new InitialJoinCommand("testkick"));
            ServerResponseDecrypter.payloadRetriever(socket2.secureObjectInputStream);
            ServerResponseDecrypter.payloadRetriever(socket2.secureObjectInputStream);


            socket3.objectOutputStream.writeObject(new InitialJoinCommand("testkick"));
            ServerResponseDecrypter.payloadRetriever(socket3.secureObjectInputStream);
            ServerResponseDecrypter.payloadRetriever(socket3.secureObjectInputStream);

            socket4.objectOutputStream.writeObject(new InitialJoinCommand("testkick"));
            ServerResponseDecrypter.payloadRetriever(socket4.secureObjectInputStream);
            ServerResponseDecrypter.payloadRetriever(socket4.secureObjectInputStream);

            ServerResponseDecrypter.payloadRetriever(objectInputStream);
            ServerResponseDecrypter.payloadRetriever(objectInputStream);
            ServerResponseDecrypter.payloadRetriever(objectInputStream);
            ServerResponseDecrypter.payloadRetriever(objectInputStream);

            ServerResponseDecrypter.payloadRetriever(socket1.secureObjectInputStream);
            ServerResponseDecrypter.payloadRetriever(socket1.secureObjectInputStream);
            ServerResponseDecrypter.payloadRetriever(socket1.secureObjectInputStream);

            ServerResponseDecrypter.payloadRetriever(socket2.secureObjectInputStream);
            ServerResponseDecrypter.payloadRetriever(socket2.secureObjectInputStream);


            objectOutputStream.writeObject(new RequestUserListCommand(null));
            LinkedList<String>list=(LinkedList<String>) ServerResponseDecrypter.payloadRetriever(objectInputStream);

            assertEquals("testkick1",list.pop());
            assertEquals("testkick2",list.pop());
            assertEquals("testkick3",list.pop());
            assertEquals("testkick4",list.pop());
            assertEquals("testkick5",list.pop());
            System.out.println("starting kick attempt");

            objectOutputStream.writeObject(new KickPlayerCommand("testkick5"));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socket1.objectOutputStream.writeObject(new KickPlayerCommand("testkick5"));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            socket2.objectOutputStream.writeObject(new KickPlayerCommand("testkick5"));

            ServerResponseDecrypter.payloadRetriever(objectInputStream);
            ServerResponseDecrypter.payloadRetriever(objectInputStream);
            ServerResponseDecrypter.payloadRetriever(objectInputStream);
            ServerResponseDecrypter.payloadRetriever(objectInputStream);

              String kickedplayer= (String) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("testkick5",kickedplayer);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    @Test
    void testclientstate(){
        sendName("testclientstate");
        ClientState state;
        try {
            objectOutputStream.writeObject(new GetClientStateCommand(null));
             state=(ClientState) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals(ClientState.INITIAl,state);

            objectOutputStream.writeObject(new CreateGameCommand("testclientstate"));
            ServerResponseDecrypter.payloadRetriever(objectInputStream);

            objectOutputStream.writeObject(new GetClientStateCommand(null));
            state=(ClientState) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals(ClientState.LOBBY,state);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    @Test
    void testLeavelobby(){
        sendName("leavelobby");
        try {
            TestSocket anothersocket=createanotherSocket("leavelobby1");
            objectOutputStream.writeObject(new CreateGameCommand("leavelobby"));
            anothersocket.objectOutputStream.writeObject(new InitialJoinCommand("leavelobby"));
            anothersocket.objectOutputStream.writeObject(new LeaveLobbyCommand(null));

           String response=(String) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("Game Created",response);
             response=(String) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("leavelobby1",response);
            response=(String) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("leavelobby1",response);

            objectOutputStream.writeObject(new RequestUserListCommand(null));
            LinkedList<String>namelist=(LinkedList<String>) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("leavelobby",namelist.pop());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
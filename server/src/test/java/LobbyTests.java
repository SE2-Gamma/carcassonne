import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.commands.CreateGameCommand;
import at.aau.se2.gamma.core.commands.*;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.*;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.core.utils.GlobalVariables;
import at.aau.se2.gamma.core.utils.ServerResponseDecrypter;
import at.aau.se2.gamma.server.Server;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class LobbyTests {
    // this is for the git demo
    private ObjectOutputStream objectOutputStream;
    private SecureObjectInputStream objectInputStream;
    Socket socket;
    static final int numberofruns=1
            ;
    public  LinkedList<Object>returncommands=new LinkedList<>();
    ResponseConsumer responseConsumer=null;

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
    public class ResponseConsumer extends Thread{
        SecureObjectInputStream in;

        boolean running=true;
        @Override
        public void run() {
            while(running){
                try {
                    synchronized (returncommands) {
                        Object object = ServerResponseDecrypter.payloadRetriever(objectInputStream);
                        returncommands.add(object);
                        System.err.println("//-----------------------------------------" + object + " added to responses--------------------------------------------/");

                    }

                } catch (IOException | ClassNotFoundException  | NullPointerException ignored){

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
            responseConsumer= new ResponseConsumer();

            waitForResponse(200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void waitForResponse(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void waitForResponse(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void sendCommand(BaseCommand command,ObjectOutputStream out){
        try {
            out.writeObject(command);waitForResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendCommand(BaseCommand command){
        try {
            objectOutputStream.writeObject(command);waitForResponse();
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
    public TestSocket createanotherSocket(String name,boolean withConsumer){
        try {
            Socket anothersocket= new Socket(GlobalVariables.getAdress(),GlobalVariables.getPort());
            ObjectOutputStream anotheroutput = new ObjectOutputStream(anothersocket.getOutputStream());
            SecureObjectInputStream anotherinput = new SecureObjectInputStream(anothersocket.getInputStream());
            anotheroutput.writeObject(new InitialSetNameCommand(name));
            ServerResponseDecrypter.payloadRetriever(anotherinput);
            return new TestSocket(anothersocket,anotherinput,anotheroutput,withConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;


    }
    @AfterEach
    public void disconnect(){
        try {
            objectOutputStream.writeObject(new DisconnectCommand(null));
            responseConsumer.running=false;
            responseConsumer.interrupt();
            System.out.println(returncommands);
            returncommands.clear();
            Server.kickAllAndEverything();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @RepeatedTest(1)
    void stormTheMainMenue(){
        sendName("storm");
        ClientState state= null;
        try {

                   objectOutputStream.writeObject(new GetClientStateCommand(null));
                   state=(ClientState)ServerResponseDecrypter.payloadRetriever(objectInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(ClientState.INITIAl,state);
    }//works without a problem
    @RepeatedTest(1)
    void stormTheMainMenueWIthoutDisconnect(){

        ClientState state= null;
        for (int i = 0; i < 1; i++) {
            try {
                System.out.println("TEST: creating a testplayer");
                socket= new Socket(GlobalVariables.getAdress(),GlobalVariables.getPort());
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new SecureObjectInputStream(socket.getInputStream());


            } catch (IOException e) {
                e.printStackTrace();
            }  try {
                objectOutputStream.writeObject(new GetClientStateCommand(null));
                state=(ClientState)ServerResponseDecrypter.payloadRetriever(objectInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            assertEquals(ClientState.INITIAl,state);
        }

    }//runs out of ports
    @RepeatedTest(numberofruns)
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
    @RepeatedTest(numberofruns)
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
            testSocket.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @RepeatedTest(numberofruns)
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
            assertEquals(ClientState.LOBBY,anotherstate);

            anothersocket.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @Test
    void testGameMapEntryWhitelist(){

        System.out.println(GameMapEntry.class.getName());
        System.out.println(GameCard.SpecialType.class.getName());
        sendName("GameMapEntryTest");
        GameMapEntry entry= new GameMapEntry(GameCardFactory.A(),new Player("testid","testplayerwer0"));
        GameMap gameMap=new GameMap();
        gameMap.placeGameMapEntry(entry,new GameMapEntryPosition(4,7));
        GameObject gameObject=new GameObject(gameMap);

        GameObject gameObject1=new GameObject(new GameMap());

        try {

            objectOutputStream.writeObject(new GameTurnCommand(gameObject1));
            System.out.println("empty gameobject finished");
            objectOutputStream.writeObject(new GameTurnCommand(gameObject));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RepeatedTest(numberofruns)
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


            socket1.objectOutputStream.writeObject(new KickPlayerCommand("testkick5"));

            socket2.objectOutputStream.writeObject(new KickPlayerCommand("testkick5"));


            ServerResponseDecrypter.payloadRetriever(objectInputStream);
            ServerResponseDecrypter.payloadRetriever(objectInputStream);
            ServerResponseDecrypter.payloadRetriever(objectInputStream);

              String kickedplayer= (String) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("testkick5",kickedplayer);

            objectOutputStream.writeObject(new RequestUserListCommand(null));
            LinkedList<String>afterlist=(LinkedList<String>) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            System.out.println(Arrays.toString(afterlist.toArray()));
            assertEquals("testkick1",afterlist.pop());
            assertEquals("testkick2",afterlist.pop());
            assertEquals("testkick3",afterlist.pop());
            assertEquals("testkick4",afterlist.pop());
            assertThrows(NoSuchElementException.class, list::pop);


socket1.disconnect();
socket2.disconnect();
socket3.disconnect();
socket4.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    @RepeatedTest(numberofruns)
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
    @RepeatedTest(numberofruns)
    void testLeavelobby(){
        sendName("leavelobby");

        LinkedList<Object>responses=new LinkedList<>();
        try {
            TestSocket anothersocket=createanotherSocket("leavelobby1");
            objectOutputStream.writeObject(new CreateGameCommand("leavelobby"));
            responses.add(ServerResponseDecrypter.payloadRetriever(objectInputStream));
            anothersocket.objectOutputStream.writeObject(new InitialJoinCommand("leavelobby"));
            anothersocket.objectOutputStream.writeObject(new LeaveLobbyCommand(null));

            responses.add(ServerResponseDecrypter.payloadRetriever(objectInputStream));
            responses.add(ServerResponseDecrypter.payloadRetriever(objectInputStream));
            assertTrue(responses.contains("leavelobby1"));
            assertTrue(responses.contains("Game Created"));

            objectOutputStream.writeObject(new RequestUserListCommand(null));
            LinkedList<String>namelist=(LinkedList<String>) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("leavelobby",namelist.pop());
            objectOutputStream.writeObject(new LeaveLobbyCommand(null));
            anothersocket.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @RepeatedTest(numberofruns)
    void testLeavingLastLobby(){
        sendName("leavinglastlobby");
        try {
            TestSocket anothertest=createanotherSocket("leavinglastlobby2");
            objectOutputStream.writeObject(new CreateGameCommand("leavinglastlobby"));
            ServerResponseDecrypter.payloadRetriever(objectInputStream);
             anothertest.objectOutputStream.writeObject(new CreateGameCommand("leavinglastlobby"));
           LinkedList<Object>errorlist=(LinkedList<Object>)ServerResponseDecrypter.payloadRetriever(anothertest.secureObjectInputStream);

           assertEquals("Server bereits vorhanden", errorlist.pop());
           objectOutputStream.writeObject(new LeaveLobbyCommand(null));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            anothertest.objectOutputStream.writeObject(new CreateGameCommand("leavinglastlobby"));
            String result=(String)ServerResponseDecrypter.payloadRetriever(anothertest.secureObjectInputStream);
            assertEquals("Game Created",result);
            anothertest.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @RepeatedTest(1)
    void testplayerready(){
        sendName("playerready");
        try {
            objectOutputStream.writeObject(new CreateGameCommand("playerready"));
            String creategameresponse=(String)ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("Game Created",creategameresponse);

            objectOutputStream.writeObject(new PlayerReadyCommand(null));
            GameObject gameobject=(GameObject) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertNotNull(gameobject);

            String response=(String)ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals("Youre ready now",response);
            GameCard gameCard=(GameCard)ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertNotNull(gameCard);

            objectOutputStream.writeObject(new GetClientStateCommand(null));
            ClientState state=(ClientState) ServerResponseDecrypter.payloadRetriever(objectInputStream);
            assertEquals(ClientState.GAME,state);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @Test
    void joinStartedGame(){
        sendName("joinstartedgame");
        TestSocket playertwo=createanotherSocket("startedgame2");
        ResponseConsumer consumer=new ResponseConsumer();
        consumer.start();
        sendCommand(new CreateGameCommand("joinstartedgame"));
        sendCommand(new PlayerReadyCommand(null));
       // sendCommand(new (null));

    }
    @Test
    void unexcpectedlyDisconnectFromLobby(){
        sendName("unexpectedlydisconnectfromlobby");
        responseConsumer.start();
        TestSocket anothersocket=createanotherSocket("unexdisc");
        sendCommand(new CreateGameCommand("disconnectunexpectedly"));
        sendCommand(new InitialJoinCommand("disconnectunexpectedly"),anothersocket.objectOutputStream);
        try {
            anothersocket.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitForResponse();
        sendCommand(new RequestUserListCommand(null));
        LinkedList<String>list2=(LinkedList<String>) returncommands.getLast();
        assertTrue(list2.contains("unexpectedlydisconnectfromlobby"));
        assertFalse(list2.contains("unexdisc"));

    }


}
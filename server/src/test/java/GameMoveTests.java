import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.commands.*;
import at.aau.se2.gamma.core.commands.GameTurnCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.commands.error.Codes;
import at.aau.se2.gamma.core.exceptions.NoSurroundingCardGameMapException;
import at.aau.se2.gamma.core.exceptions.PositionNotFreeGameMapException;
import at.aau.se2.gamma.core.exceptions.SurroundingConflictGameMapException;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.*;
import at.aau.se2.gamma.core.utils.GlobalVariables;
import at.aau.se2.gamma.core.utils.ServerResponseDecrypter;
import at.aau.se2.gamma.server.Server;
import org.junit.jupiter.api.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameMoveTests {
    //die logik dieser tests wurde von martin gestohlen, ich prüfe hier nur ob sie auch funktionieren wenn das spiel am server läuft
    GameMap gameMap;
    Player player1;
    public  ObjectOutputStream objectOutputStream;
    public  SecureObjectInputStream objectInputStream;
    ResponseConsumer responseConsumer=null;
    Socket socket;
    static final int numberofruns=30;
   public  LinkedList<Object>returncommands=new LinkedList<>();
    public class ResponseConsumer extends Thread{
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

                } catch (EOFException ignored){

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

            System.out.println("TEST: creating a testplayer");
            socket= new Socket(GlobalVariables.getAdress(),GlobalVariables.getPort());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new SecureObjectInputStream(socket.getInputStream());
            responseConsumer=new ResponseConsumer();
            responseConsumer.start();
            waitForResponse(200);
            sendName("player 1");
            waitForResponse(200);
            Object o =returncommands.getLast();
            if(o instanceof String){
                System.out.println("is this an ID?"+o);

            }


            objectOutputStream.writeObject(new CreateGameCommand("testGame"));waitForResponse();
            objectOutputStream.writeObject(new PlayerReadyCommand(null));waitForResponse();
            player1 = new Player((String)o, "player 1");

            waitForResponse();

            waitForResponse();



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
    @AfterEach
    public void disconnect(){
       // waitForResponse(1000);
        System.out.println("closing everything intentionally");
        responseConsumer.running=false;
        responseConsumer.interrupt();
        Server.kickAllAndEverything();
       Server.closeServer();
        System.out.println(returncommands);
        returncommands.clear();

    }

    public void sendName(String name){
        try {
            objectOutputStream.writeObject(new InitialSetNameCommand(name));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void positionInGameMap() {


        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.NORTH);

        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(48,49));
        try {
            waitForResponse(4000);
            objectOutputStream.writeObject(new GameTurnCommand(gameMove));
            waitForResponse();
            LinkedList<Object>error= (LinkedList<Object>) returncommands.getLast();
            String response= (String) error.pop();
            assertEquals("Surrounding Conflict on Gamemap",response);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void positionNotFree() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.NORTH);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(49,49));
        try {
            waitForResponse(4000);
            objectOutputStream.writeObject(new GameTurnCommand(gameMove));
            waitForResponse();

            LinkedList<Object>error= (LinkedList<Object>) returncommands.getLast();
            String response= (String) error.pop();
            assertEquals("Position not free on Gamemap",response);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void noSurroundings() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.NORTH);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(51,49));
        System.out.println();
        System.out.println("testing first incorrect move");
        try {
            waitForResponse(4000);
            objectOutputStream.writeObject(new GameTurnCommand(gameMove));waitForResponse();

            System.out.println(returncommands);
            LinkedList<Object>error= (LinkedList<Object>) returncommands.getLast();
            String response= (String) error.pop();
            assertEquals("No surrounding Card on Gamemap",response);


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("testing second incorrect move");
        GameMove gameMove2 = new GameMove(player1, entry, new GameMapEntryPosition(50,50));
        try {
            objectOutputStream.writeObject(new GameTurnCommand(gameMove2));

            waitForResponse();
            System.out.println(returncommands);
            LinkedList<Object>error= (LinkedList<Object>) returncommands.getLast();
            Codes.ERROR response= (Codes.ERROR) error.getLast();
            assertEquals(Codes.ERROR.INVALID_MOVE,response);


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("testing third incorrect move");

        GameMove gameMove3 = new GameMove(player1, entry, new GameMapEntryPosition(49,51));
        try {
            objectOutputStream.writeObject(new GameTurnCommand(gameMove3));
            waitForResponse();
            System.out.println(returncommands);
            LinkedList<Object>error= (LinkedList<Object>) returncommands.getLast();
            String response= (String) error.pop();
            assertEquals("No surrounding Card on Gamemap",response);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void surroundingConflict() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.EAST);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(50,49));

        /**
         *  G    C
         * S C  G S
         *  S    S
         */

        try {
            waitForResponse(4000);
            objectOutputStream.writeObject(new GameTurnCommand(gameMove));waitForResponse();

            System.out.println(returncommands);
            LinkedList<Object>error= (LinkedList<Object>) returncommands.getLast();
            String response= (String) error.pop();
            assertEquals("Surrounding Conflict on Gamemap",response);


        } catch (IOException e) {
            e.printStackTrace();
        }

    GameMapEntry entry2 = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.NORTH);
    GameMove gameMove2 = new GameMove(player1, entry2, new GameMapEntryPosition(50,49));

    /**
     *  G    G
     * S C  S C
     *  S    S
     */

        try {
            objectOutputStream.writeObject(new GameTurnCommand(gameMove2));
            waitForResponse();
            System.out.println(returncommands);
            LinkedList<Object>error= (LinkedList<Object>) returncommands.getLast();
            Codes.ERROR response= (Codes.ERROR) error.getLast();
            assertEquals(Codes.ERROR.INVALID_MOVE,response);


        } catch (IOException e) {
            e.printStackTrace();
        }
}

    @Test
    public void success() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.SOUTH);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(50,49));

        /**
         *  G    S
         * S C  C S
         *  S    G
         */

        try {
            waitForResponse(4000);
            objectOutputStream.writeObject(new GameTurnCommand(gameMove));
           waitForResponse();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(returncommands.contains("turn succesfull"));

    }
}

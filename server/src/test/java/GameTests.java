import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.commands.*;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.commands.error.Codes;
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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


public class GameTests {
    public ObjectOutputStream objectOutputStream;
    public SecureObjectInputStream objectInputStream;
    ResponseConsumer responseConsumer = null;
    Socket socket;
    Player testplayer;
    static final int numberofruns = 30;
    public LinkedList<Object> returncommands = new LinkedList<>();

    public class ResponseConsumer extends Thread {
        SecureObjectInputStream in;

        boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    synchronized (returncommands) {
                        Object object = ServerResponseDecrypter.payloadRetriever(objectInputStream);
                        returncommands.add(object);
                        System.err.println("//-----------------------------------------" + object + " added to responses--------------------------------------------/");

                    }

                } catch (IOException | ClassNotFoundException | NullPointerException ignored) {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @AfterAll
    public static void stopserver() {
        Server.closeServer();
    }

    @BeforeEach
    public void createTestPlayer() {
        System.err.println("-------------------------------------------------------------------testing----------------------------");

        try {
            InetAddress ip = null;
            try {
                ip = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            String hostname = ip.getHostAddress();

            System.out.println(hostname);
            GlobalVariables.setAdress(hostname);
            Server.startServer();

            System.out.println("TEST: creating a testplayer");
            socket = new Socket(GlobalVariables.getAdress(), GlobalVariables.getPort());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new SecureObjectInputStream(socket.getInputStream());
            responseConsumer = new ResponseConsumer();
            responseConsumer.start();
            waitForResponse(200);
            sendName("player 1");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitForResponse() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitForResponse(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public TestSocket createanotherSocket(String name) {
        try {
            Socket anothersocket = new Socket(GlobalVariables.getAdress(), GlobalVariables.getPort());
            ObjectOutputStream anotheroutput = new ObjectOutputStream(anothersocket.getOutputStream());
            SecureObjectInputStream anotherinput = new SecureObjectInputStream(anothersocket.getInputStream());
            anotheroutput.writeObject(new InitialSetNameCommand(name));
            String id = (String) ServerResponseDecrypter.payloadRetriever(anotherinput);
            TestSocket testSocket = new TestSocket(anothersocket, anotherinput, anotheroutput, true);
            testSocket.setID(id);
            return testSocket;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;


    }

    @AfterEach
    public void disconnect() {
        // waitForResponse(1000);
        System.out.println("closing everything intentionally");
        responseConsumer.running = false;
        responseConsumer.interrupt();

        Server.kickAllAndEverything();
        Server.closeServer();
        System.out.println(returncommands);
        returncommands.clear();

    }

    public void sendName(String name) {
        try {
            testplayer = null;
            objectOutputStream.writeObject(new InitialSetNameCommand(name));
            waitForResponse(20);
            String id = (String) returncommands.getLast();
            testplayer = new Player(id, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void leaveGame() {
        sendName("leaveGame");
        TestSocket playertwo = createanotherSocket("leavegame2");
        try {
            objectOutputStream.writeObject(new CreateGameCommand("leavegame"));
            waitForResponse();
            playertwo.objectOutputStream.writeObject(new InitialJoinCommand("leavegame"));
            waitForResponse();
            objectOutputStream.writeObject(new PlayerReadyCommand(null));
            waitForResponse();
            playertwo.objectOutputStream.writeObject(new PlayerReadyCommand(null));
            waitForResponse();
            waitForResponse(2000);
            playertwo.objectOutputStream.writeObject(new LeaveGameCommand(null));
            waitForResponse();
            playertwo.objectOutputStream.writeObject(new GetClientStateCommand(null));
            waitForResponse();

            assertTrue(playertwo.returncommands.contains("Game Successfully left."));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void leaveGamefromLobby() {
        sendName("leavegamefromlobby");
        try {
            objectOutputStream.writeObject(new CreateGameCommand("leavegamefromlobby"));
            waitForResponse();
            objectOutputStream.writeObject(new LeaveGameCommand(null));
            waitForResponse();
            LinkedList<Object> error = (LinkedList<Object>) returncommands.getLast();
            Codes.ERROR response = (Codes.ERROR) error.getLast();
            assertEquals(Codes.ERROR.NOT_IN_GAME, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void leaveGameOnly() {
        sendName("leavegameonly");
        try {
            objectOutputStream.writeObject(new CreateGameCommand("leavegameonly"));
            waitForResponse();
            objectOutputStream.writeObject(new PlayerReadyCommand(null));
            waitForResponse();
            waitForResponse(2000);
            objectOutputStream.writeObject(new LeaveGameCommand(null));
            waitForResponse();
            assertTrue(returncommands.contains("Game Successfully left."));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void RequestUserListIngame() {
        sendName("requestuserlistingame");
        TestSocket playertwo = createanotherSocket("requestuserlistingame2");
        try {
            objectOutputStream.writeObject(new CreateGameCommand("requestuserlistingame"));
            waitForResponse();
            playertwo.objectOutputStream.writeObject(new InitialJoinCommand("requestuserlistingame"));
            waitForResponse();
            objectOutputStream.writeObject(new PlayerReadyCommand(null));
            waitForResponse();
            sendCommand(new PlayerReadyCommand(null), playertwo.objectOutputStream);
            waitForResponse(2000);
            sendCommand(new RequestUserListCommand(null), objectOutputStream);
            LinkedList<String> list = (LinkedList<String>) returncommands.getLast();
            assertTrue(list.contains("requestuserlistingame"));
            assertTrue(list.contains("requestuserlistingame2"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void RequestUserListIngameLeave() {
        sendName("requestuserlistingamel");
        TestSocket playertwo = createanotherSocket("requestuserlistingamel2");
        try {
            objectOutputStream.writeObject(new CreateGameCommand("requestuserlistingamel"));
            waitForResponse();
            playertwo.objectOutputStream.writeObject(new InitialJoinCommand("requestuserlistingamel"));
            waitForResponse();
            objectOutputStream.writeObject(new PlayerReadyCommand(null));
            waitForResponse();
            sendCommand(new PlayerReadyCommand(null), playertwo.objectOutputStream);
            waitForResponse(2000);
            sendCommand(new RequestUserListCommand(null), objectOutputStream);
            LinkedList<String> list = (LinkedList<String>) returncommands.getLast();
            assertTrue(list.contains("requestuserlistingamel"));
            assertTrue(list.contains("requestuserlistingamel2"));
            sendCommand(new LeaveGameCommand(null), playertwo.objectOutputStream);
            waitForResponse(1000);//in case it was playertwos turn
            sendCommand(new RequestUserListCommand(null), objectOutputStream);
            LinkedList<String> list2 = (LinkedList<String>) returncommands.getLast();
            assertTrue(list2.contains("requestuserlistingamel"));
            assertFalse(list2.contains("requestuserlistingamel2"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(BaseCommand command, ObjectOutputStream out) {
        try {
            out.writeObject(command);
            waitForResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(BaseCommand command) {
        try {
            objectOutputStream.writeObject(command);
            waitForResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void DisconnectFromGame() {
        sendName("disconnectfromgame");
        sendCommand(new CreateGameCommand("testdisconnectfromgame"));
        sendCommand(new PlayerReadyCommand(null));
        waitForResponse(3000);
        sendCommand(new DisconnectCommand(null));
        assertTrue(returncommands.contains("Disconnect initiated"));

    }

    @Test
    public void cheatOnMyTurn() {
        sendName("cheatOnMyTurn");
        sendCommand(new CreateGameCommand("cheatOnMyTurn"));
        sendCommand(new PlayerReadyCommand(null));
        waitForResponse(2000);
        sendCommand(new CheatCommand(new CheatMove("cheatOnMyTurn", new Soldier(testplayer))));
        LinkedList<Object> error = (LinkedList<Object>) returncommands.getLast();
        Codes.ERROR response = (Codes.ERROR) error.getLast();
        assertEquals(Codes.ERROR.NO_CHEAT_ON_TURN, response);

    }

    @RepeatedTest(10)
    public void cheat() {
        sendName("cheat");
        TestSocket playertwo = createanotherSocket("cheat2");
        sendCommand(new CreateGameCommand("testcheat"));
        sendCommand(new InitialJoinCommand("testcheat"), playertwo.objectOutputStream);
        sendCommand(new PlayerReadyCommand(null));
        sendCommand(new PlayerReadyCommand(null), playertwo.objectOutputStream);



        waitForResponse(2000);
        Object gamestarted = returncommands.getLast();


        if (gamestarted instanceof GameCard) {

            GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), testplayer, Orientation.SOUTH);
            Soldier soldier=new Soldier(testplayer);
            soldier.setX(50);
            soldier.setY(49);
            entry.setSoldier(soldier,entry.getAlignedCardSides()[0]);
            GameMove gameMove = new GameMove(testplayer, entry, new GameMapEntryPosition(49,50));
            sendCommand(new GameTurnCommand(gameMove));
            CheatMove cheatMove= new CheatMove("cheat",soldier);
            cheatMove.setOriginalPosition(new SoldierPlacement(soldier,entry.getAlignedCardSides()[0]));
            cheatMove.setNewPosition(new SoldierPlacement(soldier,entry.getAlignedCardSides()[1]));
            sendCommand(new CheatCommand(cheatMove));
            assertTrue(returncommands.contains("cheat successfull."));

        } else {
            Player two= new Player(playertwo.id,"cheat2");
            GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), two, Orientation.SOUTH);
            Soldier soldier=new Soldier(two);
            soldier.setX(50);
            soldier.setY(49);
            entry.setSoldier(soldier,entry.getAlignedCardSides()[0]);
            GameMove gameMove = new GameMove(two, entry, new GameMapEntryPosition(49,50));
            sendCommand(new GameTurnCommand(gameMove),playertwo.objectOutputStream);
            CheatMove cheatMove= new CheatMove("cheat2",soldier);
            cheatMove.setOriginalPosition(new SoldierPlacement(soldier,entry.getAlignedCardSides()[0]));
            cheatMove.setNewPosition(new SoldierPlacement(soldier,entry.getAlignedCardSides()[1]));
            sendCommand(new CheatCommand(cheatMove),playertwo.objectOutputStream);
            assertTrue(playertwo.returncommands.contains("cheat successfull."));
        }


    }
    @Test
    public void detectCheat(){
        boolean player1cheated=false;
        sendName("detectcheat");
        TestSocket playertwo = createanotherSocket("detectcheat2");
        sendCommand(new CreateGameCommand("detectcheat"));
        sendCommand(new InitialJoinCommand("detectcheat"), playertwo.objectOutputStream);
        sendCommand(new PlayerReadyCommand(null));
        sendCommand(new PlayerReadyCommand(null), playertwo.objectOutputStream);



        waitForResponse(2000);
        Object gamestarted = returncommands.getLast();


        if (gamestarted instanceof GameCard) {

            GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), testplayer, Orientation.SOUTH);
            Soldier soldier=new Soldier(testplayer);
            soldier.setX(50);
            soldier.setY(49);
            entry.setSoldier(soldier,entry.getAlignedCardSides()[0]);
            GameMove gameMove = new GameMove(testplayer, entry, new GameMapEntryPosition(49,50));
            sendCommand(new GameTurnCommand(gameMove));
            CheatMove cheatMove= new CheatMove("detectcheat",soldier);
            cheatMove.setOriginalPosition(new SoldierPlacement(soldier,entry.getAlignedCardSides()[0]));
            cheatMove.setNewPosition(new SoldierPlacement(soldier,entry.getAlignedCardSides()[1]));
            sendCommand(new CheatCommand(cheatMove));
            assertTrue(returncommands.contains("cheat successfull."));
            player1cheated=true;

        } else {
            Player two= new Player(playertwo.id,"detectcheat2");
            GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), two, Orientation.SOUTH);
            Soldier soldier=new Soldier(two);
            soldier.setX(50);
            soldier.setY(49);
            entry.setSoldier(soldier,entry.getAlignedCardSides()[0]);
            GameMove gameMove = new GameMove(two, entry, new GameMapEntryPosition(49,50));
            sendCommand(new GameTurnCommand(gameMove),playertwo.objectOutputStream);
            CheatMove cheatMove= new CheatMove("detectcheat2",soldier);
            cheatMove.setOriginalPosition(new SoldierPlacement(soldier,entry.getAlignedCardSides()[0]));
            cheatMove.setNewPosition(new SoldierPlacement(soldier,entry.getAlignedCardSides()[1]));
            sendCommand(new CheatCommand(cheatMove),playertwo.objectOutputStream);
            assertTrue(playertwo.returncommands.contains("cheat successfull."));
        }

        //cheat assembled;

        if(player1cheated){
            Soldier soldier=new Soldier(new Player(playertwo.id,"detectcheat2"));
            soldier.setX(50);
            soldier.setY(49);
            sendCommand(new DetectCheatCommand(soldier),playertwo.objectOutputStream);
            assertTrue(playertwo.returncommands.contains("Cheat detection successfull."));
        }else{
            Soldier soldier=new Soldier(testplayer);
            soldier.setX(50);
            soldier.setY(49);
            sendCommand(new DetectCheatCommand(soldier));
            assertTrue(returncommands.contains("Cheat detection successfull."));
            returncommands.removeLast();
            LinkedList<CheatMove> cheats=(LinkedList<CheatMove>) returncommands.getLast();
            assertEquals(cheats.pop().getPlayername(),"detectcheat2");
        }




    }
    @Test
    public void detectCheatUnsuccessfull(){
        sendName("detectCheat");
        TestSocket playertwo = createanotherSocket("detectCheat2");
        sendCommand(new CreateGameCommand("detectCheat"));
        sendCommand(new InitialJoinCommand("detectCheat"), playertwo.objectOutputStream);
        sendCommand(new PlayerReadyCommand(null));
        sendCommand(new PlayerReadyCommand(null), playertwo.objectOutputStream);
        waitForResponse(2000);
        Soldier soldier=new Soldier(new Player(playertwo.id,"detectcheat"));
        soldier.setX(49);
        soldier.setY(49);
        sendCommand(new DetectCheatCommand(soldier));
        assertTrue(returncommands.contains("No such Cheat active."));
    }
}
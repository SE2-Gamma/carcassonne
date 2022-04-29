package at.aau.se2.gamma.server;

import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.*;
import at.aau.se2.gamma.core.commands.error.Codes;
import at.aau.se2.gamma.core.models.impl.Player;
import at.aau.se2.gamma.core.models.impl.Session;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.server.models.ServerPlayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ClientThread extends Thread {


    private Socket socket;
    private ClientState clientState;
    private Session session;
    private Player player;
    private String ID;
    private ServerPlayer serverPlayer;


    private SecureObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean running;


    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    //-------------------------I/O-------------------------------------------------
    @Override
    public void run() {
        running=true;
        this.clientState = ClientState.INITIAl;
        try {
            this.objectInputStream = new SecureObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while(running) {
                BaseCommand command = (BaseCommand) objectInputStream.readObject();

                BaseCommand response=handleCommand(command);

                System.out.println("Command "+response.getPayload()+" with ID "+command.getRequestId() +" handeled.");
                if(!(command instanceof DisconnectCommand)) {
                    objectOutputStream.writeObject(response);
                }
            }
            System.out.println("stopped");
        } catch(SocketException socketException){
            if(!running){
                System.out.println(serverPlayer.getName()+" closed internally");
            }else{
                socketException.printStackTrace();
            }
        }
        catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
        try {
            objectInputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

//--------------------------commandhandler-----------------------------------------

    private BaseCommand handleCommand(BaseCommand command) {


        if (command instanceof InitialJoinCommand) {
            return initialJoin((InitialJoinCommand) command);
        } else if (command instanceof InitialSetNameCommand) {
            return initialSetName((InitialSetNameCommand) command);
        } else if (command instanceof ServerResponseCommand) {
        } else if (command instanceof CreateGameCommand) {
            return createGameCommand((CreateGameCommand)command);
        } else if (command instanceof RequestUserListCommandByID) {
            return requestUserListCommandByID((RequestUserListCommandByID)command);
        } else if (command instanceof RequestUserListCommand) {
            return requestUserListCommand((RequestUserListCommand)command);
        }else if(command instanceof KickPlayerCommand) {
            return kickPlayer((KickPlayerCommand) command);
        }else if(command instanceof DisconnectCommand){
            return disconnectPlayer((DisconnectCommand) command);
        }
        else{
            System.out.println("command not suitable for current state");
        }


        return new ServerResponseCommand(new ServerResponse("Invalid command", ServerResponse.StatusCode.FAILURE),command.getRequestId());
    }
    //--------------------------------------commands-----------------------------------------------------
    private BaseCommand requestUserListCommandByID(RequestUserListCommandByID command){
        System.out.print("  current state: "+clientState);
        Session temp=null;
        try {
            temp= Server.SessionHandler.getSession((String) command.getPayload());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseCreator.getError(command,"no Session found", Codes.ERROR.NO_SESSION_FOUND);
        }
        System.out.print("   //Session found//");
        LinkedList<String> namelist=new LinkedList<>();
        for (Player player:temp.players
        ) {
            namelist.add(player.getName());
        }
        System.out.print( "  // players requested: "+ Arrays.toString(namelist.toArray()) +"//");

        System.out.print(" //current state: "+clientState +"//");
        return new ServerResponseCommand(ServerResponse.success(namelist), command.getRequestId());
    }

   private BaseCommand requestUserListCommand(RequestUserListCommand command){
        System.out.print(" //current state: "+clientState+"//");
        if(!(clientState==ClientState.LOBBY||clientState==ClientState.GAME)){
            return ResponseCreator.getError(command,"must be in Lobby or in Game", Codes.ERROR.WRONG_STATE);
        }
        LinkedList<String> namelist=new LinkedList<>();
        for (Player player:session.players
        ) {
            namelist.add(player.getName());
        }
        System.out.print( "  // players requested: "+ Arrays.toString(namelist.toArray()) +"//");
        System.out.print ("// current state: "+ clientState+"// ");
        return ResponseCreator.getSuccess(command,namelist);
    }

    private BaseCommand createGameCommand(CreateGameCommand command) {
        //check state
        if(clientState!=ClientState.INITIAl){
            return ResponseCreator.getError(command,"Not in initialState", Codes.ERROR.WRONG_STATE);
        }
        System.out.print("  current state: "+clientState);
        //getpayload

       // Player player=(Player) list.pop();
         String GameID= (String) command.getPayload();

        //check player
    /*    try {Server.identify(player);} catch (NoSuchElementException e) {
            System.err.println("Player not connected");
            e.printStackTrace();
            return new ErrorCommand(command);
        }*/
        //create session
        try {
            session = Server.SessionHandler.createSession(GameID,this.player);
        } catch (IllegalArgumentException e) {
            System.err.println("Spiel bereits vorhanden");
          return  ResponseCreator.getError(command,"Server bereits vorhanden", Codes.ERROR.SESSION_ALREADY_EXISTS);

        }
        System.out.print("   //Session created//");

        //set state
        clientState=ClientState.LOBBY;
        System.out.print(" //current state: "+clientState +"//");
        System.out.print(" //SessionID: "+session.getId()+"//  ");
        return ResponseCreator.getSuccess(command,session);
    }

    public BaseCommand initialJoin(InitialJoinCommand command){
        //checkstate
        if(clientState!=ClientState.INITIAl){
            return ResponseCreator.getError(command,"Not in initialState", Codes.ERROR.WRONG_STATE);
        }
        //getpayload
        //LinkedList<Object> list=(LinkedList<Object>) command.getPayload();
        String sessionID= (String) command.getPayload();
        //Player player= (Player) list.pop();
        //checkplayer
        try {
            Server.identify(player);
        } catch (NoSuchElementException e) {
            System.err.println("Player not connected");
            e.printStackTrace();
            return ResponseCreator.getError(command,"Player not connected", Codes.ERROR.PLAYER_NOT_CONNECTED);

        }
        //join session
        try {
            session=Server.SessionHandler.joinSession(sessionID,player);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            System.err.println("no Such session");
            return ResponseCreator.getError(command,"no such Session", Codes.ERROR.NO_SESSION_FOUND);
        }
        //set state
        clientState=ClientState.LOBBY;


        return ResponseCreator.getSuccess(command,session);
    }

    public BaseCommand initialSetName(InitialSetNameCommand command){
        //todo check github Vorschlag in issue commentary
        System.out.print(" //current state: "+clientState+"//");
        //checkstate
        if(clientState!=ClientState.INITIAl){

            return ResponseCreator.getError(command,"Not in initialState", Codes.ERROR.WRONG_STATE);
        }
        String name= (String) command.getPayload();
        for (ServerPlayer serverplayer:Server.activeServerPlayers
        ) {
            if( serverplayer.getName().equals(name)){
                return ResponseCreator.getError(command,"Name bereits vergeben", Codes.ERROR.NAME_ALREADY_TAKEN);
            }
        }
        System.out.println("  //set name: "+name+"//");


        Player player=new Player(name, ID);
        this.player=player;

        this.serverPlayer.setPlayer(player);
        this.serverPlayer.setName(name);
        this.serverPlayer.setId(ID);
        System.out.print("// serverplayer set//");

        System.out.print ("// current state: "+ clientState+"// ");
        return ResponseCreator.getSuccess(command,ID);
    }

    public BaseCommand disconnectPlayer(DisconnectCommand command){ //todo: implement errors
        System.out.print("//todo: disconnect player "+player.getName()+"//");
        try {
            objectOutputStream.writeObject(ResponseCreator.getSuccess(command,"Disconnect initiated"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(clientState.equals(ClientState.LOBBY)){
            session.removePlayer(player);
        }
        if(clientState.equals(ClientState.GAME)){
            //todo: implement
        }
        Server.activeServerPlayers.remove(serverPlayer);
        try {
            terminate();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("//player successfully removed//");
        return ResponseCreator.getSuccess(command,"Player sucessfully removed.also you shouldnt be receiving this");
    }

    public BaseCommand kickPlayer(KickPlayerCommand command) {
        String playername = (String) command.getPayload();
        System.out.print("//attempting to kick//");
        Player tempplayer;
        //String playerID=Server.identify(playername);
        try {
            tempplayer = Server.getPlayerbyName(playername);
            System.out.print("//finding player//");
        } catch (NoSuchElementException e) {
            return ResponseCreator.getError(command, "no such player found", Codes.ERROR.NO_PLAYER_WITH_MATCHING_NAME);
        }
        System.out.print("//player found//");
if(session.voteKick(tempplayer,player)){
    //todo: alert kicked player1
}

        return ResponseCreator.getSuccess(command, "vote issued");
    }
    public void sendCommand(BaseCommand command) {
        try {
            this.objectOutputStream.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //-----------------------utility methods------------------------------------------------------------------


    public void terminate() throws IOException {

        running=false;

    }
    public void closeStreams() throws IOException {

    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    public void setServerPlayer(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }
    public Socket getSocket() {
        return socket;
    }




}

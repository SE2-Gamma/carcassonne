package at.aau.se2.gamma.server;

import at.aau.se2.gamma.core.SecureObjectInputStream;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.*;
import at.aau.se2.gamma.core.commands.BroadcastCommands.BroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.PlayerJoinedBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.PlayerLeftLobbyBroadcastCommand;
import at.aau.se2.gamma.core.commands.error.Codes;
import at.aau.se2.gamma.core.exceptions.*;
import at.aau.se2.gamma.core.models.impl.*;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.server.models.ServerPlayer;
import at.aau.se2.gamma.server.models.Session;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientThread extends Thread {


    private Socket socket;

    private ClientState clientState;
    private Session session;
    private Player player;
    private String ID;
    private ServerPlayer serverPlayer;
    private boolean communicating =false;
    public int numberOfCheats=1;

    private SecureObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean running;


    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    //-------------------------I/O-------------------------------------------------

   public class BroadcastHandler extends Thread{
       @Override
       public void run() {
           super.run();
       }
   }
   LinkedBlockingDeque<BaseCommand>broadcastcommands=new LinkedBlockingDeque<>();
    @Override
    public void run() {
        running=true;
        this.clientState = ClientState.INITIAl;
        try {
            this.objectInputStream = new SecureObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while(running) {


                BaseCommand command = (BaseCommand) objectInputStream.readUnshared(); //potential issue. if server sends broadcast message while busy ready here might doublelock

                BaseCommand response=handleCommand(command);

                System.out.println(command.getClass().getName()  +" handeled.");
                
                if(!(command instanceof DisconnectCommand)) {
                    System.out.println("Size of responseCommand in Bytes: "+Server.sizeof(response));
                    checkingAvailability();
                   lock();

                    objectOutputStream.writeUnshared(response);
                    unlock();
                }



            }
            System.out.println("stopped");
        } catch(SocketException socketException){
            if(!running){
                System.out.println(serverPlayer.getName()+"'s socket closed.");
            }else{

            }
        }
        catch (EOFException e) {
            System.out.println(player.getName()+" disconnected unexpectedly.");
            if(clientState.equals(ClientState.LOBBY)){

               session.broadcastAllPlayers(new PlayerLeftLobbyBroadcastCommand(player.getName()));
               session.players.remove(player);
            }
            if(clientState.equals(ClientState.GAME)){
                session.leaveGame(player);
            }
            Server.activeServerPlayers.remove(serverPlayer);
        }
        catch (Exception exception){
            System.out.println("unknown exception");
            exception.printStackTrace();
        }
        try {
            objectInputStream.close();
            objectOutputStream.close();
        }catch (SocketException closed){
            System.out.println(player.getName()+"'s streams closed.");
        }
        catch (IOException e) {

            e.printStackTrace();
        }

    }

    private void checkingAvailability() {
        if(communicating){
            while(communicating){
                System.out.print(".");
            }
        }
        System.out.println();
        System.out.println("relieving lock");
    }
    public void broadcastMessage(BroadcastCommand command){
        System.out.print("//checking availability");

        ServerResponseCommand message=ResponseCreator.getBroadcastCommand(command);

        try {
            System.out.print("//Size of responseCommand in Bytes: "+Server.sizeof(message));
            System.out.print("//available,locking");
            checkingAvailability();
            lock();

            objectOutputStream.writeUnshared(message);
            unlock();
            System.out.print("//unlocking//");
            System.out.print("//message sent");
        } catch (IOException e) {
            //e.printStackTrace();
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
        }else if(command instanceof LeaveLobbyCommand) {
            return leaveLobby((LeaveLobbyCommand) command);

        }else if(command instanceof GetClientStateCommand) {
            return getClientState((GetClientStateCommand) command);

        }else if(command instanceof GameTurnCommand) {
            return gameTurn((GameTurnCommand) command);

        }else if(command instanceof PlayerReadyCommand) {
            return playerReady((PlayerReadyCommand) command);

        }else if(command instanceof PlayerNotReadyCommand) {
            return playerNotReady((PlayerNotReadyCommand) command);

        }else if(command instanceof RejoinGameCommand) {
            return reJoin((RejoinGameCommand) command);

        }
        else if(command instanceof ReconnectCommand) {
            return reConnect((RejoinGameCommand) command);

        }else if(command instanceof LeaveGameCommand) {
            return leaveGame((LeaveGameCommand) command);

        }else if(command instanceof CheatCommand) {
            return cheat((CheatCommand) command);

        }else if(command instanceof DetectCheatCommand) {
            return detectCheat((DetectCheatCommand) command);

        }
        else{
            System.out.println("command not suitable for current state");
        }


        return new ServerResponseCommand(new ServerResponse("Invalid command", ServerResponse.StatusCode.FAILURE),command.getRequestId());
    }




    //--------------------------------------commands-----------------------------------------------------
    private BaseCommand detectCheat(DetectCheatCommand command) {
        if(!clientState.equals(ClientState.GAME)){
            return ResponseCreator.getError(command,"youre not ingame", Codes.ERROR.NOT_IN_GAME);
        }
        try {
            session.detectCheat((Soldier) command.getPayload());
        } catch (NoSuchCheatActiveException e) {
            return ResponseCreator.getSuccess(command,"No such Cheat active.");
        }
        return ResponseCreator.getSuccess(command,"Cheat detection successfull.");

    }

    private BaseCommand cheat(CheatCommand command) {
        System.out.print("//incoming cheatmove//");
        if(!clientState.equals(ClientState.GAME)){
            return ResponseCreator.getError(command,"youre not ingame",Codes.ERROR.NOT_IN_GAME);
        }
       // if(session.gameLoop.onTurn.getId().equals(player.getId())){
          //  return ResponseCreator.getError(command,"its your turn, you cant cheat now.",Codes.ERROR.NO_CHEAT_ON_TURN);}

        CheatData cheatMove=(CheatData) command.getPayload();

        try {
            session.executeCheat(cheatMove,((int) Math.pow(2,numberOfCheats)));
        } catch (CheatMoveImpossibleException e) {
            System.err.println(e.getMessage());
           return ResponseCreator.getError(command,"Cheatmove not possible",Codes.ERROR.INVALID_CHEATMOVE);
        }catch(NullPointerException e){
            e.printStackTrace();
            return ResponseCreator.getError(command,"cheatmove error",Codes.ERROR.INVALID_CHEATMOVE);
        }
        numberOfCheats++;
        return ResponseCreator.getSuccess(command,"cheat successfull.");
    }
    private BaseCommand leaveGame(LeaveGameCommand command) {
        System.out.print("attempting to leave game");
        if(!clientState.equals(ClientState.GAME)){
            return ResponseCreator.getError(command,"youre not ingame",Codes.ERROR.NOT_IN_GAME);
        }
        try {
            session.leaveGame(player);
        } catch (NoSuchElementException e) {
            return ResponseCreator.getError(command,"youre not ingame",Codes.ERROR.NOT_IN_GAME);
        }
        clientState=ClientState.INITIAl;
        return ResponseCreator.getSuccess(command,"Game Successfully left.");
        //todo:
        //check if ingame
        //inform other players youre leaving
        //remove yourself from session.players
        //remove yourself from turnorder
        //remove yourself from gameobject?




    }
    private BaseCommand reConnect(RejoinGameCommand command) {
        System.out.print("attempting to reconnect");
        //todo:
        //check if id was present in serverplayers
        //if so, check if someone else has used the name
        //if so, return with a prompt to enter a new name
        //when no nameissues are present anymore return success

        return null;
    }
    private BaseCommand reJoin(RejoinGameCommand command) {

        System.out.print("attempting to rejoin game");
        //todo:
        //check if you are in the archives of the session
        //if not, return an error
        //if so, join game
        //inform other players youve reconnected
        //add yourself to session.players
        //add yourself to turnorder
        //connect yourself with the points youve made and the soldiers youve placed.


        return null;
    }

    private BaseCommand requestUserListCommandByID(RequestUserListCommandByID command){
        System.out.print("  current state: "+clientState);
        Session temp=null;
        try {
            temp= Server.SessionHandler.getSession((String) command.getPayload());
        } catch (NoSuchElementException e) {

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

        return ResponseCreator.getSuccess(command,"Game Created");
    }

    public BaseCommand initialJoin(InitialJoinCommand command){
        System.out.print("//startingstate: "+clientState+"//");
        //checkstate
        if(clientState!=ClientState.INITIAl){
            return ResponseCreator.getError(command,"Not in initialState", Codes.ERROR.WRONG_STATE);
        }
        //getpayload
        //LinkedList<Object> list=(LinkedList<Object>) command.getPayload();
        String sessionID= (String) command.getPayload();
        //Player player= (Player) list.pop();


        //join session
        try {
            session=Server.SessionHandler.joinSession(sessionID,player);
        } catch (NoSuchElementException e) {
            System.err.println("no Such session");
            return ResponseCreator.getError(command,"no such Session", Codes.ERROR.NO_SESSION_FOUND);
        }
        //set state
        clientState=ClientState.LOBBY;
        LinkedList<String> namelist=new LinkedList<>();
        for (Player player:session.players
        ) {
            namelist.add(player.getName());
        }

        System.out.print( "  // players currently in lobby: "+ namelist +"//");
        System.out.print("//currentState: "+clientState+"//");
        session.broadcastAllPlayers(new PlayerJoinedBroadcastCommand(player.getName())); //todo: check if this causes errors appside
        return ResponseCreator.getSuccess(command,"successfully joined");
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


        Player player=new Player(ID, name);
        this.player=player;

        this.serverPlayer.setPlayer(player);
        this.serverPlayer.setName(name);
        this.serverPlayer.setId(ID);
        System.out.print("// serverplayer set//");
       ServerPlayer archived= new ServerPlayer();

       archived.setId(ID);
       archived.setName(name);
        Server.serverArchive.add(archived);
        System.out.print("//player added to archive//");
        System.out.print ("// current state: "+ clientState+"// ");
        return ResponseCreator.getSuccess(command,ID);
    }

    public BaseCommand disconnectPlayer(DisconnectCommand command){ //todo: implement errors
        try {
            System.out.print("//todo: disconnect player "+player.getName()+"//");
        } catch (NullPointerException e) {
            System.out.print("//player not yet instantiated//");
        }
        try {
            objectOutputStream.writeObject(ResponseCreator.getSuccess(command,"Disconnect initiated"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(clientState.equals(ClientState.LOBBY)){

            try {
                session.removePlayer(player);
                session.broadcastAllPlayers(new PlayerLeftLobbyBroadcastCommand(player.getName()),player);
            } catch (NoSuchElementException e) {
                System.out.print("//Player already disconnected//");
            }
        }
        if(clientState.equals(ClientState.GAME)){
            try {
                session.leaveGame(player);
            } catch (NoSuchElementException e) {
                return ResponseCreator.getError(command,"youre not ingame",Codes.ERROR.NOT_IN_GAME);
            }
            return ResponseCreator.getSuccess(command,"Game Successfully left.");
        }
        Server.activeServerPlayers.remove(serverPlayer);
        try {
            terminate();
            socket.close();
        } catch (IOException e) {
            System.out.println(player.getName()+"'s socket closed");
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
        try {
            session.voteKick(tempplayer,player);
        } catch (IllegalStateException e) {
            System.out.print("No additional kickvote issued, hence no broadcasting.//");
            return ResponseCreator.getSuccess(command,"Command successfully handeled, but you can't issue a vote twice.");
        }


        return ResponseCreator.getSuccess(command, "vote issued");
    }

    public BaseCommand getClientState(GetClientStateCommand command){
        System.out.print("//current State: "+clientState+"//");
        return  ResponseCreator.getSuccess(command,clientState);
    }

    public BaseCommand leaveLobby(LeaveLobbyCommand command){

        System.out.print("//current State: "+clientState+"//");
        if(!clientState.equals(ClientState.LOBBY)){
            return ResponseCreator.getError(command,"Cant leave lobby because you are not in a lobby", Codes.ERROR.NOT_IN_LOBBY);
        }
        System.out.print("//Trying to leave lobby with ID "+session.getId()+"//");
        try {

            session.removePlayer(player);

        } catch (Exception e) {
            System.err.print("Some error leaving a lobby");
            return ResponseCreator.getError(command,"some error", Codes.ERROR.NOT_IN_LOBBY);

        }
        clientState=ClientState.INITIAl;

        return ResponseCreator.getSuccess(command,"Lobby successfully left");
    }

    public BaseCommand gameTurn(GameTurnCommand command){
        if(!clientState.equals(ClientState.GAME)){
            return ResponseCreator.getError(command,"youre not ingame",Codes.ERROR.NOT_IN_GAME);
        }
        GameMove gameturn=(GameMove) command.getPayload();
        System.out.print("//incoming gamemove//");
        try {
            session.executeGameMove(gameturn);
        } catch (InvalidPositionGameMapException e) {
            System.out.print("//invalid move has been answered");
            return ResponseCreator.getError(command,"Invalid Position on Gamemap",Codes.ERROR.INVALID_MOVE);
        } catch (SurroundingConflictGameMapException e) {
            System.out.print("//invalid move has been answered");
            return ResponseCreator.getError(command,"Surrounding Conflict on Gamemap",Codes.ERROR.INVALID_MOVE);
        } catch (NoSurroundingCardGameMapException e) {
            System.out.print("//invalid move has been answered");
            return ResponseCreator.getError(command,"No surrounding Card on Gamemap",Codes.ERROR.INVALID_MOVE);
        } catch (PositionNotFreeGameMapException e) {
            System.out.print("//invalid move has been answered");
            return ResponseCreator.getError(command,"Position not free on Gamemap",Codes.ERROR.INVALID_MOVE);
        }


        return ResponseCreator.getSuccess(command,"turn succesfull");

            }






    private BaseCommand playerNotReady(PlayerNotReadyCommand command) {
        System.out.print("//player "+player.getName()+" isnt ready anymore//");
        try {
            session.playerNotReady(player);
        } catch (NoSuchElementException e) {
          return ResponseCreator.getError(command,"Player wasnt ready",Codes.ERROR.PLAYER_NOT_READY);
        }
        return ResponseCreator.getSuccess(command,"Readyness resumed");

    }

    private BaseCommand playerReady(PlayerReadyCommand command) {

        session.playerReady(player);

        return ResponseCreator.getSuccess(command,"Youre ready now");

    }

    //-----------------------utility methods------------------------------------------------------------------
    public void lock(){
        communicating =true;
    }
    public void unlock(){
        communicating =false;
    }

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

    public ClientState getClientState() {
        return clientState;
    }

    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
    }




}

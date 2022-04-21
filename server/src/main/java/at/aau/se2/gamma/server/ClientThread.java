package at.aau.se2.gamma.server;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.*;
import at.aau.se2.gamma.core.models.impl.Session;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.server.models.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class ClientThread implements Runnable {
    private Socket socket;
    private ClientState clientState;
    private Session session;
    private Player player;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean running;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        running=true;
        this.player = new Player();
        this.clientState = ClientState.INITIAl;
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while(running) {
                BaseCommand command = (BaseCommand) objectInputStream.readObject();
                System.out.println("command received");
                ServerResponseCommand response=handleCommand(command);
                System.out.println("command handled");


                objectOutputStream.writeObject(response);
                System.out.println("response sent");
                System.out.println("Command "+response.getPayload()+" with ID "+command.getRequestId() +" handeled.");
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
    }
    private ServerResponseCommand handleCommand(BaseCommand command) {

        if (command.getState().equals(clientState)) {

            if (command instanceof InitialJoinCommand) {
                return initialJoin((InitialJoinCommand) command);
            } else if (command instanceof InitialSetNameCommand) {
                return initialSetName((InitialSetNameCommand) command);
            } else if (command instanceof ServerResponseCommand) {
            } else if (command instanceof CreateGameCommand) {
                return createGameCommand((CreateGameCommand)command);
            } else {
                System.out.println("command not suitable for current state");
            }

        }
        return new ServerResponseCommand(new ServerResponse("Invalid command", ServerResponse.StatusCode.FAILURE),command.getRequestId());
    }

    private ServerResponseCommand createGameCommand(CreateGameCommand command) {
        LinkedList<Object>list=(LinkedList<Object>) command.getPayload();
        String GameID=(String)list.getFirst();
        Player player=(Player)list.getFirst();
        Session session=Server.SessionHandler.createSession(GameID,player);
        return new ServerResponseCommand(new ServerResponse(session, ServerResponse.StatusCode.SUCCESS),
                command.getRequestId());
    }

    public ServerResponseCommand initialJoin(InitialJoinCommand command){
        return new ServerResponseCommand(new ServerResponse("Initial join", ServerResponse.StatusCode.SUCCESS),command.getRequestId());
    }
    public ServerResponseCommand initialSetName(InitialSetNameCommand command){
        System.out.println("set name: "+command.getPayload());
        this.player.setName((String) command.getPayload());
        return new ServerResponseCommand(ServerResponse.success(new StringResponseCommand("player accepted")),command.getRequestId());
    }
    public void sendCommand(BaseCommand command) {
        try {
            this.objectOutputStream.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void terminate(){
        running=false;
        //todo: implement
    }
}

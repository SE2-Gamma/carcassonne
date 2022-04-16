package at.aau.se2.gamma.server;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;
import at.aau.se2.gamma.core.models.impl.Session;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.server.models.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket socket;
    private ClientState clientState;
    private Session session;
    private Player player;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        this.player = new Player();
        this.clientState = ClientState.INITIAl;
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while(true) {
                BaseCommand command = (BaseCommand) objectInputStream.readObject();
                if (command.getState().equals(clientState)) {
                    if (command instanceof InitialJoinCommand) {
                    } else if (command instanceof InitialSetNameCommand) {
                        System.out.println("set name: "+command.getPayload());
                        this.player.setName((String) command.getPayload());
                        sendCommand(ServerResponseCommand.
                                fromRequestCommand(command, ServerResponse.success("Hello "+this.player.getName())));
                    }
                } else {
                    System.out.println("command not suitable for current state");
                }
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
    }

    public void sendCommand(BaseCommand command) {
        try {
            this.objectOutputStream.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package at.aau.se2.gamma.server;

import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
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
        System.out.println("here in client");
        try {
            System.out.println("try it");
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("after try it");
            while(true) {
                System.out.println("try to read");
                BaseCommand command = (BaseCommand) objectInputStream.readObject();
                System.out.println("we have one");
                if (command.getState().equals(clientState)) {
                    System.out.println("execute command in current state");
                    if (command instanceof InitialJoinCommand) {
                        System.out.println("join");
                    } else if (command instanceof InitialSetNameCommand) {
                        System.out.println("set name: "+command.getPayload());
                        this.player.setName((String) command.getPayload());
                        sendCommand(new InitialSetNameCommand("hello :)"));
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

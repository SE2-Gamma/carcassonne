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

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        this.player = new Player();
        System.out.println("here in client");
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while(true) {
                BaseCommand command = (BaseCommand) objectInputStream.readObject();
                if (command.getState().equals(clientState)) {
                    System.out.println("execute command in current state");
                    if (command instanceof InitialJoinCommand) {
                        System.out.println("join");
                    } else if (command instanceof InitialSetNameCommand) {
                        this.player.setName((String) command.getPayload());
                    }
                } else {
                    System.out.println("command not suitable for current state");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommand(BaseCommand command) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

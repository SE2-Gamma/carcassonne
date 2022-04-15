package at.aau.se2.gamma.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialJoin;

public class ClientThread implements Runnable {
    private Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("hello client");
        ArrayList<BaseCommand> commands = new ArrayList<>();
        commands.add(new InitialJoin());
        InitialJoin test = new InitialJoin();
        test.test = "HELLLLLLLLLOOO";
        sendCommand(test);
        /*while(true) {

        }*/
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

package at.aau.se2.gamma.carcassonne.network;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;

public class ServerThread extends Thread {

    public interface ConnectionHandler {
        void onConnectionFinished();
        void onServerResponse(BaseCommand command);
        void onServerFailure();
    }

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private int port;
    private String address;
    private Socket socket;
    public static ServerThread instance;
    ConnectionHandler connectionHandler;

    private ServerThread(String address, int port, ConnectionHandler connectionHandler) {
        this.address = address;
        this.port = port;
        this.connectionHandler = connectionHandler;
    }

    public static ServerThread init(String address, int port, ConnectionHandler connectionHandler) {
        ServerThread.instance = new ServerThread(address, port, connectionHandler);
        return ServerThread.instance;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(this.address, this.port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            connectionHandler.onConnectionFinished();
            while (true) {
                sendCommand(new InitialSetNameCommand("mrader"));
                BaseCommand command = (BaseCommand) objectInputStream.readObject();
                connectionHandler.onServerResponse(command);
                Log.d("RMLOG", "RESPONSE: "+command.getPayload());
                sleep(1000*2);
            }
        } catch (Exception e) {
            connectionHandler.onServerFailure();
            Log.d("RMLOG", "HERE IN ERROR"+e.toString());
            e.printStackTrace();
        }
    }

    public void sendCommand(BaseCommand command) {
        try {
            objectOutputStream.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

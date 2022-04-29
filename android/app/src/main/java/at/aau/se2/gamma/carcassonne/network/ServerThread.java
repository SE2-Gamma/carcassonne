package at.aau.se2.gamma.carcassonne.network;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;
import at.aau.se2.gamma.core.commands.error.ErrorCommand;

public class ServerThread extends Thread {
    public interface RequestResponseHandler {
        void onResponse(ServerResponse response, Object payload, BaseCommand request);
        void onFailure(ServerResponse response, Object payload, BaseCommand request);
    }

    public interface ConnectionHandler {
        void onConnectionFinished();
        void onServerFailure(Exception e);
    }

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private int port;
    private String address;
    private Socket socket;
    public static ServerThread instance;
    ConnectionHandler connectionHandler;
    private ArrayList<ServerRequest> requests = new ArrayList<>();

    private ServerThread(String address, int port, ConnectionHandler connectionHandler) {
        this.address = address;
        this.port = port;
        this.connectionHandler = connectionHandler;
    }

    public static ServerThread init(String address, int port, ConnectionHandler connectionHandler) {
        ServerThread.instance = new ServerThread(address, port, connectionHandler);
        return ServerThread.instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        try {
            socket = new Socket(this.address, this.port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            connectionHandler.onConnectionFinished();

            while (true) {
                try {
                    ServerResponseCommand responseCommand = (ServerResponseCommand) objectInputStream.readObject();
                    ServerResponse response = (ServerResponse) responseCommand.getPayload();

                    if(response.getPayload() instanceof BaseCommand) {
                        String requestID = responseCommand.getRequestId();

                        // check if a requestID exists, and if this requestID match one requested requestID
                        if (requestID != null) {
                            // get the command-handler serverRequest with the requestID
                            Optional<ServerRequest> optHandleServerRequest = requests.stream().filter(serverRequest -> serverRequest.command.getRequestId() != null
                                    && serverRequest.command.getRequestId().equals(requestID)).findFirst();

                            if (optHandleServerRequest.isPresent()) {
                                ServerRequest serverRequest = optHandleServerRequest.get();
                                serverRequest.notifyClient(response);
                            } else {
                                Logger.error("Unknown RequestID");
                            }
                        }
                    } else {
                        Logger.error("No BaseCommand as Response");
                    }
                } catch (NoSuchElementException e) { //todo: handle avoiding busy waiting
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            connectionHandler.onServerFailure(e);
            Log.d("RMLOG", "HERE IN ERROR"+e.toString());
            e.printStackTrace();
        }
    }

    public void sendCommand(BaseCommand command, RequestResponseHandler handler) {
        new SendThread(command, handler).start();
    }

    public void handleClientRequest(BaseCommand command, RequestResponseHandler handler) {
        // TODO: add timeout
        if(command.getRequestId() != null) {
            requests.add(new ServerRequest(command, handler));
        }

       try {
            objectOutputStream.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

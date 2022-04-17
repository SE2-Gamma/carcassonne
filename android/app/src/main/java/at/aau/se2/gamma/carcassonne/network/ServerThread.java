package at.aau.se2.gamma.carcassonne.network;

import android.util.Log;

import com.badlogic.gdx.utils.compression.lzma.Base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.concurrent.LinkedBlockingDeque;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.CreateGameCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;
import at.aau.se2.gamma.core.models.impl.Player;

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
    private LinkedBlockingDeque<BaseCommand> commandQueue=new LinkedBlockingDeque<>();

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
                BaseCommand command= null;

                try {
                    /*LinkedList<Object>temp=new LinkedList<>();
                    temp.add("gamename");
                    temp.add(new Player());
                    sendCommand(new CreateGameCommand(temp));*/
                    command=commandQueue.pop();
                    objectOutputStream.writeObject(command);
                    BaseCommand response = (BaseCommand) objectInputStream.readObject(); //changed from command to response for clarity
                    connectionHandler.onServerResponse(response);
                    if(command.getRequestId().equals(response.getRequestId())) {
                        Log.d("RMLOG", "RESPONSE: " + response.getPayload() + " Status:" + response.getState());
                    }else{
                        Log.e("RMLOG","Request ID doesnt match Response ID");
                    }
                } catch (NoSuchElementException e) { //todo: handle avoiding busy waiting
                    e.printStackTrace();
                }
                //sleep(10000*2);
            }
        } catch (Exception e) {

            connectionHandler.onServerFailure();
            Log.d("RMLOG", "HERE IN ERROR"+e.toString());
            e.printStackTrace();
        }
    }

    public void sendCommand(BaseCommand command) {

        commandQueue.add(command);


        // TODO: add request command to queue (because of the requestID), and
        // add a callback to this method, which is also in the queue
        // if we have a ServerResponse with the same requestID
        // return the response with the help of the callback
        // public void sendCommand(BaseCommand command, AnyCallbackInterface callback)
       /* try {
            objectOutputStream.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

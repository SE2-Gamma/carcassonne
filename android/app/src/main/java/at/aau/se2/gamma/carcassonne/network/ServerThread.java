package at.aau.se2.gamma.carcassonne.network;

import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;

public class ServerThread extends Thread {

    @Override
    public void run() {
        try {
            Socket socket = new Socket("192.168.0.47", 1234);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                objectOutputStream.writeObject(new InitialSetNameCommand("mrader"));
                BaseCommand command = (BaseCommand) objectInputStream.readObject();
                Log.d("RMLOG", "RESPONSE: "+command.getPayload());
                sleep(1000*10);
            }
        } catch (Exception e) {
            Log.d("RMLOG", "HERE IN ERROR"+e.toString());
            e.printStackTrace();
        }
    }
}

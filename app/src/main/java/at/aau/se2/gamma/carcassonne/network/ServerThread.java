package at.aau.se2.gamma.carcassonne.network;

import android.util.Log;

import java.io.ObjectInputStream;
import java.net.Socket;

import at.aau.se2.gamma.core.commands.BaseCommand;

public class ServerThread extends Thread {

    @Override
    public void run() {
        try {
            Socket socket = new Socket("192.168.0.43", 1234);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            BaseCommand command = (BaseCommand) objectInputStream.readObject();
            System.out.println("test");
        } catch (Exception e) {
            Log.d("RMLOG", "HERE IN ERROR");
            e.printStackTrace();
        }
    }
}

package at.aau.se2.gamma.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private ServerSocket socket;

    public static void main(String[] args) throws IOException {
        Server server = new Server("192.168.0.47", 1234, 10);
        server.run();
    }

    public Server(String address, int port, int maxClients) throws IOException {
        this.socket = new ServerSocket(port, maxClients, InetAddress.getByName(address));
    }

    @Override
    public void run() {
        while (true) {
            // get the new client
            Socket client = null;
            try {
                client = this.socket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // start a new thread for the new client
            new ClientThread(client).run();
        }
    }
}
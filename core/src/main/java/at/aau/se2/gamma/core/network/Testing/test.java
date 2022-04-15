package at.aau.se2.gamma.core.network.Testing;

import at.aau.se2.gamma.core.network.Server;

public class test {
    public static void main(String[] args) {
        Server server=new Server();
        server.start();
        PlayingGroundClient client=new PlayingGroundClient();
        client.start();
    }
}

package at.aau.se2.gamma.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.CreateGameCommand;
import at.aau.se2.gamma.core.models.impl.Session;
import at.aau.se2.gamma.server.models.Player;

public  class Server implements Runnable {
    static final int maxPlayers =10;

    private ServerSocket socket;
    LinkedList<Player> activePlayers =new LinkedList<>();

    public static class SessionHandler{
        static LinkedList<Session> sessions=new LinkedList<Session>(); //todo check concurrency problems
        public static Session createSession(String sessionID,Player player){
            for (Session session:sessions
                 ) {
                if(session.getId()==sessionID){
                    System.out.println("Spiel bereits vorhanden");
                    throw new IllegalArgumentException("Spiel bereits vorhanden");
                }
            }

            Session session= new Session(sessionID);
            //session.joinGame(player); //todo fix Player core and palyer server
            return session;
        }
        public Session joinSession(String sessionID, Player player){
            for (Session session:sessions
            ) {
                if(session.getId()==sessionID){
                    System.out.println("spiel gefunden");
                    //session.joinGame(player);  todo: fix Player (server) and Player (core)
                    return session;
                }
            }
            throw new IllegalArgumentException("cant find session");
        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server("192.168.0.47", 1234, maxPlayers);
        System.out.println("server running");
        server.run();

    }

    public Server(String address, int port, int maxClients) throws IOException {
        this.socket = new ServerSocket(port, maxClients, InetAddress.getByName(address));
    }

    @Override
    public void run() {
        ClientHandler clientHandler=new ClientHandler();
        clientHandler.run();

    }

    public ServerResponse removePlayer(Player player){
        try {
            activePlayers.remove(player);
            player.getClientThread().terminate();
        } catch (NoSuchElementException e) {
            return new ServerResponse("Player not found", ServerResponse.StatusCode.FAILURE);
        }
        return new ServerResponse("Some error", ServerResponse.StatusCode.FAILURE);
    }

    public class ClientHandler implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    if(activePlayers.size()< maxPlayers) {
                        ClientThread clientThread = new ClientThread(socket.accept());
                        System.out.println("new Player accepted");
                        Player player=new Player();
                        player.setClientThread(clientThread);
                        activePlayers.add(player);
                        clientThread.run();

                    }else{
                        System.err.println("Too many Players"); //todo: Clientside: add notification that the server is full
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
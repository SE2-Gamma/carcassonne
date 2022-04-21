package at.aau.se2.gamma.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.models.impl.Player;
import at.aau.se2.gamma.core.models.impl.Session;
import at.aau.se2.gamma.core.utils.globalVariables;
import at.aau.se2.gamma.server.models.ServerPlayer;

public  class Server implements Runnable {
    static final int maxPlayers =10;
    private static int uniqueID=0; //todo check concurrency problems
    private final ServerSocket socket;
    static LinkedList<ServerPlayer> activeServerPlayers =new LinkedList<>(); //todo check concurrency problems

    public static class SessionHandler{
        static LinkedList<Session> sessions=new LinkedList<Session>(); //todo check concurrency problems
        public static Session createSession(String sessionID, Player player){

            try {
               getSession(sessionID);
            } catch (NoSuchElementException e) {
                Session session= new Session(sessionID);
                session.joinGame(player);
                sessions.add(session);
                return session;
            }
            throw new IllegalArgumentException("Session already exists");


        }
        public static Session joinSession(String sessionID, Player player)throws NoSuchElementException{
            Session temp= getSession(sessionID);
            temp.joinGame(player);
            return temp;
        }
        public static Session getSession(String sessionID){
            for (Session session:sessions
            ) {
                if(session.getId().equals(sessionID)){
                    System.out.println("session found");
                    return session;
                }
            }
            throw new NoSuchElementException("no Session with given ID found");
        }

    }
    public static ServerPlayer identify(Player player){
        //matches ServerPLayer and players
        for (ServerPlayer serverplayer:activeServerPlayers
        ) {
            if(serverplayer.getId().equals(player.getId())){
                return serverplayer;
            }
        }
        throw new NoSuchElementException("No such player");
    }
    public static int getUniqueID(){
        return uniqueID++;
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server(globalVariables.getAdress(), 1234, maxPlayers);
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

    public ServerResponse removePlayer(ServerPlayer player){
        //outdated
        //need to match player and serverplayer id to properly kill clientthread
        try {
            activeServerPlayers.remove(player);
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
                    if(activeServerPlayers.size()< maxPlayers) {

                        ClientThread clientThread = new ClientThread(socket.accept());
                        System.out.println("new Player accepted");

                        int tempID=Server.getUniqueID();
                        System.out.print( " // uniqueID set: "+tempID+"//  ");

                        ServerPlayer serverPlayer =new ServerPlayer();
                        serverPlayer.setId(String.valueOf(tempID));
                        serverPlayer.setName(globalVariables.getDefaultname());
                        serverPlayer.setClientThread(clientThread);
                        activeServerPlayers.add(serverPlayer);

                        clientThread.setServerPlayer(serverPlayer);
                        clientThread.setID(String.valueOf(tempID));
                        clientThread.start();
                        System.out.println("active players:" + activeServerPlayers.size());

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
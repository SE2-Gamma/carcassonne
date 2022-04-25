package at.aau.se2.gamma.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import at.aau.se2.gamma.core.models.impl.Player;
import at.aau.se2.gamma.core.models.impl.Session;
import at.aau.se2.gamma.core.utils.GlobalVariables;
import at.aau.se2.gamma.server.models.ServerPlayer;

public  class Server implements Runnable {
    static final int maxPlayers =10;
    private static int uniqueID=0; //todo check concurrency problems
    private final ServerSocket socket;
    static LinkedList<ServerPlayer> activeServerPlayers =new LinkedList<>(); //todo check concurrency problems
    ClientHandler clientHandler=null;
    public static Server server=null;
    static Scanner scanner=new Scanner(System.in);
    //---------------classes
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
        public static Player getPLayer(String SessionID,String playerID){
            for (Session session:sessions
            ) {
                if(session.getId().equals(SessionID)){
                    for (Player player: session.players
                    ) {
                        if(player.getId().equals(playerID)){
                            return player;
                        }
                    }
                    throw new NoSuchElementException("No such player in given session");
                }
            }
            throw new NoSuchElementException("no such session found");
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
    public class ClientHandler implements Runnable{
        private boolean running=false;
        public void terminate(){
            running=false;
        }
        @Override
        public void run() {
            running=true;
            while(running){
                try {
                    if(activeServerPlayers.size()< maxPlayers) {

                        ClientThread clientThread = new ClientThread(socket.accept());
                        System.out.println("new Player accepted");

                        int tempID=Server.getUniqueID();
                        System.out.print( " // uniqueID set: "+tempID+"//  ");

                        ServerPlayer serverPlayer =new ServerPlayer();
                        serverPlayer.setId(String.valueOf(tempID));
                        serverPlayer.setName(GlobalVariables.getDefaultname());
                        serverPlayer.setClientThread(clientThread);
                        activeServerPlayers.add(serverPlayer);

                        clientThread.setServerPlayer(serverPlayer);
                        clientThread.setID(String.valueOf(tempID));
                        clientThread.start();
                        System.out.println("active players:" + activeServerPlayers.size());

                    }else{
                        System.err.println("Too many Players"); //todo: Clientside: add notification that the server is full
                    }
                }catch (SocketException socketException){
                    if(!running){
                        System.out.println("serversocket closed intentionally");
                    }else{
                        socketException.printStackTrace();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
    public Server(String address, int port, int maxClients) throws IOException {
        this.socket = new ServerSocket(port, maxClients, InetAddress.getByName(address));
    }
    //----------------utility methods
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
    public static Player getPlayerbyName(String playername){
        for (ServerPlayer serverplayer:activeServerPlayers
        ) {
            if(serverplayer.getName().equals(playername)){
                return serverplayer.getPlayer();
            }
        }
        throw new NoSuchElementException("no player found with matching name");
    }
    public static ServerPlayer getServerPlayer(String playerID){
        for (ServerPlayer player:activeServerPlayers
        ) {
            if(player.getId().equals(playerID)){
                return player;
            }
        }
        throw new NoSuchElementException("no player with matching ID");
    }

    //-----------server
    @Override
    public void run() {
        clientHandler=new ClientHandler();
        Thread thread=new Thread(clientHandler);
        thread.start();
    }
    public static boolean startServer(){
        try {
            server = new Server(GlobalVariables.getAdress(), GlobalVariables.getPort(), maxPlayers);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Thread thread=new Thread(server);
        thread.start();

        System.out.println("server running");
        return true;
    }
    public static boolean closeServer(){
        server.closeAll();
        return true;
    }
    public boolean closeAll(){
        for (ServerPlayer serverplayer:activeServerPlayers
        ) {
            ClientThread clientThread= serverplayer.getClientThread();

            try {

                clientThread.terminate();
                Socket socket=clientThread.getSocket();
                socket.close();

            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
        clientHandler.terminate();
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("serversocket closed intentionally");
            e.printStackTrace();
        }


        return true;
    }



    public static void main(String[] args) throws IOException {

        Server server = new Server(GlobalVariables.getAdress(), GlobalVariables.getPort(), maxPlayers);
        Thread thread=new Thread(server);
        thread.start();

        System.out.println("server running");
        scanner.nextLine();
        System.out.println("closing clienthandler");
        server.closeAll();


    }


}
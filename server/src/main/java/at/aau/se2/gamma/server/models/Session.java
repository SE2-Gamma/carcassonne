package at.aau.se2.gamma.server.models;

import at.aau.se2.gamma.core.commands.BroadcastCommands.*;
import at.aau.se2.gamma.core.models.impl.*;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.core.utils.KickOffer;
import at.aau.se2.gamma.server.Server;

import java.io.Serializable;
import java.util.*;

public class Session extends BaseModel implements Serializable {

//-----------------------Variables-------------------------------
    GameObject gameObject;
    Deck deck;
    String id;
    int maxPlayers=5;
    LinkedList<KickOffer>kickOffers=new LinkedList<>();
    public LinkedList<Player> players = new LinkedList<>();
    public LinkedList<Player> readyPlayers = new LinkedList<>();
    GameState gameState=null;
    GameLoop gameLoop=null;

//--------------------------Lobby-Methods---------------------
    public void playerReady(Player player){

        if(!players.contains(player)){
            readyPlayers.add(player);
            broadcastAllPlayers(new PlayerReadyBroadcastCommand(player.getName()));

        }
        if(readyPlayers.size()==players.size()){
            startGame();
        }
      }
    public void playerNotReady(Player player){
            readyPlayers.remove(player);
            broadcastAllPlayers(new PlayerNotReadyBroadcastCommand(player.getName()));
    }
    public void broadcastAllPlayers(BroadcastCommand command){
        //todo catch potential errors
        for (Player player:players
        ) {
            Server.identify(player).getClientThread().broadcastMessage(command);
        }
    }
    public void broadcastAllPlayers(BroadcastCommand command,Player notSendPlayer){
        //todo catch potential errors
        for (Player player:players
        ) {
            if(!(player.getId().equals(notSendPlayer.getId())))
            Server.identify(player).getClientThread().broadcastMessage(command);
        }
    }

    public void joinGame(Player player){
        if(players.size()>maxPlayers){
            throw new IllegalArgumentException("Spiel voll");
        }
        for (Player a:players
        ) {
            if(Objects.equals(a.getId(), player.getId())){
                throw new IllegalArgumentException("Spieler bereits vorhanden");
            }
        }
        players.add(player);
    }
    public void removePlayer(Player player){
        System.out.print("//removing player "+player.getName());
        ServerPlayer tempserverplayer=Server.identify(player);

        System.out.print("//notifying "+player.getName()+" he has been removed");
        tempserverplayer.getClientThread().broadcastMessage(new PlayerLeftLobbyBroadcastCommand(player.getName()));
        players.remove(player);
        tempserverplayer.getClientThread().setClientState(ClientState.INITIAl);


        System.out.print("//notifying all  "+player.getName()+"  has been removed");
        broadcastAllPlayers(new PlayerLeftLobbyBroadcastCommand(player.getName()));

        if(players.size()==0){
            System.out.println("no player left in session + "+id+" //");
            if( Server.SessionHandler.removeSession(this)){
                System.out.print("//Session"+id+" deleted//");
            }
        }


    }
    public boolean voteKick(Player player,Player votee) {
        int votes = 0;
        getPlayer(player.getId()); //to throw exception if player is not here
        boolean checker = true;
        KickOffer offer = null;
        for (KickOffer kickoffer : kickOffers
        ) {
            if (kickoffer.getPlayer().getId().equals(player.getId())) {
                votes = kickoffer.vote(votee);
                checker = false;
                offer=kickoffer;
            }
        }

        if (checker) {
            offer = new KickOffer(player);
            kickOffers.add(offer);
            votes=offer.vote(votee);
        }

        int tobeat = players.size() / 2;
        System.out.print("//voting to kick player " + player.getName());
        System.out.print("//"+votes + " out of " + tobeat + " to kick//");


        if (tobeat >= votes) {
            kickOffers.remove(offer);
            broadcastAllPlayers(new PlayerKickedBroadcastCommand(offer));
            removePlayer(player);
            System.out.println("//player kicked//");

            return true;
        }
        broadcastAllPlayers(new KickAttemptBroadcastCommand(offer));
        System.out.println("//not enough votes to kick//");
        return false;
    }

     public void startGame(){
        gameObject=new GameObject(new GameMap());
        for (Player temp:players
             ) {
            Server.identify(temp).getClientThread().setClientState(ClientState.GAME);
        }

        broadcastAllPlayers(new GameStartedBroadcastCommand(gameObject));
        setDeck(1);
        deck.printDeck();
        gameLoop=new GameLoop(this);
        gameLoop.start();


    }


//-----------------------------Game-Activity--------------------------
    public int timeout=60000;
    public boolean gameMovesuccessfull(GameMove gameturn) {
        //todo implement
        boolean successfull=true;
        //check if gamemove was succesfull
        if(successfull){
            //do gamemove, updating the gameobject. once updated, the gameloop will continue and send the updated gameobject to all clients
            gameLoop.interrupt();//interrupt waiting gameloop
            return true; //tell the clienthread the move is succesfull
        }
    return false; //tell the clienthtread the move was unsuccessfull. the clientthread will then wait for another turn to be made, which will be checked again
}

    public class GameLoop extends Thread{
        Session session;
        boolean playing;
        LinkedList<Player>turnOrder;
        GameLoop(Session session){
            this.session=session;
        }
        @Override
        public void run() {
            playing=true;
            //caution: reference
            turnOrder=new LinkedList<>(session.players);
            printTurnOrder(turnOrder);
            shuffle(turnOrder);

            while (playing){
                Player onTurn=turnOrder.pop();
                turnOrder.addLast(onTurn);
                GameCard card=null;
                try {
                     card=deck.drawCard();
                } catch (NoSuchElementException e) {
                    gameEnded(); //todo: implement
                }
                Server.identify(onTurn).getClientThread().broadcastMessage(new YourTurnBroadcastCommand(card));
                broadcastAllPlayers(new PlayerXsTurnBroadcastCommand(onTurn.getName()),onTurn);

                try {
                    Thread.sleep(timeout); //waiting for succesfull move to be made
                } catch (InterruptedException e) {
                    broadcastAllPlayers(new GameTurnBroadCastCommand(gameObject));
                }


            }


        }

        private void gameEnded() {
        }

        static void printTurnOrder(LinkedList<Player>list){
            int counter=1;
            for (Player player:list
                 ) {
                System.out.print("// "+counter+".: "+player.getName()+" //");
                counter++;
            }
        }
        static void shuffle(LinkedList<Player>list)
        {
            Player[] arr=new Player[list.size()];
            list.toArray(arr);

            Random rand = new Random();
            for (int i = 0; i < arr.length; i++) {

                // select index randomly
                int index = rand.nextInt(i + 1);

                // swapping between i th term and the index th
                // term
                Player g = arr[index];
                arr[index] = arr[i];
                arr[i] = g;
            }

            list.clear();
            list.addAll(Arrays.asList(arr));


        }
    }





//------------utility-------------------

    public Player getPlayer(String playerID){
    for (Player player:players
    ) {
        if(player.getId().equals(playerID)){
            return player;
        }
    }
    throw new NoSuchElementException("no player found with matching playerID");
}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Deck getDeck() {
        return deck;
    }
    public GameState getGameState() {
        return gameState;
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public Session(String id) {
        this.id = id;
    }
    public void setDeck(int multfaktor){
        System.out.print("//setting deck//");

        deck=new Deck(multfaktor);
        System.out.print("//deck set and shuffled.//");

    }

}

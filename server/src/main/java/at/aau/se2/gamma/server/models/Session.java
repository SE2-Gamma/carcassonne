package at.aau.se2.gamma.server.models;

import at.aau.se2.gamma.core.commands.BroadcastCommands.*;
import at.aau.se2.gamma.core.exceptions.InvalidPositionGameMapException;
import at.aau.se2.gamma.core.exceptions.NoSurroundingCardGameMapException;
import at.aau.se2.gamma.core.exceptions.PositionNotFreeGameMapException;
import at.aau.se2.gamma.core.exceptions.SurroundingConflictGameMapException;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.*;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.core.utils.KickOffer;
import at.aau.se2.gamma.server.Server;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Session extends BaseModel implements Serializable {

//-----------------------Variables-------------------------------
    GameObject gameObject;
    Deck deck;
    String id;
    int maxPlayers=5;
    final LinkedList<KickOffer>kickOffers=new LinkedList<>();
   // public LinkedList<Player> players = new LinkedList<>();
    public ConcurrentLinkedDeque<Player>players=new ConcurrentLinkedDeque<>();
    public ConcurrentLinkedDeque<Player>readyPlayers=new ConcurrentLinkedDeque<>();
   // public LinkedList<Player> readyPlayers = new LinkedList<>();
    GameState gameState=null;
    GameLoop gameLoop=null;

//--------------------------Lobby-Methods---------------------
    public void playerReady(Player player){
        System.out.print("//"+player.getName()+"tells he is ready//");
        if(!readyPlayers.contains(player)){
            readyPlayers.add(player);
            for (Player a:readyPlayers
                 ) {
                System.out.print("//"+a.getName()+" is ready.//");

            }
            System.out.print("//broadcasting//");
            broadcastAllPlayers(new PlayerReadyBroadcastCommand(player.getName()),player);

        }
        if(readyPlayers.size()==players.size()){
            System.out.print("//all players are ready. starting game//");
            startGame();
        }
      }
    public void playerNotReady(Player player){
        System.out.print("//"+player.getName()+"tells he is not ready//");
            readyPlayers.remove(player);
        for (Player a:readyPlayers
        ) {
            System.out.print("//"+a.getName()+" is ready.//");

        }
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
        for (Player a:players
             ) {
            System.out.print("//notified: "+a.getName());
        }
        broadcastAllPlayers(new PlayerLeftLobbyBroadcastCommand(player.getName()),player);

        if(players.size()==0){
            System.out.println("no player left in session + "+id+" //");
            if( Server.SessionHandler.removeSession(this)){
                System.out.print("//Session"+id+" deleted//");
            }
        }


    }
    public boolean voteKick(Player player,Player votee) {
        synchronized (kickOffers){
        System.out.print("//session issue kickvote//");
        int votes = 0;
        System.out.print("//session finding player//");
        getPlayer(player.getId()); //to throw exception if player is not here
        System.out.print("//session player found//");
        boolean checker = true;
        KickOffer offer = null;
        System.out.print("//session finding preexisting kickoffer//");
        try {
            System.out.print("//preixisting kickoffers:"+Arrays.toString(kickOffers.toArray())+"//");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("//no preixisting kickoffers//");
        }

        for (KickOffer kickoffer : kickOffers
        ) {
            System.out.print("//session found a kickoffer//");
            if (kickoffer.getPlayer().getId().equals(player.getId())) {
                System.out.print("//found a kickoffer//");
                votes = kickoffer.vote(votee);
                checker = false;
                offer=kickoffer;
            }
        }

        if (checker) {
            System.out.print("//session creating kickvote//");
            offer = new KickOffer(player);
            kickOffers.add(offer);
            votes=offer.vote(votee);
        }

        int tobeat = players.size() / 2;
        System.out.print("//voting to kick player " + player.getName());
        System.out.print("//"+votes + " out of " + tobeat + " to kick//");


        if (tobeat < votes) {
            kickOffers.remove(offer);
            broadcastAllPlayers(new PlayerKickedBroadcastCommand(offer));
            removePlayer(player);
            System.out.println("//player kicked//");

            return true;
        }
        broadcastAllPlayers(new KickAttemptBroadcastCommand(offer),votee);
        System.out.println("//not enough votes to kick//");
        return false;}
    }

     public void startGame(){
        //todo: check if the first gamemapentry is supposed to be in the game
        GameMap gameMap = new GameMap();
         gameMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), new Player("-1","server")), new GameMapEntryPosition(0,0));
        gameObject=new GameObject(gameMap);
        for (Player temp:players
             ) {
            Server.identify(temp).getClientThread().setClientState(ClientState.GAME);
        }

        broadcastAllPlayers(new GameStartedBroadcastCommand(gameObject));
        setDeck(1);
        deck.printDeck();
        gameLoop=new GameLoop(this);
        gameLoop.start();
         System.out.println();
         System.out.println("//------------------Game "+id+" started--------------------------//");
         System.out.println();

    }


//-----------------------------Game-Activity--------------------------
    public int timeout=60000;
    public void executeGameMove(GameMove gameturn) throws InvalidPositionGameMapException, SurroundingConflictGameMapException, NoSurroundingCardGameMapException, PositionNotFreeGameMapException {
        System.out.print("//checking incoming turn!//");
      gameObject.getGameMap().executeGameMove(gameturn); //if no exception is thrown, the gameloop will be interrupted and a succesfull message will be returned
            //do gamemove, updating the gameobject. once updated, the gameloop will continue and send the updated gameobject to all clients
        System.out.print("//turn has been succesfull!//");
        while (!interruptable) {
            gameLoop.interrupt();//interrupt waiting gameloop


        }


}
public boolean interruptable=false;
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
            shuffle(turnOrder);
            printTurnOrder(turnOrder);

            while (playing){
                interruptable=false;
                System.out.print("//a new iteration has started//");
                Player onTurn=turnOrder.pop();
                turnOrder.addLast(onTurn);
                GameCard card=null;
                System.out.println("//its "+onTurn.getName()+"'s turn!//");
                try {
                     card=deck.drawCard();
                    System.out.print(card.getCardId()+" has been drawn//");
                } catch (NoSuchElementException e) {
                    System.out.println("----------------------game ended---------------------");
                    gameEnded(); //todo: implement
                }

                Server.identify(onTurn).getClientThread().broadcastMessage(new YourTurnBroadcastCommand(card));
                System.out.print("//"+onTurn.getName()+" has been notified//");
                broadcastAllPlayers(new PlayerXsTurnBroadcastCommand(onTurn.getName()),onTurn);
                System.out.print("//notifying all players//");

                try {
                    interruptable=true;
                    System.out.print("//waiting for move to be made//");
                    Thread.sleep(timeout); //waiting for succesfull move to be made
                } catch (InterruptedException e) {
                    System.out.print("//notifying all players a turn has been made//");
                    broadcastAllPlayers(new GameTurnBroadCastCommand(gameObject));
                }
                System.out.print("//no successfull move made//");


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

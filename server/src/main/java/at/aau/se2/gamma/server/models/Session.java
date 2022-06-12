package at.aau.se2.gamma.server.models;

import at.aau.se2.gamma.core.commands.BroadcastCommands.*;
import at.aau.se2.gamma.core.exceptions.*;
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
    int amountOfSoldiers=3;
    final LinkedList<KickOffer>kickOffers=new LinkedList<>();
   // public LinkedList<Player> players = new LinkedList<>();
    public ConcurrentLinkedDeque<Player>players=new ConcurrentLinkedDeque<>();
    public ConcurrentLinkedDeque<Player>playerArchive=new ConcurrentLinkedDeque<>();
    public ConcurrentLinkedDeque<Player>readyPlayers=new ConcurrentLinkedDeque<>();
   // public LinkedList<Player> readyPlayers = new LinkedList<>();
    GameState gameState=null;
   public  GameLoop gameLoop=null;

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
        playerArchive.add(new Player(player.getId(),player.getName()));
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
         gameMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), new Player("-1","server")), new GameMapEntryPosition(49,49));
        gameObject=new GameObject(gameMap);

        for (Player temp:players
             ) {
            Server.identify(temp).getClientThread().setClientState(ClientState.GAME);
        }

         for (Player player:players
         ) {
             player.addAmountOfSoldiers(amountOfSoldiers);
         }
         gameObject.setGameStatistic(new GameStatistic(new ArrayList<>(players)));

        broadcastAllPlayers(new GameStartedBroadcastCommand(gameObject));

        gameObject.getGameMap().setGameMapHandler((GameMapHandler) detectionData -> {
            System.out.print("//field completed, sending broadcast command");
            gameObject.getGameStatistic().applyClosedFieldDetectionData(detectionData);
            broadcastAllPlayers(new FieldCompletedBroadcastCommand(gameObject.getGameStatistic()));

        });
         try {
             Thread.sleep(3000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         setDeck(1);
        deck.printDeck();
        gameLoop=new GameLoop(this,gameObject);
        gameLoop.start();
         System.out.println();
         System.out.println("//------------------Game "+id+" started--------------------------//");
         System.out.println();

    }


//-----------------------------Game-Activity--------------------------
    public int timeout=60000;
    public void executeGameMove(GameMove gameturn) throws InvalidPositionGameMapException, SurroundingConflictGameMapException, NoSurroundingCardGameMapException, PositionNotFreeGameMapException {
        System.out.print("//checking incoming turn!//");
        gameturn.setSoldierData(gameturn.getSoldierData());
        gameturn.changeToServerInstance(players, gameLoop.gameObject.getGameMap());
        SoldierPlacement soldierPlacement = null;

        // save placement in temp, to add it later.
        if (gameturn.getGameMapEntry().getSoldierPlacements().size() > 0) {

            soldierPlacement = gameturn.getGameMapEntry().getSoldierPlacements().get(0);
            System.out.println();
            System.out.println("//placing soldier at X: "+soldierPlacement.getSoldier().getX()+", Y:  "+soldierPlacement.getSoldier().getY()+"//");
            gameturn.getGameMapEntry().getSoldierPlacements().clear();
        }

        gameLoop.gameObject.getGameMap().executeGameMove(gameturn); //if no exception is thrown, the gameloop will be interrupted and a succesfull message will be returned
            //do gamemove, updating the gameobject. once updated, the gameloop will continue and send the updated gameobject to all clients

        // set soldier placement
        if (soldierPlacement != null) {
            gameturn.getGameMapEntry().setSoldier(soldierPlacement.getSoldier(), soldierPlacement.getGameCardSide());
            gameturn.setSoldierData(new SoldierData(soldierPlacement.getSoldier()));
        }

        System.out.print("//turn has been succesfull!//");
        while (!interruptable) { //if the gameloop is in another state than waiting for a turn we busywait for it to finish (only relevant if you enter a turn 1 ms after your turnstart)
            System.out.print(".");
        }



        broadcastAllPlayers(new GameTurnBroadCastCommand(gameturn));
         gameLoop.interrupt();//interrupt waiting gameloop

    }
    public void executeCheat(CheatData cheatData,int penalty) throws CheatMoveImpossibleException {
        System.out.println("data position: xy "+cheatData.getX()+" "+cheatData.getY());
        CheatMove cheatMove=CheatMove.getMoveFromData(cheatData,gameLoop.gameObject);
        cheatMove.setPenalty(penalty);
        System.out.println("cheating soldier at position x: "+cheatMove.getSoldier().getX()+"  Y: "+cheatMove.getSoldier().getY());
        System.out.print("//checking cheatmove//");

        cheatMove.changeToServerInstance(players, gameLoop.gameObject.getGameMap());

        gameLoop.gameObject.getGameMap().executeCheatMove(cheatMove);
        CheatData data=cheatMove.getData();
        broadcastAllPlayers(new CheatMoveBroadcastCommand(data));
        System.out.print("// cheatmove broadcasted//");
    }
    public void detectCheat(SoldierData data) throws NoSuchCheatActiveException {
        Soldier soldier=gameLoop.gameObject.getGameStatistic().getSoldierBySoldierData(data);
        System.out.print("//trying to detect a cheat.//");

        LinkedList<CheatMove> cheats=gameLoop.gameObject.getGameMap().detectCheatMove(soldier);
        System.out.print("//cheat detected//");
        System.out.print(cheats);
        //todo: give penalties
        gameLoop.gameObject.getGameMap().undoCheatMove(cheats);
        LinkedList<CheatData>cheatData=new LinkedList<>();
        for (CheatMove move:cheats
             ) {
         cheatData.add(move.getData());
        }
        System.out.print("//cheat undone//");

        broadcastAllPlayers(new CheatMoveDetectedBroadcastCommand(new LinkedList<>(cheatData)));
        cheats.clear();
    }
    public void leaveGame(Player player){
        gameLoop.turnOrder.remove(player);
        players.remove(player);
        if(gameLoop.turnOrder.size()==0){
            gameLoop.gameEnded();
        }
        if(gameLoop.onTurn.getId().equals(player.getId())){
            gameLoop.interrupt();
        }
        //remove player from gameobject?
        broadcastAllPlayers(new PlayerLeftGameBroadcastCommand(player.getName()));
    }
public boolean interruptable=false;
    public class GameLoop extends Thread{
        GameObject gameObject;
        Session session;
        boolean playing;
        public Player onTurn;
        LinkedList<Player>turnOrder;

        GameLoop(Session session, GameObject gameObject){
            this.session=session;
            this.gameObject=gameObject;
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
                onTurn=turnOrder.pop();
                turnOrder.addLast(onTurn);
                GameCard card=null;
                System.out.println("//its "+onTurn.getName()+"'s turn!//");
                try {
                    card=deck.drawCard();

                   while(!gameObject.getGameMap().checkCardPlaceability(card)){
                       System.out.print(card.getCardId()+" has been drawn//");
                       System.out.print("//card not placable, draw new card//");
                       deck.putBackCard(card);
                       card=deck.drawCard();
                   }

                    System.out.print(card.getCardId()+" has been drawn//");
                } catch (NoSuchElementException e) {
                    System.out.println("----------------------game ended---------------------");
                    gameEnded(); //todo: implement
                }

                Server.identify(onTurn).getClientThread().broadcastMessage(new YourTurnBroadcastCommand(card)); //throws socket exception end of stream if player disconnected
                System.out.print("//"+onTurn.getName()+" has been notified//");
                broadcastAllPlayers(new PlayerXsTurnBroadcastCommand(onTurn.getName()),onTurn);
                System.out.print("//notifying all players//");

                try {
                    interruptable=true;
                    System.out.print("//waiting for move to be made//");
                    Thread.sleep(timeout); //waiting for succesfull move to be made

                } catch (InterruptedException e) {
                    System.out.print("//notifying all players a turn has been made//");
                // broadcastAllPlayers(new GameTurnBroadCastCommand(this.gameObject));

                }



            }


        }

        private void gameEnded() {
            System.out.print("//stopping game//");
            gameLoop.playing=false;
            gameLoop.interrupt();
            broadcastAllPlayers(new GameCompletedBroadcastCommand("game ended"));
            System.out.print("//all players have been notified. //");
            Server.SessionHandler.removeSession(session);
        }

         void printTurnOrder(LinkedList<Player>list){
            int counter=1;
            for (Player player:list
                 ) {
                System.out.print("// "+counter+".: "+player.getName()+" //");
                counter++;
            }
        }
         void shuffle(LinkedList<Player>list)
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

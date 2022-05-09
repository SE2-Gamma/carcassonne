package at.aau.se2.gamma.server.models;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BroadcastCommands.BroadcastCommand;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.StringBroadcastCommand;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.*;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.core.utils.KickOffer;
import at.aau.se2.gamma.server.ResponseCreator;
import at.aau.se2.gamma.server.Server;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class Session extends BaseModel implements Serializable {
    public Deck getDeck() {
        return deck;
    }

    Deck deck;
    String id=null;
    int maxPlayers=5;
    LinkedList<KickOffer>kickOffers=new LinkedList<>();
    public LinkedList<Player> players = new LinkedList<>();
    public String getId() {
        return id;
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
    public void setId(String id) {
        this.id = id;
    }

    public GameState getGameState() {
        return gameState;
    }
    public void initializeDeck(){

    }
    public void setDeck(int multfaktor){
        System.out.print("//setting deck//");

        deck=new Deck(multfaktor);
        System.out.print("//deck set and shuffled.//");

    }
    public boolean voteKick(Player player,Player votee) {
        int votes = 0;
        getPlayer(player.getId()); //to throw exception if player is not here
        boolean checker = true;
        for (KickOffer kickoffer : kickOffers
        ) {
            if (kickoffer.getPlayer().getId().equals(player.getId())) {
                votes = kickoffer.vote(votee);
                checker = false;
            }
        }
        KickOffer offer = null;
        if (checker) {
            offer = new KickOffer(player);
            kickOffers.add(offer);
            votes=offer.vote(votee);
        }


        if (votes == 0) {
            return false;
        }
        int tobeat = players.size() / 2;
        System.out.print("//voting to kick player " + player.getName());
        System.out.print("//"+votes + " out of " + tobeat + " to kick//");
        payloadBroadcastAllPlayers(ResponseCreator.getBroadcastMessage("Kick attempted."+votes+" out of "+tobeat+" to kick player "+player.getName()));
        if (tobeat <= votes) {
            kickOffers.remove(offer);
            removePlayer(player);
            System.out.println("//player kicked//");
            return true;
        }
        System.out.println("//not enough votes to kick//");
       return false;
    }
    public void removePlayer(Player player){
        System.out.print("//removing player "+player.getName());
        ServerPlayer tempserverplayer=Server.identify(player);
        tempserverplayer.getClientThread().broadcastMessage(ResponseCreator.getBroadcastMessage("you have been removed from the lobby"));
        System.out.print("//notifying "+player.getName()+" he has been removed");
        tempserverplayer.getClientThread().setClientState(ClientState.INITIAl);
        payloadBroadcastAllPlayers(player.getName()+" has been removed");
        System.out.print("//notifying all  "+player.getName()+"  has been removed");
        players.remove(player);
        System.out.print("//has been removed from session//");
        if(players.size()==0){
            System.out.print("no player left in session + "+id+" //");
           if( Server.SessionHandler.removeSession(this)){
               System.out.print("//Session"+id+" deleted//");
           }
        }


    }
    public void payloadBroadcastAllPlayers(Object payload){
        //todo catch potential errors
        for (Player player:players
             ) {
            Server.identify(player).getClientThread().broadcastMessage(ResponseCreator.getBroadcastMessage(payload));
        }
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public Player getPlayer(String playerID){
    for (Player player:players
         ) {
        if(player.getId().equals(playerID)){
            return player;
        }
    }
  throw new NoSuchElementException("no player found with matching playerID");
    }
    GameState gameState=null;
    public Session(String id) {
        this.id = id;
    }



}

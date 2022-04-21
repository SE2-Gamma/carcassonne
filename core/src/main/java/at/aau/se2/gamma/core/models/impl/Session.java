package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.utils.KickOffer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.SynchronousQueue;

public class Session extends BaseModel implements Serializable {
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
    public boolean voteKick(Player player) {
        int votes = 0;
        getPlayer(player.getId()); //to throw exception if player is not here
        boolean checker = true;
        for (KickOffer kickoffer : kickOffers
        ) {
            if (kickoffer.getPlayer().getId().equals(player.getId())) {
                votes = kickoffer.vote();
                checker = false;
            }
        }
        KickOffer offer = null;
        if (checker) {
            offer = new KickOffer(player);
            kickOffers.add(offer);
            votes=offer.vote();
        }


        if (votes == 0) {
            return false;
        }
        int tobeat = players.size() / 2;
        System.out.print("//voting to kick player " + player.getName());
        System.out.print("//"+votes + " out of " + tobeat + " to kick//");
        if (tobeat <= votes) {
            removePlayer(player);
            return true;
        }
       return false;
    }
    public void removePlayer(Player player){
        players.remove(player);
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

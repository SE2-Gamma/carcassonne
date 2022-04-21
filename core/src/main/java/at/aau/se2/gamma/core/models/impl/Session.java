package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Session extends BaseModel implements Serializable {
    String id=null;
    int maxPlayers=5;
    public ArrayList<Player> players = new ArrayList<>();
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

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    GameState gameState=null;
    public Session(String id) {
        this.id = id;
    }



}

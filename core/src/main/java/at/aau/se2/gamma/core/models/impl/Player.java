package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.models.interfaces.PlayerInterface;

import java.io.Serializable;

public class Player extends BaseModel implements PlayerInterface, Serializable {
    private String id;
    private String name;

    private PlayerPoints playerPoints;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        playerPoints = new PlayerPoints(0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerPoints getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(PlayerPoints playerPoints) {
        this.playerPoints = playerPoints;
    }

    public void addPlayerPoints(int points) {
        this.playerPoints.addPoints(points);
    }

    public int getPoints() {
        return this.playerPoints.getPoints();
    }
}

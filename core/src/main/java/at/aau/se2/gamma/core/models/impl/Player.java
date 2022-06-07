package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.models.interfaces.PlayerInterface;

import java.io.Serializable;
import java.util.ArrayList;

public class Player extends BaseModel implements PlayerInterface, Serializable {
    private String id;
    private String name;
    private PlayerPoints playerPoints;
    private ArrayList<Soldier> soldiers;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        playerPoints = new PlayerPoints(0);
        soldiers = new ArrayList<>();
    }

    /**
     * Add soldiers to a player.
     * Should be done at the start of a game.
     * @param amount
     */
    public void addAmountOfSoldiers(int amount) {
        for(int i = 0; i < amount; i++) {
            this.soldiers.add(new Soldier(this));
        }
    }

    /**
     * get a free soldier if one is available
     * if not available returns null
     * @return
     */
    public Soldier getFreeSoldier() {
        for(Soldier soldier: soldiers) {
            if(soldier.isAvailable()) {
                return soldier;
            }
        }

        // if no soldier is free, return null
        return null;
    }

    public void clearSoldiers() {
        this.soldiers.clear();
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

    public ArrayList<Soldier> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(ArrayList<Soldier> soldiers) {
        this.soldiers = soldiers;
    }
}

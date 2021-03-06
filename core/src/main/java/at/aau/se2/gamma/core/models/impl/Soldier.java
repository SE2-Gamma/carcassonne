package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * soldiers placed on the field by the player
 */
public class Soldier implements Serializable {

    private static int lastId = 1;
    private Player player; // reference to the player, who owns this soldier
    private int id;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public LinkedList<CheatMove> getActiveCheats() {
        return activeCheats;
    }

    public void addCheat(CheatMove cheat) {
       activeCheats.add(cheat);
    }

    private LinkedList<CheatMove>activeCheats=new LinkedList<>();
    private int x;
    private int y;
    public SoldierPlacement getSoldierPlacement() {return soldierPlacement;}

    public SoldierPlacement soldierPlacement; // current placement

    public SoldierData getData(){
        return new SoldierData(this);
    }
    public Soldier(Player player) {
        this.player = player;
        this.id = lastId++;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isCurrentlyPlaced() {
        return soldierPlacement != null;
    }

    public void setSoldierPlacement(SoldierPlacement soldierPlacement) {
        this.soldierPlacement = soldierPlacement;
    }

    /**
     * let the soldier coming back to the player
     */
    public void comeBackHome() {
        this.soldierPlacement.setClosed(true);
        this.soldierPlacement = null;
    }

    /**
     * check if soldier is available for a placement
     * @return
     */
    public boolean isAvailable() {
        return this.soldierPlacement == null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

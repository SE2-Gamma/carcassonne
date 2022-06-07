package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

/**
 * soldiers placed on the field by the player
 */
public class Soldier implements Serializable {

    private Player player; // reference to the player, who owns this soldier

    public SoldierPlacement getSoldierPlacement() {return soldierPlacement;}

    private SoldierPlacement soldierPlacement; // current placement

    public boolean isCheatActive() {return cheatActive;}

    public void setCheatActive(boolean cheatActive) {this.cheatActive = cheatActive;}

    private boolean cheatActive;

    public Soldier(Player player) {
        this.player = player;
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
}

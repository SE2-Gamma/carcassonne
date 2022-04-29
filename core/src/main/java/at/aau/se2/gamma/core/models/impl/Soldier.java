package at.aau.se2.gamma.core.models.impl;

/**
 * soldiers placed on the field by the player
 */
public class Soldier {
    private Player player; // reference to the player, who owns this soldier
    private SoldierPlacement soldierPlacement; // current placement

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
}

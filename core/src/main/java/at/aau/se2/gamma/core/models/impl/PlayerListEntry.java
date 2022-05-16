package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerListEntry implements Serializable {
    private Player player;
    private int points = 0;
    private ArrayList<Soldier> soldiers = new ArrayList<>();

    public PlayerListEntry(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList<Soldier> getSoldiers() {
        return soldiers;
    }
}

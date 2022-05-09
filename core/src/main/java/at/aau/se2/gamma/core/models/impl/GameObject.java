package at.aau.se2.gamma.core.models.impl;

import java.util.ArrayList;

public class GameObject {
    private GameMap gameMap;
    private ArrayList<PlayerListEntry> playerListEntries = new ArrayList<>();

    public GameObject(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public ArrayList<PlayerListEntry> getPlayerListEntries() {
        return playerListEntries;
    }
}

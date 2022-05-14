package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

public class GameMove implements Serializable {
    private Player player;
    private GameMapEntry gameMapEntry;
    private GameMapEntryPosition gameMapEntryPosition;

    public GameMove() {

    }

    public GameMove(Player player, GameMapEntry gameMapEntry, GameMapEntryPosition gameMapEntryPosition) {
        this.player = player;
        this.gameMapEntry = gameMapEntry;
        this.gameMapEntryPosition = gameMapEntryPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameMapEntry getGameMapEntry() {
        return gameMapEntry;
    }

    public void setGameMapEntry(GameMapEntry gameMapEntry) {
        this.gameMapEntry = gameMapEntry;
    }

    public GameMapEntryPosition getGameMapEntryPosition() {
        return gameMapEntryPosition;
    }

    public void setGameMapEntryPosition(GameMapEntryPosition gameMapEntryPosition) {
        this.gameMapEntryPosition = gameMapEntryPosition;
    }
}

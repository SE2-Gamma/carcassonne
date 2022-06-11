package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.exceptions.CheatMoveImpossibleException;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

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

    public void changeToServerInstance(ConcurrentLinkedDeque<Player> players, GameMap gameMap) throws NoSuchElementException {
        boolean playerFound = false;

        for(Player player: players) {
            if (this.player.getId().equals(player.getId())) {
                this.player = player;
                playerFound = true;
            }
        }

        if (!playerFound) {
            throw new NoSuchElementException();
        }

        this.gameMapEntry.setPlacedByPlayer(player);

        // map soldier placement if needed
        if (this.gameMapEntry.getSoldierPlacements().size() > 0) {
            boolean soldierFound = false;
            for(Soldier soldier: this.player.getSoldiers()) {
                SoldierPlacement soldierPlacement = this.gameMapEntry.getSoldierPlacements().get(0);
                if (soldier.getId() == soldierPlacement.getSoldier().getId()) {
                    soldierPlacement.setSoldier(soldier);
                    soldierFound = true;
                }
            }
            if (!soldierFound) {
                throw new NoSuchElementException();
            }
        }
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

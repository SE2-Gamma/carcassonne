package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

public class SoldierPlacement implements Serializable {
    private Soldier soldier;
    private GameCardSide gameCardSide;
    private boolean closed;

    public SoldierPlacement(Soldier soldier, GameCardSide gameCardSide) {
        this.soldier = soldier;
        this.gameCardSide = gameCardSide;
        this.closed = false;
    }

    public Soldier getSoldier() {
        return soldier;
    }

    public GameCardSide getGameCardSide() {
        return gameCardSide;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setSoldier(Soldier soldiercopy) {
        this.soldier=soldiercopy;
    }

    public void setGameCardSide(GameCardSide gameCardSide) {
        this.gameCardSide = gameCardSide;
    }
}

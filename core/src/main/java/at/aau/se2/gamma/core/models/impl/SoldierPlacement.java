package at.aau.se2.gamma.core.models.impl;

public class SoldierPlacement {
    private Soldier soldier;
    private GameCardSide gameCardSide;

    public SoldierPlacement(Soldier soldier, GameCardSide gameCardSide) {
        this.soldier = soldier;
        this.gameCardSide = gameCardSide;
    }

    public Soldier getSoldier() {
        return soldier;
    }

    public GameCardSide getGameCardSide() {
        return gameCardSide;
    }
}

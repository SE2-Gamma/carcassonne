package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

public class CheatMove implements Serializable {
    Player cheater;
    boolean active;
    Soldier soldier;
    SoldierPlacement originalPosition;
    SoldierPlacement newPosition;
    int penalty; // tbd, is set in clientthread, is 2^numberOfCheats


    public SoldierPlacement getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(SoldierPlacement originalPosition) {
        this.originalPosition = originalPosition;
    }

    public SoldierPlacement getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(SoldierPlacement newPosition) {
        this.newPosition = newPosition;
    }



    public Player getCheater() {
        return cheater;
    }

    public void setCheater(Player cheater) {
        this.cheater = cheater;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Soldier getSoldier() {
        return soldier;
    }

    public void setSoldier(Soldier soldier) {
        this.soldier = soldier;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public CheatMove(Player cheater, Soldier soldier) {

        this.cheater = cheater;
        this.active = true;
        this.soldier = soldier;
    }
}

package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

public class CheatMove implements Serializable {
    String playername;
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



    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
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

    public CheatMove(String playername, Soldier soldier) {

        this.playername = playername;
        this.active = true;
        this.soldier = soldier;
    }
}

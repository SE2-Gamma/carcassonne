package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

public class CheatMove implements Serializable {
    String playername;
    boolean active;
    Soldier soldier;
    SoldierPlacement originalPosition;
    SoldierPlacement newPosition;

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

    int pointsLostIfDetected; // tbd, is set in clientthread, is 2^numberOfCheats

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

    public int getPointsLostIfDetected() {
        return pointsLostIfDetected;
    }

    public void setPointsLostIfDetected(int pointsLostIfDetected) {
        this.pointsLostIfDetected = pointsLostIfDetected;
    }

    public CheatMove(String playername, Soldier soldier) {

        this.playername = playername;
        this.active = true;
        this.soldier = soldier;
    }
}

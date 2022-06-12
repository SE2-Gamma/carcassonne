package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

public class CheatData implements Serializable {
    public int getOriginalCardSideUID() {
        return originalCardSideUID;
    }

    public void setOriginalCardSideUID(int originalCardSideUID) {
        this.originalCardSideUID = originalCardSideUID;
    }

    public int getNewCardSideUID() {
        return newCardSideUID;
    }

    public void setNewCardSideUID(int newCardSideUID) {
        this.newCardSideUID = newCardSideUID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getCheaterID() {
        return cheaterID;
    }

    public void setCheaterID(String playerID) {
        this.cheaterID = playerID;
    }

    String cheaterID;
    int originalCardSideUID;
    int newCardSideUID;
    int x;
    int y;
}

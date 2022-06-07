package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;

public class ClosedFieldDetectionData implements Serializable {
    private boolean isClosed = true;
    private int points = 0;
    private ArrayList<GameCardSide> gameCardSides;

    public ClosedFieldDetectionData() {
        gameCardSides = new ArrayList<>();
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addGameCardSide(GameCardSide gameCardSide) {
        this.gameCardSides.add(gameCardSide);
    }

    public ArrayList<GameCardSide> getGameCardSides() {
        return gameCardSides;
    }

    public void setGameCardSides(ArrayList<GameCardSide> gameCardSides) {
        this.gameCardSides = gameCardSides;
    }
}

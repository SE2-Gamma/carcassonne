package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;

public class ClosedFieldDetectionData implements Serializable {
    private boolean isClosed = true;
    private int points = 0;
    private ArrayList<GameCardSide> gameCardSides;
    private ArrayList<GameCardSide> scannedCastles; // only needed for end calculation for gras fields
    private ArrayList<ClosedFieldDetectionData> detectedCastles; // only needed for end calculation for gras fields
    private boolean endGameData = false;
    private boolean monasteryType = false;

    public ClosedFieldDetectionData() {
        gameCardSides = new ArrayList<>();
        scannedCastles = new ArrayList<>();
        detectedCastles = new ArrayList<>();
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

    public boolean isEndGameData() {
        return endGameData;
    }

    public void setEndGameData(boolean endGameData) {
        this.endGameData = endGameData;
    }

    public ArrayList<GameCardSide> getScannedCastles() {
        return scannedCastles;
    }

    public void setScannedCastles(ArrayList<GameCardSide> scannedCastles) {
        this.scannedCastles = scannedCastles;
    }

    public ArrayList<ClosedFieldDetectionData> getDetectedCastles() {
        return detectedCastles;
    }

    public void setDetectedCastles(ArrayList<ClosedFieldDetectionData> detectedCastles) {
        this.detectedCastles = detectedCastles;
    }

    public boolean isMonasteryType() {
        return monasteryType;
    }

    public void setMonasteryType(boolean monasteryType) {
        this.monasteryType = monasteryType;
    }
}

package at.aau.se2.gamma.core.models.impl;

public class ClosedFieldDetectionData {
    private boolean isClosed = true;
    private int points = 0;

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
}

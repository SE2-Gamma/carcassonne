package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * PlayerPoints to store the current points of a player, and to keep the points in history
 */
public class PlayerPoints implements Serializable {
    private ArrayList<Integer> history = new ArrayList<>();
    private int points = 0;

    public PlayerPoints(int points) {
        this.points = points;
    }

    public ArrayList<Integer> getHistory() {
        return history;
    }

    protected void setHistory(ArrayList<Integer> history) {
        this.history = history;
    }

    public int getPoints() {
        return points;
    }

    protected void setPoints(int points) {
        this.points = points;
    }

    /**
     * add the given points to the total points, and add transaction to history
     * @param points
     */
    public void addPoints(int points) {
        this.points += points;

        // add points to history
        history.add(points);
    }
}

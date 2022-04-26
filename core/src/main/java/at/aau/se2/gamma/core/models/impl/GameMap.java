package at.aau.se2.gamma.core.models.impl;

public class GameMap {
    // default values for the map
    public final static int DEFAULT_HEIGHT = 100;
    public final static int DEFAULT_WIDTH = 100;

    private GameMapEntry[][] mapArray;


    public GameMap() {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }

    public GameMap(int height, int width) {
        mapArray = new GameMapEntry[height][width];
    }

    public GameMapEntry[][] getMapArray() {
        return mapArray;
    }
}

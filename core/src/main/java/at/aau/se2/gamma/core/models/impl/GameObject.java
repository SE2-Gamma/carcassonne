package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;

public class GameObject implements Serializable {
    private GameMap gameMap;
    private GameStatistic gameStatistic;

    public GameObject(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public GameStatistic getGameStatistic() {
        return gameStatistic;
    }

    public void setGameStatistic(GameStatistic gameStatistic) {
        this.gameStatistic = gameStatistic;
    }
}

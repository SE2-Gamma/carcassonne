package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.models.impl.GameMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameMapTest {

    @Test
    public void useDefaultSizesIfNotSpecified() {
        GameMap gameMap = new GameMap();
        assertEquals(gameMap.getMapArray().length, GameMap.DEFAULT_HEIGHT);
        assertEquals(gameMap.getMapArray()[0].length, GameMap.DEFAULT_WIDTH);
    }
}

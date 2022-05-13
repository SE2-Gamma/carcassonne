package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameMapTest {

    GameMap gameMap;
    Player player1;

    @BeforeEach
    public void beforeEach() {
        player1 = new Player("1", "player 1");
        gameMap = new GameMap();
        gameMap.placeGameMapEntry(
                new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1),
                new GameMapEntryPosition(0,0));
        gameMap.placeGameMapEntry(
                new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.SOUTH),
                new GameMapEntryPosition(1,0));
        gameMap.placeGameMapEntry(
                new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.SOUTH),
                new GameMapEntryPosition(0,1));
    }

    @Test
    public void useDefaultSizesIfNotSpecified() {
        GameMap gameMap = new GameMap();
        assertEquals(gameMap.getMapArray().length, GameMap.DEFAULT_HEIGHT);
        assertEquals(gameMap.getMapArray()[0].length, GameMap.DEFAULT_WIDTH);
    }

    @Test
    public void get3x3SubMap() {
        /**
         * 0000     000
         * 10X0  -> 0X0
         * 1100     100
         */
        GameMapEntry[][] subMap = gameMap.get3x3SubMap(new GameMapEntryPosition(2, 1));
        assertEquals(subMap[0][0], gameMap.getMapArray()[0][1]);
    }

    @Test
    public void getNESWSurroundingFields() {
        /**
         * 0000      0
         * 1X00  -> 1X0 -> 0,0,1,1
         * 1100      1
         */
        GameMapEntry[] surroundings = gameMap.getNESWSurroundingFields(new GameMapEntryPosition(1, 1));
        assertNull(surroundings[0]);
        assertNull(surroundings[1]);
        assertEquals(surroundings[2], gameMap.getMapArray()[0][1]);
        assertEquals(surroundings[3], gameMap.getMapArray()[1][0]);
    }
}

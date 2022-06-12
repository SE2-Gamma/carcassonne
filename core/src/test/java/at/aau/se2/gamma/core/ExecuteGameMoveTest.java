package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.exceptions.InvalidPositionGameMapException;
import at.aau.se2.gamma.core.exceptions.NoSurroundingCardGameMapException;
import at.aau.se2.gamma.core.exceptions.PositionNotFreeGameMapException;
import at.aau.se2.gamma.core.exceptions.SurroundingConflictGameMapException;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExecuteGameMoveTest {

    GameMap gameMap;
    Player player1;

    @BeforeEach
    public void beforeEach() {
        player1 = new Player("1", "player 1");
        gameMap = new GameMap();
        gameMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1), new GameMapEntryPosition(0,0));
    }

    @Test
    public void positionInGameMap() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.NORTH);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(-1,0));

        assertThrows(InvalidPositionGameMapException.class, () -> {
            this.gameMap.executeGameMove(gameMove);
        });
    }

    @Test
    public void positionNotFree() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.NORTH);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(0,0));

        assertThrows(PositionNotFreeGameMapException.class, () -> {
            this.gameMap.executeGameMove(gameMove);
        });
    }

    @Test
    public void noSurroundings() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.NORTH);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(2,0));

        assertThrows(NoSurroundingCardGameMapException.class, () -> {
            this.gameMap.executeGameMove(gameMove);
        });

        GameMove gameMove2 = new GameMove(player1, entry, new GameMapEntryPosition(1,1));

        assertThrows(NoSurroundingCardGameMapException.class, () -> {
            this.gameMap.executeGameMove(gameMove2);
        });

        GameMove gameMove3 = new GameMove(player1, entry, new GameMapEntryPosition(0,2));

        assertThrows(NoSurroundingCardGameMapException.class, () -> {
            this.gameMap.executeGameMove(gameMove3);
        });
    }

    @Test
    public void surroundingConflict() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.EAST);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(1,0));

        /**
         *  G    C
         * S C  G S
         *  S    S
         */

        assertThrows(SurroundingConflictGameMapException.class, () -> {
            this.gameMap.executeGameMove(gameMove);
        });

        GameMapEntry entry2 = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.NORTH);
        GameMove gameMove2 = new GameMove(player1, entry2, new GameMapEntryPosition(1,0));

        /**
         *  G    G
         * S C  S C
         *  S    S
         */

        assertThrows(SurroundingConflictGameMapException.class, () -> {
            this.gameMap.executeGameMove(gameMove2);
        });
    }

    @Test
    public void success() {
        GameMapEntry entry = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1, Orientation.SOUTH);
        GameMove gameMove = new GameMove(player1, entry, new GameMapEntryPosition(1,0));

        /**
         *  G    S
         * S C  C S
         *  S    G
         */

        try {
            this.gameMap.executeGameMove(gameMove);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(this.gameMap.getMapArray()[0][1], entry);
    }
}

package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.exceptions.InvalidPositionGameMapException;
import at.aau.se2.gamma.core.exceptions.NoSurroundingCardGameMapException;
import at.aau.se2.gamma.core.exceptions.PositionNotFreeGameMapException;
import at.aau.se2.gamma.core.exceptions.SurroundingConflictGameMapException;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.factories.GameCardSideFactory;
import at.aau.se2.gamma.core.models.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameStatisticTest {
    Player player1;
    Player player2;
    Player player3;
    ArrayList<Player> players = new ArrayList<>();
    GameStatistic gameStatistic;
    GameMap gameMap;

    @BeforeEach
    public void beforeTest() {
        // add players
        player1 = new Player("person1", "Person1");
        player2 = new Player("person2", "Person2");
        player3 = new Player("person3", "Person3");

        // add players to arraylist
        players.add(player1);
        player1.addAmountOfSoldiers(5);
        players.add(player2);
        player2.addAmountOfSoldiers(5);
        players.add(player3);
        player3.addAmountOfSoldiers(5);

        // create statistics object
        gameStatistic = new GameStatistic(players);

        // add game map
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
    public void checkPlayers() {
        assertEquals(gameStatistic.getPlayers().size(), 3);
    }

    @Test
    public void addPlayer() {
        gameStatistic.addPlayer(new Player("person4", "Person4"));
        assertEquals(gameStatistic.getPlayers().size(), 4);
    }

    @Test
    public void removePlayer() {
        gameStatistic.removePlayer(player1);
        assertEquals(gameStatistic.getPlayers().size(), 2);
    }

    /**
     * test closed field with vertical fields
     *  X    X
     * X X  X X
     *  X    X
     * X C  C X
     *  X    X
     *
     * @throws InvalidPositionGameMapException
     * @throws SurroundingConflictGameMapException
     * @throws NoSurroundingCardGameMapException
     * @throws PositionNotFreeGameMapException
     */
    @Test
    public void twoCastlesWithTwoEqualPlayers() throws InvalidPositionGameMapException, SurroundingConflictGameMapException, NoSurroundingCardGameMapException, PositionNotFreeGameMapException {
        gameMap = new GameMap();
        final ClosedFieldDetectionData[] returnedDetectionData = {null};
        gameMap.setGameMapHandler(new GameMapHandler() {
            @Override
            public void onClosedField(ClosedFieldDetectionData detectionData) {
                returnedDetectionData[0] = detectionData;
            }
        });
        GameMapEntry entry1 = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1);
        entry1.setSoldier(player1.getFreeSoldier(), entry1.getCard().getSideEast());
        gameMap.placeGameMapEntry(entry1, new GameMapEntryPosition(0,0));
        GameMapEntry entry2 = new GameMapEntry(GameCardFactory.createGrassGrassCcastleCcastle(), player2);
        entry2.setSoldier(player2.getFreeSoldier(), entry2.getCard().getSideWest());
        gameMap.executeGameMove(new GameMove(player2,
                entry2,
                new GameMapEntryPosition(1,0)));

        // should be 4 points
        assertNotNull(returnedDetectionData[0]);
        assertEquals(player1.getPoints(), 0);
        assertEquals(player2.getPoints(), 0);
        assertEquals(returnedDetectionData[0].getPoints(), GameCardSideFactory.POINTS_CASTLE*2);

        // apply detection data
        gameStatistic.applyClosedFieldDetectionData(returnedDetectionData[0]);

        // player 1 and 2 should get 4 points
        assertEquals(player1.getPoints(), GameCardSideFactory.POINTS_CASTLE*2);
        assertEquals(player2.getPoints(), GameCardSideFactory.POINTS_CASTLE*2);
    }

    /**
     * test closed field with vertical fields
     *  X    X
     * X X  X X
     *  X    X
     * X C  C X
     *  X    X
     *
     * @throws InvalidPositionGameMapException
     * @throws SurroundingConflictGameMapException
     * @throws NoSurroundingCardGameMapException
     * @throws PositionNotFreeGameMapException
     */
    @Test
    public void twoCastlesWithOnePlayer() throws InvalidPositionGameMapException, SurroundingConflictGameMapException, NoSurroundingCardGameMapException, PositionNotFreeGameMapException {
        gameMap = new GameMap();
        final ClosedFieldDetectionData[] returnedDetectionData = {null};
        gameMap.setGameMapHandler(new GameMapHandler() {
            @Override
            public void onClosedField(ClosedFieldDetectionData detectionData) {
                returnedDetectionData[0] = detectionData;
            }
        });
        GameMapEntry entry1 = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1);
        entry1.setSoldier(player1.getFreeSoldier(), entry1.getCard().getSideEast());
        gameMap.placeGameMapEntry(entry1, new GameMapEntryPosition(0,0));
        GameMapEntry entry2 = new GameMapEntry(GameCardFactory.createGrassGrassCcastleCcastle(), player2);
        gameMap.executeGameMove(new GameMove(player2,
                entry2,
                new GameMapEntryPosition(1,0)));

        // should be 4 points
        assertNotNull(returnedDetectionData[0]);
        assertEquals(player1.getPoints(), 0);
        assertEquals(player2.getPoints(), 0);
        assertEquals(returnedDetectionData[0].getPoints(), GameCardSideFactory.POINTS_CASTLE*2);

        // apply detection data
        gameStatistic.applyClosedFieldDetectionData(returnedDetectionData[0]);

        // only player 1 should get 4 points
        assertEquals(player1.getPoints(), GameCardSideFactory.POINTS_CASTLE*2);
        assertEquals(player2.getPoints(), 0);
    }

    /**
     * test closed field with vertical fields
     *  X    X
     * X X  X X
     *  X    X    X
     * X C  C C  C X
     *  X    X    X
     *
     * @throws InvalidPositionGameMapException
     * @throws SurroundingConflictGameMapException
     * @throws NoSurroundingCardGameMapException
     * @throws PositionNotFreeGameMapException
     */
    @Test
    public void twoCastlesWithUnequalPlayer() throws InvalidPositionGameMapException, SurroundingConflictGameMapException, NoSurroundingCardGameMapException, PositionNotFreeGameMapException {
        gameMap = new GameMap();
        final ClosedFieldDetectionData[] returnedDetectionData = {null};
        gameMap.setGameMapHandler(new GameMapHandler() {
            @Override
            public void onClosedField(ClosedFieldDetectionData detectionData) {
                returnedDetectionData[0] = detectionData;
            }
        });
        GameMapEntry entry1 = new GameMapEntry(GameCardFactory.createGrassCcastleStreetStreet(), player1);
        entry1.setSoldier(player1.getFreeSoldier(), entry1.getCard().getSideEast());
        gameMap.placeGameMapEntry(entry1, new GameMapEntryPosition(0,0));
        GameMapEntry entry2 = new GameMapEntry(GameCardFactory.createCastleCgrassCastleCgrass(), player1, Orientation.EAST);
        entry2.setSoldier(player1.getFreeSoldier(), entry2.getCard().getSideNorth());
        gameMap.executeGameMove(new GameMove(player1,
                entry2,
                new GameMapEntryPosition(1,0)));
        GameMapEntry entry3 = new GameMapEntry(GameCardFactory.createGrassGrassCcastleCcastle(), player2);
        gameMap.executeGameMove(new GameMove(player2,
                entry3,
                new GameMapEntryPosition(2,0)));

        // should be 4 points
        assertNotNull(returnedDetectionData[0]);
        assertEquals(player1.getPoints(), 0);
        assertEquals(player2.getPoints(), 0);
        assertEquals(returnedDetectionData[0].getPoints(), GameCardSideFactory.POINTS_CASTLE*4);

        // apply detection data
        gameStatistic.applyClosedFieldDetectionData(returnedDetectionData[0]);

        // only player 1 should get 4 points
        assertEquals(player1.getPoints(), GameCardSideFactory.POINTS_CASTLE*4);
        assertEquals(player2.getPoints(), 0);
    }

    /**
     *  X    X
     * X X  X X
     *  X    G
     *  X    G    X
     * X G  GMG  G X
     *  X    G    X
     *  X    G    X
     * X X  X X  X X
     *  X    X    X
     *
     * @throws InvalidPositionGameMapException
     * @throws SurroundingConflictGameMapException
     * @throws NoSurroundingCardGameMapException
     * @throws PositionNotFreeGameMapException
     */
    @Test
    public void testApplayEndDetection() throws InvalidPositionGameMapException, SurroundingConflictGameMapException, NoSurroundingCardGameMapException, PositionNotFreeGameMapException {
        gameMap = new GameMap();
        final ArrayList<ClosedFieldDetectionData> returnedDetectionData = new ArrayList<>();
        gameMap.setGameMapHandler(new GameMapHandler() {
            @Override
            public void onClosedField(ClosedFieldDetectionData detectionData) {
                returnedDetectionData.add(detectionData);
            }
        });
        gameMap.placeGameMapEntry(
                new GameMapEntry(GameCardFactory.createCcastleCcastleGrassGrass(), player1),
                new GameMapEntryPosition(1,2));
        gameMap.executeGameMove(new GameMove(
                player1,
                new GameMapEntry(GameCardFactory.createStreetGrassGrassStreet(), player1),
                new GameMapEntryPosition(0,2))
        );
        gameMap.executeGameMove(new GameMove(
                player1,
                new GameMapEntry(GameCardFactory.createGrassGrassCcastleCcastle(), player1),
                new GameMapEntryPosition(0,1))
        );
        GameMapEntry entry00 = new GameMapEntry(GameCardFactory.createCastleCastleCastleCgrass(), player1);
        entry00.setSoldier(player1.getFreeSoldier(), entry00.getCard().getSideNorth());
        gameMap.executeGameMove(new GameMove(
                player1,
                entry00,
                new GameMapEntryPosition(0,0))
        );
        gameMap.executeGameMove(new GameMove(
                player1,
                new GameMapEntry(GameCardFactory.createGrassGrassCcastleCcastle(), player1),
                new GameMapEntryPosition(1,0))
        );
        gameMap.executeGameMove(new GameMove(
                player1,
                new GameMapEntry(GameCardFactory.createGrassCcastleGrassGrass(), player1),
                new GameMapEntryPosition(2,0))
        );
        gameMap.executeGameMove(new GameMove(
                player1,
                new GameMapEntry(GameCardFactory.createGrassCcastleGrassGrass(), player1),
                new GameMapEntryPosition(2,1))
        );
        assertEquals(returnedDetectionData.size(), 0);
        GameMapEntry entry11 = new GameMapEntry(GameCardFactory.createMonasteryGrassGrassGrassGrass(), player1);
        entry11.setSoldier(player1.getFreeSoldier(), entry11.getCard().getSideMid());
        gameMap.executeGameMove(new GameMove(
                player1,
                entry11,
                new GameMapEntryPosition(1,1))
        );

        ArrayList<ClosedFieldDetectionData> detectionData = gameMap.createFinalPointsDetectionData(players);
        gameStatistic.applyEndDetectionData(detectionData);

        assertEquals(player1.getPoints(), 13);
    }
}

package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.GameCard;
import at.aau.se2.gamma.core.models.impl.GameMapEntry;
import at.aau.se2.gamma.core.models.impl.Player;
import at.aau.se2.gamma.core.models.impl.Soldier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameMapEntryTest {
    Player player1;
    Player player2;

    Soldier soldier1Player1;
    Soldier soldier1Player2;

    GameCard gameCardPlayer1;
    GameCard gameCardPlayer2;
    GameCard gameCardMonastaryPlayer1;

    @BeforeEach
    public void beforeTest() {
        player1 = new Player("t1", "t1");
        player2 = new Player("t2", "t2");

        soldier1Player1 = new Soldier(player1);
        soldier1Player2 = new Soldier(player2);

        gameCardPlayer1 = GameCardFactory.createGrasCastleGrasStreet();
        gameCardPlayer2 = GameCardFactory.createCastleCastleGrasStreet();
        gameCardMonastaryPlayer1 = GameCardFactory.createMonasteryGrassGrassGrassGrass();
    }


    @Test
    public void setSoldier() {
        GameMapEntry gameMapEntry = new GameMapEntry(gameCardPlayer1, player1);

        assertTrue(gameMapEntry.setSoldier(soldier1Player1, gameCardPlayer1.getSideNorth()));
    }

    @Test
    public void setSoldierCheckSoldierPlacement() {
        GameMapEntry gameMapEntry = new GameMapEntry(gameCardPlayer1, player1);
        gameMapEntry.setSoldier(soldier1Player1, gameCardPlayer1.getSideNorth());

        assertTrue(soldier1Player1.isCurrentlyPlaced());
    }

    @Test
    public void setUnfreeSoldier() {
        GameMapEntry gameMapEntry = new GameMapEntry(gameCardPlayer1, player1);

        assertTrue(gameMapEntry.setSoldier(soldier1Player1, gameCardPlayer1.getSideNorth()));
        assertFalse(gameMapEntry.setSoldier(soldier1Player1, gameCardPlayer1.getSideNorth()));
    }

    @Test
    public void setSoldierOnUnownedCard() {
        GameMapEntry gameMapEntry = new GameMapEntry(gameCardPlayer1, player1);
        assertFalse(gameMapEntry.setSoldier(soldier1Player2, gameCardPlayer1.getSideNorth()));
    }

    //@Test
    public void setSoldierUnknownCardSide() {
        GameMapEntry gameMapEntry = new GameMapEntry(gameCardPlayer1, player1);
        assertFalse(gameMapEntry.setSoldier(soldier1Player1, gameCardPlayer2.getSideNorth()));
    }

    @Test
    public void setSoldierOnMidCard() {
        GameMapEntry gameMapEntry = new GameMapEntry(gameCardMonastaryPlayer1, player1);
        gameMapEntry.setSoldier(soldier1Player1, gameCardMonastaryPlayer1.getSideMid());

        assertTrue(soldier1Player1.isCurrentlyPlaced());
    }
}

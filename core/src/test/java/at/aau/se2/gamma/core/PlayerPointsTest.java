package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerPointsTest {
    PlayerPoints playerPoints;

    @BeforeEach
    public void beforeTest() {
        playerPoints = new PlayerPoints(0);
    }


    @Test
    public void addPositivePoints() {
        playerPoints.addPoints(3);
        playerPoints.addPoints(2);
        assertEquals(playerPoints.getPoints(), 5);
    }

    @Test
    public void addNegativePoints() {
        playerPoints.addPoints(3);
        playerPoints.addPoints(-2);
        assertEquals(playerPoints.getPoints(), 1);
    }

    @Test
    public void checkHistory() {
        playerPoints.addPoints(3);
        playerPoints.addPoints(-2);
        assertEquals(playerPoints.getHistory().size(), 2);
        assertEquals(playerPoints.getHistory().get(0), 3);
        assertEquals(playerPoints.getHistory().get(1), -2);
    }
}

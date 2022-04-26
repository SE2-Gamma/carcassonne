package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.factories.GameCardSideFactory;
import at.aau.se2.gamma.core.models.impl.GameCardSide;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameCardSideFactoryTest {

    @Test
    public void notClosedSides() {
        GameCardSide castle = GameCardSideFactory.createCastleSide();
        GameCardSide gras = GameCardSideFactory.createGrasSide();
        GameCardSide street = GameCardSideFactory.createStreetSide();

        assertFalse(castle.isClosingSide);
        assertFalse(gras.isClosingSide);
        assertFalse(street.isClosingSide);
    }

    @Test
    public void closedSides() {
        GameCardSide castle = GameCardSideFactory.createClosedCastleSide();
        GameCardSide gras = GameCardSideFactory.createClosedGrasSide();
        GameCardSide street = GameCardSideFactory.createClosedStreetSide();

        assertTrue(castle.isClosingSide);
        assertTrue(gras.isClosingSide);
        assertTrue(street.isClosingSide);
    }
}

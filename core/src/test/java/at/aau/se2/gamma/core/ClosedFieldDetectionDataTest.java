package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.factories.GameCardSideFactory;
import at.aau.se2.gamma.core.models.impl.ClosedFieldDetectionData;
import at.aau.se2.gamma.core.models.impl.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClosedFieldDetectionDataTest {
    ClosedFieldDetectionData closedFieldDetectionData;

    @BeforeEach
    public void beforeTest() {
        closedFieldDetectionData = new ClosedFieldDetectionData();
    }

    @Test
    public void addCardSide() {
        closedFieldDetectionData.addGameCardSide(GameCardSideFactory.createCastleSide());

        assertEquals(closedFieldDetectionData.getGameCardSides().size(), 1);
    }

    @Test
    public void setPointsPositive() {
        closedFieldDetectionData.addPoints(5);
        closedFieldDetectionData.addPoints(1);

        assertEquals(closedFieldDetectionData.getPoints(), 6);
    }

    @Test
    public void setPointsNegative() {
        closedFieldDetectionData.addPoints(5);
        closedFieldDetectionData.addPoints(-1);

        assertEquals(closedFieldDetectionData.getPoints(), 4);
    }
}

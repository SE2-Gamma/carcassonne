package at.aau.se2.gamma.core;

import at.aau.se2.gamma.core.models.impl.GameCardSide;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameCardSideTest {

    @Test
    public void canConnectOneToOne() {
        GameCardSide grasSide1 = new GameCardSide(GameCardSide.Type.GRAS, new GameCardSide.Type[]{ GameCardSide.Type.GRAS });
        GameCardSide grasSide2 = new GameCardSide(GameCardSide.Type.GRAS, new GameCardSide.Type[]{ GameCardSide.Type.GRAS });
        GameCardSide castleSide = new GameCardSide(GameCardSide.Type.CASTLE, new GameCardSide.Type[]{ GameCardSide.Type.CASTLE });

        assertTrue(grasSide2.canConnectTo(grasSide1));
        assertFalse(castleSide.canConnectTo(grasSide1));
    }

    @Test
    public void canConnectOneToMany() {
        GameCardSide grasSide1 = new GameCardSide(GameCardSide.Type.GRAS);
        GameCardSide bonusCard = new GameCardSide(GameCardSide.Type.CASTLE, new GameCardSide.Type[]{
                GameCardSide.Type.CASTLE,
                GameCardSide.Type.GRAS,
        });

        assertTrue(grasSide1.canConnectTo(bonusCard));
    }

    /**
     * Case: Gras [Gras] also connects to Bonus [Gras, Castle, Street], although gras doesn't have a possibleConnection to Bonus
     */
    @Test
    public void canConnectBonusToNormalSide() {
        GameCardSide grasSide1 = new GameCardSide(GameCardSide.Type.GRAS, new GameCardSide.Type[]{ GameCardSide.Type.GRAS });
        GameCardSide bonusCard = new GameCardSide(GameCardSide.Type.CASTLE, new GameCardSide.Type[]{
                GameCardSide.Type.CASTLE,
                GameCardSide.Type.GRAS,
        });

        assertTrue(bonusCard.canConnectTo(grasSide1));
    }

    @Test
    public void testGetter() {
        GameCardSide.Type type = GameCardSide.Type.CASTLE;
        GameCardSide.Type[] types = new GameCardSide.Type[]{
                GameCardSide.Type.CASTLE,
                GameCardSide.Type.GRAS,
        };
        GameCardSide bonusCard = new GameCardSide(type, types);

        assertEquals(bonusCard.getType(), type);
        assertArrayEquals(bonusCard.getPossibleConnectionTypes(), types);
    }
}

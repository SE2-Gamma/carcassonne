package at.aau.se2.gamma.core.factories;

import at.aau.se2.gamma.core.models.impl.GameCardSide;

public class GameCardSideFactory {

    // default points
    final public static int POINTS_DEFAULT = 1;
    final public static int POINTS_CASTLE = 2;

    private GameCardSideFactory() {}

    // open sides

    public static GameCardSide createGrasSide() {
        return new GameCardSide(GameCardSide.Type.GRAS, new GameCardSide.Type[]{GameCardSide.Type.GRAS}, false, POINTS_DEFAULT);
    }

    public static GameCardSide createCastleSide() {
        return new GameCardSide(GameCardSide.Type.CASTLE, new GameCardSide.Type[]{GameCardSide.Type.CASTLE}, false, POINTS_CASTLE);
    }

    public static GameCardSide createStreetSide() {
        return new GameCardSide(GameCardSide.Type.STREET, new GameCardSide.Type[]{GameCardSide.Type.STREET}, false, POINTS_DEFAULT);
    }

    // closed sides

    public static GameCardSide createClosedGrasSide() {
        return new GameCardSide(GameCardSide.Type.GRAS, new GameCardSide.Type[]{GameCardSide.Type.GRAS}, true, POINTS_DEFAULT);
    }

    public static GameCardSide createClosedCastleSide() {
        return new GameCardSide(GameCardSide.Type.CASTLE, new GameCardSide.Type[]{GameCardSide.Type.CASTLE}, true, POINTS_CASTLE);
    }

    public static GameCardSide createClosedStreetSide() {
        return new GameCardSide(GameCardSide.Type.STREET, new GameCardSide.Type[]{GameCardSide.Type.STREET}, true, POINTS_DEFAULT);
    }
}

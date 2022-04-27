package at.aau.se2.gamma.core.factories;

import at.aau.se2.gamma.core.models.impl.GameCard;

public class GameCardFactory {
    private GameCardFactory() {}

    public static GameCard createGrasCastleGrasStreet() {
        return new GameCard(
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(), null, 1);
    }

    public static GameCard createCastleCastleGrasStreet() {
        return new GameCard(
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createCastleSide(),
                GameCardSideFactory.createGrasSide(),
                GameCardSideFactory.createStreetSide(), null, 2);
    }
}

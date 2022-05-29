package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

public class GameCard implements Serializable {

    public enum SpecialType {
        MONASTERY
    }

    private GameCardSide sideNorth;
    private GameCardSide sideEast;
    private GameCardSide sideSouth;
    private GameCardSide sideWest;
    private GameCardSide sideMid;
    private SpecialType specialType;
    private GameCardSide[] neswSides; // north, east, south, west sides
    private String cardId; // id of card type (multiple instances with same id are possible, because we have duplicate cards in the game)

    public GameCard(GameCardSide sideNorth, GameCardSide sideEast, GameCardSide sideSouth, GameCardSide sideWest, SpecialType specialType, String cardId) {
        this.sideNorth = sideNorth;
        this.sideEast = sideEast;
        this.sideSouth = sideSouth;
        this.sideWest = sideWest;
        this.specialType = specialType;
        this.cardId = cardId;

        this.neswSides = new GameCardSide[]{ sideNorth, sideEast, sideSouth, sideWest};
    }

    public boolean containsSide(GameCardSide side) {
        for(GameCardSide cardSide: neswSides) {
            if (cardSide == side) {
                return true;
            }
        }
        return false;
    }

    public GameCardSide getSideNorth() {
        return sideNorth;
    }

    public GameCardSide getSideEast() {
        return sideEast;
    }

    public GameCardSide getSideSouth() {
        return sideSouth;
    }

    public GameCardSide getSideWest() {
        return sideWest;
    }

    public SpecialType getSpecialType() {
        return specialType;
    }

    public String getCardId() {
        return cardId;
    }

    public GameCardSide[] getNeswSides() {
        return neswSides;
    }

    public GameCardSide getSideMid() {
        return sideMid;
    }

    public void setSideMid(GameCardSide sideMid) {
        this.sideMid = sideMid;
    }
}

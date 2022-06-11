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

    /**
     * Add convenience constructor for cards with a containing midSide
     * @param sideNorth
     * @param sideEast
     * @param sideSouth
     * @param sideWest
     * @param sideMid
     * @param specialType
     * @param cardId
     */
    public GameCard(GameCardSide sideNorth, GameCardSide sideEast, GameCardSide sideSouth, GameCardSide sideWest, GameCardSide sideMid, SpecialType specialType,String cardId) {
        this(sideNorth, sideEast, sideSouth, sideWest, specialType, cardId);
        this.sideMid = sideMid;
    }

    public boolean containsSide(GameCardSide side) {
        for(GameCardSide cardSide: neswSides) {
            if (cardSide == side) {
                return true;
            }
        }

        // check if the side is the mid side
        if (sideMid != null && side == sideMid) {
            return true;
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

    public GameCardSide getGameCardSideWithUID(int UID) {
        for(GameCardSide gameCardSide: getNeswSides()) {
            if (gameCardSide.UID == UID) {
                return gameCardSide;
            }
        }

        if(this.sideMid != null && UID == this.sideMid.UID) {
            return this.sideMid;
        }

        return null;
    }
}

package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;

public class GameMapEntry implements Serializable {
    private Orientation orientation;
    private GameCard card;
    private Player placedByPlayer;
    private ArrayList<SoldierPlacement> soldierPlacements = new ArrayList<>();

    public GameMapEntry(GameCard card, Player placedByPlayer) {
        this(card, placedByPlayer, Orientation.NORTH);
    }

    public GameMapEntry(GameCard card, Player placedByPlayer, Orientation orientation) {
        this.card = card;
        this.placedByPlayer = placedByPlayer;
        this.orientation = orientation;
    }

    public boolean setSoldier(Soldier soldier, GameCardSide gameCardSide) {
        // check if soldier is free
        if (soldier.isCurrentlyPlaced()) {
            return false;
        }

        // check if the placer placed this card
        if(!soldier.getPlayer().getId().equals(this.placedByPlayer.getId())) {
            return false;
        }

        // check if the side is on this card
        if(!card.containsSide(gameCardSide)) {
            return false;
        }

        SoldierPlacement soldierPlacement = new SoldierPlacement(soldier, gameCardSide);
        soldier.setSoldierPlacement(soldierPlacement);
        soldierPlacements.add(soldierPlacement);
        return true;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public GameCard getCard() {
        return card;
    }

    public Player getPlacedByPlayer() {
        return placedByPlayer;
    }

    public ArrayList<SoldierPlacement> getSoldierPlacements() {
        return soldierPlacements;
    }

    /**
     * get cardside array, which is aligned to the current orientation
     * @return
     */
    public GameCardSide[] getAlignedCardSides() {
        GameCardSide[] neswCardSides = card.getNeswSides();
        switch (orientation) {
            case NORTH -> { return neswCardSides.clone();}
            case EAST -> {
                return new GameCardSide[]{ neswCardSides[1], neswCardSides[2], neswCardSides[3], neswCardSides[0] };
            }
            case SOUTH -> {
                return new GameCardSide[]{ neswCardSides[2], neswCardSides[3], neswCardSides[0], neswCardSides[1] };
            }
            case WEST -> {
                return new GameCardSide[]{ neswCardSides[3], neswCardSides[0], neswCardSides[1], neswCardSides[2] };
            }
        }

        return neswCardSides.clone();
    }

    /**
     * checks if a entry can connect to another entry with a specific side
     * @param otherEntry - other entry
     * @param onSide - side on which side of the other entry, the current one should connects
     * @return
     */
    public boolean canConnectTo(GameMapEntry otherEntry, Orientation onSide) {
        // get the side of our entry
        Orientation mySideOrientation = Orientation.NORTH;
        switch (onSide) {
            case NORTH -> mySideOrientation = Orientation.SOUTH;
            case EAST -> mySideOrientation = Orientation.WEST;
            case WEST -> mySideOrientation = Orientation.EAST;
        }

        // check if the both sides can connect and return the bool value
        GameCardSide mySide = this.getAlignedCardSides()[mySideOrientation.ordinal()];
        GameCardSide otherSide = otherEntry.getAlignedCardSides()[onSide.ordinal()];

        return mySide.canConnectTo(otherSide);
    }
}

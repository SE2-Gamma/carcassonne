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
}

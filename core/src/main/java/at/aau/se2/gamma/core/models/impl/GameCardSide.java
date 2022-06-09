package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class GameCardSide implements Serializable {
    public enum Type {
        GRAS,
        CASTLE,
        STREET,
        MONASTERY
    }

    public boolean isClosingSide;
    public boolean isMidOfCard;

    private static int counter;


    private Type type;
    // possible connections as array, to extend it later with bonus cards, which can connects to more than one other type
    private Type[] possibleConnectionTypes;
    private int points;
    private int multiplier = 1;

    /**
     * GameCardSide with type, which only can connects to same type (often used)
     * @param type
     */
    public GameCardSide(Type type) {
        this(type, new Type[]{type});
    }

    /**
     * Constructor with default open side and points
     * @param type
     * @param possibleConnectionTypes
     */
    public GameCardSide(Type type, Type[] possibleConnectionTypes) {
        this(type, possibleConnectionTypes, false, 1);
    }

    public GameCardSide(Type type, Type[] possibleConnectionTypes, boolean isClosingSide, int points) {

        this.type = type;
        this.possibleConnectionTypes = possibleConnectionTypes;
        this.isClosingSide = isClosingSide;
        this.points = points;
    }

    /**
     * checks, if this side can connect to other side.
     * Two sides are also connectable, if only one side have
     * the possible connection type of the other side.
     * Example: Gras [Gras] also connects to Bonus [Gras, Castle, Street], although gras doesn't have a possibleConnection to Bonus
     * The main purpose of this, is that we don't need to adjust each side, if we add bonus cards later.
     * @param other
     * @return
     */
    public boolean canConnectTo(GameCardSide other) {
        // check if this side can connect the other one
        for(Type type: possibleConnectionTypes) {
            if (type.equals(other.type)) {
                return true;
            }
        }

        // check if the other side can connect this one
        for(Type type: other.possibleConnectionTypes) {
            if (type.equals(this.type)) {
                return true;
            }
        }

        return false;
    }

    public Type getType() {
        return type;
    }

    public Type[] getPossibleConnectionTypes() {
        return possibleConnectionTypes;
    }

    @Override
    public String toString() {
        return "GameCardSide{" +
                "type=" + type +
                '}';
    }

    public boolean isClosingSide() {
        return isClosingSide;
    }

    public void setClosingSide(boolean closingSide) {
        isClosingSide = closingSide;
    }

    public boolean isMidOfCard() {
        return isMidOfCard;
    }

    public void setMidOfCard(boolean midOfCard) {
        isMidOfCard = midOfCard;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setPossibleConnectionTypes(Type[] possibleConnectionTypes) {
        this.possibleConnectionTypes = possibleConnectionTypes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
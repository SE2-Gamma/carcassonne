package at.aau.se2.gamma.core.models.impl;

import java.util.Arrays;

public class GameCardSide {
    public enum Type {
        GRAS,
        CASTLE,
        STREET
    }

    public boolean isClosingSide;

    private Type type;
    // possible connections as array, to extend it later with bonus cards, which can connects to more than one other type
    private Type[] possibleConnectionTypes;

    /**
     * GameCardSide with type, which only can connects to same type (often used)
     * @param type
     */
    public GameCardSide(Type type) {
        this(type, new Type[]{type});
    }

    /**
     * Constructor with default open side
     * @param type
     * @param possibleConnectionTypes
     */
    public GameCardSide(Type type, Type[] possibleConnectionTypes) {
        this(type, possibleConnectionTypes, false);
    }

    public GameCardSide(Type type, Type[] possibleConnectionTypes, boolean isClosingSide) {
        this.type = type;
        this.possibleConnectionTypes = possibleConnectionTypes;
        this.isClosingSide = isClosingSide;
    }

    /**
     * checks, if this side can connect to other side
     * @param other
     * @return
     */
    public boolean canConnectTo(GameCardSide other) {
        for(Type type: possibleConnectionTypes) {
            if (type.equals(other.type)) {
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
}

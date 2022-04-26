package at.aau.se2.gamma.core.models.impl;

public class GameMapEntry {
    enum Orientation {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private Orientation orientation = Orientation.NORTH;
    // TODO: Players placed
    // TODO: PlacedBy (combine with players placed in an object like PlacementData)

}

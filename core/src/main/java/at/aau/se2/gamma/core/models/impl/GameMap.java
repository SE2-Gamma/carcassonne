package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.exceptions.InvalidPositionGameMapException;
import at.aau.se2.gamma.core.exceptions.NoSurroundingCardGameMapException;
import at.aau.se2.gamma.core.exceptions.PositionNotFreeGameMapException;
import at.aau.se2.gamma.core.exceptions.SurroundingConflictGameMapException;

import java.io.Serializable;
import java.util.ArrayList;

public class GameMap implements Serializable {
    // default values for the map
    public final static int DEFAULT_HEIGHT = 100;
    public final static int DEFAULT_WIDTH = 100;

    private GameMapEntry[][] mapArray;

    public GameMap() {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }
    public GameMapHandler gameMapHandler = null;

    public GameMap(int height, int width) {
        mapArray = new GameMapEntry[height][width];
    }

    public GameMapEntry[][] getMapArray() {
        return mapArray;
    }

    /**
     * Place or replace an entry to the map array
     * @param entry
     * @param position
     * @return boolean if it was placed or not
     */
    public boolean placeGameMapEntry(GameMapEntry entry, GameMapEntryPosition position) {
        if (position.getY() >= 0 && position.getY() < mapArray.length
        && position.getX() >= 0 && position.getX() < mapArray[0].length) {
            mapArray[position.getY()][position.getX()] = entry;
            return true;
        }
        return false;
    }

    /**
     * execute a game move from one player
     * @param gameMove
     */
    public void executeGameMove(GameMove gameMove)
            throws InvalidPositionGameMapException, PositionNotFreeGameMapException,
            NoSurroundingCardGameMapException, SurroundingConflictGameMapException {
        GameMapEntryPosition position = gameMove.getGameMapEntryPosition();
        GameMapEntry entryCandidate = gameMove.getGameMapEntry();

        // check if position is in gamemap
        if (position.getY() >= this.mapArray.length || position.getY() < 0
        || position.getX() >= this.mapArray[0].length || position.getX() < 0) {
            throw new InvalidPositionGameMapException();
        }

        // check if position is free
        if (this.mapArray[position.getY()][position.getX()] != null) {
            throw new PositionNotFreeGameMapException();
        }

        // get surrounding fields with a 3x3 submap
        GameMapEntry[] neswSurroundingFields = this.getNESWSurroundingFields(position);

        // check if there are vertical or horizontal surrounding fields
        boolean neighbourExist = false;
        for(GameMapEntry entry: neswSurroundingFields) {
            if (entry != null) {
                neighbourExist = true;
                break;
            }
        }
        if (!neighbourExist) {
            throw new NoSurroundingCardGameMapException();
        }

        // check if new entry matches to neighbours
        for(int i = 0; i < neswSurroundingFields.length; i++) {
            GameMapEntry neighbour = neswSurroundingFields[i];

            if (neighbour != null) {
                // get orientation side where the neighbour is located
                Orientation side;
                switch (i) {
                    case 1: side = Orientation.EAST; break;
                    case 2: side = Orientation.SOUTH; break;
                    case 3: side = Orientation.WEST; break;
                    default: side = Orientation.NORTH;
                }

                System.out.println("side: "+i+" - "+side.toString());

                // if it can't connect to each other, throw error
                if (!neighbour.canConnectTo(entryCandidate, side)) {
                    throw new SurroundingConflictGameMapException();
                }
            }
        }

        // place the movement
        this.mapArray[position.getY()][position.getX()] = entryCandidate;

        // check current state, and notify observer if needed
        GameCardSide[] alignedSides = entryCandidate.getAlignedCardSides();
        for(int i = 0; i < alignedSides.length; i++) {
            GameCardSide cardSide = alignedSides[i];
            if (cardSide.isClosingSide) {

                // check all connected sides to check if we have a full closed thing here
                ClosedFieldDetectionData detectionData = new ClosedFieldDetectionData();
                checkClosedState(i, position, detectionData, cardSide);

                if(detectionData.isClosed()) {
                    if (gameMapHandler != null) {
                        gameMapHandler.onClosedField(detectionData);
                    }
                }
            }
        }
    }

    private void checkClosedState(int orientation, GameMapEntryPosition position, ClosedFieldDetectionData detectionData, GameCardSide currentCardSide) {
        // calculate position of neighbour card side
        GameMapEntryPosition nextPosition = null;
        switch(orientation) {
            case 0: nextPosition = new GameMapEntryPosition(position.getX(), position.getY()-1); break;
            case 1: nextPosition = new GameMapEntryPosition(position.getX()+1, position.getY()); break;
            case 2: nextPosition = new GameMapEntryPosition(position.getX(), position.getY()+1); break;
            case 3: nextPosition = new GameMapEntryPosition(position.getX()-1, position.getY()); break;
        }

        // check if position is valid
        if (nextPosition == null || nextPosition.getX() < 0 || nextPosition.getX() >= mapArray.length
        || nextPosition.getY() < 0 || nextPosition.getY() >= mapArray.length) {
            detectionData.setClosed(false);
            return;
        }

        // check if we have a card on this position
        GameMapEntry nextMapEntry = mapArray[nextPosition.getY()][nextPosition.getX()];
        if (nextMapEntry == null) {
            detectionData.setClosed(false);
            return;
        }

        // add the points for this side
        detectionData.addGameCardSide(currentCardSide);
        detectionData.addPoints(currentCardSide.getPoints() * currentCardSide.getMultiplier());

        // calculate opposite cardside
        GameCardSide[] neighbourAlignedSides = nextMapEntry.getAlignedCardSides();
        int oppositeOrientation = 2;
        switch(orientation) {
            case 1: oppositeOrientation = 3; break;
            case 2: oppositeOrientation = 0; break;
            case 3: oppositeOrientation = 1; break;
        }
        GameCardSide oppositeGameCardSide = neighbourAlignedSides[oppositeOrientation];

        // add the points for the opposite side
        detectionData.addGameCardSide(oppositeGameCardSide);
        detectionData.addPoints(oppositeGameCardSide.getPoints() * oppositeGameCardSide.getMultiplier());

        // check if it's closed
        if (oppositeGameCardSide.isClosingSide) {
            return;
        }

        // if the side isn't closed, check the other open sides of this type, and the neighbours
        for(int i = 0; i < neighbourAlignedSides.length; i++) {
            if (i != oppositeOrientation) {
                GameCardSide cardSide = neighbourAlignedSides[i];
                // check if the other side isn't closed (so it's connected to our side here), and if the types are the same
                if (!cardSide.isClosingSide && cardSide.getType() == currentCardSide.getType()) {
                    checkClosedState(i, nextPosition, detectionData, cardSide);
                }
            }
        }
    }

    /**
     * get vertical and horizontal surrounding fields in nesw order
     * @param position
     * @return
     */
    public GameMapEntry[] getNESWSurroundingFields(GameMapEntryPosition position) {
        GameMapEntry[][] submap3x3 = this.get3x3SubMap(position);
        GameMapEntry[] neswSurroundingFields = new GameMapEntry[4];
        neswSurroundingFields[0] = submap3x3[2][1];
        neswSurroundingFields[1] = submap3x3[1][2];
        neswSurroundingFields[2] = submap3x3[0][1];
        neswSurroundingFields[3] = submap3x3[1][0];

        return neswSurroundingFields;
    }

    /**
     * get a 3x3 submap of the whole map
     * @param midPosition
     * @return
     */
    public GameMapEntry[][] get3x3SubMap(GameMapEntryPosition midPosition) {
        GameMapEntry[][] surroundingFields = new GameMapEntry[3][3];

        // iterate through each field in the subarray
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                GameMapEntry entry = null;
                int posY = midPosition.getY()+y;
                int posX = midPosition.getX()+x;

                if (posY >= 0 && posY < mapArray.length
                && posX >= 0 && posX < mapArray[0].length) {
                    entry = mapArray[posY][posX];
                }

                surroundingFields[y+1][x+1] = entry;
            }
        }
        return surroundingFields;
    }

    public GameMapHandler getGameMapHandler() {
        return gameMapHandler;
    }

    public void setGameMapHandler(GameMapHandler gameMapHandler) {
        this.gameMapHandler = gameMapHandler;
    }
}
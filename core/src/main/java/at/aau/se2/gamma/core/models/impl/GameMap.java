package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.exceptions.*;
import at.aau.se2.gamma.core.factories.GameCardSideFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

public class GameMap implements Serializable {
    // default values for the map
    public final static int DEFAULT_HEIGHT = 100;
    public final static int DEFAULT_WIDTH = 100;

    private GameMapEntry[][] mapArray;
    private ConcurrentLinkedDeque<CheatMove> cheatMoves=new ConcurrentLinkedDeque<>();
    public void executeCheatMove(CheatMove cheatMove)throws CheatMoveImpossibleException{
        System.out.print("//startin to execute cheatmove//");
        int x=cheatMove.soldier.getX();
        int y=cheatMove.soldier.getY();
        GameMapEntry gameMapEntry = mapArray[y][x];
        //checks if a soldier is on the card
        System.out.print("//checking if soldier is on card//");

        if(gameMapEntry == null){
            throw new CheatMoveImpossibleException("Invalid entry");
        }

        System.out.print("//new positiion on soldier//");
        synchronized (cheatMoves) {
            //removes first soldierplacement. requires that only one soldier can be placed per gamecard
            System.out.print("/replaces soldier placement //");
            cheatMove.soldier.soldierPlacement = null;
            gameMapEntry.getSoldierPlacements().clear();
            //adds new soldierplacement
            gameMapEntry.setSoldier(cheatMove.soldier,cheatMove.newPosition.getGameCardSide());

            cheatMoves.add(cheatMove);
            cheatMove.getSoldier().getActiveCheats().add(cheatMove);
        }
    }
    public LinkedList<CheatMove> detectCheatMove(Soldier soldier) throws  NoSuchCheatActiveException {
       synchronized (cheatMoves) {
           System.out.print("//searching soldier//");

           if(soldier.getActiveCheats().size()==0){
               throw new NoSuchCheatActiveException();
           }

           return soldier.getActiveCheats();

        }

    }
    public void undoCheatMove(LinkedList<CheatMove> moves){
        synchronized (cheatMoves) {
            for (CheatMove move:moves
                 ) {
                cheatMoves.remove(move);
            }
        }

        //clears soldier placements of the gameMapEntry and sets it to the very first original position
        CheatMove cheatMove = moves.getFirst();
        Soldier soldier = cheatMove.getSoldier();
        GameMapEntry gameMapEntry = mapArray[soldier.getY()][soldier.getX()];
        soldier.soldierPlacement=null;
        gameMapEntry.getSoldierPlacements().clear();

        gameMapEntry.getSoldierPlacements().add(cheatMove.originalPosition);
        soldier.setSoldierPlacement(cheatMove.originalPosition);


        //todo: give each cheater the correct penalty (CheatMove.getPlayername <- the cheater
        // CheatMove.getPenalty <- the correct number of points lost. is independent from detected cheats but number of cheats done.
        // so if a player has done 4 cheats but the very first is detected he only loses 1 points, but if the last cheat is detected first he loses 2^4 points.
        for (CheatMove move:moves
             ) {
            System.out.print("//"+move.cheater.getName()+"lost "+move.getPenalty()+" points.//");
            move.cheater.addPlayerPoints((move.penalty*(-1)));
        }

    }

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
        if (!gameCardMatchesNeighbors(neswSurroundingFields,entryCandidate)) {
            throw new SurroundingConflictGameMapException();
        }

        // place the movement
        this.mapArray[position.getY()][position.getX()] = entryCandidate;

        // check if neighbour (diagonal, vertical and horizontal) is a monastery, if that is the case,
        // check if the monastery is finished, and return an own detection data object
        for(GameMapEntry[] row: get3x3SubMap(position)) {
            for(GameMapEntry neighbour: row) {
                if (neighbour != null
                        && neighbour.getCard().getSideMid() != null
                        && neighbour.getCard().getSideMid().getType() == GameCardSide.Type.MONASTERY) {
                    // check monastery
                    GameMapEntryPosition monasteryPosition = getPositionFromEntry(neighbour);
                    if (isFieldFullyCompleted(monasteryPosition)) {
                        ClosedFieldDetectionData monasteryDetectionData = new ClosedFieldDetectionData();
                        monasteryDetectionData.addPoints(GameCardSideFactory.POINTS_DEFAULT*9);
                        monasteryDetectionData.addGameCardSide(neighbour.getCard().getSideMid());
                        monasteryDetectionData.addGameCard(neighbour.getCard());
                        if (gameMapHandler != null) {
                            gameMapHandler.onClosedField(monasteryDetectionData);
                        }
                    }
                }
            }
        }

        // check current state, and notify observer if needed
        GameCardSide[] alignedSides = entryCandidate.getAlignedCardSides();
        ArrayList<Integer> usedIds = new ArrayList<>();
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
            } else {
                // check if the open side close something
                // pretend this side is closed, and check if it would lead to a closed field
                // repeat this step with all other sides with the same open type,
                // if all pretended closed sides leads to closed fields, then these sides are closing the whole field.

                // break if the field was already used in this else branch for a previous side
                for(int n: usedIds) {
                    if(n == i) {
                        break;
                    }
                }

                // check if this side would close a field
                ClosedFieldDetectionData detectionData = new ClosedFieldDetectionData();
                checkClosedState(i, position, detectionData, cardSide);

                // if this one would lead to a closed field, check the next sides on this card
                if(detectionData.isClosed()) {
                    ArrayList<ClosedFieldDetectionData> closedFieldDetectionDataArr = new ArrayList<>();
                    ArrayList<Integer> checkedIds = new ArrayList<>();
                    checkedIds.add(i);
                    boolean closed = true;

                    for(int j = i+1; j < alignedSides.length; j++) {
                        GameCardSide subCardSide = alignedSides[j];

                        // continue for different types or closing sides
                        if (!subCardSide.getType().equals(cardSide.getType()) || subCardSide.isClosingSide) {
                            continue;
                        }

                        // check if this side would close a field
                        ClosedFieldDetectionData subDetectionData = new ClosedFieldDetectionData();
                        checkClosedState(j, position, subDetectionData, subCardSide);

                        checkedIds.add(j);

                        if(subDetectionData.isClosed()) {
                            closedFieldDetectionDataArr.add(subDetectionData);
                            continue;
                        }

                        closed = false;
                    }

                    // add used ids
                    usedIds.addAll(checkedIds);

                    // side is not closed, if there are only one open side which closes the field
                    if (closedFieldDetectionDataArr.size() < 1) {
                        closed = false;
                    }

                    if (closed) {
                        // reset the points to 0
                        detectionData.setPoints(0);

                        // add new gamecard sides with their points to detectionData
                        for(ClosedFieldDetectionData dat: closedFieldDetectionDataArr) {
                            for(GameCardSide newGameCardSide: dat.getGameCardSides()) {
                                // if the gamecard side isn't added to the summarized detectionData
                                if (!detectionData.getGameCardSides().contains(newGameCardSide)) {
                                    detectionData.getGameCardSides().add(newGameCardSide);
                                }
                            }
                            for(GameCard gameCard: dat.getGameCards()) {
                                detectionData.addGameCard(gameCard);
                            }
                        }

                        // recalculate points
                        for(GameCardSide gameCardSide: detectionData.getGameCardSides()) {
                            detectionData.addPoints(gameCardSide.getPoints() * gameCardSide.getMultiplier());
                        }

                        // notify listener
                        if (gameMapHandler != null) {
                            gameMapHandler.onClosedField(detectionData);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<ClosedFieldDetectionData> createFinalPointsDetectionData(ArrayList<Player> players) {
        ArrayList<ClosedFieldDetectionData> finalDetectionData = new ArrayList<>();

        // getSoldiers
        ArrayList<SoldierPlacement> soldierPlacements = new ArrayList<>();
        for(Player player: players) {
            for(Soldier soldier: player.getSoldiers()) {
                if (soldier.getSoldierPlacement() != null) {
                    soldierPlacements.add(soldier.getSoldierPlacement());
                }
            }
        }

        // iterate through all fields, and check if a soldier is placed on any side
        for(int y = 0; y < mapArray.length; y++) {
            GameMapEntry[] row = mapArray[y];
            for(int x = 0; x < row.length; x++) {
                GameMapEntry entry = row[x];
                // check if a card is placed
                if (entry != null) {
                    GameCard card = entry.getCard();
                    GameCardSide[] alignedCardSides = entry.getAlignedCardSides();

                    // check if a soldier is present on one card side
                    for(int direction = 0; direction < alignedCardSides.length; direction++) {
                        GameCardSide side = alignedCardSides[direction];
                        // check if the cardSide is related to a soldierPlacement
                        for(SoldierPlacement soldierPlacement: soldierPlacements) {
                            if (soldierPlacement.getGameCardSide() == side) {
                                GameMapEntryPosition position = new GameMapEntryPosition(x, y);
                                ClosedFieldDetectionData detectionData = new ClosedFieldDetectionData();
                                detectionData.setEndGameData(true);
                                // calculate points for gras
                                if (side.getType().equals(GameCardSide.Type.GRAS)) {
                                    // get connected gras sites

                                    for(int orientationToCheck = 0; orientationToCheck < alignedCardSides.length; orientationToCheck++) {
                                        GameCardSide sideToCheck = alignedCardSides[orientationToCheck];
                                        if (sideToCheck.getType().equals(side.getType())) {
                                            // only the site with the soldier is allowed to be a closing side
                                            if (!sideToCheck.isClosingSide() || sideToCheck == side) {
                                                checkGrasFields(orientationToCheck, position, detectionData, sideToCheck);
                                            }
                                        }
                                    }

                                    finalDetectionData.add(detectionData);
                                } else {
                                    // calculate points for other unfinished sides
                                    // check each open side of this type on this card
                                    for(int orientationToCheck = 0; orientationToCheck < alignedCardSides.length; orientationToCheck++) {
                                        GameCardSide sideToCheck = alignedCardSides[orientationToCheck];
                                        if (sideToCheck.getType().equals(side.getType())) {
                                            // only the site with the soldier is allowed to be a closing side
                                            if (!sideToCheck.isClosingSide() || sideToCheck == side) {
                                                checkClosedState(orientationToCheck, position, detectionData, sideToCheck, true);
                                            }
                                        }
                                    }

                                    finalDetectionData.add(detectionData);
                                }
                            }
                        }
                    }

                    // check if a soldier is set on a mandatory
                    if (card.getSideMid() != null
                            && card.getSideMid().getType() == GameCardSide.Type.MONASTERY) {
                        GameCardSide side = card.getSideMid();
                        for(SoldierPlacement soldierPlacement: soldierPlacements) {
                            if (soldierPlacement.getGameCardSide() == side) {
                                GameMapEntry[][] fields = get3x3SubMap(new GameMapEntryPosition(x, y));
                                ClosedFieldDetectionData detectionData = new ClosedFieldDetectionData();
                                detectionData.setMonasteryType(true);
                                for(GameMapEntry[] fieldsRow: fields) {
                                    for(GameMapEntry field: fieldsRow) {
                                        if(field != null) {
                                            detectionData.addPoints(1);

                                            if (field.getCard().getSideMid() != null) {
                                                detectionData.addGameCardSide(field.getCard().getSideMid());
                                            } else {
                                                detectionData.addGameCardSide(field.getCard().getSideNorth());
                                            }
                                            detectionData.addGameCard(field.getCard());
                                        }
                                    }
                                }

                                finalDetectionData.add(detectionData);
                            }
                        }
                    }
                }
            }
        }

        return finalDetectionData;
    }

    private void checkGrasFields(int orientation, GameMapEntryPosition position, ClosedFieldDetectionData detectionData, GameCardSide currentCardSide) {
        // calculate position of neighbours card side
        GameMapEntryPosition nextPosition = getNeighbourPosition(orientation, position);

        // check if we have a card on this position
        GameMapEntry nextMapEntry = getNeighbour(orientation, position);

        detectionData.addGameCardSide(currentCardSide);
        detectionData.addGameCard(mapArray[position.getY()][position.getX()].getCard());
        if (nextMapEntry == null) {
            detectionData.setClosed(false);
            return;
        }

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
        detectionData.addGameCard(nextMapEntry.getCard());

        // if the side isn't closed, check the other open sides of this type, and the neighbours
        for(int i = 0; i < neighbourAlignedSides.length; i++) {
            // if the current neighbour is closed, prevent the opposite site check
            if (oppositeGameCardSide.isClosingSide && Math.abs(oppositeOrientation-i) != 2) {
                continue;
            }

            if (i != oppositeOrientation) {
                GameCardSide cardSide = neighbourAlignedSides[i];
                // check if the other side isn't closed (so it's connected to our side here), and if the types are the same
                if (!cardSide.isClosingSide && cardSide.getType() == currentCardSide.getType()) {
                    //check if cardSide is already visited before start a new check, to prevent a circle loop
                    boolean alreadyVisited = false;
                    for(GameCardSide cs: detectionData.getGameCardSides()) {
                        if(cs == cardSide) {
                            alreadyVisited = true;
                            break;
                        }
                    }

                    if(!alreadyVisited) {
                       checkGrasFields(i, nextPosition, detectionData, cardSide);
                    }
                } else if (cardSide.getType() == GameCardSide.Type.CASTLE) { // check if cardside is castle
                    // check if card side isn't at opposite direction.
                    if (Math.abs(oppositeOrientation-i) != 2) {
                        ArrayList<GameCardSide> scannedCastles = detectionData.getScannedCastles();
                        // check if castle was already scanned
                        if (!scannedCastles.contains(cardSide)) {
                            // check if castle is closed
                            ClosedFieldDetectionData castleDetection = new ClosedFieldDetectionData();
                            checkClosedState(i, nextPosition, castleDetection, cardSide, true);

                            // add castles to visited fields
                            scannedCastles.addAll(castleDetection.getGameCardSides());

                            // add detection to detectionData
                            if (castleDetection.isClosed()) {
                                detectionData.getDetectedCastles().add(castleDetection);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the position of an entry which is present in the array. Otherwise, it returns null.
     * @param entry
     * @return GameMapEntryPosition: position of entry if one exists
     */
    private GameMapEntryPosition getPositionFromEntry(GameMapEntry entry) {
        for(int y = 0; y < mapArray.length; y++) {
            for(int x = 0; x < mapArray[y].length; x++) {
                GameMapEntry currentEntry = mapArray[y][x];
                if (currentEntry != null && currentEntry == entry) {
                    return new GameMapEntryPosition(x, y);
                }
            }
        }
        return null;
    }

    /**
     * check if a card is fully completed like if a monastery is complete (each 8 neighbours are set)
     * @param monasteryEntryPosition
     * @return
     */
    private boolean isFieldFullyCompleted(GameMapEntryPosition monasteryEntryPosition) {
        GameMapEntry[][] submap = get3x3SubMap(monasteryEntryPosition);
        for(GameMapEntry[] row: submap) {
            for(GameMapEntry entry: row) {
                if (entry == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get the position of the neighbour, which connects to the cardSide with position
     * @param orientation of the cardSide
     * @param position of current card in map array
     * @return GameMapEntryPosition position of neighbour card
     */
    private GameMapEntryPosition getNeighbourPosition(int orientation, GameMapEntryPosition position) {
        GameMapEntryPosition nextPosition = null;

        switch(orientation) {
            case 0: nextPosition = new GameMapEntryPosition(position.getX(), position.getY()+1); break;
            case 1: nextPosition = new GameMapEntryPosition(position.getX()+1, position.getY()); break;
            case 2: nextPosition = new GameMapEntryPosition(position.getX(), position.getY()-1); break;
            case 3: nextPosition = new GameMapEntryPosition(position.getX()-1, position.getY()); break;
        }

        return nextPosition;
    }

    /**
     Get the neighbour card, which connects to the cardSide with position
     * @param orientation of the cardSide
     * @param position of current card in map array
     * @return GameMapEntry neighbour of card side if one exists, otherwise return null
     */
    private GameMapEntry getNeighbour(int orientation, GameMapEntryPosition position) {
        GameMapEntryPosition nextPosition = getNeighbourPosition(orientation, position);

        // check if position is valid
        if (nextPosition == null || nextPosition.getX() < 0 || nextPosition.getX() >= mapArray.length
                || nextPosition.getY() < 0 || nextPosition.getY() >= mapArray.length) {
            return null;
        }

        // return card or null
        return mapArray[nextPosition.getY()][nextPosition.getX()];
    }

    private void checkClosedState(int orientation, GameMapEntryPosition position, ClosedFieldDetectionData detectionData, GameCardSide currentCardSide) {
        checkClosedState(orientation, position, detectionData, currentCardSide, false);
    }

    private void checkClosedState(int orientation, GameMapEntryPosition position, ClosedFieldDetectionData detectionData, GameCardSide currentCardSide, boolean addAlsoOpenSides) {
        // calculate position of neighbour card side
        GameMapEntryPosition nextPosition = getNeighbourPosition(orientation, position);

        // check if we have a card on this position
        GameMapEntry nextMapEntry = getNeighbour(orientation, position);
        if (nextMapEntry == null) {
            detectionData.setClosed(false);
            if (addAlsoOpenSides) {
                // add the points for this side
                detectionData.addGameCardSide(currentCardSide);
                detectionData.addGameCard(mapArray[position.getY()][position.getX()].getCard());
                detectionData.addPoints(currentCardSide.getPoints() * currentCardSide.getMultiplier());
            }
            return;
        }

        // add the points for this side
        detectionData.addGameCardSide(currentCardSide);
        detectionData.addGameCard(mapArray[position.getY()][position.getX()].getCard());
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
        detectionData.addGameCard(nextMapEntry.getCard());
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
                    //check if cardSide is already visited before start a new check, to prevent a circle loop
                    boolean alreadyVisited = false;
                    for(GameCardSide cs: detectionData.getGameCardSides()) {
                        if(cs == cardSide) {
                            alreadyVisited = true;
                            break;
                        }
                    }

                    if(!alreadyVisited) {
                        checkClosedState(i, nextPosition, detectionData, cardSide, addAlsoOpenSides);
                    }
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
    //Diese Methode checkt ob es Stellen am Spielfeld gibt an denen eine Karte plaziert werden kann
    public boolean checkCardPlaceability(GameCard gameCard){

        GameMapEntry mapentry;
        GameMapEntry[] surroundingfields;
        Orientation[] orientationArr = new Orientation[4];
        orientationArr[0] = Orientation.NORTH;
        orientationArr[1] = Orientation.EAST;
        orientationArr[2] = Orientation.SOUTH;
        orientationArr[3] = Orientation.WEST;

        for(int y = 0; y < mapArray.length; y++){
            for (int x = 0; x < mapArray[y].length; x++){
                //es werden alle positionen gefunden in denen bereits eine Karte liegt da nur an diese eine neue angef??gt werden kann
                //in diesen Positionen werden dann alle leeren Nachbarn ??berpr??ft ob die momemtane Karte in irgendeiner
                //Orientierung hineingelegt werden kann
                if((mapArray[y][x] != null)){

                    for (Orientation orientation:orientationArr) {
                        if(y< mapArray.length-1&&mapArray[y+1][x] == null){
                            mapentry = new GameMapEntry(gameCard,orientation);
                            surroundingfields = getNESWSurroundingFields(new GameMapEntryPosition(x,y+1));
                            if(gameCardMatchesNeighbors(surroundingfields,mapentry)){
                                return true;
                            }
                        }
                        if(y>0&&mapArray[y-1][x] == null){
                            mapentry = new GameMapEntry(gameCard,orientation);
                            surroundingfields = getNESWSurroundingFields(new GameMapEntryPosition(x,y-1));
                            if(gameCardMatchesNeighbors(surroundingfields,mapentry)){
                                return true;
                            }
                        }
                        if(x< mapArray.length-1&&mapArray[y][x+1] == null){
                            mapentry = new GameMapEntry(gameCard,orientation);
                            surroundingfields = getNESWSurroundingFields(new GameMapEntryPosition(x+1,y));
                            if(gameCardMatchesNeighbors(surroundingfields,mapentry)){
                                return true;
                            }
                        }
                        if(x>0&&mapArray[y][x-1] == null){
                            mapentry = new GameMapEntry(gameCard,orientation);
                            surroundingfields = getNESWSurroundingFields(new GameMapEntryPosition(x-1,y));
                            if(gameCardMatchesNeighbors(surroundingfields,mapentry)){
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public GameMapHandler getGameMapHandler() {
        return gameMapHandler;
    }

    public void setGameMapHandler(GameMapHandler gameMapHandler) {
        this.gameMapHandler = gameMapHandler;
    }

    public boolean gameCardMatchesNeighbors(GameMapEntry[] neswSurroundingFields, GameMapEntry entryCandidate) {

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

                // if it can't connect to each other, set false
                if (!neighbour.canConnectTo(entryCandidate, side)) {
                    return false;
                }
            }
        }
        return true;
    }

}
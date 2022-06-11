package at.aau.se2.gamma.core.models.impl;

import at.aau.se2.gamma.core.exceptions.CheatMoveImpossibleException;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CheatMove implements Serializable {
    Player cheater;
    boolean active;
    Soldier soldier;

    SoldierPlacement originalPosition;
    SoldierPlacement newPosition;
    int penalty; // tbd, is set in clientthread, is 2^numberOfCheats

    public void changeToServerInstance(ConcurrentLinkedDeque<Player> players, GameMap gameMap) throws CheatMoveImpossibleException {
        boolean playerFound = false;

        for(Player player: players) {
            if (this.getCheater().getId().equals(player.getId())) {
                this.setCheater(player);
                playerFound = true;
            }
        }

        if (!playerFound) {
            throw new CheatMoveImpossibleException("Player not found");
        }

        boolean soldierFound = false;
        for(Soldier soldier: this.getCheater().getSoldiers()) {
            if (soldier.getId() == this.getSoldier().getId()) {
                this.setSoldier(soldier);
                soldierFound = true;
            }
        }

        if (!soldierFound) {
            throw new CheatMoveImpossibleException("Soldier not found");
        }

        GameMapEntry gameCardEntry = gameMap.getMapArray()[soldier.getY()][soldier.getX()];
        this.originalPosition.setSoldier(soldier);
        this.newPosition.setSoldier(soldier);

        if (gameCardEntry == null) {
            throw new CheatMoveImpossibleException("Invalid GameCardEntry");
        }

        this.originalPosition.setGameCardSide(gameCardEntry.getCard().getGameCardSideWithUID(this.originalPosition.getGameCardSide().UID));
        this.newPosition.setGameCardSide(gameCardEntry.getCard().getGameCardSideWithUID(this.newPosition.getGameCardSide().UID));
    }
    public CheatData getData(){
        CheatData data=new CheatData();
        data.setX(soldier.getX());
        data.setY(soldier.getY());
        data.setCheaterID(cheater.getId());
        data.setNewCardSideUID(this.newPosition.getGameCardSide().UID);
        data.setOriginalCardSideUID(this.originalPosition.getGameCardSide().UID);
        return data;
    }
   static public CheatMove getMoveFromData (CheatData data, GameObject o){
        Player cheater=null;
        for (Player player:o.getGameStatistic().getPlayers()
             ) {
            if(data.cheaterID.equals(player.getId())){
                cheater=player;
            }
        }
        if(cheater==null){
            throw new NoSuchElementException();
        }

        Soldier soldier=o.getGameMap().getMapArray()[data.y][data.x].getSoldierPlacements().get(0).getSoldier();
        soldier.setX(data.x);
        soldier.setY(data.y);
       CheatMove cheatmove=new CheatMove(cheater,soldier);
       GameMapEntry entry= o.getGameMap().getMapArray()[data.y][data.x];
       GameCardSide oldside=entry.getCard().getGameCardSideWithUID(data.originalCardSideUID);
       GameCardSide newside=entry.getCard().getGameCardSideWithUID(data.newCardSideUID);

       cheatmove.originalPosition=new SoldierPlacement(soldier,oldside);
       cheatmove.newPosition=new SoldierPlacement(soldier,newside);

        return cheatmove;
    }

    public SoldierPlacement getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(SoldierPlacement originalPosition) {
        this.originalPosition = originalPosition;
    }

    public SoldierPlacement getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(SoldierPlacement newPosition) {
        this.newPosition = newPosition;
    }



    public Player getCheater() {
        return cheater;
    }

    public void setCheater(Player cheater) {
        this.cheater = cheater;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Soldier getSoldier() {
        return soldier;
    }

    public void setSoldier(Soldier soldier) {
        this.soldier = soldier;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public CheatMove(Player cheater, Soldier soldier) {

        this.cheater = cheater;
        this.active = true;
        this.soldier = soldier;
    }
}

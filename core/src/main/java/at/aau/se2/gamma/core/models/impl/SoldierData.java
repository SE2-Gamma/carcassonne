package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

public class SoldierData implements Serializable {
    public int getSoldierID() {
        return SoldierID;
    }

    public void setSoldierID(int soldierID) {
        SoldierID = soldierID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    int SoldierID;
    int x;
    int y;

    public int getGameCardSideUID() {
        return GameCardSideUID;
    }

    public void setGameCardSideUID(int gameCardSideUID) {
        GameCardSideUID = gameCardSideUID;
    }

    int GameCardSideUID;

    public SoldierData(Soldier soldier) {
        if(soldier.getSoldierPlacement()!=null){
            this.GameCardSideUID=soldier.soldierPlacement.getGameCardSide().UID;
        }
      this.SoldierID=soldier.getId();
      this.x=soldier.getX();
      this.y=soldier.getY();
    }
}

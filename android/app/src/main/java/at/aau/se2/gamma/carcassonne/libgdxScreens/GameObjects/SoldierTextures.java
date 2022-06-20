package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.LinkedList;

import at.aau.se2.gamma.core.models.impl.Player;
import at.aau.se2.gamma.core.models.impl.Soldier;

public class SoldierTextures {
    private Texture SoldierTexutres[] = new Texture[5];
    private ArrayList<String> playerlist;
    public SoldierTextures(){
        SoldierTexutres[0]= new Texture("Soldier_Red.png");
        SoldierTexutres[1]= new Texture("Soldier_Blue.png");
        SoldierTexutres[2]= new Texture("Soldier_Green.png");
        SoldierTexutres[3]= new Texture("Soldier_Yellow.png");
        SoldierTexutres[4]= new Texture("Soldier_Black.png");
        playerlist = new ArrayList<>();
    }

    public void disposeTexutres(){
        for(Texture t : SoldierTexutres){
            t.dispose();
        }
    }

    private Texture getSoldierTexutre(int soldierNumber){
        switch (soldierNumber) {
            case 0:
                return SoldierTexutres[0];
            case 1:
                return SoldierTexutres[1];
            case 2:
                return SoldierTexutres[2];
            case 3:
                return SoldierTexutres[3];
            case 4:
                return SoldierTexutres[4];
            default:
                return SoldierTexutres[4];

        }
    }

    public Texture searchSoldierTexture(String userID){
        if(playerlist.contains(userID)){
            for(int i = 0; i<playerlist.size(); i++){
                if(playerlist.get(i).equals(userID)){
                    return getSoldierTexutre(i);
                }
            }
        }else{
            playerlist.add(userID);
            return searchSoldierTexture(userID);
        }
        return SoldierTexutres[4];
    }

    public void resetSolierTextureOrder(){
        playerlist = new ArrayList<>();
    }
}

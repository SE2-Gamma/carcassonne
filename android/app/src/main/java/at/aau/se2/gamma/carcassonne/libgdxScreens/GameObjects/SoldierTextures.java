package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import at.aau.se2.gamma.core.models.impl.Player;

public class SoldierTextures {
    private Texture SoldierTexutres[] = new Texture[5];
    private ArrayList<Player> playerlist;
    public SoldierTextures(ArrayList<Player> playerlist){
        SoldierTexutres[0]= new Texture("Soldier_Red.png");
        SoldierTexutres[1]= new Texture("Soldier_Blue.png");
        SoldierTexutres[2]= new Texture("Soldier_Green.png");
        SoldierTexutres[3]= new Texture("Soldier_Yellow.png");
        SoldierTexutres[4]= new Texture("Soldier_Black.png");
        this.playerlist = playerlist;
    }

    public void disposeTextures(){
        for(Texture t : SoldierTexutres){
            t.dispose();
        }
    }

    private Texture getSoldierTextures(int soldierNumber){
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
        for(int i = 0; i<playerlist.size(); i++){
            if(userID.equals(playerlist.get(i).getId())){
                return getSoldierTextures(i);
            }
        }
        return SoldierTexutres[4];
    }
}

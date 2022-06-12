package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Texture;

public class GameCardTextures {
    public static GameCardTextures instance = null;
    public static Texture textures[] = new Texture[24];

    private GameCardTextures(){
            for(int i = 0; i<24; i++){
                textures[i] = new Texture("Carc"+(i+1)+".jpg");
            }
    }

    public static GameCardTextures getInstance()
    {
        if (instance == null){
            instance = new GameCardTextures();
        }
        return instance;
    }

    public void disposeTextures(){
        instance = null;
        //for(Texture t : textures){
          //  t.dispose();
        //}
    }

    public Texture getTextureFromCardID(String CardID){
        switch (CardID){
            case "A":
                return textures[0];
            case "B":
                return textures[1];
            case "C":
                return textures[2];
            case "D":
                return textures[3];
            case "E":
                return textures[4];
            case "F":
                return textures[5];
            case "G":
                return textures[6];
            case "H":
                return textures[7];
            case "I":
                return textures[8];
            case "J":
                return textures[9];
            case "K":
                return textures[10];
            case "L":
                return textures[11];
            case "M":
                return textures[12];
            case "N":
                return textures[13];
            case "O":
                return textures[14];
            case "P":
                return textures[15];
            case "Q":
                return textures[16];
            case "R":
                return textures[17];
            case "S":
                return textures[18];
            case "T":
                return textures[19];
            case "U":
                return textures[20];
            case "V":
                return textures[21];
            case "W":
                return textures[22];
            case "X":
                return textures[23];
            default:
                return new Texture("testTexture.jpg");
        }
    }



}

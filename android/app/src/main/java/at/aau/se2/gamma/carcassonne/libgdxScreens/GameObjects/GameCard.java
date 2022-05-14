package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import android.util.Log;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import at.aau.se2.gamma.core.models.impl.GameCardSide;
import at.aau.se2.gamma.core.models.impl.GameMapEntry;
import at.aau.se2.gamma.core.models.impl.Orientation;

public class GameCard {
    private Texture gameCardTexture;
    private Vector2 position;
    private float rotation;

    private at.aau.se2.gamma.core.models.impl.GameMapEntry ServerMapEntry;

    public GameCard(Texture texture, Vector2 position, float rotation, at.aau.se2.gamma.core.models.impl.GameMapEntry ServerMapEntry){
        gameCardTexture = texture;
        this.position = position;
        this.rotation = rotation;
        this.ServerMapEntry = ServerMapEntry;
    }

    public GameCard(Texture texture, Vector2 position, at.aau.se2.gamma.core.models.impl.GameMapEntry ServerMapEntry){
        gameCardTexture = texture;
        this.position = position;
        this.rotation = 0;
        this.ServerMapEntry = ServerMapEntry;
    }

    public Texture getGameCardTexture() {
        return gameCardTexture;
    }

    public void setGameCardTexture(Texture gameCardTexture) { this.gameCardTexture = gameCardTexture; }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isVisible(OrthographicCamera playercam){

        //check if camera and GameCard Overlap

        float camzoom = playercam.zoom;
        //bottom left point from camera
        float x1 = playercam.position.x - playercam.viewportWidth * camzoom / 2;
        float y1 = playercam.position.y - playercam.viewportHeight * camzoom / 2;

        //top right point from camera
        float x2 = playercam.position.x + playercam.viewportWidth * camzoom / 2;
        float y2 = playercam.position.y + playercam.viewportHeight * camzoom / 2;

        //bottom left card is given
        //top right Card
        float x4 = position.x + 128;
        float y4 = position.y + 128;


        if(position.x > x2 || position.y > y2 || x1 > x4 || y1 > y4) {
            return false;
        }else return true;

    }

    public void setRotation(float rotation){
        this.rotation = rotation%360;
        Orientation currentOriantation;

        switch ((int)this.rotation){
            case 0:
                currentOriantation = Orientation.EAST;
                break;
            case 90:
                currentOriantation = Orientation.SOUTH;
                break;
            case 180:
                currentOriantation = Orientation.WEST;
                break;
            case 270:
                currentOriantation = Orientation.NORTH;
                break;
            case -90:
                currentOriantation = Orientation.NORTH;
                break;
            case -180:
                currentOriantation = Orientation.WEST;
                break;
            case -270:
                currentOriantation = Orientation.SOUTH;
                break;
            default:
                currentOriantation = Orientation.NORTH;
                break;
        }
        ServerMapEntry.setOrientation(currentOriantation);
        System.out.println("Rotation: "+this.rotation+ " sides"+ ServerMapEntry.getAlignedCardSides());
        Log.i("TAG","Rotation: "+this.rotation+ "Orientation:"+currentOriantation+ " sides"+ ServerMapEntry.getAlignedCardSides()[0]+" | " + ServerMapEntry.getAlignedCardSides()[1] +" | "+ ServerMapEntry.getAlignedCardSides()[2]+" | " + ServerMapEntry.getAlignedCardSides()[3]);
    }

    public void addRotation(float rotation){
        this.rotation = (this.rotation + rotation)%360;
    }

    public void subtractRotation(float rotation){
        this.rotation = (this.rotation - rotation)%360;
    }

    public float getRotation(){
        return rotation;
    }

    public GameMapEntry getGameMapEntry(){
        return ServerMapEntry;
    }
/*
    public GameCardSide getCardSide_TOP(){
        switch ((int)rotation){
            case 0:
               return ServerMapEntry.getSideNorth();
            case 90:
                return ServerMapEntry.getSideEast();
            case 180:
                return ServerMapEntry.getSideSouth();
            case 270:
                return ServerMapEntry.getSideWest();
        }
        return ServerMapEntry.getSideNorth();
    }

    public GameCardSide getCardSide_BOTTOM(){
        switch ((int)rotation){
            case 0:
                return ServerMapEntry.getSideSouth();
            case 90:
                return ServerMapEntry.getSideWest();
            case 180:
                return ServerMapEntry.getSideNorth();
            case 270:
                return ServerMapEntry.getSideEast();
        }
        return ServerMapEntry.getSideNorth();
    }

    public GameCardSide getCardSide_LEFT(){
        switch ((int)rotation){
            case 0:
                return ServerMapEntry.getSideWest();
            case 90:
                return ServerMapEntry.getSideNorth();
            case 180:
                return ServerMapEntry.getSideEast();
            case 270:
                return ServerMapEntry.getSideSouth();
        }
        return ServerMapEntry.getSideNorth();
    }

    public GameCardSide getCardSide_RIGHT(){
        switch ((int)rotation){
            case 0:
                return ServerMapEntry.getSideEast();
            case 90:
                return ServerMapEntry.getSideSouth();
            case 180:
                return ServerMapEntry.getSideWest();
            case 270:
                return ServerMapEntry.getSideNorth();
        }
        return ServerMapEntry.getSideNorth();
    }

 */
}

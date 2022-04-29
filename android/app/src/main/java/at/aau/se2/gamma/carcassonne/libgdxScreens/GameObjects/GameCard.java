package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class GameCard {
    private Texture gameCardTexture;
    private Vector2 position;
    private float rotation;

    public GameCard(Texture texture, Vector2 position){
        gameCardTexture = texture;
        this.position = position;
        this.rotation = 0;
    }

    public GameCard(Texture texture, Vector2 position, float rotation){
        gameCardTexture = texture;
        this.position = position;
        this.rotation = rotation;
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
}

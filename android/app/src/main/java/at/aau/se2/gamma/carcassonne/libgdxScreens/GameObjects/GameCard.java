package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class GameCard {
    private Texture gameCardImage;
    private Vector2 position;

    public GameCard(Texture texture, Vector2 position){
        gameCardImage = texture;
        this.position = position;
    }

    public Texture getGameCardImage() {
        return gameCardImage;
    }

    public void setGameCardImage(Texture gameCardImage) {
        this.gameCardImage = gameCardImage;
    }

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
}

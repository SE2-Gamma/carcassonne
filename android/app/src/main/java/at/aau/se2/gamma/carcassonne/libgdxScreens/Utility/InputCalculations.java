package at.aau.se2.gamma.carcassonne.libgdxScreens.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InputCalculations {
    //calculates the position of the tap in the Game World
    public static Vector2 touch_to_GameWorld_coordinates(float x, float y, OrthographicCamera playercam, Viewport gameviewport, int currentScreenWidth, int currentScreenHeight){

        Vector2 mapCoordinates = new Vector2();
        mapCoordinates.x = (playercam.position.x-(playercam.viewportWidth*playercam.zoom/2))+((gameviewport.getWorldWidth()/ currentScreenWidth*x*playercam.zoom));
        mapCoordinates.y = ((playercam.position.y-(playercam.viewportHeight*playercam.zoom/2))+(((gameviewport.getWorldHeight()/currentScreenHeight)*(currentScreenHeight-y)*playercam.zoom)));
        return mapCoordinates;
    }

    public static Vector2 touch_pan_to_GameWorld_pan(float deltaX, float deltaY, OrthographicCamera playercam, int currentScreenWidth, int currentScreenHeight){
        Vector2 camPanMovement = new Vector2();
        camPanMovement.x = (playercam.viewportWidth/currentScreenWidth)*deltaX * playercam.zoom;
        camPanMovement.y = (playercam.viewportHeight/currentScreenHeight)*deltaY * playercam.zoom;
        return camPanMovement;
    }

    //🗑
    public static float CalculateZoom(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2, OrthographicCamera playercam, int currentScreenWidth, int currentScreenHeight ){
        float distance1 = initialPointer1.dst(pointer1);
        //float distance2 = initialPointer2.dst(pointer2);

        float distancefingersStart = initialPointer1.dst(initialPointer2);
        float distancefingersEnd = pointer1.dst(pointer2);

        float resultingZoom=1f;

        if(distancefingersStart > distancefingersEnd){
            float stdMovement = playercam.viewportHeight / currentScreenHeight * distance1;
            resultingZoom = playercam.zoom + stdMovement / 100;

            if(resultingZoom > 10){
                resultingZoom = 10;
            }


        }else {
            float stdMovement = playercam.viewportHeight / currentScreenHeight * distance1;
            resultingZoom = playercam.zoom - stdMovement / 100;

            if(resultingZoom < 1){
                resultingZoom = 1;
            }
        }
        return resultingZoom;
    }

}

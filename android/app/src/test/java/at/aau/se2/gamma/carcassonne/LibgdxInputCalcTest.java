package at.aau.se2.gamma.carcassonne;

import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.Hud;
import at.aau.se2.gamma.carcassonne.libgdxScreens.MyGame;
import at.aau.se2.gamma.carcassonne.libgdxScreens.Screens.Gamescreen;
import at.aau.se2.gamma.carcassonne.libgdxScreens.Utility.InputCalculations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LibgdxInputCalcTest {

    public final float MY_WORLD_HEIGHT = 144;
    public final float MY_WORLD_WIDTH = 256;

    private OrthographicCamera playercam;
    private Viewport gameviewport;


    @Before
    public void before(){
       // final HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
       // config.renderInterval = 1f/60; // for 60 fps
       // myHeadlessGame = new HeadlessApplication(new MyGame(), config);
        // Gdx.gl = Mockito.mock(GL20.class);

        playercam = new OrthographicCamera();
        playercam.viewportWidth = MY_WORLD_WIDTH;
        playercam.viewportHeight = MY_WORLD_HEIGHT;
        playercam.position.x = 0;
        playercam.position.y = 0;
        gameviewport = new ExtendViewport(MY_WORLD_WIDTH,MY_WORLD_HEIGHT,playercam);
        gameviewport.setWorldSize(MY_WORLD_WIDTH, MY_WORLD_HEIGHT);
        playercam.zoom = 1f;

    }

    @Test
    public void touch_to_GameWorld_coordinates_Tests_normal(){
        Vector2 position1 = InputCalculations.touch_to_GameWorld_coordinates(360,480,playercam, gameviewport, 1920, 1080);
        Assert.assertTrue(position1.x == -80.0f);
        Assert.assertTrue(position1.y == 8.000008f);

        Vector2 position2 = InputCalculations.touch_to_GameWorld_coordinates(720,960,playercam, gameviewport, 1920, 1080);
        Assert.assertTrue(position2.x == -31.999992f);
        Assert.assertTrue(position2.y == -56.0f);

    }
    @Test
    public void touch_to_GameWorld_coordinates_Tests_Limits(){
        Vector2 position1 = InputCalculations.touch_to_GameWorld_coordinates(0,0,playercam, gameviewport, 1920, 1080);
        Assert.assertTrue(position1.x == -128.0f);
        Assert.assertTrue(position1.y == 72.0f);

        Vector2 position2 = InputCalculations.touch_to_GameWorld_coordinates(1920,1080,playercam, gameviewport, 1920, 1080);
        Assert.assertTrue(position2.x == 128.0f);
        Assert.assertTrue(position2.y == -72.0f);
    }

    @Test
    public void touch_pan_to_GameWorld_pan_Tests() {
        Vector2 position1 = InputCalculations.touch_pan_to_GameWorld_pan(30,30,playercam, 1920, 1080);
        Assert.assertTrue(position1.x == 4.0f);
        Assert.assertTrue(position1.y == 4.0f);

        Vector2 position2 = InputCalculations.touch_pan_to_GameWorld_pan(1000,1000,playercam, 1920, 1080);

        Assert.assertTrue(position2.x == 133.33334f);
        Assert.assertTrue(position2.y == 133.33334f);

        Vector2 position3 = InputCalculations.touch_pan_to_GameWorld_pan(120,1500,playercam, 1920, 1080);
        Assert.assertTrue(position3.x == 16.0f);
        Assert.assertTrue(position3.y == 200.00002f);
    }

    @Test
    public void CalculateZoom_Tests_normal() {
        float testZoom1 = InputCalculations.CalculateZoom(new Vector2(0,0), new Vector2(1000,1000), new Vector2(100,100), new Vector2(900,900), playercam, 1920, 1080);
        Assert.assertTrue(testZoom1 == 1.1885618f);

        float testZoom2 = InputCalculations.CalculateZoom(new Vector2(0,0), new Vector2(1000,1000), new Vector2(400,400), new Vector2(500,500), playercam, 1920, 1080);
        Assert.assertTrue(testZoom2 == 1.7542473f);
    }

    @Test
    public void CalculateZoom_Tests_Limimts() {
        float testZoom1 = InputCalculations.CalculateZoom(new Vector2(1000,1000), new Vector2(1920,1080), new Vector2(0,0), new Vector2(1920,1080), playercam, 1920, 1080);
        Assert.assertTrue(testZoom1 == 1.0f);

        playercam.zoom = 9f;
        float testZoom2 = InputCalculations.CalculateZoom(new Vector2(0,0), new Vector2(1920,1080), new Vector2(1900,1000), new Vector2(1920,1080), playercam, 1920, 1080);
        Assert.assertTrue(testZoom2 == 10.0f);
    }



}

package at.aau.se2.gamma.carcassonne;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameCard;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.GameMapEntry;
import at.aau.se2.gamma.core.models.impl.Orientation;
import at.aau.se2.gamma.core.models.impl.Player;

public class LibgdxGameCardTest {

    GameCard gc;
    Vector2 position;
    Texture myTexture;
    @Before
    public void before(){
        myTexture = Mockito.mock(Texture.class);
        position = new Vector2(0,0);
        at.aau.se2.gamma.core.models.impl.GameCard gm_i = GameCardFactory.D();
        Player player = new Player("1234", "Leon");
        GameMapEntry gme = new GameMapEntry(gm_i, player,Orientation.SOUTH);
        gc = new GameCard(myTexture,position, gme);
    }

    @Test
    public void GameCard_getGameCardImage_test(){
        Assert.assertEquals(gc.getGameCardTexture(), myTexture);
    }

    @Test
    public void GameCard_setGameCardImage_test(){
        Texture newTexture = Mockito.mock(Texture.class);
        gc.setGameCardTexture(newTexture);
        Assert.assertEquals(gc.getGameCardTexture(), newTexture);
    }

    @Test
    public void GameCard_getPosition_test(){
        Assert.assertEquals(gc.getPosition(), new Vector2(0,0));
    }

    @Test
    public void GameCard_setPosition_test(){
        gc.setPosition(new Vector2(100,100));
        Assert.assertEquals(gc.getPosition(), new Vector2(100,100));
    }

    @Test
    public void GameCard_isVisible_test(){
        Camera playercam = new OrthographicCamera();
        playercam.position.x = 0;
        playercam.position.y = 0;
        Assert.assertTrue(gc.isVisible((OrthographicCamera)playercam));

        playercam.position.x = 1000;
        playercam.position.y = 1000;
        Assert.assertFalse(gc.isVisible((OrthographicCamera)playercam));
    }

    @Test
    public void GameCard_set_and_get_ImplGameCard(){
        //setting new GameCard
        at.aau.se2.gamma.core.models.impl.GameCard gm_i = GameCardFactory.I();
        Player player = new Player("4321", "Leon");
        GameMapEntry gme = new GameMapEntry(gm_i, player,Orientation.NORTH);
        gc.setGameMapEntry(gme, Mockito.mock(Texture.class));

        Assert.assertTrue(gc.getGameMapEntry().equals(gme));
    }

    @Test
    public void GameCard_rotation(){
        //testing Base GameCard
        Assert.assertTrue(gc.getRotation() == 90);

        //setting new GameCard
        at.aau.se2.gamma.core.models.impl.GameCard gm_i = GameCardFactory.I();
        Player player = new Player("4321", "Leon");
        GameMapEntry gme = new GameMapEntry(gm_i, player,Orientation.NORTH);
        gc.setGameMapEntry(gme, Mockito.mock(Texture.class));

        //testing second GameCard
        Assert.assertTrue(gc.getRotation() == 270);

        Texture myTexture2 = Mockito.mock(Texture.class);
        Vector2 position2 = new Vector2(0,0);
        at.aau.se2.gamma.core.models.impl.GameCard gm_i2 = GameCardFactory.D();
        Player player2 = new Player("1234", "Leon");
        GameMapEntry gme2 = new GameMapEntry(gm_i, player,Orientation.NORTH);
        GameCard gc2 = new GameCard(myTexture,position,0, gme);

        Assert.assertTrue(gc2.getRotation() == 0);

    }

    @Test
    public void GameCard_testAll_set_Rotations(){
        gc.setRotation(0);
        Assert.assertTrue(gc.getRotation() == 0);

        gc.setRotation(90);
        Assert.assertTrue(gc.getRotation() == 90);

        gc.setRotation(180);
        Assert.assertTrue(gc.getRotation() == 180);

        gc.setRotation(270);
        Assert.assertTrue(gc.getRotation() == 270);

        gc.setRotation(-90);
        Assert.assertTrue(gc.getRotation() == -90);

        gc.setRotation(-180);
        Assert.assertTrue(gc.getRotation() == -180);

        gc.setRotation(-270);
        Assert.assertTrue(gc.getRotation() == -270);

        gc.setRotation(Orientation.EAST);
        Assert.assertTrue(gc.getRotation() == 0);
        gc.setRotation(Orientation.SOUTH);
        Assert.assertTrue(gc.getRotation() == 90);
        gc.setRotation(Orientation.WEST);
        Assert.assertTrue(gc.getRotation() == 180);
        gc.setRotation(Orientation.NORTH);
        Assert.assertTrue(gc.getRotation() == 270);

    }


}

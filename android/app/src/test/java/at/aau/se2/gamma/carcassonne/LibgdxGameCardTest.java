package at.aau.se2.gamma.carcassonne;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;

import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameCard;
import at.aau.se2.gamma.carcassonne.libgdxScreens.Utility.InputCalculations;

public class LibgdxGameCardTest {

    GameCard gc;
    Vector2 position;
    Texture myTexture;
    @Before
    public void before(){
        myTexture = Mockito.mock(Texture.class);
        position = new Vector2(0,0);
        gc = new GameCard(myTexture,position);
    }

    @Test
    public void GameCard_getGameCardImage_test(){
        Assert.assertEquals(gc.getGameCardImage(), myTexture);
    }

    @Test
    public void GameCard_setGameCardImage_test(){
        Texture newTexture = Mockito.mock(Texture.class);
        gc.setGameCardImage(newTexture);
        Assert.assertEquals(gc.getGameCardImage(), newTexture);
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


}
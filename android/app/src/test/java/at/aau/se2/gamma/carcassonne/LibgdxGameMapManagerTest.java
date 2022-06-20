package at.aau.se2.gamma.carcassonne;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameCard;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameMapManager;
import at.aau.se2.gamma.core.models.impl.GameMap;
import at.aau.se2.gamma.core.models.impl.GameMapEntry;

public class LibgdxGameMapManagerTest {
    private final float MY_WORLD_HEIGHT = 144;
    private final float MY_WORLD_WIDTH = 256;

    private OrthographicCamera playercam;
    private Viewport gameviewport;

    private GameMapManager myMap;
    private SpriteBatch sb;

    private GameCard gc;
    private Vector2 position;
    private Texture myTexture;

    private GameMapEntry gameMapEntryMock;

    private GameMap firstMap;


    //@Before
    public void before(){

        playercam = new OrthographicCamera();
        playercam.viewportWidth = MY_WORLD_WIDTH;
        playercam.viewportHeight = MY_WORLD_HEIGHT;
        playercam.position.x = 0;
        playercam.position.y = 0;
        gameviewport = new ExtendViewport(MY_WORLD_WIDTH,MY_WORLD_HEIGHT,playercam);
        gameviewport.setWorldSize(MY_WORLD_WIDTH, MY_WORLD_HEIGHT);
        playercam.zoom = 1f;

        sb = Mockito.mock(SpriteBatch.class);


        myTexture = Mockito.mock(Texture.class);
        position = new Vector2(0,0);

        gameMapEntryMock = Mockito.mock(GameMapEntry.class);
        //Mockito.when(gameMapEntryMock).get
        gc = new GameCard(myTexture,position,0, gameMapEntryMock);

        firstMap = new GameMap();
        myMap = new GameMapManager(playercam, gameviewport, sb, firstMap);
        Mockito.doNothing().when(sb).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat());
        Mockito.doNothing().when(sb).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());
        Mockito.doNothing().when(sb).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());
    }

    //@Test
    public void GameMap_draw_test(){
        //no textures set, thats why we have 0 sb.draw calls
        myMap.draw();
        Mockito.verify(sb, Mockito.times(0)).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());

        myMap.setGamecard(0,0, gc);
        myMap.draw();
        Mockito.verify(sb, Mockito.times(1)).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());

        myMap.setGamecard(1,0,gc);
        myMap.draw();
        Mockito.verify(sb, Mockito.times(3)).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());

        myMap.dispose();//does nothing currently, just added for code coverage
    }
}

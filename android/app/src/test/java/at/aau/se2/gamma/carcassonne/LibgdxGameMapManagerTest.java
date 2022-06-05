package at.aau.se2.gamma.carcassonne;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
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

import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameCard;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameMapManager;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.Hud;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.SoldierTextures;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.GameMap;
import at.aau.se2.gamma.core.models.impl.GameMapEntry;
import at.aau.se2.gamma.core.models.impl.GameMapEntryPosition;
import at.aau.se2.gamma.core.models.impl.Orientation;
import at.aau.se2.gamma.core.models.impl.Player;

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


    @Before
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
        myMap = new GameMapManager(playercam, gameviewport, sb, firstMap, false);
        Mockito.doNothing().when(sb).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat());
        Mockito.doNothing().when(sb).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());
        Mockito.doNothing().when(sb).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());
    }

    @Test
    public void GameMap_draw_test(){
        //no textures set, thats why we have 0 sb.draw calls
        myMap.draw(Hud.Hud_State.PLAYING);
        Mockito.verify(sb, Mockito.times(0)).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());

        myMap.setGamecard(0,0, gc);
        myMap.draw(Hud.Hud_State.PLAYING);
        Mockito.verify(sb, Mockito.times(1)).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());

        myMap.setGamecard(1,0,gc);
        myMap.draw(Hud.Hud_State.PLAYING);
        Mockito.verify(sb, Mockito.times(3)).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean());

        myMap.dispose();//does nothing because i cant load textures, in a unit test
    }

    @Test
    public void GameMap_setCard_TOP(){

        //testing left extreme
        firstMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.D(), null, Orientation.EAST), new GameMapEntryPosition(1, 99));
        myMap.setGameMap(firstMap);
        Player player = new Player("1234", "Leon");
        boolean test = myMap.setGamecard(new Vector2(0,144*99), new GameCard(Mockito.mock(Texture.class), new Vector2(0,144*99), new GameMapEntry(GameCardFactory.A(), player,  Orientation.EAST)));
        Assert.assertTrue(test);

        firstMap = new GameMap();
        firstMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.D(), null, Orientation.EAST), new GameMapEntryPosition(1, 99));
        myMap.setGameMap(firstMap);
        Assert.assertFalse(myMap.setGamecard(new Vector2(0,144*99), new GameCard(Mockito.mock(Texture.class), new Vector2(0,144*99), new GameMapEntry(GameCardFactory.A(), player,  Orientation.SOUTH))));

        //testing right extreme
        firstMap = new GameMap();
        firstMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.D(), null, Orientation.EAST), new GameMapEntryPosition(98, 99));
        myMap.setGameMap(firstMap);
        Assert.assertTrue(myMap.setGamecard(new Vector2(144*99,144*99), new GameCard(Mockito.mock(Texture.class), new Vector2(0,144*99), new GameMapEntry(GameCardFactory.H(), player,  Orientation.EAST))));

        firstMap = new GameMap();
        firstMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.D(), null, Orientation.EAST), new GameMapEntryPosition(98, 99));
        myMap.setGameMap(firstMap);
        Assert.assertFalse(myMap.setGamecard(new Vector2(144*99,144*99), new GameCard(Mockito.mock(Texture.class), new Vector2(0,144*99), new GameMapEntry(GameCardFactory.H(), player,  Orientation.NORTH))));

        firstMap = new GameMap();
        firstMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.D(), null, Orientation.EAST), new GameMapEntryPosition(99, 98));
        myMap.setGameMap(firstMap);
        Assert.assertTrue(myMap.setGamecard(new Vector2(144*99,144*99), new GameCard(Mockito.mock(Texture.class), new Vector2(144*99,144*99), new GameMapEntry(GameCardFactory.A(), player,  Orientation.EAST))));

        firstMap = new GameMap();
        firstMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.D(), null, Orientation.EAST), new GameMapEntryPosition(99, 98));
        myMap.setGameMap(firstMap);
        Assert.assertFalse(myMap.setGamecard(new Vector2(144*99,144*99), new GameCard(Mockito.mock(Texture.class), new Vector2(144*99,144*99), new GameMapEntry(GameCardFactory.A(), player,  Orientation.NORTH))));

        //testing middle
        firstMap = new GameMap();
        firstMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.D(), null, Orientation.EAST), new GameMapEntryPosition(98, 99));
        myMap.setGameMap(firstMap);
        Assert.assertFalse(myMap.setGamecard(new Vector2(144*99,144*99), new GameCard(Mockito.mock(Texture.class), new Vector2(0,144*99), new GameMapEntry(GameCardFactory.H(), player,  Orientation.NORTH))));

        firstMap = new GameMap();
        firstMap.placeGameMapEntry(new GameMapEntry(GameCardFactory.D(), null, Orientation.EAST), new GameMapEntryPosition(98, 99));
        myMap.setGameMap(firstMap);
        Assert.assertFalse(myMap.setGamecard(new Vector2(144*99,144*99), new GameCard(Mockito.mock(Texture.class), new Vector2(0,144*99), new GameMapEntry(GameCardFactory.H(), player,  Orientation.NORTH))));
    }
}

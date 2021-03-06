package at.aau.se2.gamma.carcassonne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.mockito.Mockito;

import java.util.ArrayList;

import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameCard;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameMapManager;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.Hud;
import at.aau.se2.gamma.carcassonne.libgdxScreens.Screens.Gamescreen;
import at.aau.se2.gamma.core.models.impl.GameMap;
import at.aau.se2.gamma.core.models.impl.GameMapEntry;

public class LibgdxHudTest {


        private final float MY_WORLD_HEIGHT = 144;
        private final float MY_WORLD_WIDTH = 256;

        private OrthographicCamera playercam;
        private Viewport gameviewport;

        private GameMapManager myMap;
        private SpriteBatch sb;

        private GameCard gc;
        private Vector2 position;
        private Texture myTexture;

        private Hud myHud;

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
            gc = new GameCard(myTexture,position, Mockito.mock(GameMapEntry.class));

            firstMap = new GameMap();
            myMap = new GameMapManager(playercam, gameviewport, sb, firstMap, true, new ArrayList<>());

            myMap = new GameMapManager(playercam, gameviewport, sb, firstMap, true, new ArrayList<>());
            Mockito.doNothing().when(sb).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat());

            //Viewport myViewport = Mockito.mock(ScreenViewport.class);
            //Mockito.when(myViewport.)
            myHud = new Hud(sb, Mockito.mock(Gamescreen.class));

            Graphics myG = Mockito.mock(Graphics.class);

            Mockito.when(myG.getHeight()).thenReturn(1080);
            Mockito.when(myG.getWidth()).thenReturn(1920);

            Gdx.graphics = myG;

        }

       // @Test
        public void Hud_draw_test(){
            //no textures set, thats why we have 0 sb.draw calls

            myHud.drawStage();
           // Mockito.verify(sb, Mockito.times(0)).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat());

            //myMap.setGamecard(new Vector2(10,10), gc);
           // myMap.draw();
           // Mockito.verify(sb, Mockito.times(1)).draw(Mockito.any(Texture.class), Mockito.anyFloat(), Mockito.anyFloat());


           // myMap.dispose();//does nothing currently, just added for code coverage
        }


}

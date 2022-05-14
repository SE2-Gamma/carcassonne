package at.aau.se2.gamma.carcassonne.libgdxScreens.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;

import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameCard;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameMapManager;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.Hud;
import at.aau.se2.gamma.carcassonne.libgdxScreens.Utility.InputCalculations;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.GameMapEntry;
import at.aau.se2.gamma.core.models.impl.Orientation;
import at.aau.se2.gamma.core.models.impl.Player;

public class Gamescreen extends ScreenAdapter implements GestureDetector.GestureListener {

    //planed Viewport size, will change depending on aspect ratio of device.
    public final float MY_WORLD_HEIGHT = 144;
    public final float MY_WORLD_WIDTH = 256;

    private OrthographicCamera playercam;
    private Viewport gameviewport;
    private SpriteBatch batch;
    Texture textures[];
    Texture errorTextur;
    Hud hud;

    GestureDetector gestureDetecor;

    ShapeRenderer shaprenderer;
    Vector2 camPos;

    //fonts
    BitmapFont myfont = new BitmapFont();


    //Variables for gestures
    Vector2 camPanGesture;
    //zooming
    float distance1;
    float distance2;
    float distancefingersStart;
    float distancefingersEnd;

    //Gamemap
    GameMapManager myMap;
    Texture currentCard;
    Texture playedCard;
    int playedCard_x;
    int playedCard_y;

    //storing currently GameCard.
    GameCard currentGameCard;
    GameCard lastCard;

    //Card Deck, Just for testing stored locally
    LinkedList<at.aau.se2.gamma.core.models.impl.GameCard> CardDeck = GameCardFactory.getDeck(1);


    public Gamescreen (){

        playercam = new OrthographicCamera();
        playercam.setToOrtho(false);

        //type of Viewport changes how game is displayed on screen -> stretched, black bars,...
        gameviewport = new ExtendViewport(MY_WORLD_WIDTH,MY_WORLD_HEIGHT,playercam);

        batch = new SpriteBatch();
        hud = new Hud(batch, this);

        shaprenderer = new ShapeRenderer();

        camPos = new Vector2(49f*144f+(128f/2f),49f*144f+(128f/2f));
        playercam.position.set(camPos.x,camPos.y,1);
        playercam.update();

        //setting up gesture detector
        gestureDetecor = new GestureDetector(this);
        //Gdx.input.setInputProcessor(gestureDetecor); //replaced with InputMultiplexer for HUD and Gamescreen Inputs

        camPanGesture = new Vector2();

        //setting up playable area / map
        myMap = new GameMapManager(playercam, gameviewport, batch);


        //error Texture for Testing
        errorTextur = new Texture("testTexture.jpg");

        //loading all gameCard Textures
        textures = new Texture[21];
        textures[0]=new Texture("Carc1.jpg");
        textures[1]=new Texture("Carc2.jpg");
        textures[2]=new Texture("Carc3.jpg");
        textures[3]=new Texture("Carc4.jpg");
        textures[4]=new Texture("Carc5.jpg");
        textures[5]=new Texture("Carc6.jpg");
        textures[6]=new Texture("Carc7.jpg");
        textures[7]=new Texture("Carc8.jpg");
        textures[8]=new Texture("Carc9.jpg");
        textures[9]=new Texture("Carc10.jpg");
        textures[10]=new Texture("Carc11.jpg");
        textures[11]=new Texture("Carc12.jpg");
        textures[12]=new Texture("Carc13.jpg");
        textures[13]=new Texture("Carc14.jpg");
        textures[14]=new Texture("Carc15.jpg");
        textures[15]=new Texture("Carc16.jpg");
        textures[16]=new Texture("Carc17.jpg");
        textures[17]=new Texture("Carc18.jpg");
        textures[18]=new Texture("Carc19.jpg");
        textures[19]=new Texture("Carc20.jpg");
        textures[20]=new Texture("Carc21.jpg");

        playedCard_x = 0;
        playedCard_y = 0;

        //for showcase of functionality, placed starter card.GameCardFactory.D()

        at.aau.se2.gamma.core.models.impl.GameMapEntry starterEntry = new GameMapEntry(GameCardFactory.D(), null, Orientation.NORTH);
        myMap.setGamecard(49,49, new GameCard(getTextureFromCardID(starterEntry.getCard().getCardId()), new Vector2(49f*144f,49f*144f), 270f, starterEntry));

        //set currentCard
        Player myPlayerID = new Player("BRUDI MUSS LOS", "LEON");
        at.aau.se2.gamma.core.models.impl.GameMapEntry newCardFromDeck = new GameMapEntry(CardDeck.get((int)(Math.random()*20)), myPlayerID, Orientation.NORTH);
        currentGameCard = new GameCard(getTextureFromCardID(newCardFromDeck.getCard().getCardId()), new Vector2(0,0),270f,newCardFromDeck);
        hud.setNextCardTexture(currentGameCard.getGameCardTexture());

        InputMultiplexer im = new InputMultiplexer(hud.getStage(), gestureDetecor);
        Gdx.input.setInputProcessor(im);

        hud.getAcceptButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                at.aau.se2.gamma.core.models.impl.GameMapEntry newCardFromDeck = new GameMapEntry(CardDeck.get((int)(Math.random()*20)), myPlayerID, Orientation.NORTH);
                currentGameCard = new GameCard(getTextureFromCardID(newCardFromDeck.getCard().getCardId()), new Vector2(0,0), 270f, newCardFromDeck);
                hud.setNextCardTexture(currentGameCard.getGameCardTexture());
                hud.setRotation(0);
                lastCard = null;
                hud.changeHudState(Hud.Hud_State.VIEWING);
            }
        });

        hud.getDeclineButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hud.changeHudState(Hud.Hud_State.PLAYING);
                currentGameCard = lastCard;
                lastCard = null;
                myMap.setGamecard(playedCard_x, playedCard_y, null);
            }
        });


        //temp zeugs für testing
        //camPos = new Vector2(0f*144f,0f*144f);
        //myMap.setGamecard(3,1, new GameCard(getTextureFromCardID(starterCard.getCardId()), new Vector2(3f*144f,1f*144f), starterCard));

    }

    @Override
    public void render(float delta) {

        playercam.position.set(camPos.x,camPos.y,0);
        playercam.update();

        Gdx.gl.glClearColor(0.545f,0.27f,0.074f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(playercam.combined);
        batch.begin();

        //drawing empty Game Map
        myMap.draw();

        //printing fps and playercam position inside of the Gameworld
        //myfont.draw(batch, String.format("fps:%.2f | x pos: %f",(float)(1/delta), playercam.position.x), 0,0);

        batch.end();


        //used to draw simple shapes
        //shaprenderer.setProjectionMatrix(playercam.combined);
        //shaprenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shaprenderer.setColor(Color.CYAN);
        //shaprenderer.rect(5.0f, 5.0f, 200.0f, 200.0f);
        //shaprenderer.end();

        hud.drawStage(String.format("fps:%.2f | x pos: %f |",(float)(1/delta), playercam.position.x));




    }

    @Override
    public void resize(int width, int height) {
        gameviewport.update(width, height);

    }

    @Override
    public void dispose() {
        myfont.dispose();
        batch.dispose();
        for(Texture t : textures){
            t.dispose();
        }
        shaprenderer.dispose();
        hud.dispose();
        myMap.dispose();
        super.dispose();

    }

    @Override
    public void hide() {
        this.dispose();
    }


    //Gesture Listener methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(currentGameCard != null && hud.getCurrentState().equals(Hud.Hud_State.PLAYING)){
            Vector2 mapPos = InputCalculations.touch_to_GameWorld_coordinates(x, y, playercam, gameviewport, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            currentGameCard.setRotation(hud.getRotation());
            if(myMap.setGamecard(mapPos, currentGameCard)){
                playedCard_x = (int) mapPos.x / 144;
                playedCard_y = (int) mapPos.y / 144;
                lastCard = currentGameCard;
                currentGameCard = null;
                hud.changeHudState(Hud.Hud_State.ACCEPT_ACTION);
            }
        }else if(hud.getCurrentState().equals(Hud.Hud_State.PLACING_SOLDIER)){

        }
        //Log.e("info"," mapPos.x: "+ mapPos.x + " mapPos.y:" + mapPos.y + "  : yCam Bottom "+(camPos.y-(playercam.viewportHeight*playercam.zoom/2)) + " | gameviewport.getWorldHeight()"+gameviewport.getWorldHeight()+ " camPos.y: "+camPos.y + " letzer teril " +((gameviewport.getWorldHeight()/Gdx.graphics.getHeight())*y*playercam.zoom));
        //Log.e("info", "button: "+button + " | count: "+count);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        playercam.zoom=1f;
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camPanGesture = InputCalculations.touch_pan_to_GameWorld_pan(deltaX, deltaY, playercam, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camPos.x-=camPanGesture.x;
        camPos.y+=camPanGesture.y;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        //MathUtils.lerp() sounds interesting
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        playercam.zoom = InputCalculations.CalculateZoom(initialPointer1, initialPointer2, pointer1, pointer2, playercam, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return false;
    }

    @Override
    public void pinchStop() {

    }

    private Texture getTextureFromCardID(String CardID){

        switch (CardID){
            case "A":
                return textures[0];
            case "B":
                return textures[1];
            case "C":
                return textures[2];
            case "D":
                return textures[3];
            case "E":
                return textures[4];
            case "F":
                return textures[5];
            case "G":
                return textures[6];
            case "H":
                return textures[7];
            case "I":
                return textures[8];
            case "J":
                return textures[9];
            case "K":
                return textures[10];
            case "L":
                return textures[11];
            case "M":
                return textures[12];
            case "N":
                return textures[13];
            case "O":
                return textures[14];
            case "P":
                return textures[15];
            case "Q":
                return textures[16];
            case "R":
                return textures[17];
            case "S":
                return textures[18];
            case "T":
                return textures[19];
            case "U":
                return textures[20];
            case "V":
                return textures[21];
            case "W":
                return textures[22];
            case "X":
                return textures[23];
            default:
                return textures[0];
        }


    }
}

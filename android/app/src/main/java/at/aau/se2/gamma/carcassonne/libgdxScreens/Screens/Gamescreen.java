package at.aau.se2.gamma.carcassonne.libgdxScreens.Screens;

import android.util.Log;
import android.view.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

import at.aau.se2.gamma.carcassonne.AndroidInterface;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.CheatMoveSoldierPosition;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameCard;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameCardTextures;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.GameMapManager;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.Hud;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.SoldierTextures;
import at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects.UISkin;
import at.aau.se2.gamma.carcassonne.libgdxScreens.Utility.InputCalculations;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.CheatMoveBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.CheatMoveDetectedBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.FieldCompletedBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.GameTurnBroadCastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.PlayerXsTurnBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.StringBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.YourTurnBroadcastCommand;
import at.aau.se2.gamma.core.commands.CheatCommand;
import at.aau.se2.gamma.core.commands.DetectCheatCommand;
import at.aau.se2.gamma.core.commands.GameTurnCommand;
import at.aau.se2.gamma.core.commands.LeaveGameCommand;
import at.aau.se2.gamma.core.exceptions.CheatMoveImpossibleException;
import at.aau.se2.gamma.core.exceptions.InvalidPositionGameMapException;
import at.aau.se2.gamma.core.exceptions.NoSurroundingCardGameMapException;
import at.aau.se2.gamma.core.exceptions.PositionNotFreeGameMapException;
import at.aau.se2.gamma.core.exceptions.SurroundingConflictGameMapException;
import at.aau.se2.gamma.core.factories.GameCardFactory;
import at.aau.se2.gamma.core.models.impl.CheatData;
import at.aau.se2.gamma.core.models.impl.CheatMove;
import at.aau.se2.gamma.core.models.impl.GameCardSide;
import at.aau.se2.gamma.core.models.impl.GameMapEntry;
import at.aau.se2.gamma.core.models.impl.GameMapEntryPosition;
import at.aau.se2.gamma.core.models.impl.GameMove;
import at.aau.se2.gamma.core.models.impl.GameObject;
import at.aau.se2.gamma.core.models.impl.GameStatistic;
import at.aau.se2.gamma.core.models.impl.Orientation;
import at.aau.se2.gamma.core.models.impl.Player;
import at.aau.se2.gamma.core.models.impl.Soldier;
import at.aau.se2.gamma.core.models.impl.SoldierData;
import at.aau.se2.gamma.core.models.impl.SoldierPlacement;

public class Gamescreen extends ScreenAdapter implements GestureDetector.GestureListener {

    //planed Viewport size, will change depending on aspect ratio of device.
    public final float MY_WORLD_HEIGHT = 144;
    public final float MY_WORLD_WIDTH = 256;

    private OrthographicCamera playercam;
    private Viewport gameviewport;
    private SpriteBatch batch;
    GameCardTextures CardTextures;
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

    //dataFromServer
    GameObject currentGameObject;
    Player myPlayerID;
    boolean myTurn;

    private CheatMoveSoldierPosition selectedCheatingSoldier;
    private CheatMove currentCheatMove;

    //for shake detection
    float last_x;
    float last_y;
    float last_z;

    //cheat detection
    private Soldier touchedSoldier;


    AndroidInterface androidInterface;
    String userName;
    String userID;

    public Gamescreen (String gameKey, String userName, String UserID, GameObject initialGameObject, AndroidInterface androidInterface){
        touchedSoldier = null;
        currentCheatMove = null;
        selectedCheatingSoldier = null;
        this.androidInterface = androidInterface;
        this.userName = userName;
        this.userID = UserID;

        currentGameObject = initialGameObject;
        for(Player p : currentGameObject.getGameStatistic().getPlayers()){
            if(p.getId().equals(UserID) && p.getName().equals(userName)){
                myPlayerID = p;
                break;
            }
        }
        myTurn = false;

        playercam = new OrthographicCamera();
        playercam.setToOrtho(false);

        //type of Viewport changes how game is displayed on screen -> stretched, black bars,...
        gameviewport = new ExtendViewport(MY_WORLD_WIDTH,MY_WORLD_HEIGHT,playercam);

        batch = new SpriteBatch();
        hud = new Hud(batch, this);
        hud.setHud_scoreboard(currentGameObject.getGameStatistic().getPlayers());

        shaprenderer = new ShapeRenderer();

        //0 mit 49 später ersätzen
        camPos = new Vector2(49f*144f+(128f/2f),49f*144f+(128f/2f));
        playercam.position.set(camPos.x,camPos.y,1);
        playercam.update();

        //setting up gesture detector
        gestureDetecor = new GestureDetector(this);
        //Gdx.input.setInputProcessor(gestureDetecor); //replaced with InputMultiplexer for HUD and Gamescreen Inputs

        camPanGesture = new Vector2();

        //loading all gameCard Textures
        CardTextures.getInstance();

        //error Texture for Testing
        errorTextur = new Texture("testTexture.jpg");

        //setting up playable area / map
        myMap = new GameMapManager(playercam, gameviewport, batch, currentGameObject.getGameMap(), true, currentGameObject.getGameStatistic().getPlayers());

        playedCard_x = 0;
        playedCard_y = 0;

        //for showcase of functionality, placed starter card.GameCardFactory.D()

        at.aau.se2.gamma.core.models.impl.GameMapEntry starterEntry = new GameMapEntry(GameCardFactory.D(), null, Orientation.NORTH);
        //myMap.setGamecard(49,49, new GameCard(CardTextures.getTextureFromCardID(starterEntry.getCard().getCardId()), new Vector2(49f*144f,49f*144f), 270f, starterEntry));

        //set currentCard

        //at.aau.se2.gamma.core.models.impl.GameMapEntry newCardFromDeck = new GameMapEntry(CardDeck.get((int)(Math.random()*20)), myPlayerID, Orientation.NORTH);
        //currentGameCard = new GameCard(CardTextures.getTextureFromCardID(newCardFromDeck.getCard().getCardId()), new Vector2(0,0),270f,newCardFromDeck);

        //hud.setNextCardTexture(currentGameCard.getGameCardTexture());

        hud.changeHudState(Hud.Hud_State.VIEWING);


        InputMultiplexer im = new InputMultiplexer(hud.getStage(), gestureDetecor);
        Gdx.input.setInputProcessor(im);

        hud.getAcceptButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hud.setNextCardTexture(null);
                Soldier mySoldier = null;
                for(Player p : currentGameObject.getGameStatistic().getPlayers()){
                    if(p.getId().equals(myPlayerID.getId())){
                        mySoldier = p.getFreeSoldier();
                        break;
                    }

                }
                if(mySoldier != null){
                    hud.changeHudState(Hud.Hud_State.PLACING_SOLDIER);
                }else {
                    hud.changeHudState(Hud.Hud_State.VIEWING);

                    GameMove gm = new GameMove(myPlayerID, lastCard.getGameMapEntry(), new GameMapEntryPosition(playedCard_x, playedCard_y));

                    ServerThread.instance.sendCommand(new GameTurnCommand(gm), new ServerThread.RequestResponseHandler() {
                        @Override
                        public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                            String responseString = (String) payload;
                            Log.i("LauncherGame", responseString);
                            if(!responseString.equals("turn succesfull")){
                                hud.showErrorText(responseString);
                            }

                        }

                        @Override
                        public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

                        }
                    });
                }

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

        hud.getAcceptButton_Soldiers().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hud.changeHudState(Hud.Hud_State.VIEWING);
                GameMove gm = new GameMove(myPlayerID, lastCard.getGameMapEntry(), new GameMapEntryPosition(playedCard_x, playedCard_y));
                gm.getGameMapEntry().getSoldierPlacements().get(0).setSoldier(lastCard.getGameMapEntry().getSoldierPlacements().get(0).getSoldier());
                gm.getGameMapEntry().getSoldierPlacements().get(0).getSoldier().setX(playedCard_x);
                gm.getGameMapEntry().getSoldierPlacements().get(0).getSoldier().setY(playedCard_y);
                gm.setSoldierData(new SoldierData(gm.getGameMapEntry().getSoldierPlacements().get(0).getSoldier()));
                ServerThread.instance.sendCommand(new GameTurnCommand(gm), new ServerThread.RequestResponseHandler() {
                    @Override
                    public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                        String responseString = (String) payload;
                        Log.i("LauncherGame", responseString);
                        if(!responseString.equals("turn succesfull")){
                            hud.showErrorText(responseString);
                        }

                    }

                    @Override
                    public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

                    }
                });

                lastCard = null;
                //HERE send GameMove with gameMapEntry etc. later

            }
        });

        hud.getDeclineButton_Soldiers().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hud.changeHudState(Hud.Hud_State.PLACING_SOLDIER);
                for(SoldierPlacement sp : lastCard.getGameMapEntry().getSoldierPlacements()){
                    sp.getSoldier().setSoldierPlacement(null);
                }

                //clears the SoldierPlacements Array to remove placed Soldiers
                // Works ONLY for new Card with single Soldier
                lastCard.getGameMapEntry().getSoldierPlacements().clear();

            }
        });

        hud.getNoSoldierButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hud.changeHudState(Hud.Hud_State.VIEWING);
                //HERE send GameMove with gameMapEntry etc. later
                //When no Soldier has been placed
                GameMove gm = new GameMove(myPlayerID, lastCard.getGameMapEntry(), new GameMapEntryPosition(playedCard_x, playedCard_y));
                ServerThread.instance.sendCommand(new GameTurnCommand(gm), new ServerThread.RequestResponseHandler() {
                    @Override
                    public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                        String responseString = (String) payload;
                        Log.i("LauncherGame", responseString);
                        if(!responseString.equals("turn succesfull")){
                            hud.showErrorText(responseString);
                        }

                    }

                    @Override
                    public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

                    }
                });

            }
        });


        //temp zeugs für testing
        //camPos = new Vector2(0f*144f,0f*144f);
        //myMap.setGamecard(3,1, new GameCard(getTextureFromCardID(starterCard.getCardId()), new Vector2(3f*144f,1f*144f), starterCard));

        ServerThread.instance.setBroadcastHandler(myBroadCastHandler);

        //play button click listener
        hud.getPlay_button().addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //hud.getPlay_button().setChecked(true);
                super.clicked(event, x, y);
                //hud.getButtonGroup().setChecked("PLAY");
                if(!(hud.getCurrentState().equals(Hud.Hud_State.PLAYING)||hud.getCurrentState().equals(Hud.Hud_State.VIEWING))){
                    if(myTurn){
                        hud.changeHudState(Hud.Hud_State.PLAYING);
                    }else {
                        hud.changeHudState(Hud.Hud_State.VIEWING);
                    }
                }

            }
        });

        //play button click listener
        hud.getReport_button().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //hud.getButtonGroup().setChecked("REPORT");
                if(hud.getCurrentState().equals(Hud.Hud_State.ACCEPT_ACTION)||
                        hud.getCurrentState().equals(Hud.Hud_State.ACCEPT_PLACING_SOLDIER) ||
                        hud.getCurrentState().equals(Hud.Hud_State.PLACING_SOLDIER)){
                    currentGameCard = lastCard;
                    hud.setNextCardTexture(currentGameCard.getGameCardTexture());
                    currentGameCard.getGameMapEntry().getSoldierPlacements().clear();
                    lastCard = null;
                    myMap.setGamecard(playedCard_x, playedCard_y, null);
                }
                hud.changeHudState(Hud.Hud_State.REPORTING);

                hud.showErrorText("Select the suspicious soldier!");


            }
        });

        //cheat button click listener
        hud.getCheat_button().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //hud.getButtonGroup().setChecked("CHEAT");
                if(hud.getCurrentState().equals(Hud.Hud_State.ACCEPT_ACTION)||
                        hud.getCurrentState().equals(Hud.Hud_State.ACCEPT_PLACING_SOLDIER) ||
                        hud.getCurrentState().equals(Hud.Hud_State.PLACING_SOLDIER)){
                    currentGameCard = lastCard;
                    hud.setNextCardTexture(currentGameCard.getGameCardTexture());
                    currentGameCard.getGameMapEntry().getSoldierPlacements().clear();
                    lastCard = null;
                    myMap.setGamecard(playedCard_x, playedCard_y, null);
                }
                hud.changeHudState(Hud.Hud_State.CHEATING);

                hud.showErrorText("Select soldier to cheat with!");
            }
        });

        //cheat button click listener
        hud.getStat_button().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //hud.getButtonGroup().setChecked("STATS");
                if(hud.getCurrentState().equals(Hud.Hud_State.ACCEPT_ACTION)||
                        hud.getCurrentState().equals(Hud.Hud_State.ACCEPT_PLACING_SOLDIER) ||
                        hud.getCurrentState().equals(Hud.Hud_State.PLACING_SOLDIER)){
                    currentGameCard = lastCard;
                    hud.setNextCardTexture(currentGameCard.getGameCardTexture());
                    currentGameCard.getGameMapEntry().getSoldierPlacements().clear();
                    lastCard = null;
                    myMap.setGamecard(playedCard_x, playedCard_y, null);
                }
                hud.changeHudState(Hud.Hud_State.SCOREBOARD);
            }
        });

        last_x = -1f;
        last_y = -1f;
        last_z = -1f;

        hud.getCheatDeclineButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });


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
        myMap.draw(hud.getCurrentState());

        //printing fps and playercam position inside of the Gameworld
        //myfont.draw(batch, String.format("fps:%.2f | x pos: %f",(float)(1/delta), playercam.position.x), 0,0);

        batch.end();


        //used to draw simple shapes
        //shaprenderer.setProjectionMatrix(playercam.combined);
        //shaprenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shaprenderer.setColor(Color.CYAN);
        //shaprenderer.rect(5.0f, 5.0f, 200.0f, 200.0f);
        //shaprenderer.end();

        //String.format("fps:%.2f | x pos: %f |",(float)(1/delta), playercam.position.x)
        hud.drawStage();

        if(hud.getCurrentState().equals(Hud.Hud_State.ACCEPT_CHEATING)){
            boolean gyroscopeAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);

            if(gyroscopeAvail){
                float gyroX = Gdx.input.getGyroscopeX();
                float gyroY = Gdx.input.getGyroscopeY();
                float gyroZ = Gdx.input.getGyroscopeZ();


                float speed = Math.abs(gyroX+gyroY+gyroZ - last_x - last_y - last_z) / delta * 10000;
                //SHAKE_THRESHOLD
                if (speed != 1500000) {
                    Log.d("sensor", "shake detected w/ speed: " + speed);
                    hud.showErrorText(""+speed);
                    //do things after smartphone was shaken
                    ServerThread.instance.sendCommand(new CheatCommand(currentCheatMove.getData()), new ServerThread.RequestResponseHandler() {
                                @Override
                                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                                    String responseString = (String) payload;
                                    Log.i("LauncherGame", responseString);
                                    //if(!responseString.equals("sucessfull")){
                                    hud.showErrorText(responseString);
                                    //}

                        }

                        @Override
                        public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

                        }
                    });

                    hud.changeHudState(Hud.Hud_State.CHEATING);

                }
                last_x = gyroX;
                last_y = gyroY;
                last_z = gyroZ;

            }else {
                hud.showErrorText("BRUHHH NO Gyroscope YIKES");
            }
        }

        Gdx.input.setCatchBackKey(true);

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            Log.d("GameScreen: Render", "isKeyPressed");
            ServerThread.instance.sendCommand(new LeaveGameCommand(null), new ServerThread.RequestResponseHandler() {
                @Override
                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                    dispose();
                    androidInterface.makeToast("You left the game!");
                    Log.d("UserName", userName);
                    Log.d("UserID", userID);
                    androidInterface.startMainActivity();
                }

                @Override
                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                    Log.d("LeaveGameCommand", "onFailure");
                    androidInterface.makeToast("Something went wrong!");
                }
            });
        }
    }


    @Override
    public void resize(int width, int height) {
        gameviewport.update(width, height);

    }

    @Override
    public void dispose() {
        myfont.dispose();
        batch.dispose();
        CardTextures.disposeTextures();
        hud.dispose();
        myMap.dispose();
        UISkin.disposeSkin();
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
        }else if(lastCard != null &&hud.getCurrentState().equals(Hud.Hud_State.PLACING_SOLDIER)){
            Vector2 mapPos = InputCalculations.touch_to_GameWorld_coordinates(x, y, playercam, gameviewport, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            Vector2 oldPos = lastCard.getPosition();

            //change lenght when churches are implemented/ no idea how my teammates planned them right now
            float[] allDistances = new float[5];

            Vector2 point_left = new Vector2(oldPos.x, oldPos.y+64);
            Vector2 point_right = new Vector2(oldPos.x+128, oldPos.y+64);
            Vector2 point_top = new Vector2(oldPos.x+64, oldPos.y+128);
            Vector2 point_bottom = new Vector2(oldPos.x+64, oldPos.y);
            Vector2 point_middle = new Vector2(oldPos.x+64, oldPos.y+64);

            allDistances[0] = distance(mapPos, point_left);
            allDistances[1] = distance(mapPos, point_right);
            allDistances[2] = distance(mapPos, point_top);
            allDistances[3] = distance(mapPos, point_bottom);
             allDistances[4] = distance(mapPos, point_middle);
             if(!(lastCard.getGameMapEntry().getCard().getSideMid() != null && lastCard.getGameMapEntry().getCard().getSideMid().getType().equals(GameCardSide.Type.MONASTERY))){
               allDistances[4] = Float.MAX_VALUE;
             }

            Soldier mySoldier = null;
            for(Player p : currentGameObject.getGameStatistic().getPlayers()){
                if(p.getId().equals(myPlayerID.getId())){
                    mySoldier = p.getFreeSoldier();
                    break;
                }

            }
            if(mySoldier != null){
                mySoldier.setX((int) mapPos.x / 144);
                mySoldier.setY((int) mapPos.y / 144);

                //getting smallest distance
                int smallestIndex = 0;
                for(int i = 0; i<allDistances.length; i++){
                    if(allDistances[i]<allDistances[smallestIndex]){
                        smallestIndex = i;
                    }
                }

                switch (smallestIndex){
                    case 0:
                        lastCard.getGameMapEntry().setSoldier(mySoldier, lastCard.getGameMapEntry().getAlignedCardSides()[3]);
                        break;
                    case 1:
                        lastCard.getGameMapEntry().setSoldier(mySoldier, lastCard.getGameMapEntry().getAlignedCardSides()[1]);
                        break;
                    case 2:
                        lastCard.getGameMapEntry().setSoldier(mySoldier, lastCard.getGameMapEntry().getAlignedCardSides()[0]);
                        break;
                    case 3:
                        lastCard.getGameMapEntry().setSoldier(mySoldier, lastCard.getGameMapEntry().getAlignedCardSides()[2]);
                        break;
                    case 4:
                        lastCard.getGameMapEntry().setSoldier(mySoldier,lastCard.getGameMapEntry().getCard().getSideMid());
                        break;
                }
            }

            hud.changeHudState(Hud.Hud_State.ACCEPT_PLACING_SOLDIER);

        } else if(hud.getCurrentState().equals(Hud.Hud_State.REPORTING)){
            Vector2 mapPos = InputCalculations.touch_to_GameWorld_coordinates(x, y, playercam, gameviewport, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            touchedSoldier = myMap.touchedSoldier(mapPos.x, mapPos.y);

            if(touchedSoldier != null){
                Log.i("ReportedPlayer", "Touched player");
                Log.i("ReportedPlayer", "myPlayerID: "+myPlayerID.getId()+" | touchedSoldier PlayerID: "+touchedSoldier.getPlayer().getId());
                Log.i("ReportedPlayer", touchedSoldier.getX()+"|"+touchedSoldier.getY());
                //if(!(myPlayerID.getId().equals(touchedSoldier.getPlayer().getId()))){
                    //Log.i("ReportedPlayer", "myPlayerID: "+myPlayerID.getId()+" | touchedSoldier PlayerID: "+touchedSoldier.getPlayer().getId());
                    hud.changeHudState(Hud.Hud_State.ACCEPT_REPORTING);
                    hud.getReportAcceptButton().addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            hud.changeHudState(Hud.Hud_State.REPORTING);
                            Log.i("REPORT", touchedSoldier.getX()+"|"+touchedSoldier.getY());
                            ServerThread.instance.sendCommand(new DetectCheatCommand(touchedSoldier.getData()), new ServerThread.RequestResponseHandler() {
                                @Override
                                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                                    String responseString = (String) payload;
                                    Log.i("ReportedPlayer", responseString);
                                    hud.showErrorText(responseString);
                                }

                                @Override
                                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

                                }
                            });
                        }
                    });
                    hud.getReportDeclineButton().addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            hud.changeHudState(Hud.Hud_State.REPORTING);
                        }
                    });
                //}
            }


        }else if(hud.getCurrentState().equals(Hud.Hud_State.CHEATING)){

            Vector2 mapPos = InputCalculations.touch_to_GameWorld_coordinates(x, y, playercam, gameviewport, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            selectedCheatingSoldier = myMap.touchedSoldierGetALL(mapPos.x, mapPos.y);

            if(selectedCheatingSoldier != null){
                Log.i("ReportedPlayer", "Touched Soldier");
                Log.i("ReportedPlayer", "myPlayerID: "+myPlayerID.getId()+" | touchedSoldier PlayerID: "+selectedCheatingSoldier.getSoldier().getPlayer().getId());
                hud.changeHudState(Hud.Hud_State.CHEATING_DIRECTION);
                hud.showErrorText("Tap the direction you want to move the soldier");
            }

        }else if(hud.getCurrentState().equals(Hud.Hud_State.CHEATING_DIRECTION)){

            Vector2 mapPos = InputCalculations.touch_to_GameWorld_coordinates(x, y, playercam, gameviewport, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            //change lenght when churches are implemented/ no idea how my teammates planned them right now
            float[] allDistances = new float[5];

            Vector2 point_left = new Vector2(selectedCheatingSoldier.getGamecard().getPosition().x, selectedCheatingSoldier.getGamecard().getPosition().y+64);
            Vector2 point_right = new Vector2(selectedCheatingSoldier.getGamecard().getPosition().x+128, selectedCheatingSoldier.getGamecard().getPosition().y+64);
            Vector2 point_top = new Vector2(selectedCheatingSoldier.getGamecard().getPosition().x+64, selectedCheatingSoldier.getGamecard().getPosition().y+128);
            Vector2 point_bottom = new Vector2(selectedCheatingSoldier.getGamecard().getPosition().x+64, selectedCheatingSoldier.getGamecard().getPosition().y);
            Vector2 point_middle = new Vector2(selectedCheatingSoldier.getGamecard().getPosition().x+64, selectedCheatingSoldier.getGamecard().getPosition().y+64);

            allDistances[0] = distance(mapPos, point_left);
            allDistances[1] = distance(mapPos, point_right);
            allDistances[2] = distance(mapPos, point_top);
            allDistances[3] = distance(mapPos, point_bottom);
            allDistances[4] = distance(mapPos, point_middle);


            if(selectedCheatingSoldier.getGamecard().getGameMapEntry().getAlignedCardSides()[0].UID == selectedCheatingSoldier.getSoldier().getSoldierPlacement().getGameCardSide().UID){
                allDistances[2] = Float.MAX_VALUE;
            }else if(selectedCheatingSoldier.getGamecard().getGameMapEntry().getAlignedCardSides()[1].UID == selectedCheatingSoldier.getSoldier().getSoldierPlacement().getGameCardSide().UID){
                allDistances[1] = Float.MAX_VALUE;
            }else if(selectedCheatingSoldier.getGamecard().getGameMapEntry().getAlignedCardSides()[2].UID == selectedCheatingSoldier.getSoldier().getSoldierPlacement().getGameCardSide().UID){
                allDistances[3] = Float.MAX_VALUE;
            }else if(selectedCheatingSoldier.getGamecard().getGameMapEntry().getAlignedCardSides()[3].UID == selectedCheatingSoldier.getSoldier().getSoldierPlacement().getGameCardSide().UID){
                allDistances[0] = Float.MAX_VALUE;
            }
            if((selectedCheatingSoldier.getGamecard().getGameMapEntry().getCard().getSideMid() != null && selectedCheatingSoldier.getGamecard().getGameMapEntry().getCard().getSideMid().UID == selectedCheatingSoldier.getSoldier().getSoldierPlacement().getGameCardSide().UID) || selectedCheatingSoldier.getGamecard().getGameMapEntry().getCard().getSideMid() == null){
                allDistances[4] = Float.MAX_VALUE;
            }

            //getting smallest distance
            int smallestIndex = 0;
            for(int i = 0; i<allDistances.length; i++){
                if(allDistances[i]<allDistances[smallestIndex]){
                    smallestIndex = i;
                }
            }

            switch (smallestIndex){
                case 0:
                    currentCheatMove = new CheatMove(myPlayerID, selectedCheatingSoldier.getSoldier());
                    currentCheatMove.setOriginalPosition(currentCheatMove.getSoldier().getSoldierPlacement());
                    currentCheatMove.setNewPosition(new SoldierPlacement(selectedCheatingSoldier.getSoldier(),selectedCheatingSoldier.getGamecard().getGameMapEntry().getAlignedCardSides()[3]));
                    break;
                case 1:
                    currentCheatMove = new CheatMove(myPlayerID, selectedCheatingSoldier.getSoldier());
                    currentCheatMove.setOriginalPosition(currentCheatMove.getSoldier().getSoldierPlacement());
                    currentCheatMove.setNewPosition(new SoldierPlacement(selectedCheatingSoldier.getSoldier(),selectedCheatingSoldier.getGamecard().getGameMapEntry().getAlignedCardSides()[1]));
                    break;
                case 2:
                    currentCheatMove = new CheatMove(myPlayerID, selectedCheatingSoldier.getSoldier());
                    currentCheatMove.setOriginalPosition(currentCheatMove.getSoldier().getSoldierPlacement());
                    currentCheatMove.setNewPosition(new SoldierPlacement(selectedCheatingSoldier.getSoldier(),selectedCheatingSoldier.getGamecard().getGameMapEntry().getAlignedCardSides()[0]));
                    break;
                case 3:
                    currentCheatMove = new CheatMove(myPlayerID, selectedCheatingSoldier.getSoldier());
                    currentCheatMove.setOriginalPosition(currentCheatMove.getSoldier().getSoldierPlacement());
                    currentCheatMove.setNewPosition(new SoldierPlacement(selectedCheatingSoldier.getSoldier(),selectedCheatingSoldier.getGamecard().getGameMapEntry().getAlignedCardSides()[2]));
                    break;
                case 4:
                    currentCheatMove = new CheatMove(myPlayerID, selectedCheatingSoldier.getSoldier());
                    currentCheatMove.setOriginalPosition(currentCheatMove.getSoldier().getSoldierPlacement());
                    currentCheatMove.setNewPosition(new SoldierPlacement(selectedCheatingSoldier.getSoldier(),selectedCheatingSoldier.getGamecard().getGameMapEntry().getCard().getSideMid()));
                    break;
            }


            hud.changeHudState(Hud.Hud_State.ACCEPT_CHEATING);

        }

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
        //MathUtils.lerp() sounds interestingHud.Hud_State.CHEATING
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

    //calulate distance between 2 points with the Pythagorean theorem
    private float distance(Vector2 object1, Vector2 object2){
        return (float)Math.sqrt(Math.pow((object2.x - object1.x), 2) + Math.pow((object2.y - object1.y), 2));
    }

    @Override
    public void resume() {
        super.resume();
        Log.i("Launcher", "HELLO Broadcast test");
        ServerThread.instance.setBroadcastHandler(myBroadCastHandler);
    }

    ServerThread.BroadcastHandler myBroadCastHandler = new ServerThread.BroadcastHandler() {
        @Override
        public void onBroadcastResponse(ServerResponse response, Object payload) {
            if(response.getPayload() instanceof StringBroadcastCommand){
                Log.i("LauncherGame", "alles");
                myTurn = false;
            }else
            if(response.getPayload() instanceof GameTurnBroadCastCommand){
                myTurn = false;
                //nach einen zug
                //currentGameObject = (GameObject) payload;
                GameMove gm = (GameMove) payload;
                gm.setSoldierData(gm.getSoldierData());
                if(gm.getSoldierData()==null){
                    gm.changeToServerInstance(new ConcurrentLinkedDeque<>(currentGameObject.getGameStatistic().getPlayers()), currentGameObject.getGameMap());

                }else{
                    gm.changeToServerInstance(new ConcurrentLinkedDeque<>(currentGameObject.getGameStatistic().getPlayers()), currentGameObject.getGameMap(),gm.getSoldierData());

                }

                try {
                    currentGameObject.getGameMap().executeGameMove(gm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myMap.setGameMap(currentGameObject.getGameMap());
                Log.i("LauncherGame", "Updated Map");
            }else if(response.getPayload() instanceof PlayerXsTurnBroadcastCommand){
                //wenn jemand anderes am zug ist
                Log.i("LauncherGame", "jemand anderes ist nun an der Reihe");

                if(hud.getCurrentState().equals(Hud.Hud_State.ACCEPT_ACTION)){
                    hud.changeHudState(Hud.Hud_State.VIEWING);
                    currentGameCard = lastCard;
                    lastCard = null;
                    myMap.setGamecard(playedCard_x, playedCard_y, null);
                }
                if(!(hud.getCurrentState().equals(Hud.Hud_State.SCOREBOARD)||hud.getCurrentState().equals(Hud.Hud_State.CHEATING)||hud.getCurrentState().equals(Hud.Hud_State.REPORTING))){
                    hud.changeHudState(Hud.Hud_State.VIEWING);
                }

                currentGameCard = null;
                hud.setNextCardTexture(null);
                hud.showInfoText("It's "+ (String)payload +"'s turn!");
                hud.setMyTurn(false);
                myTurn = false;

            } else if(response.getPayload() instanceof YourTurnBroadcastCommand){

                Log.i("LauncherGame", "Spieler ist nun an der Reihe");
                at.aau.se2.gamma.core.models.impl.GameCard gm = (at.aau.se2.gamma.core.models.impl.GameCard) payload;
                GameMapEntry gme = new GameMapEntry(gm, myPlayerID);
                currentGameCard = new GameCard(GameCardTextures.getInstance().getTextureFromCardID(gm.getCardId()), new Vector2(0,0), gme);
                hud.setNextCardTexture(currentGameCard.getGameCardTexture());

                if(hud.getCurrentState().equals(Hud.Hud_State.VIEWING)){
                    hud.changeHudState(Hud.Hud_State.PLAYING);
                }

                Log.i("LauncherGame", "Spieler ist nun an der Reihe mit dieser karte:  "+ currentGameCard.getGameMapEntry().getCard().getCardId());
                hud.showInfoText("It's Your turn!");
                hud.setMyTurn(true);
                myTurn = true;

                //at.aau.se2.gamma.core.models.impl.GameMapEntry newCardFromDeck = new GameMapEntry(CardDeck.get((int)(Math.random()*20)), myPlayerID, Orientation.NORTH);
                //currentGameCard = new GameCard(CardTextures.getTextureFromCardID(newCardFromDeck.getCard().getCardId()), new Vector2(0,0),270f,newCardFromDeck);

                //hud.setNextCardTexture(currentGameCard.getGameCardTexture());
            }else if(response.getPayload() instanceof FieldCompletedBroadcastCommand){
                currentGameObject.setGameStatistic((GameStatistic) payload);
                hud.setHud_scoreboard(currentGameObject.getGameStatistic().getPlayers());
            }else if(response.getPayload() instanceof CheatMoveBroadcastCommand){
                 CheatMove cheatMove=CheatMove.getMoveFromData((CheatData) payload,currentGameObject);
                Log.i("Reported", "GOT RESPONCE FROM SERVER for CHEATING");
                try {


                    currentGameObject.getGameMap().executeCheatMove(cheatMove);
                    myMap.setGameMap(currentGameObject.getGameMap());
                } catch (CheatMoveImpossibleException e) {
                    e.printStackTrace();
                }
            }else if(response.getPayload() instanceof CheatMoveDetectedBroadcastCommand){
                Log.i("Reported", "GOT RESPONCE FROM SERVER for CHEATING");
                LinkedList<CheatData>cheatData=(LinkedList<CheatData>) payload;
                LinkedList<CheatMove>moves=new LinkedList<>();
                for (int i = 0; i <cheatData.size(); i++) {
                    moves.add(CheatMove.getMoveFromData(cheatData.get(i),currentGameObject));

                }

                currentGameObject.getGameMap().undoCheatMove(moves);
                myMap.setGameMap(currentGameObject.getGameMap());
            }

        }

        @Override
        public void onBroadcastFailure(ServerResponse response, Object payload) {

        }
    };
}

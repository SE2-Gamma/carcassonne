package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Map;

import at.aau.se2.gamma.core.models.impl.GameCardSide;
import at.aau.se2.gamma.core.models.impl.GameMap;
import at.aau.se2.gamma.core.models.impl.Orientation;
import at.aau.se2.gamma.core.models.impl.SoldierPlacement;

public class GameMapManager {

    private Camera playercam;
    private Viewport gameviewport;
    private SpriteBatch batch;
    private ShapeRenderer shaperender;
    private GameCard[][] Playingfield;
    private int MapSize;
    private GameMap currentGameMap;
    private SoldierTextures solTextures;


    public GameMapManager(OrthographicCamera playercam, Viewport gameviewport, SpriteBatch batch, GameMap initialGameMap) {
        //Texture errorTexture = new Texture("testTexture.jpg");
        this.playercam = playercam;
        this.gameviewport = gameviewport;
        this.batch = batch;
        Playingfield = new GameCard[100][100];
        MapSize = 100;

        currentGameMap = initialGameMap;
        solTextures = new SoldierTextures();

        setGameMap(currentGameMap);



        //shaperender= new ShapeRenderer();

        //Texture errortexture = new Texture("testTexture.jpg");

        //setting all coordinates of all possible fields
        //  for(int i = 0; i<Playingfield.length; i++){
        //  for(int j=0; j<Playingfield[i].length; j++){
        //texture bitte noch richtig machen ###########################################
        //  Playingfield[i][j] = new GameCard(errortexture, new Vector2(i*144f, j*144f));
        // }
        // }
    }

    //public void setGameCard


    public void draw() {

        for (int i = 0; i < Playingfield.length; i++) {
            for (int j = 0; j < Playingfield[i].length; j++) {
                if (Playingfield[i][j] != null && Playingfield[i][j].getGameCardTexture() != null) {
                    if (Playingfield[i][j].isVisible((OrthographicCamera) playercam)) {
                        batch.draw(Playingfield[i][j].getGameCardTexture(),
                                Playingfield[i][j].getPosition().x,
                                Playingfield[i][j].getPosition().y,
                                Playingfield[i][j].getGameCardTexture().getWidth() / 2f,
                                Playingfield[i][j].getGameCardTexture().getHeight() / 2f,
                                Playingfield[i][j].getGameCardTexture().getWidth(),
                                Playingfield[i][j].getGameCardTexture().getHeight(),
                                1,
                                1,
                                Playingfield[i][j].getRotation(),
                                0,
                                0,
                                Playingfield[i][j].getGameCardTexture().getWidth(),
                                Playingfield[i][j].getGameCardTexture().getHeight(),
                                false,
                                false);
                        ArrayList<SoldierPlacement> cardSoldiers = Playingfield[i][j].getGameMapEntry().getSoldierPlacements();
                        GameCardSide cardSides[] = Playingfield[i][j].getGameMapEntry().getAlignedCardSides();
                        for(SoldierPlacement sp : cardSoldiers){
                            for(int s = 0; s<cardSides.length; s++){
                                if(cardSides[s].equals(sp.getGameCardSide())){
                                    switch (s){
                                        case 0:
                                            batch.draw(solTextures.searchSoldierTexture(Playingfield[i][j].getGameMapEntry().getPlacedByPlayer().getId()), Playingfield[i][j].getPosition().x+48, Playingfield[i][j].getPosition().y+96);
                                            break;
                                        case 1:
                                            batch.draw(solTextures.searchSoldierTexture(Playingfield[i][j].getGameMapEntry().getPlacedByPlayer().getId()), Playingfield[i][j].getPosition().x+96, Playingfield[i][j].getPosition().y+48);
                                            break;
                                        case 2:
                                            batch.draw(solTextures.searchSoldierTexture(Playingfield[i][j].getGameMapEntry().getPlacedByPlayer().getId()), Playingfield[i][j].getPosition().x+48, Playingfield[i][j].getPosition().y);
                                            break;
                                        case 3:
                                            batch.draw(solTextures.searchSoldierTexture(Playingfield[i][j].getGameMapEntry().getPlacedByPlayer().getId()), Playingfield[i][j].getPosition().x, Playingfield[i][j].getPosition().y+48);
                                            break;
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    //return true if card set, false if no card set
    public boolean setGamecard(Vector2 position, GameCard card) {
        int lastGameArrayNumber = MapSize-1;
        if ((position.x > 0 && position.y > 0)) {
            int x = (int) position.x / 144;
            int y = (int) position.y / 144;

            if(x > lastGameArrayNumber || y > lastGameArrayNumber){
                return false;
            }

            boolean CardConnectable = true;

            if (Playingfield[x][y] == null || Playingfield[x][y].getGameCardTexture() == null) {
                //checking upper y limit
                if(y == lastGameArrayNumber){
                    //left side of upper y Limit
                    if(x == 0){
                        if(Playingfield[x+1][y] != null  || Playingfield[x][y-1] != null){

                            if(Playingfield[x+1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x+1][y].getGameMapEntry(), Orientation.WEST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[x][y-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y-1].getGameMapEntry(), Orientation.NORTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(x * 144f, y * 144f));
                                Playingfield[x][y] = card;
                                return true;
                            }


                        }else return false;

                     //right side of upper y limit
                    }else if (x == lastGameArrayNumber){
                        if(Playingfield[x-1][y] != null || Playingfield[x][y-1] != null){
                            if(Playingfield[x-1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x-1][y].getGameMapEntry(), Orientation.EAST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[x][y-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y-1].getGameMapEntry(), Orientation.NORTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(x * 144f, y * 144f));
                                Playingfield[x][y] = card;
                                return true;
                            }
                        }else return false;
                        //between upper and lower x limit; y = maximum
                    }else {
                        if(Playingfield[x-1][y] != null || Playingfield[x+1][y] != null || Playingfield[x][y-1] != null){
                            if(Playingfield[x+1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x+1][y].getGameMapEntry(), Orientation.WEST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[x-1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x-1][y].getGameMapEntry(), Orientation.EAST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[x][y-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y-1].getGameMapEntry(), Orientation.NORTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(x * 144f, y * 144f));
                                Playingfield[x][y] = card;
                                return true;
                            }
                        }else return false;
                    }
                }else if(y == 0){
                    if(x == 0){
                        if(Playingfield[x+1][y] != null || Playingfield[x][y+1] != null){
                            if(Playingfield[x+1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x+1][y].getGameMapEntry(), Orientation.WEST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[x][y+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y+1].getGameMapEntry(), Orientation.SOUTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(x * 144f, y * 144f));
                                Playingfield[x][y] = card;
                                return true;
                            }
                        }else return false;
                    }else if (x == lastGameArrayNumber){
                        if(Playingfield[x-1][y] != null || Playingfield[x][y+1] != null){
                            if(Playingfield[x-1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x-1][y].getGameMapEntry(), Orientation.EAST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[x][y+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y+1].getGameMapEntry(), Orientation.SOUTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(x * 144f, y * 144f));
                                Playingfield[x][y] = card;
                                return true;
                            }
                        }else return false;
                    }else {
                        if(Playingfield[x+1][y] != null || Playingfield[x-1][y] != null || Playingfield[x][y+1] != null){
                            if(Playingfield[x+1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x+1][y].getGameMapEntry(), Orientation.WEST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[x-1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x-1][y].getGameMapEntry(), Orientation.EAST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[x][y+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y+1].getGameMapEntry(), Orientation.SOUTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(x * 144f, y * 144f));
                                Playingfield[x][y] = card;
                                return true;
                            }
                        }else return false;
                    }
                }else if(x == 0){
                    if(Playingfield[x+1][y] != null || Playingfield[x][y-1] != null || Playingfield[x][y+1] != null){
                        if(Playingfield[x+1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x+1][y].getGameMapEntry(), Orientation.WEST) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[x][y+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y+1].getGameMapEntry(), Orientation.SOUTH) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[x][y-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y-1].getGameMapEntry(), Orientation.NORTH) == false){
                            CardConnectable = false;
                        }
                        if(CardConnectable){
                            card.setPosition(new Vector2(x * 144f, y * 144f));
                            Playingfield[x][y] = card;
                            return true;
                        }
                    }else return false;
                }else if(x == lastGameArrayNumber){
                    if(Playingfield[x-1][y] != null || Playingfield[x][y-1] != null || Playingfield[x][y+1] != null){
                        if(Playingfield[x-1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x-1][y].getGameMapEntry(), Orientation.EAST) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[x][y+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y+1].getGameMapEntry(), Orientation.SOUTH) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[x][y-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y-1].getGameMapEntry(), Orientation.NORTH) == false){
                            CardConnectable = false;
                        }
                        if(CardConnectable){
                            card.setPosition(new Vector2(x * 144f, y * 144f));
                            Playingfield[x][y] = card;
                            return true;
                        }
                    }else return false;
                }else {
                    if(Playingfield[x+1][y] != null || Playingfield[x-1][y] != null || Playingfield[x][y-1] != null || Playingfield[x][y+1] != null){
                        if(Playingfield[x+1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x+1][y].getGameMapEntry(), Orientation.WEST) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[x-1][y] != null && card.getGameMapEntry().canConnectTo(Playingfield[x-1][y].getGameMapEntry(), Orientation.EAST) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[x][y+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y+1].getGameMapEntry(), Orientation.SOUTH) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[x][y-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[x][y-1].getGameMapEntry(), Orientation.NORTH) == false){
                            CardConnectable = false;
                        }
                        if(CardConnectable){
                            card.setPosition(new Vector2(x * 144f, y * 144f));
                            Playingfield[x][y] = card;
                            return true;
                        }
                    }else return false;
                }
            }
        }
        return false;


        //Vector2 mapPos = new Vector2(0,0);
        //mapPos.x = position.x/

    }

    public void setGamecard(int x, int y, GameCard card) {
        Playingfield[x][y] = card;
    }

    public void setGameMap(GameMap newGameMap){
        solTextures.resetSolierTextureOrder();
        currentGameMap = newGameMap;
        GameCardTextures test = GameCardTextures.getInstance();
        GameCardTextures CardTextures = GameCardTextures.getInstance();
        for(int i = 0; i<100; i++){
            for(int j = 0; j<100; j++){
                if(newGameMap.getMapArray()[i][j] != null){
                    Playingfield[i][j] = new GameCard(CardTextures.getTextureFromCardID(newGameMap.getMapArray()[i][j].getCard().getCardId()), new Vector2(144f*i, 144f*j), newGameMap.getMapArray()[i][j]);
                }else {
                    Playingfield[i][j] = null;
                }

            }
        }
    }

    public void dispose() {
        //batch.dispose();
        // shaperender.dispose();
         solTextures.disposeTexutres();
    }


}

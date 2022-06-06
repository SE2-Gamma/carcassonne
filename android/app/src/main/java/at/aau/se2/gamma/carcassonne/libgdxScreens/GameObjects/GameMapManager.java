package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import at.aau.se2.gamma.core.models.impl.GameCardSide;
import at.aau.se2.gamma.core.models.impl.GameMap;
import at.aau.se2.gamma.core.models.impl.Orientation;
import at.aau.se2.gamma.core.models.impl.Player;
import at.aau.se2.gamma.core.models.impl.Soldier;
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
    private boolean drawSoldiers;


    public GameMapManager(OrthographicCamera playercam, Viewport gameviewport, SpriteBatch batch, GameMap initialGameMap, boolean drawSoldiers) {
        this.drawSoldiers = drawSoldiers;
        //Texture errorTexture = new Texture("testTexture.jpg");
        this.playercam = playercam;
        this.gameviewport = gameviewport;
        this.batch = batch;
        Playingfield = new GameCard[100][100];
        MapSize = 100;

        currentGameMap = initialGameMap;
        //for Unit Tests, because i have no idea how to Mock all the opengl calls
        if(drawSoldiers){
            solTextures = new SoldierTextures();
        }
        setGameMap(currentGameMap);

    }

    //public void setGameCard


    public void draw(Hud.Hud_State currentHudState) {

        for (int j = 0; j < Playingfield.length; j++) {
            for (int i = 0; i < Playingfield[j].length; i++) {
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
                        if(drawSoldiers){
                            ArrayList<SoldierPlacement> cardSoldiers = Playingfield[i][j].getGameMapEntry().getSoldierPlacements();
                            GameCardSide cardSides[] = Playingfield[i][j].getGameMapEntry().getAlignedCardSides();
                            for(SoldierPlacement sp : cardSoldiers){
                                for(int s = 0; s<cardSides.length; s++){
                                    if(cardSides[s].equals(sp.getGameCardSide())){
                                        //Texture currentSoldierTexture = solTextures.searchSoldierTexture(Playingfield[i][j].getGameMapEntry().getPlacedByPlayer().getId());
                                        Texture currentSoldierTexture = solTextures.searchSoldierTexture(sp.getSoldier().getPlayer().getId());
                                        switch (s){
                                            case 0:
                                                batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x+48, Playingfield[i][j].getPosition().y+96);
                                                break;
                                            case 1:
                                                batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x+96, Playingfield[i][j].getPosition().y+48);
                                                break;
                                            case 2:
                                                batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x+48, Playingfield[i][j].getPosition().y);
                                                break;
                                            case 3:
                                                batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x, Playingfield[i][j].getPosition().y+48);
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
    }

    //return true if card set, false if no card set
    public boolean setGamecard(Vector2 position, GameCard card) {
        int lastGameArrayNumber = MapSize-1;
        if ((position.x >= 0 && position.y >= 0)) {
            int col = (int) position.x / 144;
            int row = (int) position.y / 144;

            if(row > lastGameArrayNumber || col > lastGameArrayNumber){
                return false;
            }

            boolean CardConnectable = true;

            if (Playingfield[row][col] == null || Playingfield[row][col].getGameCardTexture() == null) {
                //checking upper y limit
                if(row == lastGameArrayNumber){
                    //left side of upper y Limit
                    if(col == 0){
                        if(Playingfield[row-1][col] != null  || Playingfield[row][col+1] != null){

                            if(Playingfield[row][col+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col+1].getGameMapEntry(), Orientation.WEST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[row-1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row-1][col].getGameMapEntry(), Orientation.NORTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(col * 144f, row * 144f));
                                Playingfield[row][col] = card;
                                return true;
                            }


                        }else return false;

                     //right side of upper y limit
                    }else if (col == lastGameArrayNumber){
                        if(Playingfield[row][col-1] != null || Playingfield[row-1][col] != null){
                            if(Playingfield[row][col-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col-1].getGameMapEntry(), Orientation.EAST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[row-1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row-1][col].getGameMapEntry(), Orientation.NORTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(col * 144f, row * 144f));
                                Playingfield[row][col] = card;
                                return true;
                            }
                        }else return false;
                        //between upper and lower x limit; y = maximum
                    }else {
                        if(Playingfield[row][col-1] != null || Playingfield[row][col+1] != null || Playingfield[row-1][col] != null){
                            if(Playingfield[row][col+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col+1].getGameMapEntry(), Orientation.WEST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[row][col-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col-1].getGameMapEntry(), Orientation.EAST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[row-1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row-1][col].getGameMapEntry(), Orientation.NORTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(col * 144f, row * 144f));
                                Playingfield[row][col] = card;
                                return true;
                            }
                        }else return false;
                    }
                }else if(row == 0){
                    if(col == 0){
                        if(Playingfield[row][col+1] != null || Playingfield[row+1][col] != null){
                            if(Playingfield[row][col+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col+1].getGameMapEntry(), Orientation.WEST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[row+1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row+1][col].getGameMapEntry(), Orientation.SOUTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(col * 144f, row * 144f));
                                Playingfield[row][col] = card;
                                return true;
                            }
                        }else return false;
                    }else if (col == lastGameArrayNumber){
                        if(Playingfield[row][col-1] != null || Playingfield[row+1][col] != null){
                            if(Playingfield[row][col-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col-1].getGameMapEntry(), Orientation.EAST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[row+1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row+1][col].getGameMapEntry(), Orientation.SOUTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(col * 144f, row * 144f));
                                Playingfield[row][col] = card;
                                return true;
                            }
                        }else return false;
                    }else {
                        if(Playingfield[row][col+1] != null || Playingfield[row][col-1] != null || Playingfield[row+1][col] != null){
                            if(Playingfield[row][col+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col+1].getGameMapEntry(), Orientation.WEST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[row][col-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col-1].getGameMapEntry(), Orientation.EAST) == false){
                                CardConnectable = false;
                            }
                            if(Playingfield[row+1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row+1][col].getGameMapEntry(), Orientation.SOUTH) == false){
                                CardConnectable = false;
                            }
                            if(CardConnectable){
                                card.setPosition(new Vector2(col * 144f, row * 144f));
                                Playingfield[row][col] = card;
                                return true;
                            }
                        }else return false;
                    }
                }else if(col == 0){
                    if(Playingfield[row][col+1] != null || Playingfield[row-1][col] != null || Playingfield[row+1][col] != null){
                        if(Playingfield[row][col+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col+1].getGameMapEntry(), Orientation.WEST) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[row+1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row+1][col].getGameMapEntry(), Orientation.SOUTH) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[row-1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row-1][col].getGameMapEntry(), Orientation.NORTH) == false){
                            CardConnectable = false;
                        }
                        if(CardConnectable){
                            card.setPosition(new Vector2(col * 144f, row * 144f));
                            Playingfield[row][col] = card;
                            return true;
                        }
                    }else return false;
                }else if(col == lastGameArrayNumber){
                    if(Playingfield[row][col-1] != null || Playingfield[row-1][col] != null || Playingfield[row+1][col] != null){
                        if(Playingfield[row][col-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col-1].getGameMapEntry(), Orientation.EAST) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[row+1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row+1][col].getGameMapEntry(), Orientation.SOUTH) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[row-1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row-1][col].getGameMapEntry(), Orientation.NORTH) == false){
                            CardConnectable = false;
                        }
                        if(CardConnectable){
                            card.setPosition(new Vector2(col * 144f, row * 144f));
                            Playingfield[row][col] = card;
                            return true;
                        }
                    }else return false;
                }else {
                    if(Playingfield[row][col+1] != null || Playingfield[row][col-1] != null || Playingfield[row-1][col] != null || Playingfield[row+1][col] != null){
                        if(Playingfield[row][col+1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col+1].getGameMapEntry(), Orientation.WEST) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[row][col-1] != null && card.getGameMapEntry().canConnectTo(Playingfield[row][col-1].getGameMapEntry(), Orientation.EAST) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[row+1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row+1][col].getGameMapEntry(), Orientation.SOUTH) == false){
                            CardConnectable = false;
                        }
                        if(Playingfield[row-1][col] != null && card.getGameMapEntry().canConnectTo(Playingfield[row-1][col].getGameMapEntry(), Orientation.NORTH) == false){
                            CardConnectable = false;
                        }
                        if(CardConnectable){
                            card.setPosition(new Vector2(col * 144f, row * 144f));
                            Playingfield[row][col] = card;
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
        Playingfield[y][x] = card;
    }

    public void setGameMap(GameMap newGameMap){
       if(drawSoldiers){
           ArrayList<String> playerlist = new ArrayList<>();
           for (int j = 0; j < Playingfield.length; j++) {
               for (int i = 0; i < Playingfield[j].length; i++) {
                   if(Playingfield[j][i] != null && Playingfield[j][i].getGameMapEntry().getPlacedByPlayer() != null){
                       if(!playerlist.contains(Playingfield[j][i].getGameMapEntry().getPlacedByPlayer().getId())){
                           playerlist.add(Playingfield[j][i].getGameMapEntry().getPlacedByPlayer().getId());
                       }
                   }
               }
           }
           boolean reset = false;
           for(String p : playerlist){
               if(!solTextures.getPlayerList().contains(p)){
                   reset = true;
               }
           }

           if(playerlist.size() != solTextures.getNumberOfAddedPlayers() || reset){

               solTextures.resetSolierTextureOrder();
           }

       }
        currentGameMap = newGameMap;
        GameCardTextures CardTextures = null;
        if(drawSoldiers){
            CardTextures = GameCardTextures.getInstance();
        }

        for(int i = 0; i<100; i++){
            for(int j = 0; j<100; j++){
                if(newGameMap.getMapArray()[i][j] != null){
                    if(drawSoldiers){
                        Playingfield[i][j] = new GameCard(CardTextures.getTextureFromCardID(newGameMap.getMapArray()[i][j].getCard().getCardId()), new Vector2(144f*j, 144f*i), newGameMap.getMapArray()[i][j]);
                    }else {
                        //added for Testing, not used in real gameplay
                        Playingfield[i][j] = new GameCard(null, new Vector2(144f*j, 144f*i), newGameMap.getMapArray()[i][j]);
                    }

                }else {
                    Playingfield[i][j] = null;
                }

            }

        }
    }

    public Soldier touchedSoldier(float x, float y){
        for (int j = 0; j < Playingfield.length; j++) {
            for (int i = 0; i < Playingfield[j].length; i++) {
                if (Playingfield[i][j] != null && Playingfield[i][j].getGameCardTexture() != null) {
                    ArrayList<SoldierPlacement> cardSoldiers = Playingfield[i][j].getGameMapEntry().getSoldierPlacements();
                    GameCardSide cardSides[] = Playingfield[i][j].getGameMapEntry().getAlignedCardSides();
                    for (SoldierPlacement sp : cardSoldiers) {
                        for (int s = 0; s < cardSides.length; s++) {
                            if (cardSides[s].equals(sp.getGameCardSide())) {
                                switch (s) {
                                    case 0:
                                        if (checkRectangleCollisionWithPoint(Playingfield[i][j].getPosition().x + 48, Playingfield[i][j].getPosition().y + 96, 32, 32, x, y)) {
                                            return sp.getSoldier();
                                        }
                                        //batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x + 48, Playingfield[i][j].getPosition().y + 96);
                                        break;
                                    case 1:
                                        if (checkRectangleCollisionWithPoint(Playingfield[i][j].getPosition().x + 96, Playingfield[i][j].getPosition().y + 48, 32, 32, x, y)) {
                                            return sp.getSoldier();
                                        }
                                        //batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x + 96, Playingfield[i][j].getPosition().y + 48);
                                        break;
                                    case 2:
                                        if (checkRectangleCollisionWithPoint(Playingfield[i][j].getPosition().x + 48, Playingfield[i][j].getPosition().y, 32, 32, x, y)) {
                                            return sp.getSoldier();
                                        }
                                        //batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x + 48, Playingfield[i][j].getPosition().y);
                                        break;
                                    case 3:
                                        if (checkRectangleCollisionWithPoint(Playingfield[i][j].getPosition().x, Playingfield[i][j].getPosition().y + 48, 32, 32, x, y)) {
                                            return sp.getSoldier();
                                        }
                                        //batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x, Playingfield[i][j].getPosition().y + 48);
                                        break;
                                }

                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    //the thing is i have no idea right now how the cheat move is implemented on the server, so i am just passing everythign i think could be importand
    public CheatMoveSoldierPosition touchedSoldierGetALL(float x, float y){
        for (int j = 0; j < Playingfield.length; j++) {
            for (int i = 0; i < Playingfield[j].length; i++) {
                if (Playingfield[i][j] != null && Playingfield[i][j].getGameCardTexture() != null) {
                    ArrayList<SoldierPlacement> cardSoldiers = Playingfield[i][j].getGameMapEntry().getSoldierPlacements();
                    GameCardSide cardSides[] = Playingfield[i][j].getGameMapEntry().getAlignedCardSides();
                    for (SoldierPlacement sp : cardSoldiers) {
                        for (int s = 0; s < cardSides.length; s++) {
                            if (cardSides[s].equals(sp.getGameCardSide())) {
                                switch (s) {
                                    case 0:
                                        if (checkRectangleCollisionWithPoint(Playingfield[i][j].getPosition().x + 48, Playingfield[i][j].getPosition().y + 96, 32, 32, x, y)) {
                                            return new CheatMoveSoldierPosition(new Vector2(Playingfield[i][j].getPosition().x + 48,Playingfield[i][j].getPosition().y + 96), sp.getSoldier(), Playingfield[i][j]);
                                        }
                                        //batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x + 48, Playingfield[i][j].getPosition().y + 96);
                                        break;
                                    case 1:
                                        if (checkRectangleCollisionWithPoint(Playingfield[i][j].getPosition().x + 96, Playingfield[i][j].getPosition().y + 48, 32, 32, x, y)) {
                                            return new CheatMoveSoldierPosition(new Vector2(Playingfield[i][j].getPosition().x + 96,Playingfield[i][j].getPosition().y + 48), sp.getSoldier(), Playingfield[i][j]);
                                        }
                                        //batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x + 96, Playingfield[i][j].getPosition().y + 48);
                                        break;
                                    case 2:
                                        if (checkRectangleCollisionWithPoint(Playingfield[i][j].getPosition().x + 48, Playingfield[i][j].getPosition().y, 32, 32, x, y)) {
                                            return new CheatMoveSoldierPosition(new Vector2(Playingfield[i][j].getPosition().x + 48,Playingfield[i][j].getPosition().y), sp.getSoldier(), Playingfield[i][j]);
                                        }
                                        //batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x + 48, Playingfield[i][j].getPosition().y);
                                        break;
                                    case 3:
                                        if (checkRectangleCollisionWithPoint(Playingfield[i][j].getPosition().x, Playingfield[i][j].getPosition().y + 48, 32, 32, x, y)) {
                                            return new CheatMoveSoldierPosition(new Vector2(Playingfield[i][j].getPosition().x,Playingfield[i][j].getPosition().y +48), sp.getSoldier(), Playingfield[i][j]);
                                        }
                                        //batch.draw(currentSoldierTexture, Playingfield[i][j].getPosition().x, Playingfield[i][j].getPosition().y + 48);
                                        break;
                                }

                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void dispose() {
        //batch.dispose();
        // shaperender.dispose();
        if(drawSoldiers){
            solTextures.disposeTexutres();
        }

    }

    //Boi i have nooo idea how else i should do it and i got no time to check for functions in libgdx
    private boolean checkRectangleCollisionWithPoint(float BoxX,float BoxY, float BoxW, float BoxH, float PointX, float PointY){
        return ((BoxX <= PointX)&&
                (PointX <= BoxX+BoxW)&&
                (BoxY <= PointY)&&
                (PointY <= BoxY+BoxH));
    }


}

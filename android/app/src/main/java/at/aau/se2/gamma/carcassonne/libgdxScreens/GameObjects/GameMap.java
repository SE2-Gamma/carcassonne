package at.aau.se2.gamma.carcassonne.libgdxScreens.GameObjects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameMap {

    private Camera playercam;
    private Viewport gameviewport;
    private SpriteBatch batch;
    private ShapeRenderer shaperender;
    private GameCard [][] Playingfield;




    public GameMap(OrthographicCamera playercam, Viewport gameviewport, SpriteBatch batch){
        //Texture errorTexture = new Texture("testTexture.jpg");
        this.playercam = playercam;
        this.gameviewport = gameviewport;
        this.batch = batch;
        Playingfield = new GameCard[145][145];
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


    public void draw(){

        for(int i = 0; i<Playingfield.length; i++){
            for(int j=0; j<Playingfield[i].length; j++){
                if(Playingfield[i][j] != null && Playingfield[i][j].getGameCardTexture() != null){
                    if(Playingfield[i][j].isVisible((OrthographicCamera) playercam)){
                        batch.draw(Playingfield[i][j].getGameCardTexture(),
                                Playingfield[i][j].getPosition().x,
                                Playingfield[i][j].getPosition().y,
                                Playingfield[i][j].getGameCardTexture().getWidth()/2,
                                Playingfield[i][j].getGameCardTexture().getHeight()/2,
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
                    }
                }
            }
        }
    }

    //return true if card set, false if no card set
    public boolean setGamecard(Vector2 position, GameCard card){

        if((position.x > 0 && position.y > 0)){
            int x = (int)position.x / 144;
            int y = (int)position.y / 144;
            if(Playingfield[y][x] == null || Playingfield[y][x].getGameCardTexture() == null){
                card.setPosition(new Vector2(x*144f, y*144f));
                Playingfield[y][x] = card;
                return true;
            }

            //Log.e("info","------------------------------------------------- "+x+ " | "+y);
        }
        return false;


    //Vector2 mapPos = new Vector2(0,0);
    //mapPos.x = position.x/

    }

    public void setGamecard(int x, int y, GameCard card){
        Playingfield[x][y] = card;
    }

    public void dispose(){
        //batch.dispose();
       // shaperender.dispose();
    }



}

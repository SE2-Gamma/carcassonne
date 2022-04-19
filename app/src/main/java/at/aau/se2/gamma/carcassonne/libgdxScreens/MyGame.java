package at.aau.se2.gamma.carcassonne.libgdxScreens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import at.aau.se2.gamma.carcassonne.libgdxScreens.Screens.Gamescreen;

public class MyGame extends Game {
    public static MyGame INSTANCE;

    //später fixen auf gutes singelton, wenn alles funktioniert
    public MyGame(){
        INSTANCE = this;
    }

    @Override
    public void create () {
        setScreen(new Gamescreen());
    }




}

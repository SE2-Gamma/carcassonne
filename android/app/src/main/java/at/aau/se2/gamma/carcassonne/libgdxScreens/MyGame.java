package at.aau.se2.gamma.carcassonne.libgdxScreens;

import com.badlogic.gdx.Game;

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

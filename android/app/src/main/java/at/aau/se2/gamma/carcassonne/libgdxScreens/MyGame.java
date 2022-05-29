package at.aau.se2.gamma.carcassonne.libgdxScreens;

import com.badlogic.gdx.Game;

import at.aau.se2.gamma.carcassonne.AndroidInterface;
import at.aau.se2.gamma.carcassonne.libgdxScreens.Screens.Gamescreen;
import at.aau.se2.gamma.core.models.impl.GameObject;

public class MyGame extends Game {
    public static MyGame INSTANCE = null;
    public static String userName;
    public static String gameKey;
    public static String userID;
    private static GameObject initialGameObject;
    AndroidInterface androidInterface;


    //später fixen auf gutes singelton, wenn alles funktioniert
    public MyGame(String gameKey, String userName, String userID,  GameObject initialGameObject, AndroidInterface androidInterface){
        if(INSTANCE == null){
            INSTANCE = this;
            this.gameKey = gameKey;
            this.userName = userName;
            this.userID = userID;
            this.initialGameObject = initialGameObject;
            this.androidInterface = androidInterface;
        }
    }

    @Override
    public void create () {
        setScreen(new Gamescreen(gameKey, userName, userID, initialGameObject,androidInterface));
        androidInterface.makeToast("Game started!");
    }

}

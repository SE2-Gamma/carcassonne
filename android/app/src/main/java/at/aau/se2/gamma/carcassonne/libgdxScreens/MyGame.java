package at.aau.se2.gamma.carcassonne.libgdxScreens;

import android.util.Log;

import com.badlogic.gdx.Game;

import at.aau.se2.gamma.carcassonne.AndroidInterface;
import at.aau.se2.gamma.carcassonne.libgdxScreens.Screens.Gamescreen;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.LaunchSucceededCommand;
import at.aau.se2.gamma.core.models.impl.GameObject;

public class MyGame extends Game {
    public static MyGame INSTANCE = null;
    public static String userName;
    public static String gameKey;
    public static String userID;
    private static GameObject initialGameObject;
    public static AndroidInterface androidInterface;


    //später fixen auf gutes singelton, wenn alles funktioniert
    public MyGame(String gameKey, String userName, String userID,  GameObject initialGameObject, AndroidInterface androidInterface){

       // if(INSTANCE == null){

     

            INSTANCE = this;
            this.gameKey = gameKey;
            this.userName = userName;
            this.userID = userID;
            this.initialGameObject = initialGameObject;
            this.androidInterface = androidInterface;

      //  }

    }

    @Override
    public void create () {
        Log.d("MyGame: onCreate", gameKey);
        Log.d("MyGame: onCreate", userName);
        Log.d("MyGame: onCreate", userID);
        Log.d("MyGame: onCreate", androidInterface.toString());
        setScreen(new Gamescreen(gameKey, userName, userID, initialGameObject,androidInterface));
        androidInterface.makeToast("Game started!");
        notifyServerOfLaunch();
    }

    private void notifyServerOfLaunch() {
        ServerThread.instance.sendCommand(new LaunchSucceededCommand(null), new ServerThread.RequestResponseHandler() {
            @Override
            public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                Log.d("MyGame: onCreate", "Server notified of launch");
            }

            @Override
            public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                Log.d("MyGame: create", "Something went wrong");
            }
        });
    }
}

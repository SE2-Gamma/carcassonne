package at.aau.se2.gamma.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import at.aau.se2.gamma.carcassonne.libgdxScreens.MyGame;
import at.aau.se2.gamma.core.models.impl.GameObject;

public class Launcher extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        Log.i("LauncherGame", "trying to get intent");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String gameKey = extras.getString("GameKey");
        String userName = extras.getString("UserName");
        String userID = extras.getString("UserID");
        GameObject initialGameObject = (GameObject) extras.get("GameObject");
        Log.i("LauncherGame", gameKey+" | " +userName);
        initialize(new MyGame(gameKey, userName, userID,initialGameObject, new AndroidPlatform(this)), config);

    }

}

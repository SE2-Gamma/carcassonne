package at.aau.se2.gamma.carcassonne.views.lobby;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LobbyPlayerDisplay {


    String playerName;
    Button kickPlayer;

    public LobbyPlayerDisplay(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }


}

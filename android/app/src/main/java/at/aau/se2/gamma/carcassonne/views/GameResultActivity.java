package at.aau.se2.gamma.carcassonne.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyPlayerDisplay;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;

import at.aau.se2.gamma.carcassonne.databinding.ActivityGameResult2Binding;
import at.aau.se2.gamma.core.commands.RequestUserListCommand;

public class GameResultActivity extends BaseActivity {
    private ActivityGameResult2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameResult2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Intent intent= new Intent(GameResultActivity.this, MainActivity.class);

        //TODO adapt send command to Points request once created
        sendServerCommand(new RequestUserListCommand(null), new ServerThread.RequestResponseHandler() {
            @Override
            public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                LinkedList<String> players = (LinkedList<String>) payload;
                //TODO Calculate Ranking and set players in order

            }

            @Override
            public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

            }
        });
    }
}
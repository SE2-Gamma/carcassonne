package at.aau.se2.gamma.carcassonne.views.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;

import at.aau.se2.gamma.carcassonne.Launcher;
import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.databinding.ActivityLobbyBinding;

public class LobbyActivity extends BaseActivity {
    public ActivityLobbyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLobbyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Get/Update Player List From Server
        List<LobbyPlayerDisplay> playerList;
        playerList = Arrays.asList(
                new LobbyPlayerDisplay("player1"),
                new LobbyPlayerDisplay("player2"),
                new LobbyPlayerDisplay("player3"),
                new LobbyPlayerDisplay("player4"),
                new LobbyPlayerDisplay("player5")
        );

        //Disconnect from lobby
        binding.btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(LobbyActivity.this, MainActivity.class));
            }
        });

        binding.btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LobbyActivity.this, "Game started", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LobbyActivity.this, Launcher.class));
            }
        });

        binding.tvPlayerCount.setText(getResources().getString(R.string.player_count) + " " + playerList.size() + "/5");

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(playerList, LobbyActivity.this);
        binding.rvLobby.setAdapter(adapter);
        binding.rvLobby.setLayoutManager(new LinearLayoutManager(this));
    }
}
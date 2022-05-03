package at.aau.se2.gamma.carcassonne.views.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import at.aau.se2.gamma.carcassonne.Launcher;
import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityLobbyBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.PayloadResponseCommand;
import at.aau.se2.gamma.core.commands.RequestUserListCommand;

public class LobbyActivity extends BaseActivity {
    public ActivityLobbyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "LobbyActivity");
        super.onCreate(savedInstanceState);
        binding = ActivityLobbyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Get Player List from Server
        sendServerCommand(new RequestUserListCommand(null), new ServerThread.RequestResponseHandler() {
            @Override
            public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                Log.d("Server Response", "LobbyActivity");
                List<LobbyPlayerDisplay> playerList = new LinkedList<>();

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(playerList, LobbyActivity.this);
                binding.rvLobby.setAdapter(adapter);
                binding.rvLobby.setLayoutManager(new LinearLayoutManager(LobbyActivity.this));

                //PayloadResponseCommand temp = (PayloadResponseCommand) payload;
                LinkedList<String> players = (LinkedList<String>) payload;
                for (String player:players
                     ) {
                    Log.d("LobbyActivity", player);
                }
                for(int i = 0; i < players.size(); i++) {
                    playerList.add(new LobbyPlayerDisplay(players.get(i)));
                }
                adapter.notifyDataSetChanged();
                binding.tvPlayerCount.setText(getResources().getString(R.string.player_count) + " " + playerList.size() + "/5");
                Intent intent = getIntent();
                String gameKey = intent.getStringExtra("GameKey");
                binding.tvLobbyName.setText(gameKey);
            }
            @Override
            public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                Log.d("onFailure", "LobbyActivity");
                Toast.makeText(LobbyActivity.this, "Error loading players", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LobbyActivity.this, MainActivity.class));
            }
        });

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





    }
}
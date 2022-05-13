package at.aau.se2.gamma.carcassonne.views.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.LinkedList;

import at.aau.se2.gamma.carcassonne.Launcher;
import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityLobbyBinding;
import at.aau.se2.gamma.carcassonne.exceptions.NoServerInstanceException;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.KickAttemptBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.PlayerJoinedBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.PlayerKickedBroadcastCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommands.PlayerLeftLobbyBroadcastCommand;
import at.aau.se2.gamma.core.commands.KickPlayerCommand;
import at.aau.se2.gamma.core.commands.RequestUserListCommand;
import at.aau.se2.gamma.core.utils.KickOffer;

public class LobbyActivity extends BaseActivity implements RecyclerViewAdapter.RecyclerViewListener {
    public ActivityLobbyBinding binding;
    private LinkedList<LobbyPlayerDisplay> playerList;
    private RecyclerViewAdapter adapter;

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
                Log.d("Server Response", "LobbyActivity initial requestUserList");
                playerList = new LinkedList<>();

                adapter = new RecyclerViewAdapter(playerList, LobbyActivity.this, LobbyActivity.this);
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
                Toast.makeText(LobbyActivity.this, "Error requesting player list", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LobbyActivity.this, MainActivity.class));
            }
        });

        try {
            getServerThreadOrFail().setBroadcastHandler(new ServerThread.BroadcastHandler() {
                @Override
                public void onBroadcastResponse(ServerResponse response, Object payload) {
                    Log.d("Debug", "BroadCastResponseHandlerReached");
                    if(response.getPayload() instanceof KickAttemptBroadcastCommand) {
                        KickOffer kickOffer = (KickOffer) payload;
                        String playerName = kickOffer.getPlayer().getName();
                        //open Dialog for each Player
                        LobbyActivityDialog lobbyActivityDialog = new LobbyActivityDialog(playerName);
                        lobbyActivityDialog.show(getSupportFragmentManager(), "LobbyActivityDialog");
                    } else if(response.getPayload() instanceof PlayerJoinedBroadcastCommand) {
                        Log.d("Broadcast Response", "PlayerJoinedBroadcastCommand");
                        updatePlayerList();
                    } else if (response.getPayload() instanceof PlayerKickedBroadcastCommand) {
                        updatePlayerList();
                    } else if(response.getPayload() instanceof PlayerLeftLobbyBroadcastCommand) {
                        updatePlayerList();
                    }
                }

                @Override
                public void onBroadcastFailure(ServerResponse response, Object payload) {
                    Log.d("Debug", "Something went wrong: BroadcastFailure");
                }
            });
        } catch (NoServerInstanceException e) {
            e.printStackTrace();
        }




        //TODO: Implement disconnect from lobby
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onKickPlayerButtonClick(int position) {
        String playerName = playerList.get(position).getPlayerName();

        ServerThread.instance.sendCommand(new KickPlayerCommand(playerName), new ServerThread.RequestResponseHandler() {
            @Override
            public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                Toast.makeText(LobbyActivity.this, "Kick initialized", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                Toast.makeText(LobbyActivity.this, "Kick attemp failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updatePlayerList() {
        try{
            sendServerCommand(new RequestUserListCommand(null), new ServerThread.RequestResponseHandler() {
                @Override
                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                    Log.d("Server Response", "LobbyActivity updatePlayerList");
                    LinkedList<String> players = (LinkedList<String>) payload;
                    for (String player:players
                    ) {
                        Log.d("LobbyActivity update", player);
                    }
                    playerList.add(new LobbyPlayerDisplay(players.getLast()));
                    adapter.notifyItemInserted(players.size()-1);
                    binding.tvPlayerCount.setText(getResources().getString(R.string.player_count) + " " + playerList.size() + "/5");
                }
                @Override
                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                    Log.d("onFailure", "LobbyActivity");
                    Toast.makeText(LobbyActivity.this, "Error requesting player list", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LobbyActivity.this, MainActivity.class));
                }
            });
        } catch (ClassCastException e) {
            Toast.makeText(this, "You were kicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LobbyActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}
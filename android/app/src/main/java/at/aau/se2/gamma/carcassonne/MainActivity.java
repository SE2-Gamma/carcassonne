package at.aau.se2.gamma.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityMainBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.CreateSessionActivity;
import at.aau.se2.gamma.carcassonne.views.JoinSessionActivity;
import at.aau.se2.gamma.carcassonne.views.SelectNameActivity;
import at.aau.se2.gamma.carcassonne.views.UIElementsActivity;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.DisconnectCommand;
import at.aau.se2.gamma.core.commands.GetClientStateCommand;
import at.aau.se2.gamma.core.states.ClientState;

public class MainActivity extends BaseActivity implements ServerThread.BroadcastHandler {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        startService(new Intent(this, ShutdownService.class));

        String userName = getIntent().getStringExtra("UserName");
        String userID = getIntent().getStringExtra("UserID");

        binding.pbMenu.setVisibility((View.GONE));
        binding.btnBackToLobby.setVisibility(View.GONE);

        sendServerCommand(new GetClientStateCommand(null), new ServerThread.RequestResponseHandler() {
            @Override
            public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                if(payload.equals(ClientState.LOBBY)){
                    binding.btnBackToLobby.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

            }
        });

        binding.btnNavigateCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateSessionActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

        binding.btnNavigateJoinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoinSessionActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });

        binding.btnBackToLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LobbyActivity.class));
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                sendServerCommand(new DisconnectCommand(null), new ServerThread.RequestResponseHandler() {
                    @Override
                    public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                        Intent intent = new Intent(MainActivity.this, SelectNameActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                        Log.d("onFailure", "Something went wrong");
                    }
                });
            }
        };
        getOnBackPressedDispatcher().addCallback(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ServerThread.instance != null) {
            ServerThread.instance.setBroadcastHandler(this);
        }
        binding.btnBackToLobby.setVisibility(View.GONE);
        sendServerCommand(new GetClientStateCommand(null), new ServerThread.RequestResponseHandler() {
            @Override
            public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                if(payload.equals(ClientState.LOBBY)){
                    binding.btnBackToLobby.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

            }
        });
    }

    @Override
    public void onBroadcastResponse(ServerResponse response, Object payload) {
        Logger.debug("We have a broadcast message: "+payload);
        Logger.debug("type of broadcast message: "+response.getPayload().getClass().toString());
    }

    @Override
    public void onBroadcastFailure(ServerResponse response, Object payload) {

    }
}

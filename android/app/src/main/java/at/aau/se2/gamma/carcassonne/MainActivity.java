package at.aau.se2.gamma.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityMainBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.CreateSessionActivity;
import at.aau.se2.gamma.carcassonne.views.JoinSessionActivity;
import at.aau.se2.gamma.carcassonne.views.UIElementsActivity;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;

public class MainActivity extends BaseActivity implements ServerThread.BroadcastHandler {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.pbMenu.setVisibility((View.GONE));
        binding.tvServerError.setVisibility((View.INVISIBLE));

        binding.btnNavigateCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateSessionActivity.class);
                startActivity(intent);
            }
        });

        binding.btnNavigateJoinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoinSessionActivity.class);
                startActivity(intent);
            }
        });

        binding.btnUiElements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UIElementsActivity.class));
            }
        });

        binding.btnGameplayTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Launcher.class));
            }
        });

        binding.btnViewLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LobbyActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ServerThread.instance != null) {
            ServerThread.instance.setBroadcastHandler(this);
        }
    }

    @Override
    public void onBroadcastResponse(ServerResponse response, Object payload) {
        Logger.debug("We have a broadcast message: "+payload);
    }

    @Override
    public void onBroadcastFailure(ServerResponse response, Object payload) {

    }
}

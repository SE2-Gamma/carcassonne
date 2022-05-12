package at.aau.se2.gamma.carcassonne.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityCreateSessionBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.CreateGameCommand;

public class CreateSessionActivity extends BaseActivity {

    public ActivityCreateSessionBinding binding;
    String sessionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonNavigateLobby.setVisibility(View.INVISIBLE);
        binding.textViewError.setVisibility(View.INVISIBLE);
        binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);

        binding.buttonCreateSession.setOnClickListener(this::createSession);
        binding.buttonNavigateLobby.setOnClickListener(this::navigateToLobby);
    }

    public void createSession(View view) {
        binding.progressBarJoinSessionActivity.setVisibility(View.VISIBLE);
        sessionName = binding.editTextSessionname.getText().toString();
        if(sessionName.length()>0) {
            CreateSessionActivity.this.sendServerCommand(new CreateGameCommand(sessionName), new ServerThread.RequestResponseHandler() {
                @Override
                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                    binding.textViewError.setVisibility(View.VISIBLE);
                    binding.textViewError.setText("Session created!");
                    binding.textViewError.setTextColor(Color.BLACK);
                    binding.buttonNavigateLobby.setVisibility(View.VISIBLE);
                    binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
                    binding.buttonCreateSession.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                    binding.textViewError.setVisibility(View.VISIBLE);
                    binding.textViewError.setText("Error");
                    binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
                }
            });
        }else {
            binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
            binding.textViewError.setVisibility(View.VISIBLE);
            binding.textViewError.setText("Choose a name for your game!");
        }
    }

    public void navigateToLobby(View view) {
        Intent intent = new Intent(CreateSessionActivity.this, LobbyActivity.class);
        intent.putExtra("GameKey", sessionName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
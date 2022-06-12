package at.aau.se2.gamma.carcassonne.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.LinkedList;

import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityCreateSessionBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.CreateGameCommand;

public class CreateSessionActivity extends BaseActivity {

    public ActivityCreateSessionBinding binding;
    String sessionName = "";
    String userName = "";
    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userName = getIntent().getStringExtra("UserName");
        userID = getIntent().getStringExtra("UserID");

        binding.textViewError.setVisibility(View.INVISIBLE);
        binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);

        binding.buttonCreateSession.setOnClickListener(this::createSession);
    }

    public void createSession(View view) {
        binding.progressBarJoinSessionActivity.setVisibility(View.VISIBLE);
        sessionName = binding.editTextSessionname.getText().toString();

        if(sessionName.length()>0) {
            CreateSessionActivity.this.sendServerCommand(new CreateGameCommand(sessionName), new ServerThread.RequestResponseHandler() {

                @Override
                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                    Logger.debug("onResponsePayload: " + payload.toString());

                    binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
                    binding.buttonCreateSession.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(CreateSessionActivity.this, LobbyActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("GameKey", sessionName);
                    extras.putString("UserName", userName);
                    extras.putString("UserID", userID);
                    intent.putExtras(extras);
                    startActivity(intent);
                }

                @Override
                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                    Logger.debug("onFailurePayload: " + payload.toString());

                    binding.textViewError.setText(((LinkedList<String>)payload).get(0));
                    binding.textViewError.setVisibility(View.VISIBLE);
                    binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
                }

            });
          
        }else {
            binding.textViewError.setText("Choose a name for your game!");
            binding.textViewError.setVisibility(View.VISIBLE);
            binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
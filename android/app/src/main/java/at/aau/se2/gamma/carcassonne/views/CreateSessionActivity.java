package at.aau.se2.gamma.carcassonne.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityCreateSessionBinding;
import at.aau.se2.gamma.carcassonne.network.SendThread;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.CreateGameCommand;

public class CreateSessionActivity extends BaseActivity {

    public ActivityCreateSessionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String sessionName = "";
        String userName = getIntent().getStringExtra("UserName");

        binding.buttonNavigateLobby.setVisibility(View.INVISIBLE);
        binding.textViewError.setVisibility(View.INVISIBLE);
        binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);

        binding.buttonCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBarJoinSessionActivity.setVisibility(View.VISIBLE);
                String sessionName = binding.editTextSessionname.getText().toString();
                if (sessionName.length() > 0) {
                    new SendThread(new CreateGameCommand(sessionName), new ServerThread.RequestResponseHandler() {
                        @Override
                        public void onResponse(ServerResponse response, Object payload, BaseCommand request) {

                            Intent intent = new Intent(CreateSessionActivity.this, LobbyActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("GameKey", sessionName);
                            extras.putString("UserName", userName);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                            binding.textViewError.setText("Error");
                            binding.textViewError.setVisibility(View.VISIBLE);
                            binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
                        }
                    }).start();
                }
            }
        });

        binding.buttonNavigateLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

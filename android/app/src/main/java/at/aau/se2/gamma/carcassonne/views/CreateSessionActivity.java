package at.aau.se2.gamma.carcassonne.views;

import at.aau.se2.gamma.carcassonne.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityCreateSessionBinding;
import at.aau.se2.gamma.carcassonne.databinding.ActivityMainBinding;
import at.aau.se2.gamma.carcassonne.network.SendThread;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.CreateGameCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;

public class CreateSessionActivity extends BaseActivity {

    public ActivityCreateSessionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String sessionName = "";

        binding.buttonNavigateLobby.setVisibility(View.INVISIBLE);
        binding.textViewError.setVisibility(View.INVISIBLE);
        binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);

        binding.buttonCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBarJoinSessionActivity.setVisibility(View.VISIBLE);
                String sessionName = binding.editTextSessionname.getText().toString();
                if(sessionName.length()>0) {
                    Log.d("SOUT", "if");
                    CreateSessionActivity.this.sendServerCommand(new CreateGameCommand(sessionName), new ServerThread.RequestResponseHandler() {
                        @Override
                        public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                            Log.d("SOUT", "onResponse-----------------------------------");
                            binding.buttonNavigateLobby.setVisibility(View.VISIBLE);
                            binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);

                            Intent intent = new Intent(CreateSessionActivity.this, SelectNameActivity.class);
                            intent.putExtra("GameKey", sessionName);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                            Log.d("SOUT", "onFailure++++++++++++++++++++++++++++++++++");
                            binding.textViewError.setText("Error");
                            binding.textViewError.setVisibility(View.VISIBLE);
                            binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
                        }
                    });
                    /*new SendThread(new CreateGameCommand(sessionName), new ServerThread.RequestResponseHandler() {
                        @Override
                        public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                            Log.d("server-com", "response vom server: " + response.toString());
                            binding.buttonNavigateLobby.setVisibility(View.VISIBLE);
                            binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);

                            Intent intent = new Intent(CreateSessionActivity.this, SelectNameActivity.class);
                            intent.putExtra("GameKey", sessionName);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                            binding.textViewError.setText("Error");
                            binding.textViewError.setVisibility(View.VISIBLE);
                            binding.progressBarJoinSessionActivity.setVisibility(View.INVISIBLE);
                        }
                    }).start();*/
                }
            }
        });

        binding.buttonNavigateLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateSessionActivity.this, LobbyActivity.class);
                intent.putExtra("GameKey", sessionName);
                startActivity(intent);
            }
        });
    }
}
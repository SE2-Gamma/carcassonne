package at.aau.se2.gamma.carcassonne.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.UtilityKlasse;
import at.aau.se2.gamma.carcassonne.databinding.ActivitySelectNameBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;

public class SelectNameActivity extends AppCompatActivity {

    private ActivitySelectNameBinding binding;
    String filename = "nameFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_name);

        binding = ActivitySelectNameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.ptUserName.setText(UtilityKlasse.getSavedData(getBaseContext(),filename));
        binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);

        binding.btnNameSelectEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = binding.ptUserName.getText().toString();

                UtilityKlasse.saveData(getBaseContext(),filename,userInput);

                startActivity(new Intent(SelectNameActivity.this, LobbyActivity.class));

                if(userInput.length()>0) {
                    binding.pbSelectNameActivity.setVisibility(View.VISIBLE);
                    binding.tvError.setVisibility(View.INVISIBLE);

                    ServerThread.instance.sendCommand(new InitialSetNameCommand(userInput), new ServerThread.RequestResponseHandler() {
                        @Override
                        public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                            if(false) {
                                Intent intent = new Intent(SelectNameActivity.this, LobbyActivity.class);
                                startActivity(intent);
                            }else{
                                binding.tvError.setText("Key nicht existent, bitte überprüfe ihn und versuche es erneut!");
                                binding.tvError.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                            binding.tvError.setText("Keine Antwort vom Server erhalten");
                            binding.tvError.setVisibility(View.VISIBLE);
                        }
                    });

                    binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                }else{
                    binding.tvError.setText("Bitte gib einen Game Key ein!");
                    binding.tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
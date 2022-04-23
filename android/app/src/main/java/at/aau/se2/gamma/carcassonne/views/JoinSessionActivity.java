package at.aau.se2.gamma.carcassonne.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.databinding.ActivityJoinSessionBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.SelectNameActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;

public class JoinSessionActivity extends AppCompatActivity {

    private ActivityJoinSessionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.pbJoinSessionActivity.setVisibility(View.INVISIBLE);
        binding.tvError.setVisibility(View.INVISIBLE);


        binding.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.pbJoinSessionActivity.setVisibility(View.VISIBLE);

                String userInput = binding.ptInputKey.getText().toString();
                Log.d("Check","User input:" + userInput);

                if(userInput.length()>0) {
                    binding.pbJoinSessionActivity.setVisibility(View.VISIBLE);
                    binding.tvError.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(JoinSessionActivity.this, SelectNameActivity.class);
                    intent.putExtra("GameKey", userInput);
                    startActivity(intent);
                    ServerThread.instance.sendCommand(new InitialJoinCommand(userInput), new ServerThread.RequestResponseHandler() {
                        @Override
                        public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                            if(false) {
                                Intent intent = new Intent(JoinSessionActivity.this, SelectNameActivity.class);
                                intent.putExtra("GameKey", userInput);
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
                    binding.pbJoinSessionActivity.setVisibility(View.INVISIBLE);
                }else{
                    binding.tvError.setText("Bitte gib einen Game Key ein!");
                    binding.tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
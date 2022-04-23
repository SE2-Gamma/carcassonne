package at.aau.se2.gamma.carcassonne.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.databinding.ActivityJoinSessionBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.views.SelectNameActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
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

        ServerThread serverThread = (ServerThread) getIntent().getSerializableExtra("ServerConnection");

        binding.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.pbJoinSessionActivity.setVisibility(View.VISIBLE);

                String userInput = binding.ptInputKey.getText().toString();
                Log.d("Check","User input:" + userInput);

                if(userInput.length()>0) {
                    binding.pbJoinSessionActivity.setVisibility(View.VISIBLE);
                    binding.tvError.setVisibility(View.INVISIBLE);

                    serverThread.sendCommand(new InitialSetNameCommand(userInput), new ServerThread.RequestResponseHandler() {
                        @Override
                        public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                            startActivity(new Intent(JoinSessionActivity.this, SelectNameActivity.class));
                        }

                        @Override
                        public void onFailure(ServerResponse response, Object payload, BaseCommand request) {

                        }
                    });

                    binding.pbJoinSessionActivity.setVisibility(View.INVISIBLE);
                }else{
                    binding.tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
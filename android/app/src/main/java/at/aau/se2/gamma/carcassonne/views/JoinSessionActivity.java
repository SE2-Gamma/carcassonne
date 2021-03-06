package at.aau.se2.gamma.carcassonne.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivityJoinSessionBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;

public class JoinSessionActivity extends BaseActivity {

    private ActivityJoinSessionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String userName = getIntent().getStringExtra("UserName");
        String userID = getIntent().getStringExtra("UserID");
        binding.pbJoinSessionActivity.setVisibility(View.INVISIBLE);
        binding.tvError.setVisibility(View.INVISIBLE);


        binding.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.btnEnter.setEnabled(false);
                binding.pbJoinSessionActivity.setVisibility(View.INVISIBLE);

                String userInput = binding.ptInputKey.getText().toString();
                Log.d("Check","User input:" + userInput);


                if(userInput.length()>0) {
                    binding.pbJoinSessionActivity.setVisibility(View.VISIBLE);
                    binding.tvError.setVisibility(View.INVISIBLE);
                    sendServerCommand(new InitialJoinCommand(userInput), new ServerThread.RequestResponseHandler() {
                        @Override
                        public void onResponse(ServerResponse response, Object payload, BaseCommand request) {

                                Log.d("Com", response.toString());
                                Intent intent = new Intent(JoinSessionActivity.this, LobbyActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("GameKey", userInput);
                                extras.putString("UserName", userName);
                                extras.putString("UserID", userID);
                                intent.putExtras(extras);
                                startActivity(intent);
                                binding.btnEnter.setEnabled(true);
                        }

                        @Override
                        public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                            binding.tvError.setText(((LinkedList<String>)payload).get(0));
                            binding.tvError.setVisibility(View.VISIBLE);
                            binding.btnEnter.setEnabled(true);
                        }
                    });
                    binding.pbJoinSessionActivity.setVisibility(View.INVISIBLE);
                }else{
                    binding.tvError.setText("Please enter a valid Game-Key!");
                    binding.tvError.setVisibility(View.VISIBLE);
                    binding.btnEnter.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
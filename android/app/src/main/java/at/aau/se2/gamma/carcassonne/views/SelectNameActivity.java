package at.aau.se2.gamma.carcassonne.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

import at.aau.se2.gamma.carcassonne.Launcher;
import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.UtilityKlasse;
import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.databinding.ActivitySelectNameBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.utils.GlobalVariables;

public class SelectNameActivity extends BaseActivity {

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
        binding.tvError.setVisibility(View.INVISIBLE);

        binding.btnNameSelectEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnNameSelectEnter.setEnabled(false);

                binding.pbSelectNameActivity.setVisibility(View.VISIBLE);
                String userInput = binding.ptUserName.getText().toString();

                UtilityKlasse.saveData(getBaseContext(),filename,userInput);

                Log.d("Strlength",Integer.toString(userInput.length()));
                if(userInput.length()>0) {
                    binding.pbSelectNameActivity.setVisibility(View.VISIBLE);
                    binding.tvError.setVisibility(View.INVISIBLE);

                    ServerThread serverThread = ServerThread.init(GlobalVariables.getAdress(), GlobalVariables.getPort(), new ServerThread.ConnectionHandler() {
                        @Override
                        public void onConnectionFinished() {
                            Logger.debug("HEY, RESPONSE :)");
                            //ServerThread.instance.setBroadcastHandler(SelectNameActivity.this);
                            sendServerCommand(new InitialSetNameCommand(userInput), new ServerThread.RequestResponseHandler() {
                                @Override
                                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                                    Log.d("Debug", "going to next site");
                                    binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(SelectNameActivity.this, MainActivity.class);
                                    intent.putExtra("UserName", userInput);
                                    intent.putExtra("UserID", (String)payload);
                                    startActivity(intent);
                                    binding.btnNameSelectEnter.setEnabled(true);
                                }

                                @Override
                                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                                    Log.d("Check","Response: "+payload.toString());
                                    binding.tvError.setText(((LinkedList<String>)payload).get(0));
                                    binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                                    binding.tvError.setVisibility(View.VISIBLE);
                                    binding.btnNameSelectEnter.setEnabled(true);
                                }
                            });
                        }
                        @Override
                        public void onServerFailure(Exception e) {
                            Logger.debug("NOOOOOO :(");
                            binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                            binding.btnNameSelectEnter.setEnabled(true);
                        }
                    });
                    serverThread.start();
                }else{
                    binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                    binding.tvError.setText("Please enter a name!");
                    binding.tvError.setVisibility(View.VISIBLE);
                    binding.btnNameSelectEnter.setEnabled(true);
                }
            }
        });

        binding.btnGameplayTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectNameActivity.this, Launcher.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
package at.aau.se2.gamma.carcassonne.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

import java.util.LinkedList;

import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.base.BaseActivity;

import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.UtilityKlasse;
import at.aau.se2.gamma.carcassonne.databinding.ActivitySelectNameBinding;
import at.aau.se2.gamma.carcassonne.network.SendThread;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
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
                            sendServerCommand(new InitialSetNameCommand(userInput), new ServerThread.RequestResponseHandler() {
                                @Override
                                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                                    Log.d("Debug", "going to next site");
                                    binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(SelectNameActivity.this, MainActivity.class);
                                    intent.putExtra("UserName", userInput);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                                    Log.d("Check","Response: "+payload.toString());
                                    binding.tvError.setText(((LinkedList<String>)payload).get(0));
                                    binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                                    binding.tvError.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        @Override
                        public void onServerFailure(Exception e) {
                            Logger.debug("NOOOOOO :(");
                            binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                        }
                    });
                    serverThread.start();
                }else{
                    binding.pbSelectNameActivity.setVisibility(View.INVISIBLE);
                    binding.tvError.setText("Bitte gib einen Namen ein!");
                    binding.tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
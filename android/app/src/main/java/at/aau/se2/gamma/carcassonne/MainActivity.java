package at.aau.se2.gamma.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import at.aau.se2.gamma.carcassonne.base.BaseActivity;

import at.aau.se2.gamma.carcassonne.databinding.ActivityMainBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.CreateSessionActivity;
import at.aau.se2.gamma.carcassonne.views.JoinSessionActivity;
import at.aau.se2.gamma.carcassonne.views.UIElementsActivity;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.utils.GlobalVariables;

public class MainActivity extends BaseActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tvServerError.setVisibility((View.INVISIBLE));

        ServerThread serverThread = ServerThread.init(GlobalVariables.getAdress(), GlobalVariables.getPort(), new ServerThread.ConnectionHandler() {
            @Override
            public void onConnectionFinished() {
                Logger.debug("Connection created");

                MainActivity.this.sendServerCommand(new InitialSetNameCommand("fgh"), new ServerThread.RequestResponseHandler() {
                    @Override
                    public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                        Logger.debug("HEY, RESPONSE :)");
                        binding.pbMenu.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                        Logger.debug("NOOOOOO :(");
                        binding.pbMenu.setVisibility(View.INVISIBLE);

                    }
                });
            }

            @Override
            public void onServerFailure(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.error("Error at server initial connection");
                        binding.tvServerError.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                });
            }
        });
        serverThread.start();


        binding.btnNavigateCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, CreateSessionActivity.class);
                startActivity(intent);

            }
        });

        binding.btnNavigateJoinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JoinSessionActivity.class);
                startActivity(intent);
            }
        });

        binding.btnUiElements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UIElementsActivity.class));
            }
        });

        binding.btnGameplayTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Launcher.class));
            }
        });

        binding.btnViewLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LobbyActivity.class));
            }
        });
    }
}

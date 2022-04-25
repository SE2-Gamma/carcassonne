package at.aau.se2.gamma.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.badlogic.gdx.utils.Json;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

import at.aau.se2.gamma.carcassonne.databinding.ActivityMainBinding;
import at.aau.se2.gamma.carcassonne.network.SendThread;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.CreateSessionActivity;
import at.aau.se2.gamma.carcassonne.views.JoinSessionActivity;
import at.aau.se2.gamma.carcassonne.views.UIElementsActivity;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.CreateGameCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.commands.KickPlayerCommand;
import at.aau.se2.gamma.core.commands.RequestUserListCommand;
import at.aau.se2.gamma.core.utils.globalVariables;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.tvServerError.setVisibility((View.INVISIBLE));

        ServerThread serverThread = ServerThread.init("192.168.178.27", 1234, new ServerThread.ConnectionHandler() {
            @Override
            public void onConnectionFinished() {
                Logger.debug("Connection created");
                ServerThread.instance.sendCommand(new InitialSetNameCommand("mrader"), new ServerThread.RequestResponseHandler() {
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
                Logger.error("Error at server initial connection");
                binding.tvServerError.setVisibility(View.VISIBLE);
                e.printStackTrace();
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

        ServerThread serverThread = ServerThread.init(globalVariables.getAdress(), 1234, new ServerThread.ConnectionHandler() {
            @Override
            public void onConnectionFinished() {
                Logger.debug("Connection created");
                ServerThread.instance.sendCommand(new InitialSetNameCommand("leona"), new ServerThread.RequestResponseHandler() {
                    @Override
                    public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                        Logger.debug("HEY, RESPONSE :)");
                    }

                    @Override
                    public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                        Logger.debug("NOOOOOO :(");
                    }
                });

            }
    }
}

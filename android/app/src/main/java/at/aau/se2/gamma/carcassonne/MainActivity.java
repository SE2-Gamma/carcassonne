package at.aau.se2.gamma.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import at.aau.se2.gamma.carcassonne.databinding.ActivityMainBinding;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.CreateSessionActivity;
import at.aau.se2.gamma.carcassonne.views.JoinSessionActivity;
import at.aau.se2.gamma.carcassonne.views.UIElementsActivity;
import at.aau.se2.gamma.core.commands.BaseCommand;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnNavigateCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateSessionActivity.class));
            }
        });

        binding.btnNavigateJoinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, JoinSessionActivity.class));
            }
        });

        binding.btnUiElements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UIElementsActivity.class));
            }
        });

        ServerThread serverThread = ServerThread.init("192.168.0.47", 1234, new ServerThread.ConnectionHandler() {
            @Override
            public void onConnectionFinished() {
                Logger.debug("Connection created");
            }

            @Override
            public void onServerFailure() {
                Logger.error("Error at server initial connection");
            }

            @Override
            public void onServerResponse(BaseCommand command) {
                Logger.debug("RESPONSE: "+command.getPayload());
            }
        });
        serverThread.start();
    }
}

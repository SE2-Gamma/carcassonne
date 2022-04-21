package at.aau.se2.gamma.carcassonne.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.databinding.ActivityCreateSessionBinding;
import at.aau.se2.gamma.carcassonne.databinding.ActivityMainBinding;
import at.aau.se2.gamma.carcassonne.views.lobby.LobbyActivity;

public class CreateSessionActivity extends AppCompatActivity {

    public ActivityCreateSessionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_session);

        binding = ActivityCreateSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonNavigateLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateSessionActivity.this, LobbyActivity.class));
            }
        });
    }
}
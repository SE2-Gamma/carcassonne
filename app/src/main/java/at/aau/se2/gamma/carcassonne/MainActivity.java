package at.aau.se2.gamma.carcassonne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import at.aau.se2.gamma.carcassonne.databinding.ActivityMainBinding;
import at.aau.se2.gamma.carcassonne.views.CreateSessionActivity;
import at.aau.se2.gamma.carcassonne.views.JoinSessionActivity;
import at.aau.se2.gamma.carcassonne.views.SelectNameActivity;
import at.aau.se2.gamma.carcassonne.views.UIElementsActivity;

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

                //TODO: Kommunikation mit Server für Random Key einer Lobby

                startActivity(new Intent(MainActivity.this, SelectNameActivity.class));
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
    }
}

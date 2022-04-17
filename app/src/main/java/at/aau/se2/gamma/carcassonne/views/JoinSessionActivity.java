package at.aau.se2.gamma.carcassonne.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import at.aau.se2.gamma.carcassonne.databinding.ActivityJoinSessionBinding;

public class JoinSessionActivity extends AppCompatActivity {

    private ActivityJoinSessionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userInput = binding.ptInputKey.getText().toString();
                Log.d("Check","User input:" + userInput);

                //TODO: Verbindung mit dem Server und zu dem Spiel mit dem Key

                startActivity(new Intent(JoinSessionActivity.this, SelectNameActivity.class));
            }
        });
    }


}
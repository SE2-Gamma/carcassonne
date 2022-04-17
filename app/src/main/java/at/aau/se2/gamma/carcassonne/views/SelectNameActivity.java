package at.aau.se2.gamma.carcassonne.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.databinding.ActivityJoinSessionBinding;
import at.aau.se2.gamma.carcassonne.databinding.ActivitySelectNameBinding;

public class SelectNameActivity extends AppCompatActivity {

    private ActivitySelectNameBinding binding;
    String filename = "nameFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_name);

        binding = ActivitySelectNameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FileInputStream fileInputStream;
        try{
            fileInputStream = openFileInput(filename);
            java.util.Scanner s = new Scanner(fileInputStream);
            s.useDelimiter("\\A");

            String streamString = s.hasNext()?s.next():"";

            s.close();
            binding.ptUserName.setText(streamString);
            Log.d("Check","Loaded: "+streamString);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        binding.btnNameSelectEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = binding.ptUserName.getText().toString();
                FileOutputStream fileOutputStream;

                try {
                    fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    fileOutputStream.write(userInput.getBytes());
                    fileOutputStream.close();
                    Log.d("Check","Saved: "+userInput);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(SelectNameActivity.this,LobbyActivity.class));
            }
        });
    }
}
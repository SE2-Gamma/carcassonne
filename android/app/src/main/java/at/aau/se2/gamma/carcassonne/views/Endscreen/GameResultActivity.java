package at.aau.se2.gamma.carcassonne.views.Endscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import at.aau.se2.gamma.carcassonne.MainActivity;
import at.aau.se2.gamma.carcassonne.base.BaseActivity;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;

import at.aau.se2.gamma.carcassonne.databinding.ActivityGameResult2Binding;
import at.aau.se2.gamma.core.commands.LeaveGameCommand;
import at.aau.se2.gamma.core.commands.RequestUserListCommand;
import at.aau.se2.gamma.core.models.impl.Player;

public class GameResultActivity extends BaseActivity {
    private ArrayList<EndscreenPlayerDisplay> playerList;


    private ActivityGameResult2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameResult2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Log.d("DebugEnd","entered endactivity");
        playerList = new ArrayList<>();
        ArrayList<EndscreenPlayerDisplay> players = null;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        ArrayList<String> listPlayers = extras.getStringArrayList("PLAYERS");
        ArrayList<Integer> listPoints = extras.getIntegerArrayList("POINTS");
        for (String name:listPlayers) {
            Log.d("PLayerListEND",name);
        }
        for (Integer points:listPoints) {
            Log.d("PointsListEND", points.toString());
        }

/*        try {
            Scanner reader = new Scanner(statistics);
            while(reader.hasNextLine()){
                statisticsStr = reader.nextLine();
                Log.i("Loaded Data", "Data loaded "+statisticsStr);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            Log.e("Error","Failed reading statistic data from file");
        }*/

        //Go over string and extract the data with points and player
/*        String[] statisticArr = statisticsStr.split("#");
        for (int i = 0; i < statisticArr.length-1; i+=2) {
            players.add(new EndscreenPlayerDisplay(statisticArr[i],Integer.parseInt(statisticArr[i+1])));
        }*/

        ArrayList<String> addedPlayers = new ArrayList<>();

                int addedCount = 0;
                EndscreenPlayerDisplay bestPlayer;
                while(addedCount<listPlayers.size()) {
                    bestPlayer = new EndscreenPlayerDisplay("player",0);

                    for (int i=0;i<listPlayers.size();i++) {
                        if(!addedPlayers.contains(listPlayers.get(i))&&listPoints.get(i)>=bestPlayer.getPlayerpoints()){
                            bestPlayer.setPlayerName(listPlayers.get(i));
                            bestPlayer.setPlayerpoints(listPoints.get(i));

                        }
                    }
                    if(!addedPlayers.contains(bestPlayer.getPlayerName())){
                        playerList.add(bestPlayer);
                        addedPlayers.add(bestPlayer.getPlayerName());
                        Log.d("DebugEnd","Player: "+bestPlayer.getPlayerName()+" added");
                        addedCount++;
                    }
                }
                Log.d("DebugEnd","calculated order");

        for (EndscreenPlayerDisplay player:playerList) {
            Log.d("PLayerListEND","Player: "+player.getPlayerName()+" Points: "+player.getPlayerpoints());
        }
        for (String player:addedPlayers) {
            Log.d("AddedPlayers","Player: "+player);
        }

                binding.tvNameWinner.setVisibility(View.INVISIBLE);
                binding.tvPointsWinner.setVisibility(View.INVISIBLE);
                binding.tvNameSecond.setVisibility(View.INVISIBLE);
                binding.tvPointsSecond.setVisibility(View.INVISIBLE);
                binding.tvNameThird.setVisibility(View.INVISIBLE);
                binding.tvPointsThird.setVisibility(View.INVISIBLE);
                binding.tvNamefourth.setVisibility(View.INVISIBLE);
                binding.tvPointsFourth.setVisibility(View.INVISIBLE);
                binding.tvNamefifth.setVisibility(View.INVISIBLE);
                binding.tvPointsFifth.setVisibility(View.INVISIBLE);

                switch (addedCount){
                    case 5:
                        binding.tvNamefifth.setText(playerList.get(4).getPlayerName());
                        binding.tvPointsFifth.setText(String.format(" %s Points",playerList.get(4).getPlayerpoints()));
                        binding.tvNamefifth.setVisibility(View.VISIBLE);
                        binding.tvPointsFifth.setVisibility(View.VISIBLE);

                    case 4:
                        binding.tvNamefourth.setText(playerList.get(3).getPlayerName());
                        binding.tvPointsFourth.setText(String.format(" %s Points",playerList.get(3).getPlayerpoints()));
                        binding.tvNamefourth.setVisibility(View.VISIBLE);
                        binding.tvPointsFourth.setVisibility(View.VISIBLE);

                    case 3:
                        binding.tvNameThird.setText(playerList.get(2).getPlayerName());
                        binding.tvPointsThird.setText(String.format(" %s Points",playerList.get(2).getPlayerpoints()));
                        binding.tvNameThird.setVisibility(View.VISIBLE);
                        binding.tvPointsThird.setVisibility(View.VISIBLE);

                    case 2:
                        binding.tvNameSecond.setText(playerList.get(1).getPlayerName());
                        binding.tvPointsSecond.setText(String.format(" %s Points",playerList.get(1).getPlayerpoints()));
                        binding.tvNameSecond.setVisibility(View.VISIBLE);
                        binding.tvPointsSecond.setVisibility(View.VISIBLE);
                    case 1:
                        binding.tvNameWinner.setText(playerList.get(0).getPlayerName());
                        binding.tvPointsWinner.setText(String.format(" %s Points",playerList.get(0).getPlayerpoints()));
                        binding.tvNameWinner.setVisibility(View.VISIBLE);
                        binding.tvPointsWinner.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Log.d("END","No Players");

                }

        binding.btnMainAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendServerCommand(new LeaveGameCommand(null), new ServerThread.RequestResponseHandler() {
                    @Override
                    public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                        Intent intent= new Intent(GameResultActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                        Log.e("Error","no response from server");
                    }
                });

            }
        });
    }
}
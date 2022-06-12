package at.aau.se2.gamma.carcassonne.views.Endscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

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
    private LinkedList<EndscreenPlayerDisplay> playerList;


    private ActivityGameResult2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameResult2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


                playerList = new LinkedList<>();

                //PayloadResponseCommand temp = (PayloadResponseCommand) payload;
                ArrayList<Player> players = null; //TODO extract list from players
                LinkedList<String> addedPlayers = new LinkedList<>();
                int count = 0;
                int addedCount = 0;

                EndscreenPlayerDisplay bestPlayer = new EndscreenPlayerDisplay("player",0);
                while(addedCount<players.size()) {
                    for (Player player:players) {
                        if(!addedPlayers.contains(player.getName())&&player.getPoints()>=bestPlayer.getPlayerpoints()){
                            bestPlayer.setPlayerName(player.getName());
                            bestPlayer.setPlayerpoints(player.getPoints());

                        }
                    }
                    playerList.add(bestPlayer);
                    addedPlayers.add(bestPlayer.getPlayerName());
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

                switch (players.size()){
                    case 5:
                        binding.tvNameWinner.setText(playerList.get(0).getPlayerName());
                        binding.tvPointsWinner.setText(playerList.get(0).getPlayerpoints());

                        binding.tvNamefifth.setVisibility(View.INVISIBLE);
                        binding.tvPointsFifth.setVisibility(View.INVISIBLE);
                    case 4:
                        binding.tvNameSecond.setText(playerList.get(1).getPlayerName());
                        binding.tvPointsSecond.setText(playerList.get(1).getPlayerpoints());

                        binding.tvNamefourth.setVisibility(View.INVISIBLE);
                        binding.tvPointsFourth.setVisibility(View.INVISIBLE);
                    case 3:
                        binding.tvNameThird.setText(playerList.get(2).getPlayerName());
                        binding.tvPointsThird.setText(playerList.get(2).getPlayerpoints());

                        binding.tvNameThird.setVisibility(View.INVISIBLE);
                        binding.tvPointsThird.setVisibility(View.INVISIBLE);
                    case 2:
                        binding.tvNamefourth.setText(playerList.get(3).getPlayerName());
                        binding.tvPointsFourth.setText(playerList.get(3).getPlayerpoints());

                        binding.tvNameSecond.setVisibility(View.INVISIBLE);
                        binding.tvPointsSecond.setVisibility(View.INVISIBLE);
                    case 1:
                        binding.tvNamefifth.setText(playerList.get(4).getPlayerName());
                        binding.tvPointsFifth.setText(playerList.get(4).getPlayerpoints());

                        binding.tvNameWinner.setVisibility(View.INVISIBLE);
                        binding.tvPointsWinner.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        Log.d("END","No Players");

                }

        binding.btnMainAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(GameResultActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });





    }
}
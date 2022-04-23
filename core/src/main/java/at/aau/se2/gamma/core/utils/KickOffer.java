package at.aau.se2.gamma.core.utils;

import at.aau.se2.gamma.core.models.impl.Player;

import java.util.LinkedList;

public class KickOffer {
    public KickOffer(Player player) {
    }

    public Player getPlayer() {
        return player;
    }
    public int vote(Player votee){
        for (Player player:votees
             ) {
            if(player.getId().equals(votee.getId())){
                return counter;
            }
        }
        votees.add(player);
        counter++;
     return counter;
    }

    Player player;
    int counter=0;
    LinkedList<Player>votees=new LinkedList<>();
//todo: implement function to ensure a player can only issue one vote


}

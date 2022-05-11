package at.aau.se2.gamma.core.utils;

import at.aau.se2.gamma.core.models.impl.Player;

import java.io.Serializable;
import java.util.LinkedList;

public class KickOffer implements Serializable {
    public KickOffer(Player player) {
        this.player=player;
    }

    public Player getPlayer() {
        return player;
    }
    public int vote(Player votee){
        for (Player player:votees
             ) {
            if(player.getId().equals(votee.getId())){
                return votes;
            }
        }
        votees.add(player);
        votes++;
        System.out.println();
        System.err.println(votes);
        System.out.println();
     return votes;
    }

    Player player;
    int votes =0;

    LinkedList<Player>votees=new LinkedList<>();
//todo: implement function to ensure a player can only issue one vote


}

package at.aau.se2.gamma.core.utils;

import at.aau.se2.gamma.core.models.impl.Player;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class KickOffer implements Serializable {
    public KickOffer(Player player) {
        this.player=player;
        votesatomic=new AtomicInteger(0);
    }

    public Player getPlayer() {
        return player;
    }
    public int vote(Player votee){

        for (Player a:votees
             ) {

            if(a.getId().equals(votee.getId())){
                System.out.print("//votee has already issued a vote//");
                throw new IllegalStateException("you already issued a kickvote");

            }
        }
        votees.add(votee);
        votes++;
        return votesatomic.incrementAndGet();

   //  return votes;
    }

    Player player;
    AtomicInteger votesatomic;
    int votes =0;
   // ConcurrentLinkedDeque<Player>votees=new ConcurrentLinkedDeque<>();
    LinkedList<Player>votees=new LinkedList<>();
//todo: implement function to ensure a player can only issue one vote


}

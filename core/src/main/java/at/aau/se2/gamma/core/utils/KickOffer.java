package at.aau.se2.gamma.core.utils;

import at.aau.se2.gamma.core.models.impl.Player;

public class KickOffer {
    public KickOffer(Player player) {
    }

    public Player getPlayer() {
        return player;
    }
    public int vote(){
        counter++;
     return counter;
    }

    Player player;
    int counter=0;



}

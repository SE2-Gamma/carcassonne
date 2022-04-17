package at.aau.se2.gamma.core.models.interfaces;

import at.aau.se2.gamma.core.models.impl.GameState;
import at.aau.se2.gamma.core.models.impl.Player;

import java.util.ArrayList;


public interface SessionInterface {
    GameState gameState = null;
    String id = null;
    int asd = 0;
    ArrayList<Player> players = new ArrayList<>();
    
}

package at.aau.se2.gamma.core.models.interfaces;

import at.aau.se2.gamma.core.models.impl.GameState;
import at.aau.se2.gamma.core.models.impl.Player;

import java.util.ArrayList;


public interface SessionInterface {
    GameState gameState = null; //variablen in interfaces sind final und können nicht verändert werden, daher sinnlos. braucht man das interface dann noch?
    String id = null;

    ArrayList<Player> players = new ArrayList<>();
    
}

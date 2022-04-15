package at.aau.se2.gamma.core.models.interfaces;

import java.util.ArrayList;

import at.aau.se2.gamma.core.models.impl.GameState;


public interface SessionInterface {
    GameState gameState = null;
    String id = null;
    ArrayList<PlayerInterface> players = new ArrayList<>();
}

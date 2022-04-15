package at.aau.se2.gamma.core.models.interfaces;

import at.aau.se2.gamma.core.models.impl.GameState;

import java.util.ArrayList;


public interface SessionInterface {
    GameState gameState = null;
    String id = null;
    ArrayList<PlayerInterface> players = new ArrayList<>();
}

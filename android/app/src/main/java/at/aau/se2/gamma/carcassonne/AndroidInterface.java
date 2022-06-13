package at.aau.se2.gamma.carcassonne;

import java.util.ArrayList;

import at.aau.se2.gamma.core.models.impl.Player;

public interface AndroidInterface {
    public void makeToast(String message);
    public void startMainActivity();
    public void startEndActivity(ArrayList<Player> statistics);
}

package at.aau.se2.gamma.carcassonne.views.Endscreen;

public class EndscreenPlayerDisplay {
    private String playerName;

    private int playerpoints = 0;

    public EndscreenPlayerDisplay(String playerName,int playerpoints) {
        this.playerName = playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerpoints(int playerpoints) {
        this.playerpoints = playerpoints;
    }

    public int getPlayerpoints() {
        return playerpoints;
    }
}

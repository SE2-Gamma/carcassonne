package at.aau.se2.gamma.carcassonne.views.lobby;

public class LobbyPlayerDisplay {


    String playerName;

    Boolean playerState = false;

    public LobbyPlayerDisplay(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Boolean getPlayerState() {return playerState;}

    public void setPlayerState(Boolean playerState) {this.playerState = playerState;}
}

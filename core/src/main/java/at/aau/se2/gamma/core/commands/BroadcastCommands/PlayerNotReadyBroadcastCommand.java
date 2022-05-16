package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerNotReadyBroadcastCommand extends BroadcastCommand {
    //payload: String (playername)
    //wird an alle spieler einer lobby geschickt wenn ein spieler nicht mehr ready ist
    public PlayerNotReadyBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "player-not-ready";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}
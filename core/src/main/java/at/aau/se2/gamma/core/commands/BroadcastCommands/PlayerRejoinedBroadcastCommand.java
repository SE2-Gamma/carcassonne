package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerRejoinedBroadcastCommand extends BroadcastCommand {
    //payload: string (playername)
    //wird an alle spieler eines games geschickt wenn ein spieler reconnected
    public PlayerRejoinedBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "player-reconnected";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
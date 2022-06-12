package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerDisconnectedFromGameBroadcastCommand extends BroadcastCommand {
    //payload: string (playername)
    //wird an alle spieler ingame verschickt wenn ein spieler das game verlässt
    public PlayerDisconnectedFromGameBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "Player-joined";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}
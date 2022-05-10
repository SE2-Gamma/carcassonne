package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerLeftLobbyBroadcastCommand  extends BroadcastCommand {
    public PlayerLeftLobbyBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "player-left";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}
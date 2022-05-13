package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerReadyBroadcastCommand extends BroadcastCommand {
    public PlayerReadyBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "player-ready";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}
package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerNotReadyBroadcastCommand extends BroadcastCommand {
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
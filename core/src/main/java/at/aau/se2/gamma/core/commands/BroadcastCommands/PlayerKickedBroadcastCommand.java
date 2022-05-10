package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerKickedBroadcastCommand extends BroadcastCommand {
    public PlayerKickedBroadcastCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "Player-Kicked";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}

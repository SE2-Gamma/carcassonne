package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class KickPlayerBroadcastCommand extends BroadcastCommand{
    public KickPlayerBroadcastCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "kickBroadcast";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}

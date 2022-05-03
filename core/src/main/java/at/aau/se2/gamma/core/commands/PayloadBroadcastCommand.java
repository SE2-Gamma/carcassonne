package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class PayloadBroadcastCommand extends BroadcastCommand {
    public PayloadBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "generic-paylaoad";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

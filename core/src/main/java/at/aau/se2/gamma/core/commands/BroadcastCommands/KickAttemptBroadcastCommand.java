package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class KickAttemptBroadcastCommand extends BroadcastCommand {
    //payload= KickOffer
    public KickAttemptBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "Kick-Attempt";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

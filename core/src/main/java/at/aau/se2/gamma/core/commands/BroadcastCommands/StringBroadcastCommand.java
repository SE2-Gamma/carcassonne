package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.commands.BroadcastCommands.BroadcastCommand;
import at.aau.se2.gamma.core.states.ClientState;

public class StringBroadcastCommand extends BroadcastCommand {
    public StringBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "message";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

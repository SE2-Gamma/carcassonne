package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.states.ClientState;

public class BroadcastCommand extends BaseCommand {
    public BroadcastCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }

}

package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class StringResponseCommand extends BaseCommand {

    public StringResponseCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "string-response";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

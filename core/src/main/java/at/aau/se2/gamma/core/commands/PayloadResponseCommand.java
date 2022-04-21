package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class PayloadResponseCommand extends BaseCommand {

    public PayloadResponseCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "payload-response";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

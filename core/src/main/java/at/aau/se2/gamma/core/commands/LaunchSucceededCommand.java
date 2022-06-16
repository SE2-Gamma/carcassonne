package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class LaunchSucceededCommand extends BaseCommand {

    public LaunchSucceededCommand (Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "launchDone";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}

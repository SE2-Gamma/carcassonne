package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class InitialJoinCommand extends BaseCommand {

    public InitialJoinCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "join";
    }

    @Override
    public ClientState getState() {
        return ClientState.INITIAl;
    }
}

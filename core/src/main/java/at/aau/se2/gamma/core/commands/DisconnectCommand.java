package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class DisconnectCommand extends BaseCommand{

    //input: null
    //return: string

    public DisconnectCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "disconnect";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }

}
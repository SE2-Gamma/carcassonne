package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class GetClientStateCommand extends BaseCommand{

    public GetClientStateCommand(Object payload){super(payload);}

    @Override
    public String getKey() {
        return "getServerState";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

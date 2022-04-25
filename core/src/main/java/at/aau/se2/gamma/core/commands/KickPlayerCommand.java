package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class KickPlayerCommand extends BaseCommand{
    //Payload=String playerName
    //return= ServerResponse (successfull or not)

    public KickPlayerCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "kickPlayer";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

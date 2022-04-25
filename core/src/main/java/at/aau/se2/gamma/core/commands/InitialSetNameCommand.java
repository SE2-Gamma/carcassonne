package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class InitialSetNameCommand extends BaseCommand {
//Payload: String name
    //return: String unique PlayerID
    public InitialSetNameCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "setname";
    }

    @Override
    public ClientState getState() {
        return ClientState.INITIAl;
    }
}

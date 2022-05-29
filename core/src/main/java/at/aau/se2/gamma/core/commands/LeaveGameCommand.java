package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class LeaveGameCommand extends BaseCommand {
    //Payload= null
    //
    //return= null"

    public LeaveGameCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "leave-game";
    }

    @Override
    public ClientState getState() {
        return ClientState.INITIAl;
    }
}
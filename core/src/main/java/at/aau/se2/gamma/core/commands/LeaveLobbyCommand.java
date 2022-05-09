package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class LeaveLobbyCommand extends BaseCommand{

    public LeaveLobbyCommand(Object payload){super(payload);}

    @Override
    public String getKey() { return "leaveLobby"; }

    @Override
    public ClientState getState() { return ClientState.INITIAl; }
}
package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class LeaveLobbyCommand extends BaseCommand{
    //input: null
    //return: string (Lobby usccessfully left), oder error command mit entsprechendem code

    public LeaveLobbyCommand(Object payload){super(payload);}

    @Override
    public String getKey() { return "leaveLobby"; }

    @Override
    public ClientState getState() { return ClientState.INITIAl; }
}

package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class ReconnectCommand extends BaseCommand {
    //Payload= String PlayerID (you get the playerID from the initialsetName)
    //
    //return= String "reconnected.rejoin game?" or "reconnected."

    public ReconnectCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "reconnect";
    }

    @Override
    public ClientState getState() {
        return ClientState.INITIAl;
    }
}

package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class RejoinGameCommand extends BaseCommand {
    //Payload= SessionID
    //
    //return= GameObject gameobject and subscription to further game broadcasts

    public RejoinGameCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "Rejoin";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}

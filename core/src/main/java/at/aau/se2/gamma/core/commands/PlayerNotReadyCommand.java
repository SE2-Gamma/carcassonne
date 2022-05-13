package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerNotReadyCommand extends BaseCommand{
    //doesnt return apart from a serverresponsecommand with a string, but triggers a broadcastcommand to all players in the session which tells that playername is not ready anymore
     public PlayerNotReadyCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "player-not-ready";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}
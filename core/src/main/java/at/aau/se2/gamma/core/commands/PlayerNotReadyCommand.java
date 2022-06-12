package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerNotReadyCommand extends BaseCommand{
    //input: null
    //response: string (readyness resumed)
    //triggers: PlayerNotReadyBroadcastCommand to all players in lobby, payload: string (playername)
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
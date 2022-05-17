package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerReadyCommand extends BaseCommand{
    //input: null
    //response: string (readyness resumed)
    //triggers: PlayerNotReadyBroadcastCommand to all players in lobby, payload: string (playername)
    // if all players voted to start the game (each player can only get ready once) the game starts and gamestartedbroadcastcommand is sent to all
    public PlayerReadyCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "player-ready";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}

package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerXsTurnBroadcastCommand  extends BroadcastCommand {
    //paylaod: string (playername)
    //wird nach jeder gameloopiteration an alle spieler eines spiels geschickt, die NICHT am zug sind. playername ist der spieler der dran ist
    public PlayerXsTurnBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "other-players-turn";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
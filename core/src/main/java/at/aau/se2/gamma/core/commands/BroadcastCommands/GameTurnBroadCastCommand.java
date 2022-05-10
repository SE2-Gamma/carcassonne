package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class GameTurnBroadCastCommand extends BroadcastCommand {
    public GameTurnBroadCastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "Gameturn";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
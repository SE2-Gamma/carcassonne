package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class GameTurnCommand extends BaseCommand {
//payload=Gameturn
    public GameTurnCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "Game-turn";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}
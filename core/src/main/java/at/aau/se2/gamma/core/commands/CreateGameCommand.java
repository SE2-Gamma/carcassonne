package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class CreateGameCommand extends BaseCommand{
    public CreateGameCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "create";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}

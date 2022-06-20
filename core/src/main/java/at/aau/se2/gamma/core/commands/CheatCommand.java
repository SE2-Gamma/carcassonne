package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class CheatCommand extends BaseCommand {
    //payload=CheatMove
    //response: string turn successfull, oder error command: payload= beschreibung des fehlers, errorcode: invalid move
    public CheatCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "cheat";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
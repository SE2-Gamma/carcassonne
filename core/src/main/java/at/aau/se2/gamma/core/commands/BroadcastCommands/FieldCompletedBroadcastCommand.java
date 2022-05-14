package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class FieldCompletedBroadcastCommand  extends BroadcastCommand {
    //todo: implement
    public FieldCompletedBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "field-completed";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
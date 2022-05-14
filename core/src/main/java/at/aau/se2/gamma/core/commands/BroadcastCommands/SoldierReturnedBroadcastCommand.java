package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class SoldierReturnedBroadcastCommand  extends BroadcastCommand {
    //todo: implement (wird vermutlich nicht gebraucht)
    public SoldierReturnedBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "Soldier-returned";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
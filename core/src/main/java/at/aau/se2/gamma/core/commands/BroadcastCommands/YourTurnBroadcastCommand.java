package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class YourTurnBroadcastCommand  extends BroadcastCommand {
    //Payload = GameCard
    public YourTurnBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "Your-turn";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
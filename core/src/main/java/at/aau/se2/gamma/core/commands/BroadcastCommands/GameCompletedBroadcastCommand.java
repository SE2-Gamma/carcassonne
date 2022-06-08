package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class GameCompletedBroadcastCommand  extends BroadcastCommand {
    //todo:implement
    //return: GameStatistic
    public GameCompletedBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "game-completed";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
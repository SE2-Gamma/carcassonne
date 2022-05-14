package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class GameStartedBroadcastCommand  extends BroadcastCommand {
    //Payload=GameObject
    //is sent when all players in lobby are ready. appside todo: start game
    public GameStartedBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "game-started";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
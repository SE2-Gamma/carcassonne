package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class CheatMoveDetectedBroadcastCommand extends BroadcastCommand {
    //payload: cheatmove to be exectuted
    //wird getriggert und an alle spieler verschickt wenn ein erfolgreicher cheatmove ausgeführt worden ist.
    public CheatMoveDetectedBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "cheatmove-detected";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}

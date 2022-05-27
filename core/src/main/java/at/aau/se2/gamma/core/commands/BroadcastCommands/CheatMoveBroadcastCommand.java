package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class CheatMoveBroadcastCommand extends BroadcastCommand {
    //payload: cheatmove to be exectuted
   //wird getriggert und an alle spieler verschickt wenn ein erfolgreicher cheatmove ausgeführt worden ist.
      public CheatMoveBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "cheat-move";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
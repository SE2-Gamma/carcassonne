package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class GameTurnBroadCastCommand extends BroadcastCommand {
    //payload: neues GameObject
    //wird nach jedem erfolgreichen spielzug an alle spieler im spiel gesendet. Die änderungen des spielzugs sind
    //im neuen gameobject enthalten. wenn ein spielzug nicht erfolgreich ist, wird dieses command nicht verschickt
    //es ist also sichergerstellt, dass alle spielzüge in diesem object valid sind.
    public GameTurnBroadCastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "Gameturn";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
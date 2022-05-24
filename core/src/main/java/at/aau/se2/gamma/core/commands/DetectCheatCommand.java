package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.models.impl.Soldier;
import at.aau.se2.gamma.core.states.ClientState;

public class DetectCheatCommand extends BaseCommand {
    //payload=Soldier
    //response: string turn successfull, oder error command: payload= beschreibung des fehlers, errorcode: invalid move
    public DetectCheatCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "detect-cheat";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}
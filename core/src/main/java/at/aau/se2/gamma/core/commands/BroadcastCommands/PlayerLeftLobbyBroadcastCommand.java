package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerLeftLobbyBroadcastCommand  extends BroadcastCommand {
    //payload: string (playername)
    //wird ann alle spieler einer lobby verschickt wenn ein spieler aus der lobby gekickt wurde oder sie verlassen hat
    public PlayerLeftLobbyBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "player-left";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}
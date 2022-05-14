package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerJoinedBroadcastCommand extends BroadcastCommand {
    //payload: string (playername)
    //wird an alle spieler in der lobby verschickt wenn ein spieler die lobby betritt
    public PlayerJoinedBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "Player-joined";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}
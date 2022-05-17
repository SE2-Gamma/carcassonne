package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class KickAttemptBroadcastCommand extends BroadcastCommand {
    //payload= KickOffer
    //wird an alle spieler in der lobby verschickt wenn ein spieler einen kickplayercommand verschickt
    //der spieler wurde noch nicht erfolgreich gekickt, die anzahl an votes kann ausgelesen werden
    public KickAttemptBroadcastCommand(Object payload) {
        super(payload);
    }
    @Override
    public String getKey() {
        return "Kick-Attempt";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

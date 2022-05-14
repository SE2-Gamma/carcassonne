package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerKickedBroadcastCommand extends BroadcastCommand {
    //payload: kickoffer (enthalten sind: votes, who voted, wer gekickt wurde(kickoffer.player))
    //wird an alle spieler in einer lobby verschickt, wenn ein spieler gekickt wird. anschließend kommt ein playerleftlobbycommand wenn
    //dieser erfolgreich aus der lobby entfernt wurde
    public PlayerKickedBroadcastCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "Player-Kicked";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}

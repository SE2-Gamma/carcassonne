package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class CreateGameCommand extends BaseCommand{

    //Payload = LinkedList<Object>
    //1. entry: String GameID (gamename)
    //2. entry: Player player (der PLayer wird auch zur session hinzugefügt)
    //
    //serverResponseCommand payload=Session
    public CreateGameCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "create";
    }

    @Override
    public ClientState getState() {
        return ClientState.LOBBY;
    }
}

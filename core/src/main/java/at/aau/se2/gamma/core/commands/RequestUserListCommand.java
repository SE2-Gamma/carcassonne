package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class RequestUserListCommand extends BaseCommand{
    //Payload=null
    //return=LinkedList<String> names
    public RequestUserListCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "userList";
    }

    @Override
    public ClientState getState() {
        return ClientState.GAME;
    }
}

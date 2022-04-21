package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

public class RequestUserListCommandByID extends BaseCommand{ //unneccessary
    //Payload=String SessionID.
    //return=LinkedList<String> names
    public RequestUserListCommandByID(Object payload) {
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

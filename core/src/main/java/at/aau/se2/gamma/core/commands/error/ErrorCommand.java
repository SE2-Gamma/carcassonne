package at.aau.se2.gamma.core.commands.error;

import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.states.ClientState;

public class ErrorCommand extends BaseCommand {

    //Payload = LinkedList<Object>
    //1. entry: String message
    //2. entry: ErrorCode

    public ErrorCommand(Object payload) {
        super(payload);
    }

    @Override
    public String getKey() {
        return "error-default";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.states.ClientState;

public class ServerResponseCommand extends BaseCommand {

    public ServerResponseCommand(ServerResponse response, String requestId) {
        super(response, requestId);
    }

    public static ServerResponseCommand fromRequestCommand(BaseCommand requestCommand, ServerResponse response) {
        return new ServerResponseCommand(response, requestCommand.getRequestId());
    }

    @Override
    public String getKey() {
        return "server-response";
    }

    @Override
    public ClientState getState() {
        return ClientState.ANY;
    }
}

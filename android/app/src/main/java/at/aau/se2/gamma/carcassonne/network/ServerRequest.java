package at.aau.se2.gamma.carcassonne.network;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.error.ErrorCommand;

public class ServerRequest {
    BaseCommand command;
    ServerThread.RequestResponseHandler responseHandler;

    public ServerRequest(BaseCommand command, ServerThread.RequestResponseHandler responseHandler) {
        this.command = command;
        this.responseHandler = responseHandler;
    }

    public void notifyClient(ServerResponse response) {
        BaseCommand command = (BaseCommand) response.getPayload();

        // check if there is an error, or a successful response
        if (command instanceof ErrorCommand) {
            responseHandler.onFailure(response, command.getPayload(), command);
        } else {
            responseHandler.onResponse(response, command.getPayload(), command);
        }
    }
}

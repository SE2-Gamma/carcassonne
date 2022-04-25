package at.aau.se2.gamma.server;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.PayloadResponseCommand;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;
import at.aau.se2.gamma.core.commands.StringResponseCommand;
import at.aau.se2.gamma.core.commands.error.Codes;
import at.aau.se2.gamma.core.commands.error.ErrorCommand;

import java.util.LinkedList;

public class ResponseCreator {
    static public ServerResponseCommand getError(BaseCommand command, String message, Codes.ERROR code){
        System.err.println(message);
        LinkedList<Object>list=new LinkedList<>();
        list.add(message);
        list.add(command.getRequestId());
        list.add(code);
        ErrorCommand error=new ErrorCommand(list);
        return new ServerResponseCommand(ServerResponse.failure(error), command.getRequestId());

    }
    static public ServerResponseCommand getSuccess(BaseCommand command, Object payload){
        return new ServerResponseCommand(ServerResponse.success(new PayloadResponseCommand(payload)), command.getRequestId());
    }
}

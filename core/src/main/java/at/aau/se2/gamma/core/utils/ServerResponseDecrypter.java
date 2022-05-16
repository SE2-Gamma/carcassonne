package at.aau.se2.gamma.core.utils;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BroadcastCommands.BroadcastCommand;
import at.aau.se2.gamma.core.commands.PayloadResponseCommand;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;
import at.aau.se2.gamma.core.commands.error.ErrorCommand;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerResponseDecrypter {
    public static Object payloadRetriever(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ServerResponseCommand command = (ServerResponseCommand) in.readObject();


        ServerResponse sresponse= (ServerResponse) command.getPayload();

        if(sresponse.getPayload() instanceof BroadcastCommand){
            BroadcastCommand presponse= (BroadcastCommand) sresponse.getPayload();
            return presponse.getPayload();

        }
        if(sresponse.getPayload() instanceof ErrorCommand){
            ErrorCommand presponse= (ErrorCommand) sresponse.getPayload();
            return presponse.getPayload();

        }
        if(sresponse.getPayload() instanceof PayloadResponseCommand){
            PayloadResponseCommand presponse= (PayloadResponseCommand) sresponse.getPayload();
            return presponse.getPayload();
        }


        return null;
    }
}

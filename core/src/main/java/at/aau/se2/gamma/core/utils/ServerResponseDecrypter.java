package at.aau.se2.gamma.core.utils;

import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.PayloadResponseCommand;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerResponseDecrypter {
    public static Object payloadRetriever(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ServerResponseCommand command=(ServerResponseCommand) in.readObject();
        ServerResponse sresponse= (ServerResponse) command.getPayload();
        PayloadResponseCommand presponse= (PayloadResponseCommand) sresponse.getPayload();
        return presponse.getPayload();
    }
}

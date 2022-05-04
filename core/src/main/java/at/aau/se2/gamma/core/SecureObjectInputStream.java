package at.aau.se2.gamma.core;

import java.io.*;
import java.util.LinkedList;
import java.util.logging.Logger;

import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.BroadcastCommand;
import at.aau.se2.gamma.core.commands.CreateGameCommand;
import at.aau.se2.gamma.core.commands.DisconnectCommand;
import at.aau.se2.gamma.core.commands.InitialJoinCommand;
import at.aau.se2.gamma.core.commands.InitialSetNameCommand;
import at.aau.se2.gamma.core.commands.KickPlayerBroadcastCommand;
import at.aau.se2.gamma.core.commands.KickPlayerCommand;
import at.aau.se2.gamma.core.commands.PayloadBroadcastCommand;
import at.aau.se2.gamma.core.commands.PayloadResponseCommand;
import at.aau.se2.gamma.core.commands.RequestUserListCommand;
import at.aau.se2.gamma.core.commands.RequestUserListCommandByID;
import at.aau.se2.gamma.core.commands.ServerResponseCommand;
import at.aau.se2.gamma.core.commands.StringBroadcastCommand;
import at.aau.se2.gamma.core.commands.StringResponseCommand;
import at.aau.se2.gamma.core.commands.error.ErrorCommand;
import at.aau.se2.gamma.core.states.ClientState;

public class SecureObjectInputStream extends ObjectInputStream {

    public SecureObjectInputStream(InputStream in) throws IOException {
        super(in);
    }
    static LinkedList<String>allowedClasses=new LinkedList<>();
    private static boolean instantiated=false;
    static void initialise(){
        if(!instantiated){
            allowedClasses.add(BaseCommand.class.getName());
            allowedClasses.add(BroadcastCommand.class.getName());
            allowedClasses.add(CreateGameCommand.class.getName());
            allowedClasses.add(DisconnectCommand.class.getName());
            allowedClasses.add(InitialJoinCommand.class.getName());
            allowedClasses.add(InitialSetNameCommand.class.getName());
            allowedClasses.add(KickPlayerBroadcastCommand.class.getName());
            allowedClasses.add(KickPlayerCommand.class.getName());
            allowedClasses.add(PayloadBroadcastCommand.class.getName());
            allowedClasses.add(PayloadResponseCommand.class.getName());
            allowedClasses.add(RequestUserListCommand.class.getName());
            allowedClasses.add(RequestUserListCommandByID.class.getName());
            allowedClasses.add(ServerResponseCommand.class.getName());
            allowedClasses.add(StringBroadcastCommand.class.getName());
            allowedClasses.add(StringResponseCommand.class.getName());
            allowedClasses.add(ServerResponse.class.getName());
            allowedClasses.add(ErrorCommand.class.getName());
            allowedClasses.add(ServerResponse.StatusCode.class.getName());

            allowedClasses.add(String.class.getName());
            allowedClasses.add(LinkedList.class.getName());
            allowedClasses.add(ClientState.class.getName());
            instantiated=true;

        }


    }
    @Override
    protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        // Only deserialize instances of AllowedClass
       initialise();
        for (String classname:allowedClasses
             ) {
            if (osc.getName().equals(classname)) {
                System.out.println(classname+"resolved");
                return super.resolveClass(osc);
            }
        }

       throw new ClassNotFoundException("Illegal Class sent: "+osc.getName());

    }
}

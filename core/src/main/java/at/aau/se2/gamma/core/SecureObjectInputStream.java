package at.aau.se2.gamma.core;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import at.aau.se2.gamma.core.commands.*;
import at.aau.se2.gamma.core.commands.BroadcastCommands.*;
import at.aau.se2.gamma.core.commands.error.Codes;
import at.aau.se2.gamma.core.commands.error.ErrorCommand;
import at.aau.se2.gamma.core.models.impl.*;
import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.core.states.SessionState;
import at.aau.se2.gamma.core.utils.KickOffer;

public class SecureObjectInputStream extends ObjectInputStream {

    public SecureObjectInputStream(InputStream in) throws IOException {
        super(in);
    }
    static LinkedList<String>allowedClasses=new LinkedList<>();
    private static boolean instantiated=false;
    static void initialise(){
        if(!instantiated){
            allowedClasses.add(null);
            allowedClasses.add(BaseCommand.class.getName());
            allowedClasses.add(BroadcastCommand.class.getName());
            allowedClasses.add(CreateGameCommand.class.getName());
            allowedClasses.add(DisconnectCommand.class.getName());
            allowedClasses.add(InitialJoinCommand.class.getName());
            allowedClasses.add(InitialSetNameCommand.class.getName());
            allowedClasses.add(PlayerKickedBroadcastCommand.class.getName());
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
            allowedClasses.add(Codes.class.getName());
            allowedClasses.add(ServerResponse.StatusCode.class.getName());
            allowedClasses.add(GetClientStateCommand.class.getName());
            allowedClasses.add(LeaveLobbyCommand.class.getName());
            allowedClasses.add(FieldCompletedBroadcastCommand.class.getName());
            allowedClasses.add(GameCompletedBroadcastCommand.class.getName());
            allowedClasses.add(GameStartedBroadcastCommand.class.getName());
            allowedClasses.add(GameTurnBroadCastCommand.class.getName());
            allowedClasses.add(KickAttemptBroadcastCommand.class.getName());
            allowedClasses.add(PlayerJoinedBroadcastCommand.class.getName());
            allowedClasses.add(PlayerKickedBroadcastCommand.class.getName());
            allowedClasses.add(PlayerLeftLobbyBroadcastCommand.class.getName());
            allowedClasses.add(PlayerXsTurnBroadcastCommand.class.getName());
            allowedClasses.add(SoldierReturnedBroadcastCommand.class.getName());
            allowedClasses.add(YourTurnBroadcastCommand.class.getName());
            allowedClasses.add(KickOffer.class.getName());
            allowedClasses.add(Player.class.getName());
            allowedClasses.add(BaseModel.class.getName());
            allowedClasses.add(GameCard.class.getName());
            allowedClasses.add(GameCardSide.class.getName());
            allowedClasses.add(GameMap.class.getName());
            allowedClasses.add(GameMapEntry.class.getName());
            allowedClasses.add(GameMapEntryPosition.class.getName());
            allowedClasses.add(GameMove.class.getName());
            allowedClasses.add(GameObject.class.getName());
            allowedClasses.add(GameState.class.getName());
            allowedClasses.add(PlayerListEntry.class.getName());
            allowedClasses.add(Soldier.class.getName());
            allowedClasses.add(SoldierPlacement.class.getName());
            allowedClasses.add(ClientState.class.getName());
            allowedClasses.add(SessionState.class.getName());
            allowedClasses.add(Orientation.class.getName());
            allowedClasses.add(PlayerReadyCommand.class.getName());
            allowedClasses.add(PlayerNotReadyCommand.class.getName());
            allowedClasses.add(PlayerReadyBroadcastCommand.class.getName());
            allowedClasses.add(PlayerNotReadyBroadcastCommand.class.getName());
            allowedClasses.add(ArrayList.class.getName());


            allowedClasses.add(String.class.getName());
            allowedClasses.add(LinkedList.class.getName());
            allowedClasses.add(ClientState.class.getName());

            instantiated=true;
            for (String a:allowedClasses
            ) {
                System.out.println("allowed classes: "+a);
            }
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
        if(osc.getName().equals(GameMapEntry.class.getName())){
            return super.resolveClass(osc);
        }else{
            System.err.println("for some reason gamemapetnry istn accepted by whitelist");
        }
        return super.resolveClass(osc);
       //throw new ClassNotFoundException("Illegal Class sent: "+osc.getName());

    }
}

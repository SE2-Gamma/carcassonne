package at.aau.se2.gamma.core;

import java.io.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
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
            allowedClasses.add(Codes.ERROR.class.getName());
            allowedClasses.add(Codes.SUCCESS.class.getName());
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
            allowedClasses.add(PlayerLeftGameBroadcastCommand.class.getName());
            allowedClasses.add(LeaveGameCommand.class.getName());
            allowedClasses.add(ArrayList.class.getName());
            allowedClasses.add(ConcurrentLinkedDeque.class.getName());
            allowedClasses.add(AtomicInteger.class.getName());
            allowedClasses.add(int.class.getName());
            allowedClasses.add(boolean.class.getName());
            allowedClasses.add(String.class.getName());
            allowedClasses.add(long.class.getName());
            allowedClasses.add(short.class.getName());
            allowedClasses.add(char.class.getName());
            allowedClasses.add(float.class.getName());
            allowedClasses.add(double.class.getName());
            allowedClasses.add(Integer.class.getName());
            allowedClasses.add(Double.class.getName());
            allowedClasses.add(Array.class.getName());
            allowedClasses.add(GameTurnCommand.class.getName());
            allowedClasses.add(GameCard.SpecialType.class.getName());
            allowedClasses.add(GameCardSide.Type.class.getName());
            allowedClasses.add(GameCardSide.Type.class.getName());
            allowedClasses.add(java.lang.Number.class.getName());
            allowedClasses.add(java.lang.Enum.class.getName());
            allowedClasses.add(at.aau.se2.gamma.core.models.impl.GameMapEntry.class.getName());
            allowedClasses.add(at.aau.se2.gamma.core.models.impl.GameCardSide.class.getName());
            allowedClasses.add(CheatMove.class.getName());
            allowedClasses.add(CheatCommand.class.getName());
            allowedClasses.add(CheatMoveBroadcastCommand.class.getName());
            allowedClasses.add(CheatMoveDetectedBroadcastCommand.class.getName());
            allowedClasses.add(DetectCheatCommand.class.getName());
            allowedClasses.add(PlayerListEntry.class.getName());
            allowedClasses.add(PlayerPoints.class.getName());
            allowedClasses.add(ClosedFieldDetectionData.class.getName());
            allowedClasses.add(GameMapHandler.class.getName());
            allowedClasses.add(GameStatistic.class.getName());
            allowedClasses.add(GameState.class.getName());
            allowedClasses.add(CheatData.class.getName());
            allowedClasses.add(SoldierData.class.getName());
            allowedClasses.add(LaunchSucceededCommand.class.getName());


            allowedClasses.add(java.lang.StackTraceElement.class.getName());

            allowedClasses.add(java.lang.Throwable.class.getName());
            allowedClasses.add(java.io.IOException.class.getName());
            allowedClasses.add(java.lang.Exception.class.getName());
            allowedClasses.add(java.io.ObjectStreamException.class.getName());
            allowedClasses.add(java.io.NotSerializableException.class.getName());

            allowedClasses.add(at.aau.se2.gamma.core.models.impl.GameCardSide.Type.class.getName());
            allowedClasses.add("[[Lat.aau.se2.gamma.core.models.impl.GameMapEntry");

            allowedClasses.add("[[Lat.aau.se2.gamma.core.models.impl.GameMapEntry;");
            allowedClasses.add("[Lat.aau.se2.gamma.core.models.impl.GameMapEntry;");
            allowedClasses.add("[Lat.aau.se2.gamma.core.models.impl.GameCardSide;");
            allowedClasses.add("[Lat.aau.se2.gamma.core.models.impl.GameCardSide$Type;");
            allowedClasses.add("[Ljava.lang.StackTraceElement;");
            allowedClasses.add("java.util.Collections$EmptyList");


            allowedClasses.add(String.class.getName());
            allowedClasses.add(LinkedList.class.getName());
            allowedClasses.add(ClientState.class.getName());

            instantiated=true;


        }


    }

    static AtomicInteger counter=new AtomicInteger(0);


    @Override
    protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        // Only deserialize instances of AllowedClass
       initialise();
        System.out.print("//"+counter.incrementAndGet()+". use of whitelist//");

        for (String classname:allowedClasses
             ) {
            if (osc.getName().equals(classname)) {
                System.out.print("//"+osc.getName()+" resolved//");
                return super.resolveClass(osc);
            }
        }

       System.err.println(osc.getName()+" not resolved");
       throw new ClassNotFoundException("Illegal Class sent: "+osc.getName());
    }
}

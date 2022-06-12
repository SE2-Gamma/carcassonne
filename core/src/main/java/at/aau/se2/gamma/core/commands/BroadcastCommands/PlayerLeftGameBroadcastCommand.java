package at.aau.se2.gamma.core.commands.BroadcastCommands;

import at.aau.se2.gamma.core.states.ClientState;

public class PlayerLeftGameBroadcastCommand   extends BroadcastCommand {
        //payload: string (playername)
        //wird ann alle spieler eines games verschickt wenn ein spieler es verlassen hat
    //wenn der spieler am zug war wird der spielzug unterbrochen und es werden zeitlgeich yourturn und playerxsturnbroadcastcommands verschickt.
        public PlayerLeftGameBroadcastCommand(Object payload) {
            super(payload);
        }
        @Override
        public String getKey() {
            return "player-left";
        }

        @Override
        public ClientState getState() {
            return ClientState.GAME;
        }
    }


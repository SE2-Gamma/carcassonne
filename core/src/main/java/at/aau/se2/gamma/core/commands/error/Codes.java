package at.aau.se2.gamma.core.commands.error;

public class Codes {
    public enum ERROR{
        SESSION_ALREADY_EXISTS,
        PLAYER_NOT_CONNECTED,
        WRONG_STATE,
        NO_SESSION_FOUND,
        NAME_ALREADY_TAKEN,

    }
    public enum SUCCESS {
        PLAYER_ACCEPTED,
        SESSION_CREATED,

    }
}

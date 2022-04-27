package at.aau.se2.gamma.core.commands.error;

public class Codes {
    public enum ERROR{
        SESSION_ALREADY_EXISTS,
        PLAYER_NOT_CONNECTED,
        WRONG_STATE,
        NO_SESSION_FOUND,
        NAME_ALREADY_TAKEN,
        NO_PLAYER_WITH_MATCHING_ID, NO_PLAYER_WITH_MATCHING_NAME,

    }
    public enum SUCCESS {
        PLAYER_ACCEPTED,
        SESSION_CREATED,

    }
}

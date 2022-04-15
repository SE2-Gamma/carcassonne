package at.aau.se2.gamma.core.commands;

public class InitialJoin extends BaseCommand {

    public String test = null;

    @Override
    public Object run(Object payload) {
        // do anything, send payload
        return null;
    }

    @Override
    public String getKey() {
        return "join";
    }
}

package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;

import java.io.Serializable;

abstract public class BaseCommand implements Serializable {
    public BaseCommand(Object payload) {
        this.payload = payload;
    }
    protected Object payload = null;
    abstract public String getKey();
    abstract public ClientState getState();

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}

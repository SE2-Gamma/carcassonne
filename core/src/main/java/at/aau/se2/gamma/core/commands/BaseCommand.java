package at.aau.se2.gamma.core.commands;

import at.aau.se2.gamma.core.states.ClientState;
import at.aau.se2.gamma.core.utils.RandomUtil;

import java.io.Serializable;

abstract public class BaseCommand implements Serializable {
    protected Object payload;
    protected String requestId;

    public BaseCommand() {
        this(null);
    }

    public BaseCommand(Object payload) {
        this(payload, RandomUtil.randomToken());
    }

    public BaseCommand(Object payload, String requestId) {
        this.payload = payload;
        this.requestId = requestId;
    }

    abstract public String getKey();
    abstract public ClientState getState();

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}

package at.aau.se2.gamma.core;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    public enum StatusCode {
        SUCCESS,
        FAILURE
    }

    private StatusCode statusCode;
    private Object payload;

    public ServerResponse(Object payload, StatusCode statusCode) {
        this.payload = payload;
        this.statusCode = statusCode;
    }

    public static ServerResponse success(Object payload) {
        return new ServerResponse(payload, StatusCode.SUCCESS);
    }

    public static ServerResponse failure(Object payload) {
        return new ServerResponse(payload, StatusCode.FAILURE);
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}

package at.aau.se2.gamma.core;

public class ServerResponse {
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

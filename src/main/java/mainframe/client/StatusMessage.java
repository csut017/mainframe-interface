package mainframe.client;

import java.util.HashMap;

public enum StatusMessage {
    NOT_IMPLEMENTED(1, "The functionality has not been implemented"),
    UNABLE_TO_PARSE(2, "Unable to parse server response"),
    SERVER_FAILURE(3, "The server could not process the message"),
    INTERNAL_FAILURE(4, "An unexpected internal error has occurred"),
    NETWORK_FAILURE_UNAVAILABLE(10, "The remote host has responded that it is currently unavailable");

    private final String message;
    private final Integer code;

    StatusMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public MessageResponse generateMessageResponse(long transactionId) {
        return new MessageResponse(
                new Status(code, message, transactionId),
                new HashMap<>());
    }
}

package mainframe.client;

import java.time.LocalDateTime;

// the status of a mainframe send
public final class Status {
    private final int errorCode;
    private final String errorMessage;
    private final LocalDateTime timestamp;
    private long transactionId;

    // initialize a new Status instance for a success message.
    public Status(long transactionId) {
        this.errorCode = 0;
        this.errorMessage = null;
        this.timestamp = LocalDateTime.now();
        this.transactionId = transactionId;
    }

    // initialize a new Status instance for an error message.
    public Status(int errorCode, String errorMessage, long transactionId) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
        this.transactionId = transactionId;
    }

    // was the operation successful or not.
    public Boolean getWasSuccessful() {
        return errorCode == 0;
    }

    // gets the error code.
    public Integer getErrorCode() {
        return errorCode;
    }

    // gets the error message.
    public String getErrorMessage() {
        return errorMessage;
    }

    // gets the timestamp.
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // gets the transaction id.
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}

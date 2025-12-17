package com.fms.exception;

public class FMSException extends Exception {
    public FMSException(String message) {
        super(message);
    }

    public FMSException(String message, Throwable cause) {
        super(message, cause);
    }
}

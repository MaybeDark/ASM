package org.exception;

public class NotNullException extends RuntimeException {
    public NotNullException() {
        super();
    }
    public NotNullException(String message) {
        super(message);
    }
}
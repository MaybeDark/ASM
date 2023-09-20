package org.exception;

public class NotLoadException extends RuntimeException {
    public NotLoadException() {
        super();
    }
    public NotLoadException(String message) {
        super(message);
    }
}

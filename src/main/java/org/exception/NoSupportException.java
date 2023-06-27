package org.exception;

public class NoSupportException extends RuntimeException{
    public NoSupportException() {
        super();
    }
    public NoSupportException(String message) {
        super(message);
    }
}

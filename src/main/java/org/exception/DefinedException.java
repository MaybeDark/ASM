package org.exception;

public class DefinedException extends RuntimeException{
    public DefinedException() {
        super();
    }
    public DefinedException(String message) {
        super(message);
    }
}
package org.exception;

public class LocalNotFoundException extends RuntimeException{
    public LocalNotFoundException() { }
    public LocalNotFoundException(String msg) {
        super(msg);
    }
}

package org.exception;

public class InvalidDescriptorException extends RuntimeException{
    public InvalidDescriptorException() {
        super();
    }
    public InvalidDescriptorException(String message) {
        super(message);
    }
}

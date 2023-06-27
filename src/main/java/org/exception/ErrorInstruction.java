package org.exception;

public class ErrorInstruction  extends RuntimeException {
    public ErrorInstruction() {
        super();
    }
    public ErrorInstruction(String message) {
        super(message);
    }
}

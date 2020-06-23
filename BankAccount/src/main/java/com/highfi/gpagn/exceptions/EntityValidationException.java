package com.highfi.gpagn.exceptions;

public class EntityValidationException extends Exception {
    public EntityValidationException(String message) {
        super(message);
    }

    public EntityValidationException(String message, Throwable exception) {
        super(message, exception);
    }

}

package com.royal.receiverService.exception;

public class DeadLetterPersistenceException extends RuntimeException {
    public DeadLetterPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

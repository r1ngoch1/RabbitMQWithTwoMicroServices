package com.royal.receiverService.exception;

public class MessagePersistenceException extends RuntimeException {
    public MessagePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
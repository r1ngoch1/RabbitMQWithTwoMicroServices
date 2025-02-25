package com.royal.receiverService.exception;

/**
 * Исключение, выбрасываемое при ошибках сохранения сообщений в базе данных.
 */
public class MessagePersistenceException extends RuntimeException {
    /**
     * Конструктор исключения с сообщением об ошибке и причиной.
     *
     * @param message описание ошибки.
     * @param cause   причина ошибки.
     */
    public MessagePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.royal.receiverService.exception;

/**
 * Исключение, выбрасываемое при ошибках сохранения в мертвую очередь.
 * Используется для обработки ошибок, возникающих при сохранении сообщений в мертвой очереди.
 */
public class DeadLetterPersistenceException extends RuntimeException {

    /**
     * Конструктор для создания исключения с сообщением об ошибке и причиной.
     *
     * @param message Сообщение об ошибке.
     * @param cause   Причина ошибки.
     */
    public DeadLetterPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.royal.senderService.exception;

/**
 * Исключение, выбрасываемое при ошибках взаимодействия с RabbitMQ.
 */
public class RabbitMQException extends RuntimeException {
    /**
     * Конструктор исключения с сообщением об ошибке.
     *
     * @param message описание ошибки.
     */
    public RabbitMQException(String message) {
        super(message);
    }

    /**
     * Конструктор исключения с сообщением об ошибке и причиной.
     *
     * @param message описание ошибки.
     * @param cause   причина ошибки.
     */
    public RabbitMQException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.royal.receiverService.exception;

/**
 * Исключение, выбрасываемое, когда сообщение уже существует.
 * Это исключение используется для обработки ситуации, когда сообщение
 * с таким же идентификатором или содержанием уже имеется в системе.
 */
public class MessageAlreadyExistsException extends RuntimeException {

    /**
     * Конструктор для создания исключения с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public MessageAlreadyExistsException(String message) {
        super(message);
    }
}

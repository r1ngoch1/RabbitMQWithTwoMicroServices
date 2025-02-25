package com.royal.receiverService.controller;

import com.royal.receiverService.dto.Message;
import com.royal.receiverService.exception.MessagePersistenceException;
import com.royal.receiverService.repository.DeadLetterRepository;
import com.royal.receiverService.repository.MessageRepository;
import com.royal.receiverService.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с обычными сообщениями.
 * Предоставляет API для получения списка сообщений.
 */
@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private final MessageService messageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    /**
     * Конструктор с внедрением зависимости сервиса обработки сообщений.
     *
     * @param messageService сервис для работы с сообщениями
     */
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Обрабатывает GET-запрос для получения всех сообщений.
     *
     * @return список сообщений или сообщение об ошибке
     */
    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages() {
        try {
            LOGGER.info("Попытка получения всех сообщений через контроллер");
            List<Message> messages = messageService.getAllMessages();
            LOGGER.info("Успешно получено {} сообщений через контроллер", messages.size());
            return ResponseEntity.ok(messages);
        } catch (MessagePersistenceException e) {
            LOGGER.error("Ошибка при получении сообщений через контроллер", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении сообщений из базы данных: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Произошла непредвиденная ошибка в контроллере", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
}

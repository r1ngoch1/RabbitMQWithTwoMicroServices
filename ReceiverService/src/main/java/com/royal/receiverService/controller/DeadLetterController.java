package com.royal.receiverService.controller;

import com.royal.receiverService.dto.DeadLetterMessage;
import com.royal.receiverService.exception.DeadLetterPersistenceException;
import com.royal.receiverService.service.DeadLetterService;
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
 * Контроллер для обработки запросов, связанных с мертвыми письмами (Dead Letters).
 * Предоставляет API для получения списка мертвых сообщений.
 */
@RestController
@RequestMapping("/api/v1")
public class DeadLetterController {

    private final DeadLetterService deadLetterService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterController.class);

    /**
     * Конструктор с внедрением зависимости сервиса обработки мертвых сообщений.
     *
     * @param deadLetterService сервис для работы с мертвыми сообщениями
     */
    @Autowired
    public DeadLetterController(DeadLetterService deadLetterService) {
        this.deadLetterService = deadLetterService;
    }

    /**
     * Обрабатывает GET-запрос для получения всех мертвых сообщений.
     *
     * @return список мертвых сообщений или сообщение об ошибке
     */
    @GetMapping("/dead-letters")
    public ResponseEntity<?> getAllDeadLetters() {
        try {
            LOGGER.info("Попытка получения всех мертвых сообщений через контроллер");
            List<DeadLetterMessage> deadLetters = deadLetterService.getAllDeadLetters();
            LOGGER.info("Успешно получено {} мертвых сообщений через контроллер", deadLetters.size());
            return ResponseEntity.ok(deadLetters);
        } catch (DeadLetterPersistenceException e) {
            LOGGER.error("Ошибка при получении мертвых сообщений через контроллер: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении записей из таблицы dead_letter_messages: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Произошла непредвиденная ошибка в контроллере: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
}

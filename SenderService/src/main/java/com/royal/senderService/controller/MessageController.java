package com.royal.senderService.controller;

import com.royal.senderService.dto.Message;
import com.royal.senderService.exception.RabbitMQException;
import com.royal.senderService.service.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для обработки HTTP-запросов, связанных с отправкой сообщений в RabbitMQ.
 */
@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private final RabbitMQProducer rabbitMQProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    /**
     * Конструктор контроллера, инициализирующий сервис RabbitMQProducer.
     *
     * @param rabbitMQProducer Сервис для отправки сообщений в RabbitMQ.
     */
    @Autowired
    public MessageController(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    /**
     * Обрабатывает POST-запрос для публикации сообщения в RabbitMQ.
     *
     * @param message Сообщение, которое нужно отправить.
     * @return Ответ с результатом отправки.
     */
    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        try {
            LOGGER.info("Получен запрос на отправку сообщения: {}", message.toString());

            rabbitMQProducer.sendMessage(message);

            LOGGER.info("Сообщение успешно отправлено: {}", message.toString());

            return ResponseEntity.ok("Сообщение успешно отправлено");
        } catch (RabbitMQException e) {
            LOGGER.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при отправке сообщения: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Неизвестная ошибка: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Неизвестная ошибка: " + e.getMessage());
        }
    }
}

package com.royal.senderService.controller;


import com.royal.senderService.dto.Message;
import com.royal.senderService.service.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MessageController {


    private final RabbitMQProducer rabbitMQProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    public MessageController(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        try {
            LOGGER.info("Получен запрос на отправку сообщения: {}", message.toString());

            rabbitMQProducer.sendMessage(message);

            LOGGER.info("Сообщение успешно отправлено: {}", message.toString());

            return ResponseEntity.ok("Сообщение успешно отправлено");
        } catch (Exception e) {
            LOGGER.error("Ошибка при отправке сообщения: {}", e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }
}

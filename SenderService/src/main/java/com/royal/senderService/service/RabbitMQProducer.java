package com.royal.senderService.service;

import com.royal.senderService.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.name}")
    private String routingJsonKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Message message) {
        try {
            message.setTimestamp(new Timestamp(System.currentTimeMillis()));

            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

            LOGGER.info("Отправка сообщения: {}", message);

            rabbitTemplate.convertAndSend(exchange, routingJsonKey, message, correlationData);

            LOGGER.info("Сообщение успешно отправлено в RabbitMQ: {}", message);
        } catch (Exception e) {
            LOGGER.error("Ошибка при отправке сообщения в RabbitMQ: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при отправке сообщения в RabbitMQ", e);
        }
    }
}

package com.royal.receiverService.service;

import com.rabbitmq.client.Channel;
import com.royal.receiverService.dto.Message;
import com.royal.receiverService.exception.DeadLetterPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitDLXConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitDLXConsumer.class);
    private final DeadLetterService deadLetterService;

    @Autowired
    public RabbitDLXConsumer(DeadLetterService deadLetterService) {
        this.deadLetterService = deadLetterService;
    }

    @RabbitListener(queues = "${rabbitmq.dlx.queue.name}", ackMode = "MANUAL")
    public void consumeDLX(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            LOGGER.error("Сообщение попало в DLX: {}", message.toString());

            deadLetterService.saveDeadLetter(message.toString(), "Ошибка обработки в основной очереди");

            channel.basicAck(deliveryTag, false);
            LOGGER.info("Мертвое письмо успешно обработано и подтверждено.");
        } catch (DeadLetterPersistenceException e) {
            LOGGER.error("Ошибка сохранения мертвого письма в БД: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        } catch (Exception e) {
            LOGGER.error("Неизвестная ошибка при обработке DLX: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, true);
        }
    }
}

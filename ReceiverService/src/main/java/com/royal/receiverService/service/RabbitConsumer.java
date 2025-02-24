package com.royal.receiverService.service;

import com.rabbitmq.client.Channel;
import com.royal.receiverService.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitConsumer {
    private final MessageService messageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConsumer.class);

    @Autowired
    public RabbitConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}", ackMode = "MANUAL")
    public void consume(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            LOGGER.info("Получено сообщение: {}", message.toString());
            LOGGER.info("Delivery tag: {}", deliveryTag);

            messageService.save(message);

            channel.basicAck(deliveryTag, false);
            LOGGER.info("Сообщение подтверждено: delivery tag {}", deliveryTag);
        } catch (Exception e) {
            LOGGER.error("Ошибка обработки сообщения: {}", e.getMessage(), e);

            channel.basicNack(deliveryTag, false, false);
            LOGGER.info("Сообщение отправлено в очередь мертвых писем (DLX): delivery tag {}", deliveryTag);
        }
    }


}

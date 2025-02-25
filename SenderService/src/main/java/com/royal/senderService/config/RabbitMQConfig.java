package com.royal.senderService.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ, включая основные и DLX (Dead Letter Exchange) очереди, обменники и биндинги.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;
    @Value("${rabbitmq.dlx.queue.name}")
    private String dlxQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.dlx.exchange.name}")
    private String dlxExchange;

    @Value("${rabbitmq.routing.key.name}")
    private String routingKey;
    @Value("${rabbitmq.dlx.routing.key.name}")
    private String dlxRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    /**
     * Создает основную очередь с DLX (Dead Letter Exchange).
     *
     * @return Основная очередь.
     */
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queue)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", dlxRoutingKey)
                .build();
    }

    /**
     * Создает очередь мертвых писем (DLX).
     *
     * @return Очередь DLX.
     */
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(dlxQueue).build();
    }

    /**
     * Создает основной DirectExchange.
     *
     * @return Основной обменник.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    /**
     * Создает Dead Letter Exchange (DLX).
     *
     * @return DLX обменник.
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    /**
     * Создает биндинг основной очереди к основному обменнику.
     *
     * @return Биндинг основной очереди.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    /**
     * Создает биндинг очереди мертвых писем к DLX.
     *
     * @return Биндинг очереди DLX.
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRoutingKey);
    }

    /**
     * Определяет конвертер сообщений для работы с JSON.
     *
     * @return Конвертер сообщений.
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Создает шаблон для работы с RabbitMQ, добавляя поддержку JSON-конвертера и логирование подтверждений.
     *
     * @param connectionFactory Фабрика соединений RabbitMQ.
     * @return Конфигурированный шаблон RabbitMQ.
     */
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                LOGGER.info("CorrelationData: {}", correlationData);
            } else {
                LOGGER.error("CorrelationData: {}", correlationData);
            }
        });
        return rabbitTemplate;
    }
}

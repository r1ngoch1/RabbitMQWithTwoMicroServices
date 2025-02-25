package com.royal.receiverService.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки RabbitMQ.
 * Определяет очереди, обменники, ключи маршрутизации и шаблон RabbitTemplate.
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

    /**
     * Создает основную очередь с привязкой к Dead Letter Exchange (DLX).
     * Если сообщение не попало в основную очередь, то оно отправляется в очередь мертвых сообщений
     *
     * @return настроенная очередь
     */
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queue)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", dlxRoutingKey)
                .build();
    }

    /**
     * Создает очередь для Dead Letter Exchange (DLX).
     *
     * @return очередь DLX
     */
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(dlxQueue).build();
    }

    /**
     * Создает основной обменник типа Direct.
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    /**
     * Создает Dead Letter Exchange (DLX) типа Direct.
     *
     * @return DirectExchange для DLX
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange);
    }

    /**
     * Создает привязку между основной очередью и основным обменником.
     *
     * @return объект Binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    /**
     * Создает привязку между DLX-очередью и DLX-обменником.
     *
     * @return объект Binding для DLX
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRoutingKey);
    }

    /**
     * Конфигурирует конвертер сообщений в JSON-формат.
     *
     * @return конвертер сообщений
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Настраивает шаблон RabbitTemplate с заданным соединением и конвертером сообщений.
     *
     * @param connectionFactory соединение
     * @return настроенный RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}

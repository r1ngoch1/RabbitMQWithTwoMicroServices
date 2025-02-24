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


    @Bean
    public Queue queue() {
        return new Queue(queue);
    }


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxQueue);
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxExchange);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
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
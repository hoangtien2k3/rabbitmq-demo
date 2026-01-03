package com.hoangtien2k3.rabbitmq_demo.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
public class BaseRabbitPublisher<T extends Serializable> implements RabbitPublisher<T> {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(String exchange, String routingKey, T data) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, data);
            log.info("Successfully sent message to exchange: {}, routingKey: {}, data: {}", exchange, routingKey, data);
        } catch (Exception e) {
            log.error("Failed to send message to exchange: {}, routingKey: {}. Error: {}", exchange, routingKey, e.getMessage());
        }
    }

    @Override
    public void publish(String queueName, T data) {
        try {
            rabbitTemplate.convertAndSend(queueName, data);
            log.info("Successfully sent message to queue: {}, data: {}", queueName, data);
        } catch (Exception e) {
            log.error("Failed to send message to queue: {}. Error: {}", queueName, e.getMessage());
        }
    }
}

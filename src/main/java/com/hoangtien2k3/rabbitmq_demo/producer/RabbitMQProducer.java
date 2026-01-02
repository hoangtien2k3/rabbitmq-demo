package com.hoangtien2k3.rabbitmq_demo.producer;

import com.hoangtien2k3.rabbitmq_demo.common.constant.RabbitMQConstant;
import com.hoangtien2k3.rabbitmq_demo.model.Message;
import com.hoangtien2k3.rabbitmq_demo.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Producer Service
 * Sends messages to RabbitMQ queues via exchanges.
 * Uses RabbitTemplate which is a Spring wrapper for RabbitMQ client.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendHelloMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.QUEUE_HELLO, message);
        log.info("Hello message sent: {}", message);
    }

    public void sendUserMessage(User user) {
        rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT,
                RabbitMQConstant.ROUTING_KEY_USER, user);
        log.info("User event sent for ID: {}", user.getId());
    }

    public void sendNormalMessage(Message message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstant.EXCHANGE_DIRECT,
                RabbitMQConstant.ROUTING_KEY_MESSAGE_INFO,
                message);
        log.info("Normal message sent: {}", message.getId());
    }

    public void sendCriticalMessage(Message message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstant.EXCHANGE_DIRECT,
                RabbitMQConstant.ROUTING_KEY_MESSAGE_ERROR,
                message);
        log.info("Critical message sent: {}", message.getId());
    }

    public void sendDirectMessage(Message message, String routingKey) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstant.EXCHANGE_DIRECT,
                routingKey,
                message);
        log.info("Direct message sent to {}: {}", routingKey, message.getId());
    }

    public void publishOrderCreatedEvent(String orderId, Object data) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstant.EXCHANGE_TOPIC,
                RabbitMQConstant.ROUTING_KEY_ORDER_CREATED,
                data);
        log.info("Order created event sent: {}", orderId);
    }

    public void publishOrderShippedEvent(String orderId, Object data) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstant.EXCHANGE_TOPIC,
                RabbitMQConstant.ROUTING_KEY_ORDER_SHIPPED,
                data);
        log.info("Order shipped event sent: {}", orderId);
    }
}

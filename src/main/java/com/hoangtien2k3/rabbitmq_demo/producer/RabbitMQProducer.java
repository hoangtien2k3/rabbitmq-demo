package com.hoangtien2k3.rabbitmq_demo.producer;

import com.hoangtien2k3.rabbitmq_demo.common.constant.RabbitMQConstant;
import com.hoangtien2k3.rabbitmq_demo.common.util.MessageIdGenerator;
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

    // ==================== Direct Exchange - Simple Messaging ====================

    public void sendHelloMessage(String message) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConstant.QUEUE_HELLO, message);
            log.info("Message sent to hello.queue: {}", message);
        } catch (Exception e) {
            log.error("Error sending message to hello queue", e);
        }
    }

    public void sendUserMessage(User user) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConstant.EXCHANGE_DIRECT, 
                    RabbitMQConstant.ROUTING_KEY_USER, user);
            log.info("User message sent: {}", user.getId());
        } catch (Exception e) {
            log.error("Error sending user message", e);
        }
    }

    // ==================== Direct Exchange - Message Routing ====================

    public void sendNormalMessage(Message message) {
        try {
            if (message.getId() == null) {
                message.setId(MessageIdGenerator.generateMessageId("MSG"));
            }
            rabbitTemplate.convertAndSend(
                    RabbitMQConstant.EXCHANGE_DIRECT,
                    RabbitMQConstant.ROUTING_KEY_MESSAGE_INFO,
                    message
            );
            log.info("Normal message sent: {}", message.getId());
        } catch (Exception e) {
            log.error("Error sending normal message", e);
        }
    }

    public void sendCriticalMessage(Message message) {
        try {
            if (message.getId() == null) {
                message.setId(MessageIdGenerator.generateMessageId("CRITICAL"));
            }
            rabbitTemplate.convertAndSend(
                    RabbitMQConstant.EXCHANGE_DIRECT,
                    RabbitMQConstant.ROUTING_KEY_MESSAGE_ERROR,
                    message
            );
            log.info("Critical message sent: {}", message.getId());
        } catch (Exception e) {
            log.error("Error sending critical message", e);
        }
    }

    public void sendDirectMessage(Message message, String routingKey) {
        try {
            if (message.getId() == null) {
                message.setId(MessageIdGenerator.generateMessageId());
            }
            rabbitTemplate.convertAndSend(
                    RabbitMQConstant.EXCHANGE_DIRECT,
                    routingKey,
                    message
            );
            log.info("Direct message sent with routing key '{}': {}", routingKey, message.getId());
        } catch (Exception e) {
            log.error("Error sending direct message", e);
        }
    }

    // ==================== Topic Exchange - Event Publishing ====================

    public void publishOrderCreatedEvent(String orderId, String orderData) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConstant.EXCHANGE_TOPIC,
                    RabbitMQConstant.ROUTING_KEY_ORDER_CREATED,
                    orderData
            );
            log.info("Order created event published: {}", orderId);
        } catch (Exception e) {
            log.error("Error publishing order created event", e);
        }
    }

    public void publishOrderShippedEvent(String orderId, String orderData) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConstant.EXCHANGE_TOPIC,
                    RabbitMQConstant.ROUTING_KEY_ORDER_SHIPPED,
                    orderData
            );
            log.info("Order shipped event published: {}", orderId);
        } catch (Exception e) {
            log.error("Error publishing order shipped event", e);
        }
    }

    public void publishOrderEvent(String message, String routingKey) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConstant.EXCHANGE_TOPIC,
                    routingKey,
                    message
            );
            log.info("Order event published with routing key '{}': {}", routingKey, message);
        } catch (Exception e) {
            log.error("Error publishing order event", e);
        }
    }

    // ==================== Fanout Exchange - Broadcasting ====================

    public void publishHeadersMessage(String message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConstant.EXCHANGE_FANOUT,
                    "",
                    message,
                    msg -> {
                        msg.getMessageProperties().setHeader("x-trace-id", 
                                MessageIdGenerator.generateTraceId());
                        return msg;
                    }
            );
            log.info("Headers message published: {}", message);
        } catch (Exception e) {
            log.error("Error publishing headers message", e);
        }
    }
}

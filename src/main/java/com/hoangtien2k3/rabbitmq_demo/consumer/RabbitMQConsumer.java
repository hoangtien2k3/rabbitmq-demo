package com.hoangtien2k3.rabbitmq_demo.consumer;

import com.hoangtien2k3.rabbitmq_demo.common.constant.RabbitMQConstant;
import com.hoangtien2k3.rabbitmq_demo.model.Message;
import com.hoangtien2k3.rabbitmq_demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Consumer Service
 * Receives messages from RabbitMQ queues.
 * @RabbitListener annotation listens to specified queues.
 */
@Slf4j
@Service
public class RabbitMQConsumer {

    // ==================== 1. SIMPLE QUEUE CONSUMER ====================

    // ==================== Direct Exchange - Simple Messaging ====================

    @RabbitListener(queues = RabbitMQConstant.QUEUE_HELLO)
    public void consumeHelloMessage(String message) {
        try {
            log.info("Message received from hello queue: {}", message);
            Thread.sleep(500);
            log.info("Message processed: {}", message);
        } catch (InterruptedException e) {
            log.error("Error processing hello message", e);
            Thread.currentThread().interrupt();
        }
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_USER)
    public void consumeUserMessage(User user) {
        try {
            log.info("User message received: {}", user.getId());
            Thread.sleep(300);
            log.info("User message processed: {}", user.getId());
        } catch (InterruptedException e) {
            log.error("Error processing user message", e);
            Thread.currentThread().interrupt();
        }
    }

    // ==================== Direct Exchange - Message Routing ====================

    @RabbitListener(queues = RabbitMQConstant.QUEUE_MESSAGE_NORMAL)
    public void consumeNormalMessage(Message message) {
        try {
            log.info("Normal message received: {}", message.getId());
            Thread.sleep(300);
            log.info("Normal message processed: {}", message.getId());
        } catch (InterruptedException e) {
            log.error("Error processing normal message", e);
            Thread.currentThread().interrupt();
        }
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_MESSAGE_CRITICAL)
    public void consumeCriticalMessage(Message message) {
        try {
            log.warn("CRITICAL MESSAGE RECEIVED: {}", message.getId());
            message.setStatus(RabbitMQConstant.STATUS_PROCESSING);
            Thread.sleep(500);
            message.setStatus(RabbitMQConstant.STATUS_SUCCESS);
            log.info("Critical message processed: {}", message.getId());
        } catch (InterruptedException e) {
            log.error("Error processing critical message", e);
            Thread.currentThread().interrupt();
        }
    }

    // ==================== Topic Exchange - Order Events ====================

    @RabbitListener(queues = RabbitMQConstant.QUEUE_ORDER_CREATED)
    public void handleOrderCreatedEvent(String message) {
        try {
            log.info("Order created event received: {}", message);
            Thread.sleep(400);
            log.info("Order created event processed: {}", message);
        } catch (InterruptedException e) {
            log.error("Error processing order created event", e);
            Thread.currentThread().interrupt();
        }
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_ORDER_SHIPPED)
    public void handleOrderShippedEvent(String message) {
        try {
            log.info("Order shipped event received: {}", message);
            Thread.sleep(300);
            log.info("Order shipped event processed: {}", message);
        } catch (InterruptedException e) {
            log.error("Error processing order shipped event", e);
            Thread.currentThread().interrupt();
        }
    }

    // ==================== Fanout Exchange - Broadcasting ====================

    @RabbitListener(queues = RabbitMQConstant.QUEUE_HEADERS)
    public void handleHeadersMessage(String message) {
        try {
            log.info("Headers message received: {}", message);
            Thread.sleep(200);
            log.info("Headers message processed: {}", message);
        } catch (InterruptedException e) {
            log.error("Error processing headers message", e);
            Thread.currentThread().interrupt();
        }
    }
}

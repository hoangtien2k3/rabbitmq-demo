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
 * 
 * @RabbitListener annotation listens to specified queues.
 */
@Slf4j
@Service
public class RabbitMQConsumer {

    @RabbitListener(queues = RabbitMQConstant.QUEUE_HELLO)
    public void consumeHello(String message) {
        log.info("📥 [Simple] Received from hello.queue: {}", message);
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_USER)
    public void consumeUser(User user) {
        log.info("📥 [Direct] Received User creation: {} ({})", user.getUsername(), user.getEmail());
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_MESSAGE_NORMAL)
    public void consumeNormal(Message message) {
        log.info("📥 [Direct] Received Normal message {}: {}", message.getId(), message.getContent());
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_MESSAGE_CRITICAL)
    public void consumeCritical(Message message) {
        log.warn("📥 [Direct] Received CRITICAL message {}: {}", message.getId(), message.getContent());
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_ORDER_CREATED)
    public void handleOrderCreated(String message) {
        log.info("📥 [Topic] Received Order Created: {}", message);
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_ORDER_SHIPPED)
    public void handleOrderShipped(String message) {
        log.info("📥 [Topic] Received Order Shipped: {}", message);
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_HEADERS)
    public void handleBroadcast(String message) {
        log.info("📥 [Fanout] Received Broadcast: {}", message);
    }
}

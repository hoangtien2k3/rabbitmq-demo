package com.hoangtien2k3.rabbitmq_demo.service;

import com.hoangtien2k3.rabbitmq_demo.common.util.MessageIdGenerator;
import com.hoangtien2k3.rabbitmq_demo.model.Message;
import com.hoangtien2k3.rabbitmq_demo.model.User;
import com.hoangtien2k3.rabbitmq_demo.producer.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final RabbitMQProducer producer;

    public void sendHello(String text) {
        log.info("Processing hello message: {}", text);
        producer.sendHelloMessage(text);
    }

    public User createAndNotifyUser(User user) {
        if (user.getId() == null) {
            user.setId(System.currentTimeMillis());
        }
        log.info("Processing user creation: {}", user.getUsername());
        producer.sendUserMessage(user);
        return user;
    }

    public Message sendNormalMessage(Message message) {
        prepareMessage(message, "MSG");
        producer.sendNormalMessage(message);
        return message;
    }

    public Message sendCriticalMessage(Message message) {
        prepareMessage(message, "CRITICAL");
        producer.sendCriticalMessage(message);
        return message;
    }

    public Message sendDirectMessage(Message message, String routingKey) {
        prepareMessage(message, "DIRECT");
        producer.sendDirectMessage(message, routingKey);
        return message;
    }

    public void publishOrderCreated(String orderId) {
        String data = "Order Created Event: " + orderId + " at " + LocalDateTime.now();
        producer.publishOrderCreatedEvent(orderId, data);
    }

    public void publishOrderShipped(String orderId) {
        String data = "Order Shipped Event: " + orderId + " at " + LocalDateTime.now();
        producer.publishOrderShippedEvent(orderId, data);
    }

    private void prepareMessage(Message message, String prefix) {
        if (message.getId() == null) {
            message.setId(MessageIdGenerator.generateMessageId(prefix));
        }
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }
    }
}

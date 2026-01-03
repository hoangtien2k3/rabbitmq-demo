package com.hoangtien2k3.rabbitmq_demo.service;

import com.hoangtien2k3.rabbitmq_demo.common.util.MessageIdGenerator;
import com.hoangtien2k3.rabbitmq_demo.model.Message;
import com.hoangtien2k3.rabbitmq_demo.model.User;
import com.hoangtien2k3.rabbitmq_demo.publisher.topic.GeneralTopic;
import com.hoangtien2k3.rabbitmq_demo.publisher.topic.MessageTopic;
import com.hoangtien2k3.rabbitmq_demo.publisher.topic.OrderTopic;
import com.hoangtien2k3.rabbitmq_demo.publisher.topic.UserTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final UserTopic userTopic;
    private final OrderTopic orderTopic;
    private final MessageTopic messageTopic;
    private final GeneralTopic generalTopic;

    public void sendHello(String text) {
        log.info("Processing hello message: {}", text);
        generalTopic.sendHello(text);
    }

    public User createAndNotifyUser(User user) {
        if (user.getId() == null) {
            user.setId(System.currentTimeMillis());
        }
        log.info("Processing user creation: {}", user.getUsername());
        userTopic.sendUserMessage(user);
        return user;
    }

    public Message sendNormalMessage(Message message) {
        prepareMessage(message, "MSG");
        messageTopic.sendNormalMessage(message);
        return message;
    }

    public Message sendCriticalMessage(Message message) {
        prepareMessage(message, "CRITICAL");
        messageTopic.sendCriticalMessage(message);
        return message;
    }

    public Message sendDirectMessage(Message message, String routingKey) {
        prepareMessage(message, "DIRECT");
        messageTopic.sendDirectMessage(message, routingKey);
        return message;
    }

    public void publishOrderCreated(String orderId) {
        String data = "Order Created Event: " + orderId + " at " + LocalDateTime.now();
        orderTopic.publishOrderCreated(data);
    }

    public void publishOrderShipped(String orderId) {
        String data = "Order Shipped Event: " + orderId + " at " + LocalDateTime.now();
        orderTopic.publishOrderShipped(data);
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

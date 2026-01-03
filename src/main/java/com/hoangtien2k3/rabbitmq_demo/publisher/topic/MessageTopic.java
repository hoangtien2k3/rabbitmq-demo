package com.hoangtien2k3.rabbitmq_demo.publisher.topic;

import com.hoangtien2k3.rabbitmq_demo.config.RabbitQueueProperties;
import com.hoangtien2k3.rabbitmq_demo.model.Message;
import com.hoangtien2k3.rabbitmq_demo.publisher.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageTopic {

    private final RabbitPublisher<Message> rabbitPublisher;
    private final RabbitQueueProperties rabbitQueueProperties;

    private static final String MESSAGE_NORMAL_KEY = "message-normal";
    private static final String MESSAGE_CRITICAL_KEY = "message-critical";

    public void sendNormalMessage(Message message) {
        RabbitQueueProperties config = rabbitQueueProperties.getQueues().get(MESSAGE_NORMAL_KEY);
        if (config != null) {
            rabbitPublisher.publish(config.getExchange(), config.getRoutingKey(), message);
        }
    }

    public void sendCriticalMessage(Message message) {
        RabbitQueueProperties config = rabbitQueueProperties.getQueues().get(MESSAGE_CRITICAL_KEY);
        if (config != null) {
            rabbitPublisher.publish(config.getExchange(), config.getRoutingKey(), message);
        }
    }

    public void sendDirectMessage(Message message, String routingKey) {
        RabbitQueueProperties config = rabbitQueueProperties.getQueues().get(MESSAGE_NORMAL_KEY);
        if (config != null) {
            rabbitPublisher.publish(config.getExchange(), routingKey, message);
        }
    }
}

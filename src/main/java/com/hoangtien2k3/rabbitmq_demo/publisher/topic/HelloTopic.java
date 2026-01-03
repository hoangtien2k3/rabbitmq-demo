package com.hoangtien2k3.rabbitmq_demo.publisher.topic;

import com.hoangtien2k3.rabbitmq_demo.config.RabbitQueueProperties;
import com.hoangtien2k3.rabbitmq_demo.publisher.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelloTopic {

    private final RabbitPublisher<String> rabbitPublisher;
    private final RabbitQueueProperties rabbitQueueProperties;

    private static final String MESSAGE_HELLO = "hello";

    public void sendHello(String message) {
        RabbitQueueProperties config = rabbitQueueProperties.getQueues().get(MESSAGE_HELLO);
        if (config != null) {
            if (config.getExchange() != null && !config.getExchange().isEmpty()) {
                rabbitPublisher.publish(config.getExchange(), config.getRoutingKey(), message);
            } else {
                rabbitPublisher.publish(config.getQueue(), message);
            }
        }
    }
}

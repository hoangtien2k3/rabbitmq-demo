package com.hoangtien2k3.rabbitmq.publisher.topic;

import com.hoangtien2k3.rabbitmq.config.RabbitQueueProperties;
import com.hoangtien2k3.rabbitmq.model.User;
import com.hoangtien2k3.rabbitmq.publisher.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTopic {

    private final RabbitPublisher<User> rabbitPublisher;
    private final RabbitQueueProperties rabbitQueueProperties;

    private static final String USER_QUEUE_KEY = "user";

    public void sendUserMessage(User user) {
        RabbitQueueProperties config = rabbitQueueProperties.getQueues().get(USER_QUEUE_KEY);
        if (config != null) {
            rabbitPublisher.publish(config.getExchange(), config.getRoutingKey(), user);
        }
    }
}

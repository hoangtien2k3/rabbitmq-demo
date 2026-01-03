package com.hoangtien2k3.rabbitmq_demo.publisher.topic;

import com.hoangtien2k3.rabbitmq_demo.config.RabbitQueueProperties;
import com.hoangtien2k3.rabbitmq_demo.model.User;
import com.hoangtien2k3.rabbitmq_demo.publisher.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserTopic {

    private final RabbitPublisher<User> rabbitPublisher;
    private final Map<String, RabbitQueueProperties> rabbitQueuesMap;

    private static final String USER_QUEUE_KEY = "user";

    public void sendUserMessage(User user) {
        RabbitQueueProperties config = rabbitQueuesMap.get(USER_QUEUE_KEY);
        if (config != null) {
            rabbitPublisher.publish(config.getExchange(), config.getRoutingKey(), user);
        }
    }
}

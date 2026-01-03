package com.hoangtien2k3.rabbitmq_demo.publisher.topic;

import com.hoangtien2k3.rabbitmq_demo.config.RabbitQueueProperties;
import com.hoangtien2k3.rabbitmq_demo.publisher.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeneralTopic {

    private final RabbitPublisher<String> rabbitPublisher;
    private final Map<String, RabbitQueueProperties> rabbitQueuesMap;

    public void sendHello(String message) {
        RabbitQueueProperties config = rabbitQueuesMap.get("hello");
        if (config != null) {
            if (config.getExchange() != null && !config.getExchange().isEmpty()) {
                rabbitPublisher.publish(config.getExchange(), config.getRoutingKey(), message);
            } else {
                rabbitPublisher.publish(config.getQueue(), message);
            }
        }
    }
}

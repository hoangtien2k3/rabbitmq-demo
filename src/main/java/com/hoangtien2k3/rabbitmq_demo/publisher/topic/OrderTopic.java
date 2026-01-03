package com.hoangtien2k3.rabbitmq_demo.publisher.topic;

import com.hoangtien2k3.rabbitmq_demo.config.RabbitQueueProperties;
import com.hoangtien2k3.rabbitmq_demo.publisher.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class OrderTopic {

    private final RabbitPublisher<Serializable> rabbitPublisher;
    private final RabbitQueueProperties rabbitQueueProperties;

    private static final String ORDER_CREATED_KEY = "order-created";
    private static final String ORDER_SHIPPED_KEY = "order-shipped";

    public void publishOrderCreated(Object orderData) {
        RabbitQueueProperties config = rabbitQueueProperties.getQueues().get(ORDER_CREATED_KEY);
        if (config != null && orderData instanceof Serializable) {
            rabbitPublisher.publish(config.getExchange(), config.getRoutingKey(), (Serializable) orderData);
        }
    }

    public void publishOrderShipped(Object orderData) {
        RabbitQueueProperties config = rabbitQueueProperties.getQueues().get(ORDER_SHIPPED_KEY);
        if (config != null && orderData instanceof Serializable) {
            rabbitPublisher.publish(config.getExchange(), config.getRoutingKey(), (Serializable) orderData);
        }
    }
}
